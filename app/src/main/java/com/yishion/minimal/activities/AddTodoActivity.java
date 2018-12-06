package com.yishion.minimal.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yishion.minimal.R;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.utils.DateUtils;
import com.yishion.minimal.utils.KeybroadUtils;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;

//添加提示事件
public class AddTodoActivity extends BaseActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.inputLayout)
    TextInputLayout inputLayout;
    @BindView(R.id.editContent)
    EditText edtContent;
    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;

    @BindView(R.id.imgAlarm)
    ImageButton imgAlarm;
    @BindView(R.id.tv)
    TextView tv;

    @BindView(R.id.edtDate)
    EditText edtDate;
    @BindView(R.id.edtTime)
    EditText edtTime;

    @BindView(R.id.llRenderTimeAndDate)
    LinearLayout llRenderTimeAndDate;

    @BindView(R.id.tvRenderText)
    TextView tvRenderText;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private TodoItem todoItem;
    private String content;

    private boolean hasReminder;

    private Date mReminderDate;

    @Override
    protected int contentID() {
        return R.layout.activity_add_todo;
    }

    @Override
    protected boolean initArgs(Intent intent) {
        todoItem = (TodoItem) intent.getSerializableExtra(TODO_ITEM);
        if (todoItem != null) {
            content = todoItem.content;
            mReminderDate = todoItem.date;
            hasReminder = todoItem.hasReminder;
        }

        return super.initArgs(intent);
    }

    @Override
    protected void initWidget() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_close);
            mToolbar.setTitle("");
        }
        if (SharedPreferencesUtils.isThemeLight(this)) {
            imgAlarm.setImageResource(R.drawable.ic_alarm_add_grey);
            tv.setTextColor(getResources().getColor(R.color.secondary_text));
        }
        else {
            imgAlarm.setImageResource(R.drawable.ic_alarm_add_white);
            tv.setTextColor(Color.WHITE);
        }

        edtContent.setText(TextUtils.isEmpty(content) ? "" : content);
        switchCompat.setChecked(hasReminder);
        llRenderTimeAndDate.setVisibility(hasReminder ? View.VISIBLE : View.INVISIBLE);
        setDateAndTimeReminder();

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddTodoActivity.this.content = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 2) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError(getResources().getString(R.string.input_content));
                }
                else {
                    inputLayout.setErrorEnabled(false);
                }
            }
        });

        edtContent.requestFocus();
        KeybroadUtils.showKeyboard(edtContent);

    }

    @OnClick({R.id.llBottom, R.id.appbar})
    void onBottomClick() {
        KeybroadUtils.hideKeyboard(edtContent);
    }

    @OnTouch(R.id.toolbar)
    boolean onToolbarTouch() {
        KeybroadUtils.hideKeyboard(edtContent);
        return false;
    }

    @OnCheckedChanged(R.id.switch_compat)
    void onSwitchReminderTime(SwitchCompat mView, boolean isChecked) {
        hasReminder = isChecked;
        if (!hasReminder) {
            mReminderDate = null;//这里赋值null，因为没有设置提醒，一旦打开了，mRenderDate就有值了
        }
        //展开还是收缩布局
        expandOrCollapseDateAndTimeLayout(isChecked);
        //设置日期和时间的提醒
        setDateAndTimeEditText();
        //设置数据为日期和时间，以及提示信息
        setDateAndTimeReminder();
        //隐藏键盘
        KeybroadUtils.hideKeyboard(edtContent);
    }

    //展开还是收缩布局
    private void expandOrCollapseDateAndTimeLayout(boolean f) {
        if (f) {
            llRenderTimeAndDate.animate()
                    .alpha(1.0f)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            llRenderTimeAndDate.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
        }
        else {
            llRenderTimeAndDate.animate()
                    .alpha(0.0f)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            llRenderTimeAndDate.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            llRenderTimeAndDate.setVisibility(View.INVISIBLE);
                        }
                    })
                    .start();
        }
    }

    //设置日期内容
    private void setDateEditText() {
        if (mReminderDate != null) {
            edtDate.setText(DateUtils.formatDate(this, mReminderDate));
        }
    }

    //设置时间内容
    private void setTimeEditText() {
        if (mReminderDate != null) {
            edtTime.setText(DateUtils.formatTime(this, mReminderDate));
        }
    }

    //设置时间和日期的提醒
    private void setDateAndTimeEditText() {
        //有通知提醒，并且提醒的日期不为null
        if (todoItem.hasReminder && mReminderDate != null) {

        }
        else {
            //说明1，消息已经提醒过了，说明是过期时间
            //2.提醒的时间是空的

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
            calendar.set(Calendar.MINUTE, 0);

            mReminderDate = calendar.getTime();
        }
        String date = DateUtils.formatDate(this, mReminderDate);
        String time = DateUtils.formatTime(this, mReminderDate);
        edtDate.setText(date);
        edtTime.setText(time);
    }

    //设置数据为日期和时间，以及提示信息
    private void setDateAndTimeReminder() {
        if (mReminderDate != null) {
            tvRenderText.setVisibility(View.VISIBLE);
            if (mReminderDate.before(new Date())) {
                tvRenderText.setTextColor(Color.RED);
                tvRenderText.setText(getResources().getString(R.string.date_error_check_again));
                return;
            }

            String d = DateUtils.formatDate(this, mReminderDate);
            String t = DateUtils.formatTime(this, mReminderDate);
            String a = DateUtils.format(this, "a", mReminderDate);
            edtDate.setText(d);
            edtTime.setText(t);
            String format = getResources().getString(R.string.remind_date_and_time);
            tvRenderText.setText(String.format(Locale.getDefault(), format, d, t, a));
            tvRenderText.setTextColor(getResources().getColor(R.color.secondary_text));

        }
        else {
            tvRenderText.setVisibility(View.INVISIBLE);
        }

    }


    //设置日期选择
    @OnClick(R.id.edtDate)
    void onDateSelect() {
        Calendar c = Calendar.getInstance();
        c.setTime(mReminderDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog();
        dialog.initialize(this, year, month, day);
        if (!SharedPreferencesUtils.isThemeLight(this)) {
            dialog.setThemeDark(true);
        }
        dialog.show(getFragmentManager(), "DateFragment");
    }

    //这里在选择值时，无需考虑mRenderDate为null的情形
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (c.getTime().before(new Date())) {
            Toast.makeText(this,
                    "My time-machine is a bit rusty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        c.setTime(mReminderDate);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mReminderDate = c.getTime();
        setDateEditText();
        setDateAndTimeReminder();
    }

    //设置时间选择
    @OnClick(R.id.edtTime)
    void onTimeSelect() {
        Calendar c = Calendar.getInstance();
        c.setTime(mReminderDate);

        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        boolean time24 = DateFormat.is24HourFormat(this);
        TimePickerDialog dialog = new TimePickerDialog();
        dialog.initialize(this, hour, minute, time24);
        if (!SharedPreferencesUtils.isThemeLight(this)) {
            dialog.setThemeDark(true);
        }
        dialog.show(getFragmentManager(), "TimeFragment");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(mReminderDate);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        mReminderDate = c.getTime();
        setTimeEditText();
        setDateAndTimeReminder();
    }

    @OnClick(R.id.fab)
    void onSendClick() {

        if (edtContent.length() < 1) {
            edtContent.setError(getResources().getString(R.string.todo_error));
            return;
        }
        else if (mReminderDate != null && mReminderDate.before(new Date())) {
            setResult(Activity.RESULT_CANCELED);
        }
        else {
            KeybroadUtils.hideKeyboard(edtContent);
            makeResult();
            finish();
        }
    }

    private void makeResult() {
        Intent i = new Intent();
        todoItem.date = mReminderDate;
        String body = edtContent.getText().toString();
        if (!TextUtils.isEmpty(body)) {
            todoItem.content = Character.toUpperCase(body.charAt(0)) + body.substring(1);
        }
        else {
            todoItem.content = "";
        }
        todoItem.hasReminder = this.hasReminder;
        i.putExtra(TODO_ITEM, todoItem);
        setResult(Activity.RESULT_OK, i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeybroadUtils.hideKeyboard(edtContent);
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
