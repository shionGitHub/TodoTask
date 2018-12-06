package com.yishion.minimal.model;

import android.graphics.Color;
import android.text.TextUtils;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

//要展示的任务列表
public class TodoItem implements Serializable {

    public static final String CONTENT = "CONTENT";
    public static final String DATE = "date";
    public static final String COLOR = "color";
    public static final String ID = "uuid";
    public static final String REMINDER = "reminder";

    public String content;//任务内容
    public Date date;//任务日期
    public int color;//任务字体的颜色
    public UUID uuid;//相当于身份唯一性的id
    public boolean hasReminder;//是否需要通知


    private TodoItem() {
        this.uuid = UUID.randomUUID();
        this.color = Color.parseColor("#727272");
        this.content = "clean my data";
    }

    public TodoItem(String body, boolean hasReminder, Date date) {
        this.content = body;
        this.hasReminder = hasReminder;
        this.uuid = UUID.randomUUID();
        this.color = ColorGenerator.MATERIAL.getRandomColor();
        this.date = date;
    }


    //将对象转化成json
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (TextUtils.isEmpty(content)) {
                jsonObject.put(CONTENT, "");
            }
            else {
                jsonObject.put(CONTENT, content);
            }
            if (date != null) {
                jsonObject.put(DATE, date.getTime());
            }
            jsonObject.put(COLOR, color);
            jsonObject.put(ID, uuid.toString());
            jsonObject.put(REMINDER, hasReminder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    public static TodoItem getObject(JSONObject object) {
        TodoItem item = new TodoItem();
        try {
            item.content = object.optString(CONTENT);
            item.uuid = UUID.fromString(object.optString(ID));
            item.color = object.optInt(COLOR);
            item.hasReminder = object.optBoolean(REMINDER);
            if (object.has(DATE)) {
                item.date = new Date(object.optLong(DATE));
            }
            else {
                item.date = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(uuid, todoItem.uuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid);
    }
}
