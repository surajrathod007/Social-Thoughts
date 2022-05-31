package com.surajrathod.socialthoughts

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.Query
import com.surajrathod.socialthoughts.daos.PostDao
import com.surajrathod.socialthoughts.daos.ProgramsDao
import com.surajrathod.socialthoughts.models.Post
import com.surajrathod.socialthoughts.models.Programs
import kotlinx.android.synthetic.main.activity_view_programs.*

class ViewProgramsActivity : AppCompatActivity() {

    private lateinit var adapter : ProgramsAdapter
    private lateinit var programsDao: ProgramsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_programs)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.status_bar_color_1)))
        supportActionBar?.title = "Programs"
        window.statusBarColor = ContextCompat.getColor(this,R.color.status_bar_color_1)

        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        programsDao = ProgramsDao()
        val programsCollection = programsDao.programCollection
        val query = programsCollection.whereEqualTo("sem",1)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Programs>().setQuery(query, Programs::class.java).build()

        adapter = ProgramsAdapter(recyclerViewOptions)

        rvPrograms.adapter = adapter
        rvPrograms.layoutManager = LinearLayoutManager(this)


    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
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