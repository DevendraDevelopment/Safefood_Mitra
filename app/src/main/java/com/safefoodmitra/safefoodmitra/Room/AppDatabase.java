package com.safefoodmitra.safefoodmitra.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TaskSync.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
