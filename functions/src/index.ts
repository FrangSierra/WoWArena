import * as admin from 'firebase-admin'
import * as functions from 'firebase-functions'
import * as warcraftApi from './warcraft_service/WarcraftApi'

admin.initializeApp();

export const firestoreInstance = admin.firestore();

export const fetchRankings = functions.https.onRequest((request, response) => {
    return warcraftApi.retrieveLeaderboard("eu", "2v2", response)
});