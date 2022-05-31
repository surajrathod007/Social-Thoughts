package com.surajrathod.socialthoughts.daos

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.models.Post
import com.surajrathod.socialthoughts.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth //it gives currently signed in user


    fun addPost(text : String)
    {

        val currentUserId = auth.currentUser!!.uid //get currentusers uid
        GlobalScope.launch(Dispatchers.IO) {

            val usersDao = UsersDao()
            val user = usersDao.getUserById(currentUserId).await().toObject(User::class.java)!! //get user task from document and convert it to the objevt of our user class
            val currentTime = System.currentTimeMillis()

            val post = Post(text,user,currentTime)

            postCollection.document().set(post)

        }



    }

    fun deletePost(postId : String)
    {
        GlobalScope.launch {
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            if(auth.currentUser?.uid == post.createdBy.uid )
            {
                postCollection.document(postId).delete().addOnSuccessListener {
                }.addOnFailureListener {
                }
            }else{


            }

        }


    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }

    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }

    }
}