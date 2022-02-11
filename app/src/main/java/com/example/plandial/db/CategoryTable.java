package com.example.plandial.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryTable {

    //Table 속성
    @PrimaryKey(autoGenerate = true)
    private int id = 0; //각 카테고리의 고유한 ID

    private String categoryName;   // 카테고리의 이름을 저장

    private String categoryToTemplate;    // 템플릿 아이디 저장인데... 왜 만들었는지 모르겠음. 필요없으면 제거예정

    private String categoryColor;  //카테고리 color code 저장

    // 인스턴스 생성할 때 값도 같이 입력
    public CategoryTable(String categoryName, String categoryToTemplate, String categoryColor) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryToTemplate = categoryToTemplate;
        this.categoryColor = categoryColor;
    }

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