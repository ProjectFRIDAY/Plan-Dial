package com.friday.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IDialDao {

    @Insert
    void insertDial(DialTable dial);     // 다이얼 생성

    @Update
    void updateDial(DialTable dial);    // 다이얼 수정

    @Delete
    void deleteDial(DialTable dial);    // 다이얼 삭제

    // 모든 다이얼 데티어 쿼리
    @Query("SELECT * FROM DialTable")
    List<DialTable> getDialAll();

    // 활성화 다이얼 데이터 쿼리
    @Query("SELECT * FROM DialTable WHERE not DialTable.dialDisabled")
    List<DialTable> getDialDis();

    // 다이얼 이름으로 데이터 불러오기 쿼리
    @Query("SELECT * FROM DialTable WHERE DialTable.dialName = :name")
    List<DialTable> getNameDial(String name);

    // 데이터 전부 삭제 -> IDialDao
    @Query("DELETE FROM DialTable")
    void delDialAll();

    // 다이얼 테이블의 다음 id값 반환
    @Query("SELECT MAX(id) FROM DialTable")
    int getLastId();
}



