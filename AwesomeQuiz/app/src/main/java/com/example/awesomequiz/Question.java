package com.example.awesomequiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    public static final String DIFFICULTY_EASY="Easy";
    public static final String DIFFICULTY_MEDIUM="Medium";
    public static final String DIFFICULTY_HARD="Hard";

    private String question;
    private String Option1;
    private String Option2;
    private String Option3;
    private int Answernr;
    private String difficulty;

    public Question() {}

    public Question(String question, String option1, String option2, String option3, int answernr,String difficulty) {
        this.question = question;
        this.Option1 = option1;
        this.Option2 = option2;
        this.Option3 = option3;
        this.Answernr = answernr;
        this.difficulty=difficulty;
    }

    protected Question(Parcel in) {
        question = in.readString();
        Option1 = in.readString();
        Option2 = in.readString();
        Option3 = in.readString();
        Answernr = in.readInt();
        difficulty=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(Option1);
        dest.writeString(Option2);
        dest.writeString(Option3);
        dest.writeInt(Answernr);
        dest.writeString(difficulty);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getOption3() {
        return Option3;
    }

    public void setOption3(String option3) {
        Option3 = option3;
    }

    public int getAnswernr() {
        return Answernr;
    }

    public void setAnswernr(int answernr) {
        Answernr = answernr;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public static String[] getAllDifficultyLevels(){
        return new String[]{
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        };
    }
}
