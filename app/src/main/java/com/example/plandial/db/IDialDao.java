package com.example.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.plandial.db.DialTable;

@Dao
public interface IDialDao {

    @Insert
    void insetDial(DialTable dial);    // 다이얼 생성

    @Update
    void updateDial(DialTable dial);   // 다이얼 수정

    @Delete
    void deleteDial(DialTable dial);   // 다이얼 삭제

    //쿼리 명령어 : 일단 나중에 / 필요에 따른 쿼리 명령문 작성할 예정
}
