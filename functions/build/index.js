"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const admin = require("firebase-admin");
const functions = require("firebase-functions");
const warcraftApi = require("./warcraft_service/WarcraftApi");
admin.initializeApp();
exports.firestoreInstance = admin.firestore();
exports.battleNetApiKey = functions.config().battle_net.apikey;
exports.cronRequestSecret = functions.config().cron.key;
exports.fetchRanking2vs2 = functions.https.onRequest((request, response) => {
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "2v2", request, response),
        warcraftApi.retrieveLeaderboard("us", "2v2", request, response),
        warcraftApi.retrieveLeaderboard("kr", "2v2", request, response),
        warcraftApi.retrieveLeaderboard("tw", "2v2", request, response)
    ]);
});
exports.fetchRanking3vs3 = functions.https.onRequest((request, response) => {
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "3v3", request, response),
        warcraftApi.retrieveLeaderboard("us", "3v3", request, response),
        warcraftApi.retrieveLeaderboard("kr", "3v3", request, response),
        warcraftApi.retrieveLeaderboard("tw", "3v3", request, response)
    ]);
});
exports.fetchRankingRbg = functions.https.onRequest((request, response) => {
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "rbg", request, response),
        warcraftApi.retrieveLeaderboard("us", "rbg", request, response),
        warcraftApi.retrieveLeaderboard("kr", "rbg", request, response),
        warcraftApi.retrieveLeaderboard("tw", "rbg", request, response)
    ]);
});
//# sourceMappingURL=index.js.map