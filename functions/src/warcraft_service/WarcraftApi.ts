import * as _ from "lodash";
import {battleNetApiKey, cronRequestSecret, firestoreInstance} from "../index";
import {CHARACTERS, MAX_BATCH_SIZE, MAX_EXPANSION_LEVEL, SERVERS} from "../constants";

export async function retrieveLeaderboard(region: String, bracket: String, firebaseRequest : any, firebaseResponse: any, date : number) {
    if (firebaseRequest.query.cron_secret != cronRequestSecret) return firebaseResponse.status(403).send("Wrong cron token");

    let request = require("request");
    let uri = `https://${region}.api.battle.net/wow/leaderboard/${bracket}?locale=en_GB`;

    let propertiesObject = {apikey: battleNetApiKey};

    console.log(uri);

    return request({
        uri: uri,
        method: "GET",
        qs: propertiesObject,
        json: true
    }, function (error, response, body) {
        if (response.statusCode == 200) {
            return serializeResponse(body, region, bracket, firebaseResponse, date)
        } else {
            console.log('error:', error); // Print the error if one occurred
            console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
            console.log('body:', body); // Print the HTML for the Google homepage.
            return firebaseResponse.status(400).send("The petition to Battle.NET API has failed");
        }
    });
}

async function serializeResponse(body: any, region: String, bracket: String, response: any, timestamp : number) {
    try {
        console.log('Starting serialization for ', bracket, ' in region ', region, ' with ', body.rows.length, ' rows');
        let mappedItems = body.rows.map(value => {
            //console.log('New row with data:', value);
            let character = {
                classId: value.classId,
                level: MAX_EXPANSION_LEVEL,
                raceId: value.raceId,
                realm: value.realmName,
                region: region,
                username: value.name,
                tier: value.tier
            };

            let seasonPlayed = parseInt(value.seasonWins) + parseInt(value.seasonLosses);
            let weeklyPlayed = parseInt(value.weeklyLosses) + parseInt(value.weeklyWins);
            let bracketData = {
                rating: value.rating,
                seasonLost: value.seasonLosses,
                seasonPlayed: seasonPlayed,
                seasonWon: value.seasonWins,
                weeklyLost: value.weeklyLosses,
                weeklyPlayed: weeklyPlayed,
                weeklyWon: value.weeklyWins
            };

            switch(bracket) {
                case "2v2" : {
                    return {
                        character: character,
                        timestamp: timestamp,
                        vs2: bracketData
                    };
                }
                case "3v3" : {
                    return {
                        character: character,
                        timestamp: timestamp,
                        vs3: bracketData
                    };
                }
                case "rbg" : {
                    return {
                        character: character,
                        timestamp: timestamp,
                        rbg: bracketData
                    };
                }
                default: {
                    return response.status(500).send('Wrong bracket');
                }
            }
        });

        console.log('Starting Firestore work for ', bracket, ' in region ', region);

        const batches: Array<FirebaseFirestore.WriteBatch> = [];
        const serverRef = firestoreInstance.collection(region.toUpperCase()).doc(SERVERS);

        if (mappedItems.length == 0){
            return response.status(200).send('There is no registered data for this bracket');
        }
        //Generate the right amount of batches for each type of update
        Array.prototype.push.apply(batches, _.chunk(mappedItems, MAX_BATCH_SIZE)
            .map(characters => {
                //console.log('new write batch');
                const writeBatch = firestoreInstance.batch();
                characters.forEach(charData => {

                    let realm = (charData as any).character.realm.toString();
                    let name = (charData as any).character.username.toString();
                    let date = (charData as any).timestamp.toString();

                    let ref = serverRef
                        .collection(realm)
                        .doc(CHARACTERS)
                        .collection(name)
                        .doc(date);

                    //console.log('New batch set of', charData, ' into ', ref);
                    writeBatch.set(ref, charData, {merge: true});
                });
                return writeBatch.commit();
            }));
        console.log('Write Batches ready for ', bracket, ' in region ', region);
        await Promise.all(batches);
        return response.status(200).send('The profiles for region', region, ' and bracket ', bracket, 'have been updated');
    } catch (err) {
        return response.status(500).send('Failed serializing the body for region', region, ' and bracket ', bracket, 'with error', err);
    }
}