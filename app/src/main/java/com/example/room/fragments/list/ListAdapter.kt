package com.example.room.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.room.model.User
import com.example.room.databinding.CustomRowBinding

class ListAdapter(private val clickListener: ItemClickListener) :
    RecyclerView.Adapter<ListAdapter.MyListViewHolder>() {
    private lateinit var binding: CustomRowBinding
    private var userList = emptyList<User>()


//    var itemClickListener: ( (view: View,item: User, position: Int ) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.onBind(currentUser, clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class MyListViewHolder(private val binding: CustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: User, action: ItemClickListener) {
            binding.apply {
                tvTitle.text = user.title
                tvBody.text = user.body
                tvTime.text = user.time
            }
            //setting single item click listener
            binding.root.setOnClickListener {
                action.onItemClicked(it, user, adapterPosition)
            }
            binding.btnDelete.setOnClickListener {
                action.onItemClicked(it, user, adapterPosition)
            }

        }


    }

    fun setData(myUserList: List<User>) {
        userList = myUserList
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClicked(view: View, user: User, position: Int)
    }
}