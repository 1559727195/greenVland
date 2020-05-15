package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.WheelView.WPopupWindow;
import com.massky.greenlandvland.View.WheelView.WheelView;
import com.massky.greenlandvland.View.imagepicker.GlideImageLoader;
import com.massky.greenlandvland.View.imagepicker.ImagePickerAdapter;
import com.massky.greenlandvland.View.imagepicker.SelectDialog;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_complaintCategory;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_submitComplaint;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,ImagePickerAdapter.DeleteIctemCkickListener{
    private ImageView back;//回退键
    private ImageView iv_record;//投诉建议
    private TextView tv_typeselect;//类型选择
    private TextView title;
    private TextView content;
    private Button send,takephone;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;               //允许选择图片最大数

    List<Sc_complaintCategory.ComplaintCategoryResult.ComplaintCategory> complaintCategoryList;
    String phone;
    private String token;
    private String projectCode;
    private String roomNo;
    private String complaintTitle;
    private String complaintCategory;
    private String complaintContent;
    private int id=1;
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
    public static ComplainActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        instance=this;

        token=SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);

        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();

        initcomplaintCategory();
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        iv_record= (ImageView) findViewById(R.id.iv_record);
        tv_typeselect= (TextView) findViewById(R.id.tv_typeselect);
        send= (Button) findViewById(R.id.send);
        takephone= (Button) findViewById(R.id.takephone);
        content= (TextView) findViewById(R.id.content);
        title= (TextView) findViewById(R.id.title);

        //添加监听
        back.setOnClickListener(clickListener);
        iv_record.setOnClickListener(clickListener);
        tv_typeselect.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
        takephone.setOnClickListener(clickListener);

        registerMessageReceiver();
    }

    private void initcomplaintCategory() {
//        final String token= SharedPreferencesUtils.getToken(ComplainActivity.this);
//        final String projectCode=SharedPreferencesUtils.getProjectCode(ComplainActivity.this);
        mDialog=DialogThridUtils.showWaitDialog(ComplainActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_complaintCategory"
                , new Gson().toJson(new Sc_complaintCategory.ComplaintCategoryParams(token, projectCode))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
                        Sc_complaintCategory.ComplaintCategoryResult complaintCategoryResult=new Gson().fromJson(data, Sc_complaintCategory.ComplaintCategoryResult.class);
                        int result=complaintCategoryResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            complaintCategoryList=complaintCategoryResult.getComplaintCategoryList();
//                            Log.e("TAG","complaintCategoryList="+complaintCategoryList);
                            phone=complaintCategoryResult.getPropertyPhone();
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initcomplaintCategory();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ComplainActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"数据获取失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"数据获取失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(ComplainActivity.this,"网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainActivity.this, 1);
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
                case R.id.iv_record://投诉记录键
                    startActivity(new Intent(ComplainActivity.this,ComplainRecordActivity.class));
                    break;
                case R.id.tv_typeselect://类型选择

                    View wh= LayoutInflater.from(ComplainActivity.this).inflate(R.layout.item_popup_typeselect,null);
                    final WheelView picker= (WheelView) wh.findViewById(R.id.wheelview);
                    ImageView iv_ensure,iv_cancle;
                    iv_ensure= (ImageView) wh.findViewById(R.id.iv_ensure);
                    iv_cancle= (ImageView) wh.findViewById(R.id.iv_cancel);


                    for (int i=0;i<complaintCategoryList.size();i++){
                        picker.addData(complaintCategoryList.get(i).getCategoryName());
                    }
                    picker.setCenterItem(0);
                    final WPopupWindow popupWindow=new WPopupWindow(wh);
                    popupWindow.showAtLocation(wh, Gravity.BOTTOM, 0, 0);


                    iv_ensure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            picker.getCenterItem();
//                            Log.e("TAG","11111="+picker.getCenterItem());
                            tv_typeselect.setText(picker.getCenterItem().toString());
                            popupWindow.dismiss();
                        }
                    });
                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });

                    break;
                case R.id.send:
                    complaintCategory=tv_typeselect.getText().toString()+"";
                    complaintTitle=title.getText().toString()+"";
                    complaintContent=content.getText().toString()+"";
                    if(!TextUtils.isEmpty(complaintCategory)){
                        if(!TextUtils.isEmpty(complaintTitle)){
                            if(!TextUtils.isEmpty(complaintContent)){
                                submitComplaint();
                            }else {
                                ToastUtil.showToast(ComplainActivity.this,"内容为空");
                            }
                        }else {
                            ToastUtil.showToast(ComplainActivity.this,"标题为空");
                        }
                    }else {
                        ToastUtil.showToast(ComplainActivity.this,"类型为空");
                    }

                    break;
                case R.id.takephone:
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void submitComplaint() {
        mDialog=DialogThridUtils.showWaitDialog(ComplainActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_submitComplaint",
                new Gson().toJson(new Sc_submitComplaint.SubmitComplaintParams(token,projectCode,complaintTitle,complaintCategory,complaintContent,complaintImageList,roomNo)),
                new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","token="+token);
                        Log.e("TAG","projectCode="+projectCode);
                        Log.e("TAG","complaintTitle="+complaintTitle);
                        Log.e("TAG","complaintCategory="+complaintCategory);
                        Log.e("TAG","complaintContent="+complaintContent);
                        Log.e("TAG","complaintImageList="+complaintImageList);

                        Sc_submitComplaint.SubmitComplaintResult submitComplaintResult=new Gson().fromJson(data, Sc_submitComplaint.SubmitComplaintResult.class);
                        int result=submitComplaintResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"操作失败");
                        }else if(result==100){
                            id=1;
                            Log.e("TAG","100-成功");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"投诉成功");
                            finish();
                        }else if(result==101){
                            id=id+1;
                            new GetToken(new CallBackInterface() {
                                @Override
                                public void gettoken(String str) {
                                    token=str;
                                    Log.e("TAG","token="+token);
                                    if(!TextUtils.isEmpty(token)){
                                        submitComplaint();
                                    }else {
                                        showerror();
                                    }
                                }
                            },ComplainActivity.this);
                        }else if(result==102){
                            id=1;
                            Log.e("TAG","102-projectc错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(ComplainActivity.this,"网络操作失败");
                    }
                });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        adapter.setDeleteIctemCkickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(ComplainActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(ComplainActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onDeleteItemClick(int position) {
        Log.e("TAG","点击删除position="+position);
        Log.e("TAG","删除前imagesize="+selImageList.size());
        selImageList.remove(position);
        Log.e("TAG","删除后imagesize="+selImageList.size());

//        Boolean isnull=selImageList==null?true:false;
//        Log.e("TAG","isnull="+isnull);
        if(selImageList!=null){
            //打开选择,本次允许选择的数量
            ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
            adapter.setImages(selImageList);
//            Log.e("TAG","complaintImageList清空前的数量为："+complaintImageList.size());
            complaintImageList.clear();
//            Log.e("TAG","complaintImageList清空后的数量为："+complaintImageList.size());
//        Boolean isnull=complaintImageList==null?true:false;
//        Log.e("TAG","isnull="+isnull);
            if(selImageList.size()>0){
                for (int i=0;i<selImageList.size();i++) {
                    bitmap = BitmapFactory.decodeFile(selImageList.get(i).path);
                    base64 = PictureUtil.bitmapToString(bitmap);
                    complaintImageList.add(new Sc_submitComplaint.SubmitComplaintParams.ComplaintImage(base64));
                }
            }
//        Log.e("TAG","complaintImageList删除后的数量为："+complaintImageList.size());
        }else {
            Log.e("TAG","删除后image的数量为0");
        }
    }

    ArrayList<ImageItem> images = null;

    List<Sc_submitComplaint.SubmitComplaintParams.ComplaintImage> complaintImageList=new ArrayList();
    Bitmap bitmap;
    String base64;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }

        if(selImageList !=null){
            for (int i=0;i<selImageList.size();i++){
                bitmap= BitmapFactory.decodeFile(selImageList.get(i).path);
                base64= PictureUtil.bitmapToString(bitmap);
                complaintImageList.add(new Sc_submitComplaint.SubmitComplaintParams.ComplaintImage(base64));
                Log.e("TAG","complaintImageList未删除的数量为："+complaintImageList.size());
            }
        }
    }



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
        AlertDialog.Builder builder=new AlertDialog.Builder(ComplainActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(ComplainActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(ComplainActivity.this);
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
