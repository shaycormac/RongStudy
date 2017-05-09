package com.assassin.rongstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpParams params = new HttpParams();
        params.put("Account","dingweichen");
        params.put("Password", "Iris198925~");
        //请求
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
                
    }
}
