package com.yishion.minimal.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.yishion.minimal.R;
import com.yishion.minimal.activities.MainActivity;
import com.yishion.minimal.activities.ReminderActivity;
import com.yishion.minimal.model.StoreRetrieveData;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import java.util.List;

public class TodoNotificationService extends IntentService {

    public static final String TODOTEXT = "com.yishion.minimal.TODOTEXT";
    public static final String TODOUUID = "com.yishion.minimal.TODOUUID";

    private String mTodoText;
    private String mTodoUUID;

    public TodoNotificationService() {
        super("TodoNotificationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = intent.getStringExtra(TODOUUID);

        notification();

        //获取本地的数据
        StoreRetrieveData mStoreRetrieveData = new StoreRetrieveData(this);
        List<TodoItem> mLocalDatas = mStoreRetrieveData.getDataList();

        //移除对应数据
        for (int i = 0; i < mLocalDatas.size(); i++) {
            TodoItem todoItem = mLocalDatas.get(i);
            if (todoItem.uuid.toString().equals(mTodoUUID)) {
                todoItem.hasReminder = false;
                break;
            }
        }
        //保存
        mStoreRetrieveData.saveData(mLocalDatas);
        //标记变化
        SharedPreferencesUtils.saveDataChange(this, true);
        //通知
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);


    }

    private void notification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, null);
        builder.setSmallIcon(R.drawable.ic_done);
        builder.setContentText(mTodoText);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setTicker("TodoItem Coming !!!");
        builder.setWhen(System.currentTimeMillis());
        builder.setOnlyAlertOnce(true);

        Intent i = new Intent(this, ReminderActivity.class);
        i.putExtra(TODOUUID, mTodoUUID);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                mTodoUUID.hashCode(),
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
        deleteIntent.putExtra(TODOUUID, mTodoUUID);
        PendingIntent deletePi = PendingIntent.getService(
                this,
                mTodoUUID.hashCode(),
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setDeleteIntent(deletePi);

        //适配8.0的通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getClass().getName();
            String channelName = String.valueOf(getClass().getName().hashCode());
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
            //设置渠道号id
            builder.setChannelId(channelId);
        }

        nm.notify(mTodoUUID.hashCode(), builder.build());
    }


}
