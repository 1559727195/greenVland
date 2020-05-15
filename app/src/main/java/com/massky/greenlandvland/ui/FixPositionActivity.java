package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_myLocation;
import com.massky.greenlandvland.model.entity.Sc_myLocationReload;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FixPositionActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private RelativeLayout vGroup;
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;
    //    private ImageView background;
    private Handler mHandler=new Handler();

    int width,height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_position);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);


//        Log.e("TAG","locationList="+locationList);
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        vGroup= (RelativeLayout) findViewById(R.id.viewgroup);
//        background= (ImageView) findViewById(R.id.background);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        vGroup.measure(w, h);
        height =vGroup.getMeasuredHeight();
        width =vGroup.getMeasuredWidth();




        //添加监听
        back.setOnClickListener(clickListener);
        //没有定位数据时


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();

    }

    private void getLocation() {
        HttpClient.post(CommonUtil.APPURL, "sc_myLocation"
                , new Gson().toJson(new Sc_myLocation.MyLocationParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+token);
//                                    Log.e("TAG","projectCode="+projectCode);
                        Sc_myLocation.MyLocationResoult myLocationResoult=new Gson().fromJson(data, Sc_myLocation.MyLocationResoult.class);
                        int result=myLocationResoult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            final String bgImg=myLocationResoult.getBackgroundImg();
                            Log.e("TAG","bgImg="+bgImg);
                            List<Sc_myLocation.MyLocationResoult.Location> locationList=new ArrayList<Sc_myLocation.MyLocationResoult.Location>();
                            locationList=myLocationResoult.getLocationList();

//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Bitmap bitmap=getNetBitmap(bgImg);
//                                    final Drawable drawable =new BitmapDrawable(bitmap);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            vGroup.setBackground(drawable);
//                                        }
//                                    });
//                                }
//                            }).start();
                            Glide.with(FixPositionActivity.this)
                                    .load(bgImg)
//                                    .bitmapTransform(new RoundedCornersTransformation(FixPositionActivity.this, 20, 0, RoundedCornersTransformation.CornerType.ALL))
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            vGroup.setBackground(new BitmapDrawable(resource));
                                        }
                                    });

                            if(locationList.size()==0){
                                Toast toast=Toast.makeText(getApplicationContext(),
                                        "没有定位数据", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }

                            mHandler.post(reload);
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            getLocation();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },FixPositionActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            ToastUtil.showToast(FixPositionActivity.this,"数据获取失败");
                        }else {
                            id=1;
                            ToastUtil.showToast(FixPositionActivity.this,"数据获取失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        ToastUtil.showToast(FixPositionActivity.this,"网络连接失败");
                    }
                });
    }

    //每两秒刷新一次
    Runnable reload=new Runnable() {
        @Override
        public void run() {
//            final String token= SharedPreferencesUtils.getToken(FixPositionActivity.this);
//            final String projectCode=SharedPreferencesUtils.getProjectCode(FixPositionActivity.this);
            locationReload();

            mHandler.postDelayed(this,2000);
        }
    };

    private void locationReload() {
        HttpClient.post(CommonUtil.APPURL, "sc_myLocationReload"
                , new Gson().toJson(new Sc_myLocationReload.MyLocationReloadParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                Log.e("TAG","data="+data);
//                                Log.e("TAG","token="+token);
//                                Log.e("TAG","projectCode="+projectCode);
                        Sc_myLocationReload.MyLocationReloadResult myLocationReloadResult=new Gson().fromJson(data, Sc_myLocationReload.MyLocationReloadResult.class);
                        int result=myLocationReloadResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-解析错误");
                            id=1;
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;

                            ImageView[] imageView=new   ImageView[myLocationReloadResult.getLocationList().size()];
                            TextView[] textViews=new TextView[myLocationReloadResult.getLocationList().size()];

                            vGroup.removeAllViews();
                            for (int i=0;i<myLocationReloadResult.getLocationList().size();i++) {
                                imageView[i]=new ImageView(FixPositionActivity.this);
                                imageView[i].setImageResource(R.drawable.location);

                                textViews[i]=new TextView(FixPositionActivity.this);
                                textViews[i].setText(myLocationReloadResult.getLocationList().get(i).getIbeaconName());
                                textViews[i].setTextColor(Color.BLACK);
                                RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(80,80);

                                lParams.leftMargin = myLocationReloadResult.getLocationList().get(i).getReadX()*width/100;
                                Log.e("TAG","x="+lParams.leftMargin);
                                lParams.topMargin = myLocationReloadResult.getLocationList().get(i).getReadY()*height/100;
                                Log.e("TAG","y="+lParams.topMargin);
//                                imageView[i].setId(i);
                                vGroup.addView(imageView[i],lParams);


                                RelativeLayout.LayoutParams lParams2=new RelativeLayout
                                        .LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                                layoutParams2.addRule(RelativeLayout.RIGHT_OF,i);//设置相对于某个控件的位置
//                                layoutParams2.addRule(RelativeLayout.ALIGN_TOP,i);
                                if(myLocationReloadResult.getLocationList().get(i).getReadX()*width/100>800){
                                    lParams2.leftMargin=myLocationReloadResult.getLocationList().get(i).getReadX()*width/100-70;
                                }else {
                                    lParams2.leftMargin=myLocationReloadResult.getLocationList().get(i).getReadX()*width/100+70;
                                }
                                lParams2.topMargin=myLocationReloadResult.getLocationList().get(i).getReadY()*height/100;
                                vGroup.addView(textViews[i],lParams2);
                            }
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            locationReload();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },FixPositionActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            ToastUtil.showToast(FixPositionActivity.this,"获取数据失败");
                        }else {
                            id=1;
                            ToastUtil.showToast(FixPositionActivity.this,"获取数据失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        ToastUtil.showToast(FixPositionActivity.this,"网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FixPositionActivity.this, 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(reload);
        finish();
    }

    public Bitmap getNetBitmap(String urlPath) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();//获取图片的数据的输入流
            bitmap = BitmapFactory.decodeStream(is);//吧图片的字节流变成比bitmap类型

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
