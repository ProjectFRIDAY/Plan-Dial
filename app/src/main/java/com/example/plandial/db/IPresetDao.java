package com.example.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.plandial.db.PresetTable;

@Dao
public interface IPresetDao {

    @Insert
    void insetPreset(PresetTable preset);    // 프리셋 생성

    @Update
    void updatePreset(PresetTable preset);   // 프리셋 수정

    @Delete
    void deletePreset(PresetTable preset);   // 프리셋 삭제

    //쿼리 명령어 : 일단 나중에 / 필요에 따른 쿼리 명령문 작성할 예정

}
