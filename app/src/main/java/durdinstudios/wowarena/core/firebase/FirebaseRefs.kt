package durdinstudios.wowarena.core.firebase

import com.google.firebase.firestore.FirebaseFirestore
import durdinstudios.wowarena.profile.Character
import durdinstudios.wowarena.profile.CharacterInfo

const val SERVERS = "Servers"
const val CHARACTERS = "Characters"

fun FirebaseFirestore.character(character : CharacterInfo) = collection(character.region.name)
        .document(SERVERS)
        .collection(character.realm)
        .document(CHARACTERS)
        .collection(character.username)

fun FirebaseFirestore.characterData(character : CharacterInfo, timestamp : Long) = collection(character.region.name)
        .document(SERVERS)
        .collection(character.realm)
        .document(CHARACTERS)
        .collection(character.username)
        .document(timestamp.toString())