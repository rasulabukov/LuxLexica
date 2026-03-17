package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int testId;
    public String questionText;
    public String optionsJson;
    public int correctAnswerIndex;

    public Question(int testId, String questionText, String optionsJson, int correctAnswerIndex) {
        this.testId = testId;
        this.questionText = questionText;
        this.optionsJson = optionsJson;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
