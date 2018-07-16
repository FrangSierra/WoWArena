"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const admin = require("firebase-admin");
const _ = require("lodash");
const index_1 = require("../index");
const constants_1 = require("../constants");
const fieldValue = admin.firestore.FieldValue;
function retrieveLeaderboard(region, bracket, responsePetition) {
    return __awaiter(this, void 0, void 0, function* () {
        let request = require("request");
        let uri = `https://${region}.api.battle.net/wow/leaderboard/${bracket}`;
        let propertiesObject = { apikey: 'kr5ft58a2skpsgqgsyqj5646x58npmtk' };
        console.log(uri);
        return request({
            uri: uri,
            method: "GET",
            qs: propertiesObject,
            json: true
        }, function (error, response, body) {
            if (response.statusCode == 200) {
                return serializeResponse(body, region, bracket, responsePetition);
            }
            else {
                console.log('error:', error); // Print the error if one occurred
                console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
                console.log('body:', body); // Print the HTML for the Google homepage.
                return responsePetition.status(400).send("The petition to Battle.NET API has failed");
            }
        });
    });
}
exports.retrieveLeaderboard = retrieveLeaderboard;
function serializeResponse(body, region, bracket, response) {
    return __awaiter(this, void 0, void 0, function* () {
        try {
            console.log('Starting serialization');
            let mappedItems = body.rows.map(value => {
                let character = {
                    classId: value.classId,
                    level: constants_1.MAX_EXPANSION_LEVEL,
                    raceId: value.raceId,
                    realm: value.realmName,
                    region: region,
                    username: value.name
                };
                let timestamp = Date.now();
                let seasonPlayed = parseInt(value.seasonWins) + parseInt(value.seasonLosses);
                let weeklyPlayed = parseInt(value.weeklyLosses) + parseInt(value.weeklyWins);
                let vs2 = {
                    rating: value.rating,
                    seasonLost: value.seasonLosses,
                    seasonPlayed: seasonPlayed,
                    seasonWon: value.seasonWins,
                    weeklyLost: value.weeklyLosses,
                    weeklyPlayed: weeklyPlayed,
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
            const batches = [];
            const serverRef = index_1.firestoreInstance.collection(region.toUpperCase()).doc(constants_1.SERVERS);
            //Generate the right amount of batches for each type of update
            Array.prototype.push.apply(batches, _.chunk(mappedItems, constants_1.MAX_BATCH_SIZE)
                .map(characters => {
                //console.log('new write batch');
                const writeBatch = index_1.firestoreInstance.batch();
                characters.forEach(charData => {
                    let realm = charData.character.realm.toString();
                    let name = charData.character.username.toString();
                    let date = charData.timestamp.toString();
                    let ref = serverRef
                        .collection(realm)
                        .doc(constants_1.CHARACTERS)
                        .collection(name)
                        .doc(date);
                    //console.log('New batch set of', charData, ' into ', ref);
                    writeBatch.set(ref, charData, { merge: true });
                });
                return writeBatch.commit();
            }));
            console.log('Write batches ready, executing');
            yield Promise.all(batches);
            return response.status(200).send("The profiles for region', region, ' and bracket ', bracket, 'have been updated");
        }
        catch (err) {
            return response.status(500).send('Failed serializing the body with error', err);
        }
    });
}
//# sourceMappingURL=WarcraftApi.js.map