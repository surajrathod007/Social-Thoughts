package com.surajrathod.socialthoughts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.surajrathod.socialthoughts.models.Programs

class ProgramsAdapter(options: FirestoreRecyclerOptions<Programs>) : FirestoreRecyclerAdapter<Programs,ProgramsAdapter.ProgramViewHolder>(options) {

    class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val title : TextView = itemView.findViewById(R.id.txtTitleItem)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val viewHolder = ProgramViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_programs,parent,false))






        return  viewHolder
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int, model: Programs) {
        holder.title.text = model.title.toString()

        holder.title.setOnClickListener {

            val program = Programs(model.title,model.content,model.sem,model.unit,model.subject)

            val i = Intent(holder.title.context,ProgramDetailsActivity::class.java)
            i.putExtra("program",program)
            holder.title.context.startActivity(i)

        }

    }
}