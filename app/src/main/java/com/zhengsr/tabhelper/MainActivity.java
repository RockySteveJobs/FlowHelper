package com.zhengsr.tabhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zhengsr.tabhelper.activity.LabelActivity;
import com.zhengsr.tabhelper.activity.LabelShowMoreActivity;
import com.zhengsr.tabhelper.activity.NetTestActivity;
import com.zhengsr.tabhelper.activity.TabActivity;
import com.zhengsr.tabhelper.activity.TabNoViewPagerActivity;
import com.zhengsr.tabhelper.activity.VerticalTabActivity;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // startActivity(new Intent(this, LabelActivity.class));
    }


    public void tablayout(View view) {
        startActivity(new Intent(this, TabActivity.class));
    }

    public void tablayout2(View view) {
        startActivity(new Intent(this, TabNoViewPagerActivity.class));
    }

    public void labelflow(View view) {
        startActivity(new Intent(this, LabelActivity.class));
    }

    public void tablayout3(View view) {
        startActivity(new Intent(this, NetTestActivity.class));
    }

    public void tablayout4(View view) {
        startActivity(new Intent(this,VerticalTabActivity.class));
    }

    public void labelflowshomore(View view) {
        startActivity(new Intent(this, LabelShowMoreActivity.class));
    }
}
