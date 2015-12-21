package mj.project.chat_ex.dparse;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import mj.project.chat_ex.R;
import mj.project.chat_ex.custom.CustomActivity;
import mj.project.chat_ex.utils.AlertDialogFragment;


public class UserList extends CustomActivity { //userlist 정의 부분

    /*The Chat List*/
    private ArrayList<ParseUser> uLists;
    ListView lv_user_name;
    /*The User*/
    public static ParseUser parseUser;
    public static final String CONS = "";
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        lv_user_name = (ListView) findViewById(R.id.list);
        mUserAdapter=new UserAdapter();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        updateUserStatus(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListuser();
    }

    private void loadListuser() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.alert_loading));

        //ParseUser extend ParseObject

        ParseUser.getQuery().whereNotEqualTo("username", parseUser.getUsername()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                progressDialog.dismiss();
               if (list != null) {
                   if (list.size() == 0) {
                       Toast.makeText(UserList.this, getString(R.string.not_found_user), Toast.LENGTH_LONG).show();
                   }


                   uLists = new ArrayList<ParseUser>(list);

                   lv_user_name.setAdapter(mUserAdapter);

                    lv_user_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            startActivity(new Intent(UserList.this, Chat.class).putExtra("EXTRA_DATA",
                                    uLists.get(position).getUsername()));
                        }
                    });
                } else {
                    alertDialog();

                }
            }
        });

    }

    private void updateUserStatus(boolean online) {
        parseUser.put("online", online);
        parseUser.saveEventually();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void alertDialog() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        //dialogFragment.show(getFragmentManager(), "");
        dialogFragment.show(getFragmentManager(), "");
    }

    private class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return uLists.size();
        }

        @Override
        public Object getItem(int position) {
            return uLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.chat_item, null);

            }
            ParseUser parseUser = uLists.get(position);
            TextView tv_user = (TextView) convertView;
            tv_user.setText(parseUser.getUsername());


            tv_user.setCompoundDrawablesWithIntrinsicBounds(parseUser.getBoolean("online")
                    ? R.drawable.online : R.drawable.offline, 0, R.drawable.row_right, 0);
            return convertView;
        }
    }

}
