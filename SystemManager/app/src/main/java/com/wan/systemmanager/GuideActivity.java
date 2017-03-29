package com.wan.systemmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.wan.systemmanager.R;


/**
 * Created by 万文杰 on 2016/6/14.
 */
public class GuideActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new guide_frame1();
                    case 1:
                        return new guide_frame2();
                    case 2:
                        return new guide_frame3();
                }
                return new guide_frame1();
            }
/*
            @Override
            public CharSequence getPageTitle(int position) {
                return super.getPageTitle(position);
            }*/

            @Override
            public int getCount() {
                return 3;
            }
        });

    }
}

class guide_frame1 extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frame1, null);

        return view;
    }
}

class guide_frame2 extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frame2, null);

        return view;
    }
}

class guide_frame3 extends Fragment {
    View view;
    Button btn;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frame3, null);
        btn = (Button) view.findViewById(R.id.button);
        init();
        return view;
    }

    public void init() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivityold.class);
                startActivity(intent);
            }
        });
    }
}
