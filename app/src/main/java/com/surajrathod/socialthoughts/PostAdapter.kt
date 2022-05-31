package com.surajrathod.socialthoughts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surajrathod.socialthoughts.Utils.Utils
import com.surajrathod.socialthoughts.daos.PostDao
import com.surajrathod.socialthoughts.models.Post


class PostAdapter(options: FirestoreRecyclerOptions<Post>,val listner : IPostAdapter) : FirestoreRecyclerAdapter<Post,PostAdapter.PostViewHolder>(options) {


    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val postText : TextView = itemView.findViewById(R.id.postTitle)
        val userText : TextView = itemView.findViewById(R.id.userName)
        val createdAt : TextView = itemView.findViewById(R.id.createdAt)
        val likeCount : TextView = itemView.findViewById(R.id.likeCount)
        val userImage : ImageView = itemView.findViewById(R.id.userImage)
        val likeButton : ImageView = itemView.findViewById(R.id.likeButton)
        val itemCard : CardView = itemView.findViewById(R.id.itemCard)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
        viewHolder.likeButton.setOnClickListener {
            listner.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        viewHolder.itemCard.setOnLongClickListener {


            val postDao = PostDao()
            postDao.deletePost(snapshots.getSnapshot(viewHolder.adapterPosition).id) //it gives the current clicked document's id

            true
        }



        return viewHolder

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_baseline_favorite_24))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_baseline_favorite_border_24))
        }




    }

    interface IPostAdapter {
        fun onLikeClicked(postId: String)
    }
}