package com.example.plandial.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithDial {
    @Embedded public CategoryTable categoryTable;
    @Relation(
            parentColumn = "id",
            entityColumn = "dialToCategory"
    )
    public List<DialTable> dialTables;
}
