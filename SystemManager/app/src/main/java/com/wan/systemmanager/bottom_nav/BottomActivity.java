package com.wan.systemmanager.bottom_nav;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.systemmanager.R;
import com.wan.systemmanager.bottom_nav.Bottom_frame1;
import com.wan.systemmanager.bottom_nav.Bottom_frame2;
import com.wan.systemmanager.bottom_nav.Bottom_frame3;

/**
 * Created by 万文杰 on 2016/6/15.
 */
public class BottomActivity extends AppCompatActivity {
    ImageView btn1;
    ImageView btn2;
    ImageView btn3;
    ImageView btn4;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    Intent usermsg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usermsg=getIntent();
        setContentView(R.layout.activity_footmain);
        init();
    }

    public void init() {
        
        btn1 = (ImageView) findViewById(R.id.btn1);
        btn2 = (ImageView) findViewById(R.id.btn2);
        btn3 = (ImageView) findViewById(R.id.btn3);
        btn4 = (ImageView) findViewById(R.id.btn4);

        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);
        text3=(TextView)findViewById(R.id.text3);
        text4=(TextView)findViewById(R.id.text4);
/*
        btn2.setBackgroundResource(R.drawable.icon_home_nor);
        btn1.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_nor));*/
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content, new Bottom_frame1()).commit();
                resetcolor();
                btn1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_pre));
                text1.setTextColor(Color.parseColor("#FF69B4"));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content, new Bottom_frame2()).commit();
                resetcolor();
                btn2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_message_pre));
                text2.setTextColor(Color.rgb(255,105,180));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content, new Bottom_frame3()).commit();
                resetcolor();
                btn3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_find_pre));
                text3.setTextColor(Color.rgb(255,105,180));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content, new Fragment4()).commit();
                resetcolor();
                btn4.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user_pre));
                text4.setTextColor(Color.rgb(255,105,180));
            }
        });
    }
    public void resetcolor(){
        btn1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_nor));
        btn2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_message_nor));
        btn3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_find_nor));
        btn4.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user_nor));
        text1.setTextColor(Color.parseColor("#8E8E8E"));
        text2.setTextColor(Color.parseColor("#8E8E8E"));
        text3.setTextColor(Color.parseColor("#8E8E8E"));
        text4.setTextColor(Color.parseColor("#8E8E8E"));
    }


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
