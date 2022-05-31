package com.surajrathod.socialthoughts.daos

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.models.Programs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProgramsDao {

    val db = FirebaseFirestore.getInstance()
    val programCollection = db.collection("programs")
    //val auth = Firebase.auth

    fun addProgram(title : String,content : String,sem : Int,unit : Int,subject : String){


        GlobalScope.launch(Dispatchers.IO) {
            val program = Programs(title,content,sem,unit,subject)
            programCollection.document(title).set(program)
        }


    }
}