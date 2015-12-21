package mj.project.chat_ex.dparse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import mj.project.chat_ex.R;


public class IntroActivity extends AppCompatActivity {

    Handler handler_intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handler_intro = new Handler();
        handler_intro.postDelayed(run_intro, 3000);

    }
    Runnable run_intro = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };
    @Override
    public void onBackPressed(){

    }

}
