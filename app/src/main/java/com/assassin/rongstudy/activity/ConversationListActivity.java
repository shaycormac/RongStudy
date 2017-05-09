package com.assassin.rongstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.assassin.rongstudy.R;
//会话 Fragment 跟会话列表是完全一致的，您可以用同样的方式快速的配置好。
public class ConversationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
    }
}
