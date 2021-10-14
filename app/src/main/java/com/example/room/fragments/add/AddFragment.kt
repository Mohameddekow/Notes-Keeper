package com.example.room.fragments.add

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.room.R
import com.example.room.model.User
import com.example.room.databinding.FragmentAddBinding
import com.example.room.viewmodel.UserViewModel

class AddFragment : Fragment() {

    private  var _binding: FragmentAddBinding? = null
    private val binding  get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

       binding.btnAdd.setOnClickListener {
           // dismiss keyboard
           val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
           inputManager.hideSoftInputFromWindow(binding.btnAdd.windowToken, 0)

           insertDataIntoDatabase()
       }


        return binding.root
    }

    private fun insertDataIntoDatabase() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val age = binding.etAge.text.toString().trim()

        if (inputCheck(firstName, lastName, age)){
            //create a user object
            val user = User(0,firstName, lastName, Integer.parseInt(age))

            //add data  to database
            userViewModel.addUser(user)
            Toast.makeText(requireContext(), "user successfully added", Toast.LENGTH_SHORT).show()

            //navigate back the user
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(firstName: String, lastName: String, age: String): Boolean{
        return (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(age))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}