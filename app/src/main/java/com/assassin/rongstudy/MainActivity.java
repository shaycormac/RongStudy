package com.assassin.rongstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.assassin.rongstudy.util.BitMap2File;
import com.assassin.rongstudy.util.RongUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpParams params = new HttpParams();
        params.put("Account","dingweichen");
        params.put("Password", "Iris198925~");
        //请求(先从后台获取一个token,然后通过这个token传给融云)
        OkGo.post("http://172.16.55.29:9527/api/gm/ERP_SMSAPI/LogOn")
                .tag(this)
                .headers(new HttpHeaders("Content-Type","application/x-www-form-urlencoded"))
                .params(params)
                ./*execute(new AbsCallback<Object>() 
                {

                    @Override
                    public Object convertSuccess(Response response) throws Exception 
                    {
                        VolleyLog.d("convertSuccess :respond %s",response.toString());
                        return response.body();
                    }

                    @Override
                    public void onSuccess(Object o, Call call, Response response) 
                    {
                        VolleyLog.d("hehe %s",o.toString());
                        VolleyLog.d("respond %s",response.toString());
                    }
                });*/
             execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) 
            {
             //   VolleyLog.d("获取的结果： %s", s);
                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                  String token = (String) jsonObject.get("Token");
                  //  VolleyLog.d("token的值为：%s",token);
                    //连接融云
                  //  jsonObject.getJSONObject("Token");
                    JSONObject jsonObject1 = new JSONObject(token);
                    String token1 = jsonObject1.getString("token");
                    String userId = jsonObject1.getString("userId");
                
                    RongUtil.connectRong(token1, null, null);
                } catch (JSONException e) 
                {
                    e.printStackTrace();
                }

            }
        });
        button = (Button) findViewById(R.id.btnStartChart);
        button1 = (Button) findViewById(R.id.btnStartChartRoom);
        button2 = (Button) findViewById(R.id.btnStartServer);
        button3 = (Button) findViewById(R.id.btnStartGroup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                RongUtil.startPrivateChat(MainActivity.this, "9527", "花花");    
            }
        });        
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                RongUtil.startChatroom(MainActivity.this,"9527","我是标题");
               // RongUtil.startPrivateChat(MainActivity.this, "9527", "花花");    
            }
        });        
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                RongUtil.startPublicService(MainActivity.this,"9527","我是标题");
            }
        });        
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                RongUtil.startGroup(MainActivity.this,"9527","我是标题");
            }
        });

        BitMap2File.getFilePath(this, "nima");
    }
}
