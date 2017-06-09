package com.assassin.rongstudy;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.assassin.rongstudy.util.BitMap2File;
import com.assassin.rongstudy.util.RongUtil;
import com.assassin.rongstudy.util.VolleyLog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //权限回调所用的回调码
    public static final int MY_PERMISSONS_REQUEST_CALL_PHONE = 1;

    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
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
                
                    RongUtil.connectRong(token1, "谢伊寇马克", "https://pic.pocketuni.com.cn/data/sys_pic/entry/xy_grow.png?v=5.9.01493347234");
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
        //权限回调
        if (!MPermissions.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSONS_REQUEST_CALL_PHONE)
        &&!MPermissions.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSONS_REQUEST_CALL_PHONE)) {
            MPermissions.requestPermissions(activity,MY_PERMISSONS_REQUEST_CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

       // BitMap2File.getFilePath(this, "nima");
      
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @PermissionDenied(MY_PERMISSONS_REQUEST_CALL_PHONE)
    public void deny()
    {
        //继续让他处理
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }
    @PermissionGrant(MY_PERMISSONS_REQUEST_CALL_PHONE)
    public void grant()
    {
        //允许

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                String path;
                for (int i = 0; i <10 ; i++)
                {
                    path= BitMap2File.INSTANCE.getFilePath(MainActivity.this, "20000"+i,"张三"+i);
                    VolleyLog.d("得到的路径为：%s", path);
                }
            }
        }).start();
        
    }
    @ShowRequestPermissionRationale(MY_PERMISSONS_REQUEST_CALL_PHONE)
    public void showReason()
    {
        //说明原因后，还需要继续申请
        Toast.makeText(activity, "必须获得权限", Toast.LENGTH_SHORT).show();
        MPermissions.requestPermissions(activity,MY_PERMISSONS_REQUEST_CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
