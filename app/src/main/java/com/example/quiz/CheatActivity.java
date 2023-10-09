package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    public static final String CHEAT_IDX = "cheat_idx"; // 传递给CheatActivity的问题索引
    public static final String IS_CHEATED = "is_cheated";  // 传递给QuizActivity的是否作弊的标志
    private TextView mTvAnswer;
    private Button mBtnCheat;
    private TextView mTVQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mTVQuestion = findViewById(R.id.tv_cheat_question);
        mTvAnswer = findViewById(R.id.tv_answer);
        mBtnCheat = findViewById(R.id.btn_show_answer);

        Intent intent = getIntent();//获取启动该Activity的Intent对象
        int idx =
                intent.getIntExtra(CHEAT_IDX,
                        0);
        mTVQuestion.setText(QuizActivity.sQuestionBank[idx].getTextResId());
        mBtnCheat.setOnClickListener(new View.OnClickListener() {//设置监听器
            @Override
            public void onClick(View view) {

                boolean answer = QuizActivity.sQuestionBank[idx].isAnswerTrue();
                mTvAnswer.setText(answer ? "正确" : "错误");
                Intent data = new Intent();
                data.putExtra(IS_CHEATED, true);
                setResult(RESULT_OK, data);
            }
        });
    }
}