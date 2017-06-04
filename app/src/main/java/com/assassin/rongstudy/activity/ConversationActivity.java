package com.assassin.rongstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.assassin.rongstudy.R;
import com.assassin.rongstudy.util.VolleyLog;

//会话界面
public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getIntent().getData().getQueryParameter("title");
        VolleyLog.d("得到的标题为 %s",getIntent().getData().getQueryParameter("title"));
    }
}
