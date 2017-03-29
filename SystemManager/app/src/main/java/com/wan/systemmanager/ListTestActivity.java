package com.wan.systemmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.gui.layout.ListviewTitleLayout;

/**
 * Created by 万文杰 on 2016/6/24.
 */
public class ListTestActivity extends AppCompatActivity {
    ListView listView;
    List<Map<String, String>> data;

    class MyBaseAdapter extends BaseAdapter {
        Context context;
        List<Map<String, String>> data;

        public MyBaseAdapter(Context context, List<Map<String, String>> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.sub_listview, null);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView age = (TextView) view.findViewById(R.id.age);
            ImageView viewById = (ImageView) view.findViewById(R.id.img);
            name.setText(((Map) data.get(position)).get("uname").toString());
            age.setText(((Map) data.get(position)).get("birthday").toString());
            x.image().bind(viewById, InitConfig.baseUrl + ((Map) data.get(position)).get("photo").toString());
            Button btn = (Button) view.findViewById(R.id.testbtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, ((Map) data.get(position)).get("uname").toString(), Toast.LENGTH_LONG).show();
                }
            });
            return view;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.listview);
        data = new ArrayList<Map<String, String>>();
        String url = InitConfig.baseUrl + "findUserInfo";
        RequestParams rp = new RequestParams(url);
        rp.addBodyParameter("page", "1");
        rp.addBodyParameter("rows", "10");
        x.http().post(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jobj = JSON.parseObject(result);
                JSONArray jarr = (JSONArray) jobj.get("rows");
                Gson gson = new Gson();
                data = gson.fromJson(gson.toJson(jarr), new TypeToken<List<Map<String, String>>>() {
                }.getType());
              /*  SimpleAdapter simpleAdapter = new SimpleAdapter(ListViewActivity.this,
                        data, R.layout.sub_listview,
                        new String[]{"uname", "photo", "usex"}, new int[]{R.id.name, R.id.age, R.id.img});*/
                MyBaseAdapter mybaseadapter = new MyBaseAdapter(ListTestActivity.this, data);
                listView.setAdapter(mybaseadapter);

               /* Toast.makeText(ListViewActivity.this,result,Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
/*        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,str);
        listView.setAdapter(arrayAdapter);*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListTestActivity.this);
                dialog.setTitle("消息提示");
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setMessage(((Map) (data.get(position))).get("name").toString());
                dialog.setPositiveButton("知道了", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }
}
