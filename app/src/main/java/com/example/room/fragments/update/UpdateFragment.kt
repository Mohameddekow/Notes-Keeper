package com.example.room.fragments.update

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.room.R
import com.example.room.databinding.FragmentUpdateBinding
import com.example.room.model.User
import com.example.room.viewmodel.UserViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        //details of current user from nav args
        val firstName = args.currentUser.firstName
        val lastName = args.currentUser.lastName
        val age = args.currentUser.age.toString()

        //initialize viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        binding.apply {
            etUpdateFirstName.setText(firstName)
            etUpdateLastName.setText(lastName)
            etUpdateAge.setText(age)
        }
        binding.btnUpdate.setOnClickListener {
            updateUser()
            // dismiss keyboard
            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.btnUpdate.windowToken, 0)

        }

        return binding.root
    }


    //update a single selected user
    private fun updateUser() {
        val firstName = binding.etUpdateFirstName.text.toString()
        val lastName = binding.etUpdateLastName.text.toString()
        val age = binding.etUpdateAge.text.toString()
        if (inputCheck(firstName, lastName, age)){
            val currentUser = User(args.currentUser.id, firstName, lastName, Integer.parseInt(age))
            userViewModel.updateUser(currentUser)

            //navigate back the user
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

            Toast.makeText(requireContext(), " User successfully updated.", Toast.LENGTH_SHORT).show()


        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }


    //check if the inputs are empty
    private fun inputCheck(firstName: String, lastName: String, age: String): Boolean{
        return (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(age))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}