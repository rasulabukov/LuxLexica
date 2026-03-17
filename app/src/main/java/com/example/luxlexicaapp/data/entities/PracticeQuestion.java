package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "practice_questions")
public class PracticeQuestion {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int moduleId;
    public String questionText;
    public String optionsJson;
    public int correctAnswerIndex;

    public PracticeQuestion(int moduleId, String questionText, String optionsJson, int correctAnswerIndex) {
        this.moduleId = moduleId;
        this.questionText = questionText;
        this.optionsJson = optionsJson;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
