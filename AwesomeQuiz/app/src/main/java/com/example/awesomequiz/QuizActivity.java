package com.example.awesomequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE="extrascore";
    private static final long COUNTDOWN_IN_MILLIS=30000;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewCountDown;
    private TextView textViewDifficulty;
    private TextView textViewQuestionCount;
    private RadioGroup rbgrp;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button confirmNextBtn;

    private static final String KEY_SCORE="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MIILIS_LEFT="keyMillisLeft";
    private static final String KEY_ANSWERED="keyAnswered";
    private static final String KEY_QUESTION_LIST="keyQuestionList";


    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score=0;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion=findViewById(R.id.textview_questions);
        textViewScore=findViewById(R.id.textview_score);
        textViewCountDown=findViewById(R.id.textview_countdown);
        textViewQuestionCount=findViewById(R.id.textview_questions_count);
        textViewDifficulty=findViewById(R.id.textview_difficulty);
        rbgrp=findViewById(R.id.radio_group);
        rb1=findViewById(R.id.radiobtn1);
        rb2=findViewById(R.id.radiobtn2);
        rb3=findViewById(R.id.radiobtn3);
        confirmNextBtn=findViewById(R.id.btn_confirm_next);

        Intent intent =getIntent();
        String difficulty=intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewDifficulty.setText("Difficulty: "+ difficulty);


        textColorDefaultRb=rb1.getTextColors();
        getTextColorDefaultCd=textViewCountDown.getTextColors();

        if(savedInstanceState==null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getQuestions(difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        }else{
            questionList=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCounter=savedInstanceState.getInt(KEY_QUESTION_COUNT);
            questionCountTotal=questionList.size();
            score=savedInstanceState.getInt(KEY_MIILIS_LEFT);
            timeLeftInMillis=savedInstanceState.getLong(KEY_MIILIS_LEFT);
            currentQuestion=questionList.get(questionCounter-1);
            answered=savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered){
                startCountDown();
            }else{
                updateCountDownText();
                showSolution();
            }
        }

        textViewScore.setText("Score: 0");

        confirmNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this,"plz select an option",Toast.LENGTH_LONG).show();
                    }
                }else{
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbgrp.clearCheck();

        if(questionCounter<questionCountTotal){
            currentQuestion=questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered=false;
            confirmNextBtn.setText("Confirm");

            timeLeftInMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();

        }else{
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText(){
        int min=(int)(timeLeftInMillis/1000)/60;
        int sec=(int)(timeLeftInMillis/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",min,sec);

        textViewCountDown.setText(timeFormatted);
        if(timeLeftInMillis<10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(getTextColorDefaultCd);
        }
    }


    private void checkAnswer(){
        answered=true;

        countDownTimer.cancel();
        RadioButton rbSelected=findViewById(rbgrp.getCheckedRadioButtonId());
        int answerNr=rbgrp.indexOfChild(rbSelected)+1;

        if(answerNr==currentQuestion.getAnswernr()){
            score++;
            textViewScore.setText("Score: "+score);
        }
        showSolution();
    }

    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswernr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is Correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is Correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is Correct");
                break;
        }

        if(questionCounter<questionCountTotal){
            confirmNextBtn.setText("NEXT");
        }else{
            confirmNextBtn.setText("FINISH");
        }
    }

    private void finishQuiz(){
        Intent resultIntent=new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000>System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this,"Press back Again to exit",Toast.LENGTH_SHORT).show();
        }

        backPressedTime=System.currentTimeMillis();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putInt(KEY_SCORE,score);
        outState.putLong(KEY_MIILIS_LEFT,timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }
}
