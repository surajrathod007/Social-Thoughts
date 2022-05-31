package com.surajrathod.socialthoughts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.surajrathod.socialthoughts.daos.ProgramsDao
import kotlinx.android.synthetic.main.activity_add_program.*

class AddProgramActivity : AppCompatActivity() {

    private  var programDao = ProgramsDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_program)

        btnSubmit.setOnClickListener {

            programDao.addProgram(editTitle.text.toString(),editContent.text.toString(),Integer.parseInt(editSem.text.toString()),Integer.parseInt(editUnit.text.toString()),editSubject.text.toString())

            Toast.makeText(this,"Program Added ",Toast.LENGTH_SHORT).show()

        }
    }
}