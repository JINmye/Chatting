package mj.project.chat_ex.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;


public class TouchEffect implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Drawable drawable = v.getBackground();
            drawable.mutate();//co the thay doi
            drawable.setAlpha(150);
            v.setBackgroundDrawable(drawable);

        } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                || event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = v.getBackground();
            drawable.setAlpha(255);
            v.setBackgroundDrawable(drawable);
        }
        return false;
    }
}
