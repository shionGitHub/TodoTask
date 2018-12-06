package com.yishion.minimal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yishion.minimal.R;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TODO_ITEM = "TODO_ITEM";

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (!initArgs(getIntent())) {
            finish();
            return;
        }
        initTheme();
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(contentID());
        ButterKnife.bind(this);
        toolbar();
        initWidget();
        initDate();
    }

    protected boolean initArgs(Intent intent) {
        return true;
    }

    //初始化主题样式
    protected void initTheme() {
        if (SharedPreferencesUtils.isThemeLight(this)) {
            setTheme(R.style.CustomStyle_LightTheme);
        }
        else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
    }

    //初始化Window
    protected void initWindow() {

    }

    protected void toolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                //点Toolbar上的返回键，就像点android.R.id.home一样
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://Toolbar的返回键点击执行这里
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化数据
    protected void initDate() {

    }

    //初始化控件
    protected void initWidget() {

    }

    //获取布局id
    @LayoutRes
    protected abstract int contentID();


}
