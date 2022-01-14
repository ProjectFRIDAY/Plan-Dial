package com.example.plandial.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryTable {

    //Table 속성
    @PrimaryKey(autoGenerate = true)
    private int id = 0; //각 카테고리의 고유한 ID

    private String categoryName;   // 카테고리의 이름을 저장

    private String categoryToTemplate;    // ❗️각 카테고리가 어디 템플릿에 연결되어있는지 확인 데이터베이스 연결 조사후 수정

    private String categoryColor;  //카테고리 color code 저장


    //getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryToTemplate() {
        return categoryToTemplate;
    }

    public void setCategoryToTemplate(String categoryToTemplate) {
        this.categoryToTemplate = categoryToTemplate;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}