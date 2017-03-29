package com.wan.systemmanager.bottom_nav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.systemmanager.R;

/**
 * Created by 万文杰 on 2016/6/16.
 */
public class Bottom_frame3 extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.frame3,null);

        return view;
    }
}
