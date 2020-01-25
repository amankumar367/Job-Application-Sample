package com.aman.findjob.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aman.findjob.room.entity.Form

@Dao
interface FormDao {

    @Insert
    fun addForm(form: Form)

    @Update
    fun updateForm(form: Form)

    @Query("SELECT * FROM FORM")
    fun getAllTaskList(): List<Form>

    @Delete
    fun deleteForm(form: Form)

}