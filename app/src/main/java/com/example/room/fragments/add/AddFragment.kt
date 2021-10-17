              package com.example.room.fragments.add

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.room.R
import com.example.room.model.User
import com.example.room.databinding.FragmentAddBinding
import com.example.room.util.setupToast
import com.example.room.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

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

           //add user
           insertDataIntoDatabase()
       }


        return binding.root
    }

    //get the current time and format it
    val currentDateTime = Calendar.getInstance().time
                  val formatter = SimpleDateFormat.getDateTimeInstance()
                  val formattedDate = formatter.format(currentDateTime)


    private fun insertDataIntoDatabase() {
        val title = binding.etTitle.text.toString().trim()
        val body = binding.etBody.text.toString().trim()
        var time = formattedDate.toString()


        //get time view

        if (inputCheck(title, body)){
            //create a user object
            val user = User(0,title, body, time)

            //add data  to database
            userViewModel.addUser(user)
            //navigate back the user
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            //notify the user of the addition
            setupToast(requireContext(), "item added successfully")

        }else{
            setupToast(requireContext(), "Please fill out all fields.")
        }

    }

    private fun inputCheck(title: String, body: String): Boolean{
        return (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}