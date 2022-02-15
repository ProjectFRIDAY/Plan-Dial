package com.example.plandial.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DialTable.class, CategoryTable.class}, version = 1)
public abstract class PlanDatabase extends RoomDatabase {

    public abstract IDialDao iDialDao();

    public abstract ICategoryDao iCategoryDao();
}
