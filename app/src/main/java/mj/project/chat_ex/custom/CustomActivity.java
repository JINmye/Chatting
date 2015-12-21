package mj.project.chat_ex.custom;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mj.project.chat_ex.R;
import mj.project.chat_ex.utils.TouchEffect;


public class CustomActivity extends AppCompatActivity implements View.OnClickListener {
    public static final TouchEffect TOUCH=new TouchEffect();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
    }

    private void setupActionBar() {
        final android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar==null){
            return;
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_actionbar));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
    public View setTouchNClick(int id) {
        View v = setClick(id); //touch 되면 build된 파일의 id를 읽어옴
    if(v!=null){
        v.setOnTouchListener(TOUCH);
    }
        return v;
    }
    public View setClick(int id){
        View v=findViewById(id);
        if(v!=null){
            v.setOnClickListener(this);
        }
        return v;
    }
    @Override
    public void onClick(View v) {

    }
}
