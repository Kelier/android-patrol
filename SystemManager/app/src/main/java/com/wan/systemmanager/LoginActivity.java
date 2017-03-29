package com.wan.systemmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.test.AndroidTestCase;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.systemmanager.bottom_nav.BottomActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TimerTask;

/**
 * Created by 万文杰 on 2016/6/15.
 */
public class LoginActivity extends AppCompatActivity {
    private ImageView logoImage;
    private ImageView image_user;
    private ImageView image_clear;
    private ImageView image_pwd;
    private ImageView image_eye;
    private TextView topText;
    private TextView register;
    private TextPaint tp;
    private Button loginbtn;
    private EditText username;
    private EditText password;
    private Drawable mIconPerson;
    private Drawable mIconLock;
    private Drawable mIconPerson2;
    private Drawable mIconLock2;
    private Drawable mIconclear;
    private Drawable mIconOpeneye;
    private Drawable mIconCloseeye;
    private String uname;
    private String upwd;
    private int pwd_state = 0;
    RelativeLayout relative_user;
    RelativeLayout relative_pwd;
    private CheckBox cb_rem;
    private ImageButton qq;
    private ImageButton weixin;
    private ImageButton weibo;
    ProgressDialog progress;
    Dialog waitdialog;

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
        setContentView(R.layout.activity_login);
        initWindow();
        initUI();
        initListen();
    }

    //沉浸式布局
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
        //二个编辑框外层布局样式修改
        relative_user = (RelativeLayout) findViewById(R.id.relative_user);
        relative_user.getBackground().setAlpha(100);
        relative_pwd = (RelativeLayout) findViewById(R.id.relative_pwd);
        relative_pwd.getBackground().setAlpha(100);
        //二个编辑框
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
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
        //注册链接
        register = (TextView) findViewById(R.id.register);
        //登录按钮
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.getBackground().setAlpha(220);//设置的透明度

        //底部三链接
        cb_rem = (CheckBox) findViewById(R.id.remember);
        qq = (ImageButton) findViewById(R.id.qq);
        weixin = (ImageButton) findViewById(R.id.weixin);
        weibo = (ImageButton) findViewById(R.id.weibo);

        qq.getBackground().setAlpha(200);
        weixin.getBackground().setAlpha(200);
        weibo.getBackground().setAlpha(200);

        //记录
        //本地文件是否记录帐号密码
        SharedPreferences spf = getSharedPreferences("login_info", MODE_PRIVATE);
        username.setText(spf.getString("uname", ""));
        password.setText(spf.getString("upwd", ""));
        if (spf.getInt("remempwd", 0) == 1)
            cb_rem.setChecked(true);
        else
            cb_rem.setChecked(false);
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

        //TODO 注册监听事件
        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    register.setTextColor(Color.rgb(255, 105, 180));
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //   v.setTextColor(Color.rgb(255,255,255));
                    register.setTextColor(Color.rgb(255, 255, 255));
                }
                return true;
            }
        });

        //TODO 登录按钮监听
        loginbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(150);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(220);
                    uname = username.getText().toString();
                    upwd = password.getText().toString();
                    if (check(uname, upwd)) {
                        do_login(uname, upwd);
                    }
                }
                return true;
            }

        });
    }

    //TODO 检查账号和密码
    private boolean check(String uname, String upwd) {
        //检查空值
        if (uname.equals("")) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (upwd.equals("")) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void do_login(String uname, String upwd) {
        //  new VideoServiceTest().testLocalIpAndMac();
        //请求服务器的等待图
       /* progress=new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在请求网络");
        progress.show();*/
        waitdialog = WaitDialog.createLoadingDialog(this, "登录中，请稍后。。");
        waitdialog.show();
        String url = InitConfig.baseUrl + "androidlogin";
        RequestParams rp = new RequestParams(url);
        rp.addBodyParameter("uname", uname);
        rp.addBodyParameter("upwd", upwd);
        //发送请求
        x.http().post(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("adminLoginSuccess")) {
                    Toast.makeText(LoginActivity.this, "~\\(≧▽≦)/~啦啦啦，欢迎GM", Toast.LENGTH_LONG).show();
                    waitdialog.cancel();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    },1000);

                }
                if (result.equals("plantLoginSuccess")) {
                    Toast.makeText(LoginActivity.this, "~\\(≧▽≦)/~啦啦啦，欢迎平民", Toast.LENGTH_LONG).show();
                    waitdialog.cancel();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    },1000);
                }
                if (result.equals("loginError")) {
                    Toast.makeText(LoginActivity.this, "⊙︿⊙，用户名或密码错误", Toast.LENGTH_LONG).show();
                    waitdialog.cancel();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "请求登录失败", Toast.LENGTH_LONG).show();
                waitdialog.cancel();

            }

            @Override
            public void onCancelled(CancelledException cex) {
                waitdialog.cancel();

            }

            @Override
            public void onFinished() {
                //waitdialog.cancel();

            }
        });
    }

    public void login() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.putExtra("uname", uname);
        intent.putExtra("upwd", upwd);
        startActivity(intent);
        finish();
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
        SharedPreferences spf = getSharedPreferences("login_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        if (cb_rem.isChecked()) {
            editor.putString("uname", username.getText().toString());
            editor.putString("upwd", password.getText().toString());
            editor.putInt("remempwd", 1);
        } else {
            // editor.putString("uname",txt_uname.getText().toString());
            editor.clear();
        }
        editor.commit();
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


    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */


}

class VideoServiceTest extends AndroidTestCase {
    private static final String TAG = "VideoServiceTest";

    public void testLocalIpAndMac() {
        Log.i(TAG, "IP: " + getLocalIpAddress());
    }

    /**
     * 获取Android本机IP地址
     *
     * @return
     */
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }
    /**
     * 获取Android本机MAC
     *
     * @return
     */
  /*  private String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) this.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }*/


}

