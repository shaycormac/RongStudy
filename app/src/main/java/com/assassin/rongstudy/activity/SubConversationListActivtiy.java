package com.assassin.rongstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.assassin.rongstudy.R;
//这是您的聚合会话列表 Activity 对应的布局文件：subconversationlist.xml。 
// 注意 android:name 固定为融云的 SubConversationListFragment。
public class SubConversationListActivtiy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_conversation_list_activtiy);
    }
}
