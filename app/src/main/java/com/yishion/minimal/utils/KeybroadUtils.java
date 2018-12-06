package com.yishion.minimal.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeybroadUtils {

    //隐藏软键盘
    public static void hideKeyboard(EditText et) {
        if (et == null) return;
        Context ctx = et.getContext();
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    //打开软键盘
    public static void showKeyboard(EditText et) {
        if (et == null) return;
        Context ctx = et.getContext();
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

}
