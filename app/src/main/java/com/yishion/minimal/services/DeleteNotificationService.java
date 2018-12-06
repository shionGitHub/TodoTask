package com.yishion.minimal.services;

import android.app.IntentService;
import android.content.Intent;

import com.yishion.minimal.activities.MainActivity;
import com.yishion.minimal.model.StoreRetrieveData;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import java.util.List;


public class DeleteNotificationService extends IntentService {


    public DeleteNotificationService() {
        super("DeleteNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String uuid = intent.getStringExtra(TodoNotificationService.TODOUUID);

        //获取本地的数据
        StoreRetrieveData mStoreRetrieveData = new StoreRetrieveData(this);
        List<TodoItem> mLocalDatas = mStoreRetrieveData.getDataList();
        //移除对应数据
        for (int i = 0; i < mLocalDatas.size(); i++) {
            TodoItem todoItem = mLocalDatas.get(i);
            if (todoItem.uuid.toString().equals(uuid)) {
                mLocalDatas.remove(todoItem);
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


}
