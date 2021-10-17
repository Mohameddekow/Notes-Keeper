package com.example.room.fragments.update

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.room.R
import com.example.room.databinding.FragmentUpdateBinding
import com.example.room.model.User
import com.example.room.util.setupToast
import com.example.room.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        val title = args.currentUser.title
        val body = args.currentUser.body

        //initialize viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        binding.apply {
            etTitle.setText(title)
            etBody.setText(body)
        }
        binding.btnUpdate.setOnClickListener {
            updateUser()
            // dismiss keyboard
            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.btnUpdate.windowToken, 0)

        }

        return binding.root
    }

    //updated time
    private val currentDateTime = Calendar.getInstance().time
    private val formatter = SimpleDateFormat.getDateTimeInstance()
    private val formattedDate = formatter.format(currentDateTime)

    //update a single selected user
    private fun updateUser() {
        val title = binding.etTitle.text.toString()
        val body = binding.etBody.text.toString()
        val time: String = formattedDate

        if (inputCheck(title, body)){
            val currentUser = User(args.currentUser.id, title, body, "$time   (updated)")
            userViewModel.updateUser(currentUser)

            //navigate back the user
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

            setupToast(requireContext(),"updated successfully.")


        }else{
            setupToast(requireContext(), "Please fill out all fields.")
        }
    }


    //check if the inputs are empty
    private fun inputCheck(title: String, body: String): Boolean{
        return (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}