package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.LoadMoreListView;
import com.massky.greenlandvland.View.swipelayout.SwipeMenuLayout;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_cancelInviteRecord;
import com.massky.greenlandvland.model.entity.Sc_deleteInviteRecord;
import com.massky.greenlandvland.model.entity.Sc_inviteRecord;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.MyVisiterSwipeAdapter;

import java.util.List;

public class MyVisiterActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private LoadMoreListView listview;
    private MyVisiterSwipeAdapter myVisiterSwipeAdapter;
    private TextView tv_invite;
    private List<Sc_inviteRecord.InviteRecordResult.Invite> inviteList;
    private View view1;
    private PopupWindow popupWindow;
    private int height;
    private int width;
    private String token;
    private String projectCode;
    private int id=1;
    private String page;
    private int pages;
    private String roomNo;
    private String quanxians="";
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    public static MyVisiterActivity instance=null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            inviteList= (List<Sc_inviteRecord.InviteRecordResult.Invite>) msg.obj;
            Log.e("TAG","inviteList="+inviteList);
            myVisiterSwipeAdapter=new MyVisiterSwipeAdapter(MyVisiterActivity.this,inviteList,R.layout.item_swipelist_myvisiter) {
                @Override
                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                    holder.setText(R.id.tv_name,inviteList.get(position).getVisitor().get(0).getName());
                    holder.setText(R.id.tv_time,inviteList.get(position).getEndTime());
                    if(inviteList.get(position).getStatus()==0){
                        holder.setImageResource(R.id.iv_invite,R.drawable.invited);
                        holder.setText(R.id.btnDelete,"取消");
                    }else if(inviteList.get(position).getStatus()==1){
                        holder.setImageResource(R.id.iv_invite,R.drawable.stale);
                        holder.setText(R.id.btnDelete,"删除");
                    }else {
                        holder.setImageResource(R.id.iv_invite,R.drawable.canceled);
                        holder.setText(R.id.btnDelete,"删除");
                    }

                    //删除或取消
                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog=DialogThridUtils.showWaitDialog(MyVisiterActivity.this,true);
                            final int recordId=inviteList.get(position).getId();
                            if(inviteList.get(position).getStatus()==0){
                                HttpClient.post(CommonUtil.APPURL, "sc_cancelInviteRecord",
                                        new Gson().toJson(new Sc_cancelInviteRecord.CancelInviteRecordParams(token, projectCode, recordId)),
                                        new UICallback() {
                                            @Override
                                            public void process(String data) {
                                                Log.e("TAG","data="+data);
                                                Sc_cancelInviteRecord.CancelInviteRecordResult recordResult=new Gson().fromJson(data, Sc_cancelInviteRecord.CancelInviteRecordResult.class);
                                                int result=recordResult.getResult();
                                                if(result==1){
                                                    Log.e("TAG","1-json解析失败");
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }else if(result==100){
                                                    Log.e("TAG","100-成功");
                                                    id=1;
                                                    ((SwipeMenuLayout) holder.getConvertView()).quickClose();
//                                                    mHandler.sendEmptyMessage(1);
                                                    DialogThridUtils.closeDialog(mDialog);
                                                    initVisitor();
                                                }else if(result==101){
                                                    if(id==1){
                                                        id=id+1;
                                                        new GetToken(new CallBackInterface() {
                                                            @Override
                                                            public void gettoken(String str) {
                                                                token=str;
                                                                HttpClient.post(CommonUtil.APPURL, "sc_cancelInviteRecord",
                                                                        new Gson().toJson(new Sc_cancelInviteRecord.CancelInviteRecordParams(token, projectCode, recordId)),
                                                                        new UICallback() {
                                                                            @Override
                                                                            public void process(String data) {
                                                                                Sc_cancelInviteRecord.CancelInviteRecordResult recordResult=new Gson().fromJson(data, Sc_cancelInviteRecord.CancelInviteRecordResult.class);
                                                                                int result=recordResult.getResult();
                                                                                if(result==1){
                                                                                    Log.e("TAG","1-json解析失败");
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==100){
                                                                                    Log.e("TAG","100-成功");
                                                                                    id=1;
                                                                                    ((SwipeMenuLayout) holder.getConvertView()).quickClose();
//                                                                                    mHandler.sendEmptyMessage(1);
                                                                                    DialogThridUtils.closeDialog(mDialog);
                                                                                    initVisitor();
                                                                                }else if(result==101){
                                                                                    Log.e("TAG","101-token错误");
                                                                                    id=1;
                                                                                    showerror();
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==102){
                                                                                    Log.e("TAG","102-projectCode错误");
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==103){
                                                                                    Log.e("TAG","103-取消失败");
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else {
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onError(String data) {
                                                                                id=1;
                                                                                ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                                                mHandler.sendEmptyMessage(1);
                                                                            }
                                                                        });
                                                            }
                                                        },MyVisiterActivity.this);
                                                    }else {
                                                        id = 1;
                                                        showerror();
                                                        mHandler.sendEmptyMessage(1);
                                                    }
                                                }else if(result==102){
                                                    Log.e("TAG","102-projectCode错误");
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }else if(result==103){
                                                    Log.e("TAG","103-取消失败");
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }else {
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }
                                            }

                                            @Override
                                            public void onError(String data) {
                                                id=1;
                                                ToastUtil.showToast(MyVisiterActivity.this,"取消失败");
                                                mHandler.sendEmptyMessage(1);
                                            }
                                        });
                            }else {
                                HttpClient.post(CommonUtil.APPURL, "sc_deleteInviteRecord"
                                        , new Gson().toJson(new Sc_deleteInviteRecord.DeleteInviteRecordParams(token, projectCode, recordId))
                                        , new UICallback() {
                                            @Override
                                            public void process(String data) {
                                                Log.e("TAG","data="+data);
                                                Log.e("TAG","token="+token);
                                                Log.e("TAG","projectCode="+projectCode);
                                                Log.e("TAG","recordId="+recordId);
                                                Sc_deleteInviteRecord.DeleteInviteRecordResult deleteInviteRecordResult=new Gson().fromJson(data, Sc_deleteInviteRecord.DeleteInviteRecordResult.class);
                                                int result=deleteInviteRecordResult.getResult();
                                                if(result==1){
                                                    Log.e("TAG","1-json解析失败");
                                                    id=1;
                                                    mHandler.sendEmptyMessage(1);
                                                }else if(result==100){
                                                    Log.e("TAG","100-成功");
                                                    id=1;
                                                    ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                    inviteList.remove(position);
                                                    notifyDataSetChanged();
                                                    mHandler.sendEmptyMessage(1);
                                                }else if(result==101){
                                                    Log.e("TAG","101-token错误");
                                                    if(id==1) {
                                                        id = id + 1;
                                                        new GetToken(new CallBackInterface() {
                                                            @Override
                                                            public void gettoken(String str) {
                                                                token = str;
                                                                HttpClient.post(CommonUtil.APPURL, "sc_deleteInviteRecord"
                                                                        , new Gson().toJson(new Sc_deleteInviteRecord.DeleteInviteRecordParams(token, projectCode, recordId))
                                                                        , new UICallback() {
                                                                            @Override
                                                                            public void process(String data) {
                                                                                Log.e("TAG","data="+data);
                                                                                Log.e("TAG","token="+token);
                                                                                Log.e("TAG","projectCode="+projectCode);
                                                                                Log.e("TAG","recordId="+recordId);
                                                                                Sc_deleteInviteRecord.DeleteInviteRecordResult deleteInviteRecordResult=new Gson().fromJson(data, Sc_deleteInviteRecord.DeleteInviteRecordResult.class);
                                                                                int result=deleteInviteRecordResult.getResult();
                                                                                if(result==1){
                                                                                    Log.e("TAG","1-json解析失败");
                                                                                    id=1;
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==100){
                                                                                    Log.e("TAG","100-成功");
                                                                                    id=1;
                                                                                    ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                    inviteList.remove(position);
                                                                                    notifyDataSetChanged();
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==101){
                                                                                    Log.e("TAG","101-token错误");
                                                                                    id=1;
                                                                                    showerror();
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else if(result==102){
                                                                                    Log.e("TAG","102-projectCode错误");
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }else {
                                                                                    id=1;
                                                                                    ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onError(String data) {
                                                                                id=1;
                                                                                ToastUtil.showToast(MyVisiterActivity.this,"网络连接失败");
                                                                                mHandler.sendEmptyMessage(1);
                                                                            }
                                                                        });
                                                            }
                                                        }, MyVisiterActivity.this);
                                                    }else {
                                                        id = 1;
                                                        showerror();
                                                        mHandler.sendEmptyMessage(1);
                                                    }
                                                }else if(result==102){
                                                    Log.e("TAG","102-projectCode错误");
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }else {
                                                    id=1;
                                                    ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                                                    mHandler.sendEmptyMessage(1);
                                                }
                                            }

                                            @Override
                                            public void onError(String data) {
                                                id=1;
                                                ToastUtil.showToast(MyVisiterActivity.this,"网络连接失败");
                                                mHandler.sendEmptyMessage(1);
                                            }
                                        });
                            }
                        }
                    });

                    //点击事件
                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view1 = LayoutInflater.from(MyVisiterActivity.this).inflate(R.layout.dialog_detial, null);
                            popupWindow = new PopupWindow(view1, width/4*3, height/5*2, true);
//                            Log.e("TAG","11111="+width/3*2);
//                            Log.e("TAG","22222="+height/3*2);
                            //点击其他地方popupwindow消失
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.showAtLocation(view1, Gravity.CENTER,0,0);

                            ImageView close= (ImageView) view1.findViewById(R.id.close);
                            TextView invitetime= (TextView) view1.findViewById(R.id.invitetime);
                            TextView visitor= (TextView) view1.findViewById(R.id.visitor);
                            TextView name= (TextView) view1.findViewById(R.id.name);
                            TextView phone= (TextView) view1.findViewById(R.id.phone);
                            TextView time= (TextView) view1.findViewById(R.id.time);
                            TextView validtime= (TextView) view1.findViewById(R.id.validtime);
                            TextView quanxian= (TextView) view1.findViewById(R.id.quanxian);

                            invitetime.setText("邀请时间："+inviteList.get(position).getInviteTime());
//                            for(int i=0;i<inviteList.get(position).getVisitor().size();i++){
//                                visitor.setText("访客"+(i+1)+"："+"\n");
//                            }

//                            for (int i=0;i<inviteList.get(position).getVisitor().size();i++){
                                name.setText(inviteList.get(position).getVisitor().get(0).getName()+"\n");
//                            }
//                            for (int i=0;i<inviteList.get(position).getVisitorPhone().size();i++){
                                phone.setText(inviteList.get(position).getVisitorPhone().get(0).getPhone()+"\n");
//                            }

                            time.setText("有效次数："+inviteList.get(position).getUseTime());
                            validtime.setText("从"+inviteList.get(position).getBeginTime()+"\n"+"到"+inviteList.get(position).getEndTime());
                            quanxians="";
                            for (int i=0;i<inviteList.get(position).getQuanxian().size();i++){
                                quanxians=quanxians+inviteList.get(position).getQuanxian().get(i).getDoorName()+"\n";

                            }
                            quanxian.setText(quanxians);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });


//                            AlertDialog.Builder builder = new AlertDialog.Builder(MyVisiterActivity.this);
//
//                            ImageView close= (ImageView) view1.findViewById(R.id.close);
//                            TextView invitetime= (TextView) view1.findViewById(R.id.invitetime);
//                            TextView visitor= (TextView) view1.findViewById(R.id.visitor);
//                            TextView name= (TextView) view1.findViewById(R.id.name);
//                            TextView phone= (TextView) view1.findViewById(R.id.phone);
//                            TextView time= (TextView) view1.findViewById(R.id.time);
//                            TextView validtime= (TextView) view1.findViewById(R.id.validtime);
//                            TextView quanxian= (TextView) view1.findViewById(R.id.quanxian);
//
//                            invitetime.setText("邀请时间："+inviteList.get(position).getInviteTime());
//                            for(int i=0;i<inviteList.get(position).getVisitor().size();i++){
//                                visitor.setText("访客"+(i+1)+"："+"\n");
//                            }
//                            for (int i=0;i<inviteList.get(position).getVisitor().size();i++){
//                                name.setText(inviteList.get(position).getVisitor().get(i).getName()+"\n");
//                            }
//                            for (int i=0;i<inviteList.get(position).getVisitorPhone().size();i++){
//                                phone.setText(inviteList.get(position).getVisitorPhone().get(i).getPhone()+"\n");
//                            }
//
//                            time.setText("有效次数："+inviteList.get(position).getUseTime());
//                            validtime.setText("从"+inviteList.get(position).getBeginTime()+"\n"+"到"+inviteList.get(position).getEndTime());
//                            for (int i=0;i<inviteList.get(position).getQuanxian().size();i++){
//                                quanxian.setText(inviteList.get(position).getQuanxian().get(i).getQuanxian()+"\n");
//                            }
//
//                            builder.setView(view1);
//
//                            final AlertDialog dialog=builder.show();//实例化dialog才能调用其dismiss()方法实现dialog窗口的关闭
//                            close.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog.dismiss();
//                                }
//                            });
                        }
                    });
                }
            };
            listview.setAdapter(myVisiterSwipeAdapter);

            listview.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                @Override
                public void onloadMore() {
                    loadMore();
                }
            });
        }
    };

    private void loadMore() {
        pages=Integer.parseInt(page)+1;
        page="0"+String.valueOf(pages);
        HttpClient.post(CommonUtil.APPURL, "sc_inviteRecord"
                , new Gson().toJson(new Sc_inviteRecord.InviteRecordParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_inviteRecord.InviteRecordResult inviteRecordResult=new Gson().fromJson(data, Sc_inviteRecord.InviteRecordResult.class);
                        int result=inviteRecordResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            page=String.valueOf((pages-1));
                            listview.setLoadCompleted();
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            if(inviteRecordResult.getInviteList()==null||inviteRecordResult.getInviteList().size()<=0){
                                ToastUtil.showToast(MyVisiterActivity.this,"没有数据了");
                                listview.setLoadCompleted();
                            }else {
                                inviteList.addAll(inviteRecordResult.getInviteList());
                                myVisiterSwipeAdapter.appendData(inviteList,false);
                                myVisiterSwipeAdapter.upData();
                                listview.setLoadCompleted();
                            }
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            page=String.valueOf((pages-1));
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            loadMore();
                                        }else {
                                            listview.setLoadCompleted();
                                            showerror();
                                        }
                                    }
                                },MyVisiterActivity.this);
                            }else {
                                id=1;
                                listview.setLoadCompleted();
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode 不正确");
                            id=1;
                            page=String.valueOf((pages-1));
                            listview.setLoadCompleted();
                        }else {
                            id=1;
                            listview.setLoadCompleted();
                            page=String.valueOf((pages-1));
                            ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        listview.setLoadCompleted();
                        page=String.valueOf((pages-1));
                        ToastUtil.showToast(MyVisiterActivity.this,"网络操作失败");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_visiter);
        instance=this;
        token = SharedPreferencesUtils.getToken(this);
        projectCode = SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        registerMessageReceiver();

        //获取控件
        back = (ImageView) findViewById(R.id.back);
        listview = (LoadMoreListView) findViewById(R.id.listview);
        tv_invite = (TextView) findViewById(R.id.tv_invite);

        tv_invite.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);

        initVisitor();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        Log.e("TAG","width="+width);
    }

    private void initVisitor() {
        mDialog = DialogThridUtils.showWaitDialog(MyVisiterActivity.this, true);
        page = "01";
        HttpClient.post(CommonUtil.APPURL, "sc_inviteRecord"
                , new Gson().toJson(new Sc_inviteRecord.InviteRecordParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG", "data=" + data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_inviteRecord.InviteRecordResult inviteRecordResult = new Gson().fromJson(data, Sc_inviteRecord.InviteRecordResult.class);
                        int result = inviteRecordResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id=1;
                            Message message = handler.obtainMessage();
                            message.obj = inviteRecordResult.getInviteList();
                            handler.sendMessage(message);
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initVisitor();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },MyVisiterActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode 错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(MyVisiterActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(MyVisiterActivity.this,"网络操作失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyVisiterActivity.this, 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.tv_invite://邀请按钮
                    startActivity(new Intent(MyVisiterActivity.this, InvitevisitorActivity.class));
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        isForeground = true;
        isLogin();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.massky.greenlandvland.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                        Log.e("TAG", "showMsg=" + showMsg);
                        com.massky.greenlandvland.model.entity.Message message = new Gson().fromJson(extras, com.massky.greenlandvland.model.entity.Message.class);
                        Log.e("TAG", "message=" + message);
                        if (message.getType().equals("2")) {
                            showdialog();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TAG","错误");
            }
        }
    }

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MyVisiterActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MyVisiterActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(MyVisiterActivity.this);
                MainActivity.instance.finish();
                InvitevisitorActivity.instance.finish();
                InviteVisitorsActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }

    private void isLogin() {
        final String phoneNumber= SharedPreferencesUtils.getPhoneNumber(this);
        final String phoneId = SharedPreferencesUtils.getPhoneId(this);
        HttpClient.post(CommonUtil.APPURL, "sc_isLoginNew"
                , new Gson().toJson(new Sc_isLoginNew.LoginNewParams(phoneNumber,phoneId,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","phoneNumber="+phoneNumber);
                        Log.e("TAG","phoneId="+phoneId);
                        Sc_isLoginNew.LoginNewResult isLoginResult=new Gson().fromJson(data, Sc_isLoginNew.LoginNewResult.class);
                        int result=isLoginResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json格式解析失败");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                        }else if(result==103){
                            Log.e("TAG","103-已登录");
                            showdialog();
                        }else {

                        }
                    }

                    @Override
                    public void onError(String data) {

                    }
                });
    }
}
