package com.wan.systemmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 万文杰 on 2016/6/14.
 */
public class ListViewActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.listview);
        String str[] = {"aaaa", "bbb", "cccc"};
        final List data = new ArrayList<Map<String, String>>();
        Map map1 = new HashMap();
        map1.put("name", "張三");
        map1.put("age", 34);
        map1.put("img", R.drawable.i1);
        Map map2 = new HashMap();
        map2.put("name", "李四");
        map2.put("age", 24);
        map2.put("img", R.drawable.i1);
        Map map3 = new HashMap();
        map3.put("name", "王五");
        map3.put("age", 21);
        map3.put("img", R.drawable.i1);
        data.add(map1);
        data.add(map2);
        data.add(map3);
/*        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,str);
        listView.setAdapter(arrayAdapter);*/
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                data, R.layout.sub_listview,
                new String[]{"name", "age", "img"}, new int[]{R.id.name, R.id.age, R.id.img});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(ListViewActivity.this);
                dialog.setTitle("消息提示");
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setMessage((((HashMap)(data.get(position))).get("name")).toString());
                dialog.setPositiveButton("知道了",new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


    }


}
