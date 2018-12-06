package com.yishion.minimal.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yishion.minimal.R;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.version)
    TextView tvVersion;

    @Override
    protected int contentID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initWidget() {
        mToolbar.setTitle(R.string.about);
        PackageManager pm = getPackageManager();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            String format = getResources().getString(R.string.app_version);
            tvVersion.setText(
                    String.format(format, versionName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
