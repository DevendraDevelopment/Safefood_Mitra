package com.safefoodmitra.safefoodmitra.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasksync")
    List<TaskSync> getAll();

    @Insert
    void insert(TaskSync tasksync);

    @Delete
    void delete(TaskSync tasksync);

    @Query("DELETE FROM tasksync")
    void deleteAll();

    @Update
    void update(List<TaskSync> tasksync);
}
