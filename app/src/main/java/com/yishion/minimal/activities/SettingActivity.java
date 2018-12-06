package com.yishion.minimal.activities;

import com.yishion.minimal.R;
import com.yishion.minimal.frags.SettingFragment;

public class SettingActivity extends BaseActivity {

    @Override
    protected int contentID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mToolbar.setTitle(R.string.action_settings);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingFragment())
                .commit();

    }
}
