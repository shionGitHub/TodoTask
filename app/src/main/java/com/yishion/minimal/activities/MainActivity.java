package com.yishion.minimal.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yishion.minimal.R;
import com.yishion.minimal.adapter.BaseListAdapter;
import com.yishion.minimal.adapter.ItemClickListener;
import com.yishion.minimal.model.StoreRetrieveData;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.services.TodoNotificationService;
import com.yishion.minimal.utils.SharedPreferencesUtils;
import com.yishion.minimal.views.RecyclerOnScrollListener;
import com.yishion.minimal.views.RecyclerViewSupportEmptyView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements
        ItemClickListener<TodoItem>, View.OnClickListener {
    private static final int REQUEST_ITEM_RESULT = 0x100;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.recycler)
    RecyclerViewSupportEmptyView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private BaseListAdapter adapter;
    private List<TodoItem> mTodoList;
    private final StoreRetrieveData mStoreRetrieveData = new StoreRetrieveData(this);

    @Override
    protected int contentID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        mRecyclerView.setEmptyView(findViewById(R.id.empty));
        adapter = new BaseListAdapter(this, mTodoList = getLocalToDoData());
        adapter.setItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerOnScrollListener() {
            @Override
            public void hide() {
                //这里也可以自己定义一些复杂的动画
                mFab.hide();
            }

            @Override
            public void show() {
                //这里也可以自己定义一些复杂的动画
                mFab.show();
            }
        });
        mFab.setOnClickListener(this);

        ItemTouchHelper mTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//支持上下拖拽
                int swipFlag = ItemTouchHelper.START | ItemTouchHelper.END;//支持左右滑动
                return makeMovementFlags(dragFlag, swipFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder holder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPos = holder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                Collections.swap(mTodoList, fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                saveDate(mTodoList);
                return true;
            }

            private int mJustDeleteIndex = -1;
            private TodoItem mJustDeleteTodoItem;

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int pos = viewHolder.getAdapterPosition();
                mJustDeleteIndex = pos;
                mJustDeleteTodoItem = mTodoList.remove(pos);
                adapter.notifyItemRemoved(pos);
                saveDate(mTodoList);
                Snackbar snackbar = Snackbar.make(coordinator, R.string.app_name, Toast.LENGTH_SHORT);
                snackbar.setAction(R.string.noRemove, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTodoList.add(mJustDeleteIndex, mJustDeleteTodoItem);
                        adapter.notifyItemInserted(mJustDeleteIndex);
                        saveDate(mTodoList);
                    }
                });
                snackbar.show();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });
        mTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    protected void initDate() {
        //加载本地数据,采用的是文件存储

    }

    //加载本地数据
    private List<TodoItem> getLocalToDoData() {
        return mStoreRetrieveData.getDataList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting: {
                Intent setting = new Intent(this,
                        SettingActivity.class);
                startActivity(setting);
            }
            break;
            case R.id.about: {
                Intent about = new Intent(this,
                        AboutActivity.class);
                startActivity(about);
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //点击item
    @Override
    public void onItemClick(int position, TodoItem item) {
        Intent i = new Intent(MainActivity.this, AddTodoActivity.class);
        i.putExtra(TODO_ITEM, item);
        startActivityForResult(i, REQUEST_ITEM_RESULT);
    }

    //fab点击
    @Override
    public void onClick(View v) {
        TodoItem item = new TodoItem("", false, null);
        Intent i = new Intent(MainActivity.this, AddTodoActivity.class);
        i.putExtra(TODO_ITEM, item);
        startActivityForResult(i, REQUEST_ITEM_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == REQUEST_ITEM_RESULT
                && data != null) {
            TodoItem item = (TodoItem) data.getSerializableExtra(TODO_ITEM);

            if (TextUtils.isEmpty(item.content)) {
                return;
            }
            addAlarmForTodoItem(item);
            if (mTodoList.contains(item)) {
                //更新适配器的数据
                int index = mTodoList.indexOf(item);
                mTodoList.set(index, item);
                adapter.notifyItemChanged(index);
            }
            else {
                //新数据要添加到适配器
                mTodoList.add(item);
                adapter.notifyItemInserted(mTodoList.size() - 1);
            }
            //存储到文件里面
            saveDate(mTodoList);
        }

    }


    private void saveDate(List<TodoItem> list) {
        mStoreRetrieveData.saveData(list);
    }

    @Override
    public void onBackPressed() {
        //这里这样做
        // 点返回键的时候，让应用退到后台
        //有的手机魅族点击Home键也会执行这里
        moveTaskToBack(true);
    }


    //给那些需要提醒的任务添加闹钟提示
    private void addAlarmForTodoItem(TodoItem todoItem) {
        if (todoItem != null && todoItem.hasReminder) {
            int requestCode = todoItem.uuid.hashCode();
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, TodoNotificationService.class);
            intent.putExtra(TodoNotificationService.TODOTEXT, todoItem.content);
            intent.putExtra(TodoNotificationService.TODOUUID, todoItem.uuid.toString());
            PendingIntent pi = PendingIntent.getService(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, todoItem.date.getTime(), pi);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //主题发生改变
        if (SharedPreferencesUtils.isReCreate(this)) {
            SharedPreferencesUtils.reCreate(this, false);
            recreate();
        }

        //更新数据
        if (SharedPreferencesUtils.isDataChanged(this)) {
            SharedPreferencesUtils.saveDataChange(this, false);
            adapter.addDataList(getLocalToDoData());
        }
    }
}
