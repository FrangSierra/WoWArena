"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const admin = require("firebase-admin");
const functions = require("firebase-functions");
const warcraftApi = require("./warcraft_service/WarcraftApi");
admin.initializeApp();
exports.firestoreInstance = admin.firestore();
exports.fetchRankings = functions.https.onRequest((request, response) => {
    return warcraftApi.retrieveLeaderboard("eu", "2v2", response);
});
//# sourceMappingURL=index.js.map