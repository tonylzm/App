package com.example.quiz;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String KEY_IDX = "CurIdx";
    public static final int REQ_SHOW_ANSWER = 0x0F01;
    private TextView mTvQuestion;
    private Button mBtnTrue;
    private Button mBtnFalse;
    private Button mBtnPrev;
    private Button mBtnNext;
    private Button mBtnCheat;
    public static Question[] sQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, true),
            new Question(R.string.question_africa, true),
    };
    private int mCurIdx = 0;
    // 03 方法配套
    private ActivityResultLauncher<Intent> register;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_IDX, mCurIdx);
        Log.d("GeoQuizLog", "onSaveInstanceState CurIdx:"+mCurIdx+"@"+System.identityHashCode(this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null)
        {
            mCurIdx = savedInstanceState.getInt(KEY_IDX);
            Log.d("GeoQuizLog", "from savedInstanceState CurIdx:"+mCurIdx+"@"+System.identityHashCode(this));
        }

        mTvQuestion = findViewById(R.id.txtQuestion);
        mBtnTrue = findViewById(R.id.btnTrue);
        mBtnFalse = findViewById(R.id.btnFalse);
        mBtnPrev = findViewById(R.id.btnPrev);
        mBtnNext = findViewById(R.id.btnNext);
        mBtnCheat = findViewById(R.id.btn_cheat);

        mTvQuestion.setText(sQuestionBank[mCurIdx].getTextResId());
        Log.d("GeoQuizLog", "onCreate CurIdx:"+mCurIdx+"@"+System.identityHashCode(this));
        mBtnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        mBtnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurIdx = (mCurIdx + 1) % sQuestionBank.length;
                Log.d("GeoQuizLog", "btnNext CurIdx:"+mCurIdx+"@"+System.identityHashCode(this));
                mTvQuestion.setText(sQuestionBank[mCurIdx].getTextResId());
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurIdx == 0) {
                    mCurIdx = sQuestionBank.length - 1;
                }
                else {
                    mCurIdx = (mCurIdx - 1) % sQuestionBank.length;
                }
                Log.d("GeoQuizLog", "btnPrev CurIdx:"+mCurIdx+"@"+System.identityHashCode(this));
                mTvQuestion.setText(sQuestionBank[mCurIdx].getTextResId());
            }
        });
        register = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result != null) {
                            Intent data = result.getData();
                            if (data != null && result.getResultCode() == RESULT_OK) {
                                boolean isCheated =
                                        data.getBooleanExtra(CheatActivity.IS_CHEATED,
                                                false);
                                if (isCheated) {
                                    Toast.makeText(getApplicationContext(), "使用答案提示", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "没有使用答案提示", LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "从CheatAcitivity返回", LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        mBtnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(QuizActivity.this,CheatActivity.class);
                //intent.putExtra(ANSWER_CHEAT, sQuestionBank[mCurrentIdx].isAnswerTrue());
                intent.putExtra(CheatActivity.CHEAT_IDX, mCurIdx);
                // 01 不需要回传数据
                //startActivity(intent);
                // 02 需要回传数据，但是Google已经废弃了startActivityForResult和onActivityResult方法。
                // startActivityForResult(intent, REQ_SHOW_ANSWER);
                // 03 需要回传数据，用新版的ActivityResultContract和ActivityResultLauncher
                register.launch(intent);

            }
        });
    }
    // 02 配套
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SHOW_ANSWER) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    boolean isCheated =
                            data.getBooleanExtra(CheatActivity.IS_CHEATED,
                                    false);
                    if (isCheated) {
                        Toast.makeText(getApplicationContext(), "使用答案提示", LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "没有使用答案提示", LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "从CheatAcitivity返回", LENGTH_SHORT).show();
            }
        }

    }

     */
    private void checkAnswer(boolean answer) {
        if (sQuestionBank[mCurIdx].isAnswerTrue() == answer) {
            Toast.makeText(QuizActivity.this,
                    R.string.answer_right,
                    LENGTH_SHORT
            ).show();
        }
        else {
            Toast.makeText(QuizActivity.this,
                    R.string.answer_incorrect,
                    LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("GeoQuizLog","OnStart"+"@"+System.identityHashCode(this));
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("GeoQuizLog","onRestart"+"@"+System.identityHashCode(this));

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("GeoQuizLog","onStop"+"@"+System.identityHashCode(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GeoQuizLog","onDestroy"+"@"+System.identityHashCode(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GeoQuizLog","onPause"+"@"+System.identityHashCode(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GeoQuizLog","onResume"+"@"+System.identityHashCode(this));
    }


}