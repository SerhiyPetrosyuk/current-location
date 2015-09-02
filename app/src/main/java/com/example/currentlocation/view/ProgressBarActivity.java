package com.example.currentlocation.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.currentlocation.R;
import com.example.currentlocation.adapter.RecyclerAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serhiy Petrosyuk on 14.08.15.
 */
public class ProgressBarActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSetProgressButton;
    private Button mDeleteProgressButton;
    private EditText mProgressEditText;
    private TextView mCurrentValueTextView;
    private TextView mCurrentWaterTextView;
    private CircularProgressView mProgressView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private int mPrevProgress;
    private int mPrevProgressInMl;
    private float mTarget = 2475; // ml.

    public static final int TIME = 0;
    public static final int WATER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        mSetProgressButton = (Button) findViewById(R.id.btn_set_progress);
        mDeleteProgressButton = (Button) findViewById(R.id.btn_remove_progress);
        mProgressEditText = (EditText) findViewById(R.id.et_progress_input);
        mProgressView = (CircularProgressView) findViewById(R.id.progress_view);
        mCurrentValueTextView = (TextView) findViewById(R.id.tv_current_value);
        mCurrentWaterTextView = (TextView) findViewById(R.id.tv_current);

        mSetProgressButton.setOnClickListener(this);
        mDeleteProgressButton.setOnClickListener(this);
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        float progress = Float.parseFloat(mProgressEditText.getText().toString());
        switch (v.getId()) {
            case R.id.btn_set_progress:
                updateProgressBar(progress);
                addItem(mProgressEditText.getText().toString());
                break;
            case R.id.btn_remove_progress:
                progress *= -1;
                updateProgressBar(progress);
                break;
            default:
                break;
        }
    }

    private void updateProgressBar(float progress) {
        int progressInMl = (int) (mPrevProgressInMl + progress);
        int progressInPercents = (int) ((progress / mTarget) * 100) + mPrevProgress;
        final ObjectAnimator animation = ObjectAnimator.ofFloat(mProgressView, "progress", mPrevProgress, progressInPercents);

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(mPrevProgress, progressInPercents);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                mCurrentValueTextView.setText(Integer.toString(currentValue));
            }
        });

        final ValueAnimator currentValueAnimator = ValueAnimator.ofInt(mPrevProgressInMl, progressInMl);
        currentValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                mCurrentWaterTextView.setText(getString(R.string.current, Integer.toString(currentValue)));
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.play(animation).before(valueAnimator).before(currentValueAnimator);
        animatorSet.start();

        mPrevProgressInMl = progressInMl;
        mPrevProgress = progressInPercents;

    }

    private void initRecyclerView() {
        mRecyclerAdapter = new RecyclerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addItem(String progress) {
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR);
        int mins = c.get(Calendar.MINUTE);
        String time = hours + ":" + mins;
        Map<Integer, String> item = new HashMap<>();
        item.put(WATER, progress);
        item.put(TIME, time);

        mRecyclerAdapter.addItem(item);
     }
 }
