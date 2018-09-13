import * as admin from 'firebase-admin'
import * as functions from 'firebase-functions'
import * as warcraftApi from './warcraft_service/WarcraftApi'

admin.initializeApp();

export const firestoreInstance = admin.firestore();
export const battleNetApiKey = functions.config().battle_net.apikey;
export const cronRequestSecret = functions.config().cron.key;

firestoreInstance.settings( { timestampsInSnapshots: true });

export const fetchRanking2vs2 = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "2v2", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "2v2", request, response, timestamp)
    ])
});

export const fetchRanking3vs3 = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "3v3", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "3v3", request, response, timestamp)
    ])
});

export const fetchRankingRbg = functions.https.onRequest((request, response) => {
    let timestamp = Date.now();
    return Promise.all([
        warcraftApi.retrieveLeaderboard("eu", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("us", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("kr", "rbg", request, response, timestamp),
        warcraftApi.retrieveLeaderboard("tw", "rbg", request, response, timestamp)
    ])
});