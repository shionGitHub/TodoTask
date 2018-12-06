package com.yishion.minimal.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class StoreRetrieveData {


    private Context mContext;
    private static final String FILE_NAME = "todoItem.json";

//    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public StoreRetrieveData(Context mContext) {
        this.mContext = mContext;
    }

    public void saveData(final List<TodoItem> list) {
        if (list == null || list.size() == 0) return;

//        mExecutorService.execute(new Runnable() {
//            @Override
//            public void run() {
                save(list);
//            }
//        });


    }

    private void save(List<TodoItem> list) {
        JSONArray array = new JSONArray();
        try {
            for (TodoItem item : list) {
                JSONObject object = new JSONObject(item.toJSON());
                array.put(object);
            }

            FileOutputStream output = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(array.toString());
            writer.flush();
            output.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TodoItem> getDataList() {
        List<TodoItem> items = new ArrayList<>();
        try {
            FileInputStream input = mContext.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }

            JSONArray array = new JSONArray(sb.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                items.add(TodoItem.getObject(jsonObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


}
