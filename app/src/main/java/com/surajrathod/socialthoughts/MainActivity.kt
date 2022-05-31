package com.surajrathod.socialthoughts

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.daos.PostDao
import com.surajrathod.socialthoughts.models.Post
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class MainActivity : AppCompatActivity(), PostAdapter.IPostAdapter {

    private lateinit var adapter : PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        supportActionBar?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.status_bar_color_1)))





        auth = Firebase.auth
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this,R.color.status_bar_color_1)
        setUpRecyclerView()

        fab.setOnClickListener {
            startActivity(Intent(this,CreatePostActivity::class.java))
        }



    }

    private fun setUpRecyclerView()
    {

        postDao = PostDao()
        val postsCollections = postDao.postCollection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...


            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.logout_message)
                .setPositiveButton(R.string.start,
                    DialogInterface.OnClickListener { dialog, id ->
                        // START THE GAME!
                        //Toast.makeText(this,"Settins Clicked",Toast.LENGTH_SHORT).show()
                        Firebase.auth.signOut()


                        finish()
                    })
                .setNegativeButton("No",{dialog , id->
                    dialog.cancel()

                })

            builder.create().show()
            true
        }

        R.id.action_add_program -> {

            startActivity(Intent(this,AddProgramActivity::class.java))
            true
        }

        R.id.action_view_programs ->
        {
            startActivity(Intent(this,ViewProgramsActivity::class.java))
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.action_bar_options,menu)
        return super.onCreateOptionsMenu(menu)

    }
}