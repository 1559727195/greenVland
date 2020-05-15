package com.massky.greenlandvland.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.massky.greenlandvland.R;
import com.massky.greenlandvland.ui.adapter.LeadImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeadActivity extends AppCompatActivity {
    private ViewPager viewPager;//滑动页面
    private ImageView[] points=new ImageView[4];//4个原点指示器
    private LeadImageAdapter adapter;//滑动页面适配器
    private List<View> list=new ArrayList<View>();//放5个ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        //判断是否是第一次登录
        //取数据判断是否是第一次登录
        SharedPreferences sp=getSharedPreferences("runConfig", Context.MODE_PRIVATE);
        boolean isFirstRun=sp.getBoolean("isFirstRun",true);//第一次默认第一次登录
        if(!isFirstRun){//如果不是第一次登录则直接进入LogoActivity动画页面
            startActivity(new Intent(LeadActivity.this,LogoActivity.class));
            finish();
            return;
        }
        //初始化控件
        //原点指示器
        points[0]= (ImageView) findViewById(R.id.iv_p1);
        points[1]= (ImageView) findViewById(R.id.iv_p2);
        points[2]= (ImageView) findViewById(R.id.iv_p3);
        points[3]= (ImageView) findViewById(R.id.iv_p4);
        setPoint(0);
        //ViewPager初始化
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        //存储5个页面的集合
        list.add(getLayoutInflater().inflate(R.layout.lead_1,null));
        list.add(getLayoutInflater().inflate(R.layout.lead_2,null));
        list.add(getLayoutInflater().inflate(R.layout.lead_3,null));
        list.add(getLayoutInflater().inflate(R.layout.lead_4,null));
        //初始化适配器
        adapter=new LeadImageAdapter(list);
        //设置适配器
        viewPager.setAdapter(adapter);
        //设置滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//重写滑动操作
                setPoint(position);
                if(position>=3){//当滑动到第5个页面时
                    startActivity(new Intent(LeadActivity.this,LogoActivity.class));
                    finish();//销毁当前页面
                    //存储数据
                    //构建SharedPreferences对象
                    SharedPreferences sp=getSharedPreferences("runConfig",Context.MODE_PRIVATE);
                    //构建Editor对象
                    SharedPreferences.Editor editor=sp.edit();
                    //存储数据
                    editor.putBoolean("isFirstRun",false);
                    //提交数据
                    editor.commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPoint(int position) {
        for(int i=0;i<points.length;i++){
            if(position==i){//与当前页面位置相同的圆点指示器设置为不透明
                points[i].setAlpha(1.0f);//不透明
            }else{//其它的圆点指示器设置为半透明
                points[i].setAlpha(0.5f);//半透明
            }
        }
    }
}
