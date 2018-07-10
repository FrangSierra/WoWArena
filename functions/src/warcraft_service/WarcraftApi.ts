import * as admin from "firebase-admin";
import * as _ from "lodash";
import {firestoreInstance} from "../index";
import {CHARACTERS, MAX_BATCH_SIZE, SERVERS} from "../constants";

const fieldValue = admin.firestore.FieldValue;

export async function retrieveLeaderboard(region: String, bracket: String) {
    let request = require("request");
    let uri = `https://${region}.api.battle.net/wow/leaderboard/${bracket}`;

    let propertiesObject = {apikey: 'kr5ft58a2skpsgqgsyqj5646x58npmtk'};

    console.log(uri);

    return request({
        uri: uri,
        method: "GET",
        qs: propertiesObject,
        json:true
    }, function (error, response, body) {
        if (response.statusCode == 200) {
            return serializeResponse(body, region, bracket)
        } else {
            console.log('error:', error); // Print the error if one occurred
            console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
            console.log('body:', body); // Print the HTML for the Google homepage.
        }
    });
}

async function serializeResponse(body: any, region: String, bracket : String) {
    try {
        console.log('Starting serialization');
        let mappedItems = body.rows.map(value => {
            let character = {
                classId: value.classId,
                level: value.level,
                raceId: value.raceId,
                realm: value.realmName,
                region: region,
                thumbnail: value.thumbnail,
                username: value.name
            };

            let timestamp = Date.now();
            let seasonPlayed = parseInt(value.seasonWins) + parseInt(value.seasonLosses);
            let vs2 = {
                rating: value.rating,
                seasonLost: value.seasonLosses,
                seasonPlayed: seasonPlayed,
                seasonWon: value.seasonWins,
                weeklyLost: value.weeklyLosses,
                weeklyPlayed: value.weeklyPlayed,
                weeklyWon: value.weeklyWins
            };
            return {
                character: character,
                timestamp: timestamp,
                vs2: vs2
            };
        });
        console.log('serialization ended');
        console.log('Starting Firestore');

        const batches: Array<FirebaseFirestore.WriteBatch> = [];
        const serverRef = firestoreInstance.collection(region.toUpperCase()).doc(SERVERS);

        //Generate the right amount of batches for each type of update
        Array.prototype.push.apply(batches, _.chunk(mappedItems, MAX_BATCH_SIZE)
            .map(characters => {
                console.log('new write batch');
                const writeBatch = firestoreInstance.batch();
                characters.forEach(charData => {
                    let ref = serverRef
                        .collection((charData as any).character.realm.toString())
                        .doc(CHARACTERS)
                        .collection((charData as any).character.username.toString())
                        .doc((charData as any).timestamp.toString());

                    console.log('New batch set of', charData,' into ', ref);
                    writeBatch.set(ref, charData, {merge: true});
                });
                return writeBatch.commit();
            }));
        await Promise.all(batches);
        console.log('The profiles for region', region, ' and bracket ', bracket, 'have been updated')
    } catch (err) {
        console.error('Failed serializing the body with error', err);
    }
}