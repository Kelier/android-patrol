package com.wan.systemmanager.bottom_nav;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wan.systemmanager.R;

/**
 * Created by 闫江南 on 2016/6/16.
 */
public class FragPersonActivity extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.personcenter_frag,null);
        String str=getArguments().get("name").toString();
        TextView textView=(TextView)view.findViewById(R.id.txt_view);
        textView.setText(str);
        return view;
    }
}
