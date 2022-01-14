package com.example.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.plandial.db.CategoryTable;

@Dao
public interface ICategoryDao {

    @Insert
    void insetCategory(CategoryTable category);    // 카테고리 생성

    @Update
    void updateCategory(CategoryTable category);   // 카테고리 수정

    @Delete
    void deleteCategory(CategoryTable category);   // 카테고리 삭제

    //쿼리 명령어 : 일단 나중에 / 필요에 따른 쿼리 명령문 작성할 예정
}
