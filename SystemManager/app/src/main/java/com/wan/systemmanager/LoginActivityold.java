package com.wan.systemmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.systemmanager.bottom_nav.BottomActivity;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivityold extends Activity {
    // 从短信SDK应用后台注册得到的APPKEY、APPSECRET  仅供测试使用
    private static String APPKEY = "141c17df979bc";
    private static String APPSECRET = "97b2d4c52d8975c5d93664e740d89973";
    private ImageView loginImage;
    private TextView topText;
    private TextView register;
    private TextView vcode;
    private TextPaint tp;
    private Button loginbtn;
    private Button vcodebtn;
    private UserEditText username;
    private EditText password;
    private Drawable mIconPerson;
    private Drawable mIconLock;
    private Drawable mIconPerson2;
    private Drawable mIconLock2;
    private Drawable mIconclear;
    private Drawable mIconOpeneye;
    private Drawable mIconCloseeye;
    private boolean ready;
    private CheckBox cb_rem;
    private ImageButton qq;
    private ImageButton weixin;
    private ImageButton weibo;
    private String uname;
    private String upwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        setContentView(R.layout.activity_loginold);
        initWindow();
        username=(UserEditText)findViewById(R.id.username);
        username.getBackground().setAlpha(100);
        password=(EditText)findViewById(R.id.password);
        password.getBackground().setAlpha(100);
        register= (TextView) findViewById(R.id.register);
        vcode= (TextView) findViewById(R.id.vcode);
        cb_rem= (CheckBox) findViewById(R.id.remember);
        qq= (ImageButton) findViewById(R.id.qq);
        weixin= (ImageButton) findViewById(R.id.weixin);
        weibo= (ImageButton) findViewById(R.id.weibo);

        qq.getBackground().setAlpha(200);
        weixin.getBackground().setAlpha(200);
        weibo.getBackground().setAlpha(200);

        mIconclear=getResources().getDrawable(R.drawable.icon_clear);
        mIconclear.setBounds(-20, 0, 35, 55);
        mIconPerson=getResources().getDrawable(R.drawable.icon_loginuser);
        mIconPerson.setBounds(0, 0, 80, 55);
        mIconLock=getResources().getDrawable(R.drawable.icon_loginpassword);
        mIconLock.setBounds(0, 0, 80, 55);
        mIconPerson2=getResources().getDrawable(R.drawable.icon_loginuser2);
        mIconPerson2.setBounds(0, 0, 80, 55);
        mIconLock2=getResources().getDrawable(R.drawable.icon_loginpassword2);
        mIconLock2.setBounds(0, 0, 80, 55);
        mIconOpeneye=getResources().getDrawable(R.drawable.icon_loginkejian);
        mIconCloseeye=getResources().getDrawable(R.drawable.icon_loginyincang);



     //   username.setCompoundDrawables(mIconPerson, null, null, null);
     //   password.setCompoundDrawables(mIconLock, null, mIconCloseeye, null);
        //用户编辑框获取到焦点
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   username.setCompoundDrawables(mIconPerson2, null, mIconclear,null );
                    password.setCompoundDrawables(mIconLock, null, null, null);
                }
            }
        });
        //密码编辑框获取到焦点
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    username.setCompoundDrawables(mIconPerson, null, null, null);
                    password.setCompoundDrawables(mIconLock2, null, null, null);
                }
            }
        });
        //注册监听事件
        register.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    register.setTextColor(Color.rgb(255,105,180));
                    Intent intent=new Intent();
                    intent.setClass(LoginActivityold.this,RegisterActivity.class);
                    startActivity(intent);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                 //   v.setTextColor(Color.rgb(255,255,255));
                    register.setTextColor(Color.rgb(255,255,255));
                }
                return true;
            }
        });
        //本地文件是否记录帐号密码
        SharedPreferences spf=getSharedPreferences("login_info",MODE_PRIVATE);
        username.setText(spf.getString("uname",""));
        password.setText(spf.getString("upwd",""));
        if(spf.getInt("remempwd",0)==1)
            cb_rem.setChecked(true);
        else
            cb_rem.setChecked(false);
        init();
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void init(){
        //短信SDK初始化
        initSDK();
        //图片下方名称
        topText=(TextView)findViewById(R.id.topname);
        topText.setTextColor(Color.rgb(255,105,180));
        topText.setTextSize(24.0f);
        topText.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
        //使用TextPaint的仿“粗体”设置setFakeBoldText为true。目前还无法支持仿“斜体”方法
        tp=topText.getPaint();
        tp.setFakeBoldText(true);
        ///////////////////////////////////////////////////////////////////////////////
        //界面顶部图标
        loginImage=(ImageView)findViewById(R.id.loginImage);
        loginImage.setBackgroundDrawable(new BitmapDrawable(Util.toRoundBitmap(this, "logo.jpg")));
        loginImage.getBackground().setAlpha(0);
        loginImage.setImageBitmap(Util.toRoundBitmap(this, "logo.jpg"));
        ///////////////////////////////////////////////////////////////////////////////
        //验证码按钮
        vcodebtn= (Button) findViewById(R.id.vcodebtn);
        vcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送信息请求验证码
                String country="86";
                uname=username.getText().toString();
                if(uname.equals("")){
                    Toast.makeText(LoginActivityold.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    SMSSDK.getVerificationCode(country,uname);
                }
            }
        });
        //登录按钮
        loginbtn=(Button)findViewById(R.id.loginbtn);
        loginbtn.getBackground().setAlpha(220);//设置的透明度
        loginbtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(220);
                    uname=username.getText().toString();
                    upwd=password.getText().toString();
                    if(check(uname,upwd)){
                        login();
                    }
                }
                return true;
            }

        });
    }

    private void initSDK() {
        // 初始化短信SDK
        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调监听接口
        ready = true;
    }
    private boolean check(String uname,String upwd){
        //检查空值
        if(uname.equals("")){
            Toast.makeText(LoginActivityold.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if(upwd.equals("")){
            Toast.makeText(LoginActivityold.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if(vcode.equals("")){
            Toast.makeText(LoginActivityold.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void login(){
        Intent intent=new Intent();
        intent.setClass(LoginActivityold.this, BottomActivity.class);
        startActivity(intent);
        finish();
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            Log.d("TAG","afterTextChanged--------------->");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            Log.d("TAG","beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.d("TAG","onTextChanged--------------->");
            try {
                if ((username.getText().toString())!=null)
                    username.setCompoundDrawables(null, null, mIconPerson, null);
                else
                    username.setCompoundDrawables(null, null, null, null);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    protected void onStop(){
        super.onStop();
        SharedPreferences spf=getSharedPreferences("login_info",MODE_PRIVATE);
        SharedPreferences.Editor editor=spf.edit();
        if(cb_rem.isChecked()){
            editor.putString("uname",username.getText().toString());
            editor.putString("upwd",password.getText().toString());
            editor.putInt("remempwd",1);
        }else{
            // editor.putString("uname",txt_uname.getText().toString());
            editor.clear();
        }
        editor.commit();
    }
    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
    }
}
