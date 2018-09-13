"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const admin = require("firebase-admin");
const functions = require("firebase-functions");
const warcraftApi = require("./warcraft_service/WarcraftApi");
admin.initializeApp();
exports.firestoreInstance = admin.firestore();
exports.battleNetApiKey = functions.config().battle_net.apikey;
exports.cronRequestSecret = functions.config().cron.key;
exports.firestoreInstance.settings({ timestampsInSnapshots: true });
exports.fetchRanking2vs2 = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "2v2", request, response, timestamp)
    ]);
});
exports.fetchRanking3vs3 = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "3v3", request, response, timestamp)
    ]);
});
exports.fetchRankingRbg = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "rbg", request, response, timestamp)
    ]);
});
//# sourceMappingURL=index.js.map