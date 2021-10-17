package com.example.room.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.R
import com.example.room.databinding.FragmentListBinding
import com.example.room.model.User
import com.example.room.util.setupToast
import com.example.room.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class ListFragment : Fragment(), ListAdapter.ItemClickListener, SearchView.OnQueryTextListener{

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

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


        //show the loading text as the data is retrieved
        binding.progressBar.isVisible = true
        binding.textViewLoading.isVisible = true

        //RecyclerView
        myAdapter = ListAdapter(this)
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

            if (user.isEmpty()){
                binding.tvNoTask.isVisible = true
            }else if (user.isNotEmpty()){
                binding.tvNoTask.isVisible = false
            }

        })

        //setup the delete menu
        setHasOptionsMenu(true)

        return binding.root
    }


    //inflate the delete menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_main_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView =  search?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }


    //it will be triggered when one character is typed
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchDatabase(query)
        }
        return true
    }

    //it will be triggered when a character(s) is submitted only
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchDatabase(query)
        }
        return true
    }

    //it fetches data from the dataBase
    private fun searchDatabase(search: String){

        //the searchQuery should be like this so that our sql can understand
        val searchQuery = "%$search%"

        //we observe the database to see if it has the typed text and return it
        userViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer { user ->
            user.let {
                myAdapter.setData(it)
            }
        })
    }

    //set item click listener to my delete menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteAllUsers()
            }
            else -> {
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
            else -> {
                //root layout clickListener //to update fragment
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(user)
                findNavController().navigate(action)
            }
        }


    }

    //delete a single user
    private fun deleteUser(user: User) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

            userViewModel.deleteUser(user)
            snackBarToUndoDeletedUser(user,binding.root)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ${user.title}?")
        builder.setMessage("Do you want to delete ${user.title}?")
        builder.create()
        builder.show()
    }

    //Snack Bar to undo deleted user
    private fun snackBarToUndoDeletedUser(user: User, view: View){
        val snackbar = Snackbar.make(view, "You deleted an item.", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") { _, ->
            userViewModel.addUser(user)
            myAdapter.notifyDataSetChanged()

        }
        snackbar.show()
    }



    //delete all users
    private fun deleteAllUsers() {
        userViewModel.readAllData.observe(this, Observer { user ->

            if (user.isNotEmpty()) {
                //the database contains user(even a single one) delete all
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->
                    userViewModel.deleteAllUsers()
                    setupToast(requireContext(), "All data deleted successfully")
                    //remove the observer after clicking yes to avoid the other condition being executed
                    userViewModel.readAllData.removeObservers(this)
                }
                builder.setNegativeButton("No") { _, _ ->

                }
                builder.setTitle("Delete Everything?")
                builder.setMessage("Do you want to delete all data?")
                builder.create()
                builder.show()


            } else if (user.isEmpty()) {
                //when there are no users just display this alert
                val builder = AlertDialog.Builder(requireContext())
                builder.setNegativeButton("Ok") { _, _ ->

                }
                builder.setTitle("Sorry!")
                builder.setMessage("We couldn't find any data to delete")
                builder.create()
                builder.show()

            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}