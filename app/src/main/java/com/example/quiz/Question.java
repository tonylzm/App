package com.example.quiz;

public class Question {
    private int mTextResId;       //问题文本对应id
    private boolean mAnswerTrue;   //答案
    private int mAnswerId;  //分离在资源文件中答案id

    //用于数组
    private String questionString;
    private String answerString;

    public Question(String questionString, String answerString) {
        this.questionString = questionString;
        this.answerString = answerString;
    }

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public Question(int textResId, int answerId) {
        mTextResId = textResId;
        mAnswerId = answerId;
    }

    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public int getAnswerId() {
        return mAnswerId;
    }

    public void setAnswerId(int answerId) {
        mAnswerId = answerId;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}


