package com.example.room.repository

import androidx.lifecycle.LiveData
import com.example.room.model.User
import com.example.room.data.UserDao
import org.intellij.lang.annotations.Flow

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }


    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    fun searchDatabase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<User>>{
        return userDao.searchDatabase(searchQuery)
    }

}