package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank;
    private int mCurrentIndex = 0;
    private int[] flag;   //答题情况，0未答，1正确，2错误

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //初始化问题数组
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] answers = res.getStringArray(R.array.answers);
        int length = questions.length;
        mQuestionBank = new Question[length];
        for (int i = 0; i < length; i++) {
            mQuestionBank[i] = new Question(questions[i], answers[i]);
        }
        //初始化答题情况数组
        flag = new int[length];

        //引用文本框，将其设置为问题内容
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

        //正确按钮
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                if (flag[mCurrentIndex] != 0) {        //已答题
                    mTrueButton.setEnabled(false);       //禁掉作答按钮
                    mFalseButton.setEnabled(false);
                }
            }
        });
        //错误按钮
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                if (flag[mCurrentIndex] != 0) {        //已答题
                    mTrueButton.setEnabled(false);       //禁掉作答按钮
                    mFalseButton.setEnabled(false);
                }
            }
        });
        //设置next按钮
        mNextButton = (Button) findViewById(R.id.next_button);//下一题
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mQuestionBank.length - 1)
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                if (flag[mCurrentIndex] == 0) {   //按钮解禁
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                } else {
                    mTrueButton.setEnabled(false);       //禁掉作答按钮
                    mFalseButton.setEnabled(false);
                }
            }
        });
        Button mPreButton = (Button) findViewById(R.id.pre_button);//上一题
        mPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0)
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
                if (flag[mCurrentIndex] == 0) {   //按钮解禁
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                } else {
                    mTrueButton.setEnabled(false);       //禁掉作答按钮
                    mFalseButton.setEnabled(false); //禁掉作答按钮
                }
            }
        });

    }

    //更新问题
    private void updateQuestion() {
        String question = mQuestionBank[mCurrentIndex].getQuestionString();
        mQuestionTextView.setText(question);
    }

    //判断答案对错
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = Boolean.parseBoolean(mQuestionBank[mCurrentIndex].getAnswerString());
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            flag[mCurrentIndex] = 1;   //记录
        } else {
            messageResId = R.string.incorrect_toast;
            flag[mCurrentIndex] = 2;    //记录
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if (mCurrentIndex == mQuestionBank.length - 1) {  //答题完毕
            double trueNum = 0;
            for (int i = 0; i < flag.length; i++) {
                if (flag[i] == 1)
                    trueNum++;
            }
            trueNum = trueNum / (mQuestionBank.length * 1.0);   //正确率
            NumberFormat format = NumberFormat.getPercentInstance();
            format.setMaximumFractionDigits(2);//设置保留几位小数
            System.out.println("百分比为：" + format.format(trueNum));
            Toast.makeText(this, "正确率:" + format.format(trueNum), Toast.LENGTH_SHORT).show();
        }
    }
}

