package com.wan.systemmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.systemmanager.bottom_nav.BottomActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 万文杰 on 2016/6/15.
 */
public class RegisterActivity extends AppCompatActivity {
    // 从短信SDK应用后台注册得到的APPKEY、APPSECRET  仅供测试使用
    private static String APPKEY = "141c17df979bc";
    private static String APPSECRET = "97b2d4c52d8975c5d93664e740d89973";
    private ImageView logoImage;
    private ImageView image_user;
    private ImageView image_clear;
    private ImageView image_pwd;
    private ImageView image_eye;
    private TextView topText;
    private TextPaint tp;
    private Button registerbtn;
    private Button vcodebtn;
    private EditText username;
    private EditText password;
    private EditText vcode;
    private Drawable mIconPerson;
    private Drawable mIconLock;
    private Drawable mIconPerson2;
    private Drawable mIconLock2;
    private Drawable mIconclear;
    private boolean ready;
    private boolean isSend;
    private String uname;
    private String upwd;
    private int timemsg = 60;
    private int pwd_state = 0;
    RelativeLayout relative_user;
    RelativeLayout relative_pwd;
    RelativeLayout relative_vcode;
    Timer vcodetimer;
    VcodeTask task;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        // 设置窗口风格为顶部显示Actionbar
        /*
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("返回");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);
        // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，
        // 否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        // force use of overflow menu on devices with menu button
        // 在actionbar中显示溢出菜单选项
*/
        setContentView(R.layout.activity_register);
        initWindow();
        initUI();
        initSDK();//短信SDK初始化
        initListen();
    }

    //TODO 初始化Handler
    Handler vcodehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 要做的事情
            timemsg--;
            vcodebtn.setText(timemsg + "");
            if (timemsg == 0) {
                timemsg = 60;
                vcodebtn.setText("再次获取");
                vcodetimer.cancel();//取消时钟
                task.cancel();  //将原任务从队列中移除
            }
        }
    };


    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void initUI() {
        //图片下方名称
        topText = (TextView) findViewById(R.id.topname);
        topText.setTextColor(Color.rgb(255, 105, 180));
        topText.setTextSize(24.0f);
        topText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);
        //使用TextPaint的仿“粗体”设置setFakeBoldText为true。目前还无法支持仿“斜体”方法
        tp = topText.getPaint();
        tp.setFakeBoldText(true);
        ///////////////////////////////////////////////////////////////////////////////
        //三个编辑框外层布局样式修改
        relative_user = (RelativeLayout) findViewById(R.id.relative_user);
        relative_user.getBackground().setAlpha(100);
        relative_pwd = (RelativeLayout) findViewById(R.id.relative_pwd);
        relative_pwd.getBackground().setAlpha(100);
        relative_vcode = (RelativeLayout) findViewById(R.id.relative_vcode);
        relative_vcode.getBackground().setAlpha(100);
        //三个编辑框
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        vcode = (EditText) findViewById(R.id.vcode);
        //编辑框左侧图标
        image_user = (ImageView) findViewById(R.id.image_user);
        image_pwd = (ImageView) findViewById(R.id.image_pwd);
        //密码眼睛图标
        image_eye = (ImageView) findViewById(R.id.image_eye);
        image_eye.setImageResource(R.drawable.icon_loginyincang);
        //清除图标
        image_clear = (ImageView) findViewById(R.id.image_clear);
        //界面顶部图标
        logoImage = (ImageView) findViewById(R.id.logoImage);
        logoImage.setBackgroundDrawable(new BitmapDrawable(Util.toRoundBitmap(this, "logo.jpg")));
        logoImage.getBackground().setAlpha(0);
        logoImage.setImageBitmap(Util.toRoundBitmap(this, "logo.jpg"));
        //////////////////////////////////////////////////////////////////////////////
        //验证码按钮
        vcodebtn = (Button) findViewById(R.id.vcodebtn);
        //注册按钮
        registerbtn = (Button) findViewById(R.id.registerbtn);
        registerbtn.getBackground().setAlpha(220);//设置的透明度
    }

    private void initListen() {
        //用户编辑框获取到焦点
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    image_user.setImageResource(R.drawable.icon_login_user2);
                    image_pwd.setImageResource(R.drawable.icon_login_pwd);
                    if (username.length() > 0) image_clear.setImageResource(R.drawable.icon_clear);
                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.length() > 0) {
                    image_clear.setImageResource(R.drawable.icon_clear);
                } else {
                    image_clear.setImageBitmap(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //密码编辑框获取到焦点
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    image_user.setImageResource(R.drawable.icon_login_user);
                    image_pwd.setImageResource(R.drawable.icon_login_pwd2);
                    image_clear.setImageBitmap(null);
                }
            }
        });
        //密码查看图标监听
        image_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_state == 0) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    image_eye.setImageResource(R.drawable.icon_loginkejian);
                    pwd_state = 1;
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    image_eye.setImageResource(R.drawable.icon_loginyincang);
                    pwd_state = 0;
                }
            }
        });
        //清除按钮监听
        image_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });
        //TODO 验证码按钮监听
        vcodebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                  vcodebtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.vcode_on));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    vcodebtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.vcode));
                    //发送信息请求验证码
                    String country = "86";
                    uname = username.getText().toString();
                    if (uname.equals("")) {
                        Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        SMSSDK.getVerificationCode(country, uname);
                        Toast.makeText(RegisterActivity.this, "请求获取验证码。。。", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        //TODO 注册按钮监听
        registerbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(150);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(220);
                    uname = username.getText().toString();
                    upwd = password.getText().toString();
                    if (check(uname, upwd)) {
                        register(uname, upwd);
                    }
                }
                return true;
            }

        });
    }

    private void initSDK() {
        // 初始化短信SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            toast("验证成功");
                        } else {
                            toast("验证失败");
                        }
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            toast("验证码已发送，注意查收");                        //默认的智能验证是开启的,我已经在后台关闭
                            dotimer();
                        } else {
                            toast("获取验证码失败");
                        }
                        break;
                }
            }
        };


     /*   EventHandler eh2 = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                       // 提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                       // 获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };*/

        SMSSDK.registerEventHandler(eh); //注册短信回调监听接口
        Toast.makeText(RegisterActivity.this, "注册短信回调监听接口", Toast.LENGTH_SHORT).show();
        ready = true;
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dotimer(){
        vcodetimer = new Timer();
        task = new VcodeTask() { // 每次新建计时器任务。
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                vcodehandler.sendMessage(message);
            }
        };
        vcodetimer.schedule(task, 0, 1000);
    }
    //TODO 检查账号和密码
    private boolean check(String uname, String upwd) {
        //检查空值
        if (uname.equals("")) {
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (upwd.equals("")) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (vcode.equals("")) {
            Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //TODO 注册帐号密码功能
    public void register(String uname,String upwd){
        //请求服务器的等待图
        progress=new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在请求网络");
        progress.show();
        String url=InitConfig.baseUrl+"register";
        RequestParams rp=new RequestParams(url);
        rp.addBodyParameter("uname",uname);
        rp.addBodyParameter("upwd",upwd);
        //发送请求
        x.http().post(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("registerSuccess")){
                    Toast.makeText(RegisterActivity.this,"~\\(≧▽≦)/~啦啦啦，注册成功了",Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    login();
                }
                if (result.equals("registerError")){
                    Toast.makeText(RegisterActivity.this,"(┬＿┬)，注册失败了",Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                if (result.equals("existuser")){
                    Toast.makeText(RegisterActivity.this,"(+﹏+)~狂晕，用户名已存在",Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this,"请求登录失败",Toast.LENGTH_LONG).show();
                progress.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                progress.dismiss();
            }

            @Override
            public void onFinished() {
                progress.dismiss();
            }
        });
    }

    public void login() {
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, BottomActivity.class);
        intent.putExtra("uname",uname);
        intent.putExtra("upwd",upwd);
        startActivity(intent);
     //   finish();
    }


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

            Log.d("TAG", "afterTextChanged--------------->");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

            Log.d("TAG", "beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.d("TAG", "onTextChanged--------------->");
            try {
                if ((username.getText().toString()) != null)
                    username.setCompoundDrawables(null, null, mIconPerson, null);
                else
                    username.setCompoundDrawables(null, null, null, null);
            } catch (Exception e) {

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class VcodeTask extends TimerTask {
        @Override
        public void run() {

        }
    }
}


