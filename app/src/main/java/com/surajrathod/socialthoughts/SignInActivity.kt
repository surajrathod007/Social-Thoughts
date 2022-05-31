package com.surajrathod.socialthoughts

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.Utils.SnackMessage
import com.surajrathod.socialthoughts.daos.UsersDao

import com.surajrathod.socialthoughts.models.User
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 123
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        window.statusBarColor = ContextCompat.getColor(this,R.color.status_bar_color_1)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth




        signInButton.setOnClickListener {
            signIn()
        }


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try{

            val account = task?.getResult(ApiException::class.java)

            firebaseAuthWithGoogle(account!!.idToken)
        }catch (e : ApiException)
        {

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
       // Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
        val credential = GoogleAuthProvider.getCredential(idToken, null)




        animatioView.visibility = View.GONE
        signInButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {

            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user

            //update ui in Ui thread/Main thread
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser) //this is done on ui/main thread, you can not update ui elements using background thread
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {

        //if sign in is succesfull
        if(firebaseUser != null)
        {

            val user = User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
            val usersDao = UsersDao()
            usersDao.addUser(user) //add user to firebase




            val intent = Intent(this,MainActivity::class.java)

            startActivity(intent)

            Toast.makeText(this,"Welcome back, ${firebaseUser.displayName} ;) ",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            //Toast.makeText(this,"Sign in failed",Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
            animatioView.visibility = View.VISIBLE

        }
    }

    override fun onStart() {
        super.onStart()

        //check if current user is already signed in or not
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }




}