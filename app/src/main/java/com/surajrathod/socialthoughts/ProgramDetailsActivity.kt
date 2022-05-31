package com.surajrathod.socialthoughts

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.models.Programs
import kotlinx.android.synthetic.main.activity_program_details.*
import javax.annotation.meta.When

class ProgramDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this,R.color.status_bar_color_1)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.status_bar_color_1)))

        val item = intent.getParcelableExtra<Programs>("program")

        supportActionBar?.title = item?.subject.toString()

        txtProgramTitle.text = item?.title.toString()
        txtContent.text = item?.content.toString()
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        android.R.id.home ->
        {
            onBackPressed()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



}