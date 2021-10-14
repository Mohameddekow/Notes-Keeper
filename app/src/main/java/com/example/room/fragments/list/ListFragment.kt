package com.example.room.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.R
import com.example.room.databinding.FragmentListBinding
import com.example.room.model.User
import com.example.room.viewmodel.UserViewModel


class ListFragment : Fragment(), ListAdapter.ItemClickListener {

    private  var _binding: FragmentListBinding? = null
    private val binding  get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var myAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.progressBar.isVisible = true
        binding.textViewLoading.isVisible = true

        //RecyclerView
        myAdapter = ListAdapter(this)
//        myAdapter.itemClickListener = { view,item, position ->
//            if (view.id == R.id.btnDelete){
//                Toast.makeText(requireContext().applicationContext, "clicked ${R.id.btnDelete}", Toast.LENGTH_SHORT).show()
//            }
//        }
        binding.myRecyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //user viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            binding.progressBar.isVisible = false
            binding.textViewLoading.isVisible = false

            myAdapter.setData(user)
        })

        //setup the delete menu
        setHasOptionsMenu(true)

        return binding.root
    }

    //inflate the delete menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    //set item click listener to my delete menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete ->{
                userViewModel.readAllData.observe(this, Observer {user ->
                    if (!user.isEmpty()){
                        //the database contains user(even a single one) delete all
                        deleteAllUsers()
                    }else{
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setNegativeButton("Ok"){_,_ ->

                        }
                        builder.setTitle("Sorry!")
                        builder.setMessage("We couldn't find any data to delete")
                        builder.create().show()
                    }
                })
            }
        }
        return true
    }


    //clickListeners of my recyclerView items
    override fun onItemClicked(view: View, user: User, position: Int) {

        when (view.id) {
            R.id.btnDelete -> {
                deleteUser(user)
            }
            R.id.btnEdit -> {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(user)
                findNavController().navigate(action)
                //Toast.makeText(requireContext().applicationContext, "clicked the edit btn of user: of user: ${user.firstName}", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //root layout clickListener
                //Toast.makeText(requireContext().applicationContext, "clicked the root item of user: ${user.firstName}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    //delete a single user
    private fun deleteUser(user:User){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _, ->
            userViewModel.deleteUser(user)
            Toast.makeText(requireContext().applicationContext, "user ${user.firstName} successfully deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete ${user.firstName}?")
        builder.setMessage("Do you want to delete user ${user.firstName}?")
        builder.create()
        builder.show()
    }


    //delete all users
    private fun deleteAllUsers(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            userViewModel.deleteAllUsers()
            Toast.makeText(requireContext().applicationContext, "All data successfully deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_, _ ->

        }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Do you want to delete all data?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}