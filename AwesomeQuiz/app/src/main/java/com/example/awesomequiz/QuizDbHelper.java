package com.example.awesomequiz;

import android.content.ContentValues;
import android.content.Context;
import com.example.awesomequiz.QuizContract.*;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION=1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db=db;

        final String SQL_CREATE_QUESTIONS_TABLE="CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT " +
                 ")";

                db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
                fillQuestionsTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);

    }

    private void fillQuestionsTable(){
        Question q1=new Question("What is an activity in Android?","A - Activity performs the actions on the screen","B - Manage the Application content","C - Screen UI",1,Question.DIFFICULTY_EASY);
        addQuestions(q1);
        Question q2=new Question("What are the layouts available in android?","A - Linear Layout","B - Frame Layout","C - All of above",3,Question.DIFFICULTY_EASY);
        addQuestions(q2);
        Question q3=new Question("How to stop the services in android?","A - finish()","B - stopSelf() and stopService()","C - system.exit() ",2,Question.DIFFICULTY_EASY);
        addQuestions(q3);
        Question q4=new Question("What does httpclient.execute() returns in android?","A - Http entity","B - Http response","C - Http result",2,Question.DIFFICULTY_MEDIUM);
        addQuestions(q4);
        Question q5=new Question("Is it mandatory to call onCreate() and onStart() in android?","A - No, we can write the program without writing onCreate() and onStart()","B - Yes, we should call onCreate() and onStart() to write the program","C - At least we need to call onCreate() once",1,Question.DIFFICULTY_MEDIUM);
        addQuestions(q5);
        Question q6=new Question("Can a class be immutable in android?","A - No, it can't","Yes, Class can be immutable","C - Can't make the class as final class",2,Question.DIFFICULTY_MEDIUM);
        addQuestions(q6);
        Question q7=new Question("Select a component which is NOT part of Android architecture.","A - Android Framework","B - Android Document","C - Linux Kernel",2,Question.DIFFICULTY_HARD);
        addQuestions(q7);
        Question q8=new Question("Required folder when Android project is created.","A - build/","B - bin","C - bin/",3,Question.DIFFICULTY_HARD);
        addQuestions(q8);
        Question q9=new Question("Adb stands for","A - Android Drive Bridge.","B - Android Debug Bridge.","C - Android Delete Bridge.",2,Question.DIFFICULTY_HARD);
        addQuestions(q9);



    }

    private void addQuestions(Question question){
        ContentValues cv=new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR,question.getAnswernr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY,question.getDifficulty());
        db.insert(QuestionsTable.TABLE_NAME,null,cv);
    }

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList=new ArrayList<>();
        db=getReadableDatabase();
        Cursor cr=db.rawQuery("SELECT * FROM "+ QuestionsTable.TABLE_NAME,null);

        if(cr.moveToFirst()){
            do {
                Question question=new Question();
                question.setQuestion(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswernr(cr.getInt(cr.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            }while(cr.moveToNext());
        }
        cr.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(String difficulty){
        ArrayList<Question> questionList=new ArrayList<>();
        db=getReadableDatabase();
        String[] selectionArgs=new String[]{difficulty};
        Cursor cr=db.rawQuery("SELECT * FROM "+ QuestionsTable.TABLE_NAME +
                " WHERE "+ QuestionsTable.COLUMN_DIFFICULTY + " = ?",selectionArgs);

        if(cr.moveToFirst()){
            do {
                Question question=new Question();
                question.setQuestion(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswernr(cr.getInt(cr.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cr.getString(cr.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            }while(cr.moveToNext());
        }
        cr.close();
        return questionList;
    }
}
