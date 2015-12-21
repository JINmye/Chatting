package mj.project.chat_ex.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils { //keyboard를 자동으로 없애려고 한건데 사용안함
    public static final void hideKeyboard(Activity ctx){
        if(ctx.getCurrentFocus()!=null){
            InputMethodManager imn= (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),0);
        }
    }

    public static final void hideKeyboard(Activity ctx,View v){
        try {
            InputMethodManager imn= (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(v.getWindowToken(),0);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
