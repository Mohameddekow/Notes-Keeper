package com.example.room.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.room.model.User
import java.util.concurrent.Flow

@Dao
interface UserDao {

    /// the user represent  a post

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE  FROM user_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE title LIKE :searchQuery OR body LIKE :searchQuery ")
    fun searchDatabase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<User>>
}