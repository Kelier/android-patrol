package com.wan.systemmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by 闫江南 on 2016/7/6.
 */
public class DataActivity extends AppCompatActivity implements View.OnClickListener {
    private Button linechart_bt;
    private Button barchart_bt;
    private Button piechart_bt;
    private WebView chartshow_wb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        linechart_bt=(Button)findViewById(R.id.linechart_bt);
        barchart_bt=(Button)findViewById(R.id.barchart_bt);
        piechart_bt=(Button)findViewById(R.id.piechart_bt);
        linechart_bt.setOnClickListener(this);
        barchart_bt.setOnClickListener(this);
        piechart_bt.setOnClickListener(this);
        chartshow_wb=(WebView)findViewById(R.id.chartshow_wb);

        chartshow_wb.getSettings().setJavaScriptEnabled(true);
        chartshow_wb.loadUrl("file:///assets/myechart.html");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linechart_bt:
                chartshow_wb.loadUrl("javascript:createChart('line',[89,78,77]);");
                break;
            case R.id.barchart_bt:
                chartshow_wb.loadUrl("javascript:createChart('bar',[89,78,77]);");
                break;
            case R.id.piechart_bt:
                chartshow_wb.loadUrl("javascript:createChart('pie',[89,78,77]);");
                break;
            default:
                break;
        }
    }

}

