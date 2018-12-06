package com.yishion.minimal.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.yishion.minimal.R;
import com.yishion.minimal.model.StoreRetrieveData;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.services.TodoNotificationService;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReminderActivity extends BaseActivity {

    @BindView(R.id.content)
    TextView tvContent;
    @BindView(R.id.button)
    Button btnRemove;
    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;

    private String uuid;
    private TodoItem todoItem;
    private List<TodoItem> mTodoItems;
    private StoreRetrieveData mStoreRetrieveData;

    @Override
    protected int contentID() {
        return R.layout.activity_reminder;
    }

    @Override
    protected boolean initArgs(Intent intent) {
        mStoreRetrieveData = new StoreRetrieveData(this);
        uuid = intent.getStringExtra(TodoNotificationService.TODOUUID);
        mTodoItems = mStoreRetrieveData.getDataList();
        for (TodoItem item : mTodoItems) {
            if (item.uuid.toString().equalsIgnoreCase(uuid)) {
                this.todoItem = item;
                this.todoItem.hasReminder = false;//已经提醒了
                break;
            }
        }
        return super.initArgs(intent);
    }

    @Override
    protected void initWidget() {
        tvContent.setText(todoItem.content);
        if (SharedPreferencesUtils.isThemeLight(this)) {
            textView.setTextColor(getResources().getColor(R.color.secondary_text));
            Drawable d = getResources().getDrawable(R.drawable.ic_alarm_add_grey);
            textView.setCompoundDrawables(d, null, null, null);
        }
        else {
            textView.setTextColor(getResources().getColor(R.color.secondary_text));
            Drawable d = getResources().getDrawable(R.drawable.ic_alarm_add_white);
            DrawableCompat.setTint(d, getResources().getColor(R.color.colorAccent));
            textView.setCompoundDrawables(d, null, null, null);
        }

        String[] array = getResources().getStringArray(R.array.snooze_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_text_view, array);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @OnClick(R.id.button)
    void onRemoveClick() {
        mTodoItems.remove(todoItem);
        mStoreRetrieveData.saveData(mTodoItems);
        SharedPreferencesUtils.saveDataChange(this, true);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            SharedPreferencesUtils.saveDataChange(this, true);
            //更新数据
            todoItem.date = saveTimetoData(valueFromSpinner());
            todoItem.hasReminder = true;
            int pos = mTodoItems.indexOf(todoItem);
            mTodoItems.set(pos, todoItem);
            mStoreRetrieveData.saveData(mTodoItems);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int valueFromSpinner() {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 60;
            default:
                return 0;
        }
    }

    private Date saveTimetoData(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);//增加这么多分钟
        return calendar.getTime();
    }

}
