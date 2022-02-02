package com.example.plandial.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.plandial.db.PresetTable;

import java.util.List;

@Dao
public interface IPresetDao {

    @Insert
    void insertPreset(PresetTable preset);    // 프리셋 생성

    @Update
    void updatePreset(PresetTable preset);   // 프리셋 수정

    @Delete
    void deletePreset(PresetTable preset);   // 프리셋 삭제

    //쿼리 명령어 : 일단 나중에 / 필요에 따른 쿼리 명령문 작성할 예정
    // 특정 템플릿 정보 가져오는 쿼리 (템플릿 아이디 활용, 열 정보 끌어오기 활용)
    // 프리셋 개수 알려주는 쿼리

    // 정보 전부 가져오기
    @Query("SELECT * FROM PresetTable")
    List<PresetTable> getPresetAll();

    // 데이터 전부 삭제
    @Query("DELETE FROM PresetTable")
    void delPresetAll();

    // 템플릿 가지고 올 때 각 템플릿의 프리셋들을 한번에 여러개 가져올 수 있도록 제작
    // 1은 나중에 진짜 templet아이디로 바뀜
    @Query("SELECT * FROM PresetTable WHERE templateId ='t1' ")
    List<PresetTable> getPreset1();
    @Query("SELECT * FROM PresetTable WHERE templateId ='t2' ")
    List<PresetTable> getPreset2();
    @Query("SELECT * FROM PresetTable WHERE templateId ='t3' ")
    List<PresetTable> getPreset3();
    @Query("SELECT * FROM PresetTable WHERE templateId ='t4' ")
    List<PresetTable> getPreset4();
    @Query("SELECT * FROM PresetTable WHERE templateId ='t5' ")
    List<PresetTable> getPreset5();

}
