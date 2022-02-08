package com.example.plandial.db;

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

    //쿼리 명령어 : 일단 나중에 / 필요에 따른 쿼리 명령문 작성할 예정
    // 다이얼의 개수 가져오는 쿼리
    // 다이얼 모든 정보를 가져오는 쿼리
    // 특정 열의 정보를 가져오는 쿼리

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


    // 특정 카테고리 아이디를 가지고 있는 다이얼 데이터 쿼리


}



