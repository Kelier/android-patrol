package com.wan.systemmanager.bottom_nav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wan.systemmanager.R;


public class Fragment4 extends Fragment {
    View view;
    ListView listView;
    ListView v;
    Button btn;
    DrawerLayout drawerLayout;
    String str[]={"初夏","盛夏","末夏"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_4,null);
        btn=(Button)view.findViewById(R.id.btn_s);
        listView=(ListView)view.findViewById(R.id.menu);
        v=(ListView)view.findViewById(R.id.menu);
        drawerLayout=(DrawerLayout)view.findViewById(R.id.drawer);
        drawerLayout.openDrawer(v);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,str);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragPersonActivity fragPersonActivity=new FragPersonActivity();
                FragmentManager fragmentManager=getFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("name",str[position]);
                fragPersonActivity.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frame,fragPersonActivity).commit();
                drawerLayout.closeDrawer(v);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (drawerLayout.isDrawerOpen(v))
                drawerLayout.closeDrawer(v);
                else drawerLayout.openDrawer(v);
            }
        });
        return view;
    }
}



