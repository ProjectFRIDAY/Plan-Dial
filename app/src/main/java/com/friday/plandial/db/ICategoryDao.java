package com.friday.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ICategoryDao {

    @Insert
    void insertCategory(CategoryTable category);    // 카테고리 생성

    @Update
    void updateCategory(CategoryTable category);   // 카테고리 수정

    @Delete
    void deleteCategory(CategoryTable category);   // 카테고리 삭제

    // 정보 전부 가져오기
    @Query("SELECT * FROM CategoryTable")
    List<CategoryTable> getCategoryAll();

    // 다이얼 이름으로 데이터 불러오기 쿼리
    @Query("SELECT * FROM CategoryTable WHERE CategoryTable.categoryName = :name")
    List<CategoryTable> getNameCategory(String name);

    // 데이터 전부 삭제 -> ICategoryDao
    @Query("DELETE FROM CategoryTable")
    void delCategoryAll();

}
