package mj.project.chat_ex.dparse;

import android.app.Application;

import com.parse.Parse;

import mj.project.chat_ex.R;

public class DemoParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.ID), getString(R.string.ClientID));
    }
}
