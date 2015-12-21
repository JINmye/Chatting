package mj.project.chat_ex.dparse;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mj.project.chat_ex.R;
import mj.project.chat_ex.custom.CustomActivity;
import mj.project.chat_ex.model.Conversation;

public class Chat extends CustomActivity {
    public static final String TAG=Chat.class.getSimpleName();
    private ArrayList<Conversation> consList;
    private ChatAdapter mChatAdapter;

    private EditText edtCompose;
    private String buddy;
    private Date lastMsgDate;
    private boolean isRunnning;
    private static Handler handler;
    //private String tag="에게";

    //notification implements
    private NotificationManager nm;
    private Bitmap m;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        consList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list_item);
        mChatAdapter = new ChatAdapter();
        list.setAdapter(mChatAdapter);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        edtCompose = (EditText) findViewById(R.id.edt_send_text);
        edtCompose.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE); //edit 넣을 타입 설정
        setTouchNClick(R.id.btn_send);
        buddy = getIntent().getStringExtra("EXTRA_DATA");
        //  getActionBar().setTitle(buddy);
        getSupportActionBar().setTitle(buddy);
        handler = new Handler();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        isRunnning = true;
        loadConversationList();
        Toast.makeText(Chat.this,"채팅방 입장", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunnning = false;
        Toast.makeText(Chat.this,"채팅방 퇴장", Toast.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_send) {
            sendMessage();

            /*nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            String t = edtCompose.getText().toString();
            Log.d("t의 값", t+"입니다");
            String text = buddy.toString();

            Notification notification;
            notification = new Notification.Builder(Chat.this).setContentTitle(text)
                    .setContentText(t).setSmallIcon(android.R.drawable.ic_input_add)
                    .setLargeIcon(m).setWhen(System.currentTimeMillis())
                    .setSound(Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/니뭐하노문자확인해라.mp3"))
                    . build();



            nm.notify(1234, notification); // 1234 means notification unique id.

            Toast.makeText(Chat.this, "Message 전송 완료", Toast.LENGTH_LONG).show();
            */
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendMessage() {
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (edtCompose.length() == 0) {
            Toast.makeText(Chat.this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        InputMethodManager imn= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.hideSoftInputFromWindow(edtCompose.getWindowToken(),0);

        String s=edtCompose.getText().toString();
        final Conversation c=new Conversation(s, new Date(), UserList.parseUser.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
        consList.add(c);
        mChatAdapter.notifyDataSetChanged();
        edtCompose.setText(null);

        ParseObject po=new ParseObject("Chat");
        po.put("sender",UserList.parseUser.getUsername());
        po.put("receiver",buddy);
        po.put("message",s);
        po.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    c.setStatus(Conversation.STATUS_SENT);
                } else {
                    c.setStatus(Conversation.STATUS_FAILED);
                }
                mChatAdapter.notifyDataSetChanged();
            }
        });

        String t = edtCompose.getText().toString();
        Log.d("t의 값", t + "입니다");
        String text = buddy.toString();


        Notification notification;
        notification = new Notification.Builder(Chat.this).setContentTitle(text)
                .setContentText(t).setSmallIcon(android.R.drawable.ic_input_add)
                .setLargeIcon(m).setWhen(System.currentTimeMillis())
                .setSound(Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/니뭐하노문자확인해라.mp3"))
                . build();

        nm.notify(1234, notification); // 1234 means notification unique id.

        //Toast.makeText(Chat.this, "Message 전송 완료", Toast.LENGTH_LONG).show();

    }

    private void loadConversationList() {
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");

        if (consList.size()==0) {
            //load all message...
            ArrayList<String> al = new ArrayList<String>();
            al.add(buddy);
            al.add(UserList.parseUser.getUsername());
            q.whereContainedIn("sender", al);
            q.whereContainedIn("receiver", al);

        } else {
            if (lastMsgDate != null) {
                q.whereGreaterThan("createdAt", lastMsgDate);
            }
            q.whereEqualTo("sender", buddy);
            q.whereEqualTo("receiver", UserList.parseUser.getUsername());

        }

        q.orderByDescending("createdAt"); // Date Time
        q.setLimit(30); //maximum 30임
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e ) {

                if (list != null && list.size() > 0) {
                    for (int i = list.size() - 1; i >= 0; i--) {
                        ParseObject po = list.get(i);
                        Conversation c = new Conversation(po.getString("message"), po.getCreatedAt(), po.getString("sender"));
                        consList.add(c);
                        if (lastMsgDate == null || lastMsgDate.before(c.getDate())) {
                            lastMsgDate = c.getDate();
                            //TODO
                        }
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunnning) {
                            loadConversationList();
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return consList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return consList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Conversation c = consList.get(position);

            if (c.isSent()) {
                convertView = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            } else {
                convertView = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            }
            TextView lbl = (TextView) convertView.findViewById(R.id.tv1);

            lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c.getDate().getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));
            lbl= (TextView) convertView.findViewById(R.id.tv2);
            lbl.setText(c.getMsg());
            lbl= (TextView) convertView.findViewById(R.id.tv3);
            if (c.isSent()){
                if(c.getStatus()==Conversation.STATUS_SENT){
                    lbl.setText("보냄");
                }else if(c.getStatus()==Conversation.STATUS_SENDING){
                    lbl.setText("보내는 중...");
                }else {
                    lbl.setText("Failed");
                }
            }else{
                lbl.setText("");
            }
            return convertView;
        }
    }
}
