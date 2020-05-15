package com.massky.greenlandvland.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.DateChangeWheelView.TimePickerDialog;
import com.massky.greenlandvland.View.DateChangeWheelView.data.Type;
import com.massky.greenlandvland.View.DateChangeWheelView.listener.OnDateSetListener;
import com.massky.greenlandvland.View.RoundImageView;
import com.massky.greenlandvland.View.imagepicker.SelectDialog;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_myAccount;
import com.massky.greenlandvland.model.entity.Sc_updateAccount;
import com.massky.greenlandvland.model.entity.Sc_updateAvatar;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by masskywcy on 2018-11-09.
 */

public class Fragment_Detials extends Fragment {
    private View view;
    private ImageView back;//回退键
    private Switch sw_gender;
    private TextView tv_man, tv_woman;
    private TextView tv_username, tv_nickname, tv_birthday, tv_address;
    private RelativeLayout rl_myavatar, rl_username, rl_nickname, rl_birthday, rl_address, rl_changepassword;
    private RoundImageView image;
    private Bitmap bitmap;//存储处理过的图片
    private String token;
    private String projectCode;
    private String roomNo;
    private String avatar;
    private String realName;
    private String nickName;
    private String birthday;
    private String address;
    private String gender;
    private String mobilePhone;
    private boolean isTrue = false;
    private int id = 1;
    private Intent intent;
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
    TimePickerDialog mDialogYearMonthDay;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private String birth;
    private long date;

    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detials, container, false);
        token = SharedPreferencesUtils.getToken(getContext());
        projectCode = SharedPreferencesUtils.getProjectCode(getContext());
        roomNo = SharedPreferencesUtils.getRoomNo(getContext());


        init();//初始化控件

        return view;
    }

    OnDateSetListener dateSetListener=new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
            mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
            String text = getDateToString(millseconds);
            tv_birthday.setText(text);

            //修改生日数据交互
            changeBirthday(text);
        }
    };


    //初始化控件
    private void init() {
        //获取控件
        back = view.findViewById(R.id.back);
        image = view.findViewById(R.id.image);
        sw_gender = view.findViewById(R.id.sw_gender);
        tv_man = view.findViewById(R.id.tv_man);
        tv_woman = view.findViewById(R.id.tv_woman);
        tv_username = view.findViewById(R.id.tv_username);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_birthday = view.findViewById(R.id.tv_birthday);
        tv_address = view.findViewById(R.id.tv_address);
        rl_myavatar = view.findViewById(R.id.rl_myavatar);
        rl_username = view.findViewById(R.id.rl_username);
        rl_nickname = view.findViewById(R.id.rl_nickname);
        rl_birthday = view.findViewById(R.id.rl_birthday);
        rl_address = view.findViewById(R.id.rl_address);
        rl_changepassword = view.findViewById(R.id.rl_changepassword);

        //添加监听
        back.setOnClickListener(clickListener);
        sw_gender.setOnClickListener(clickListener);
        rl_myavatar.setOnClickListener(clickListener);
        rl_username.setOnClickListener(clickListener);
        rl_nickname.setOnClickListener(clickListener);
        rl_birthday.setOnClickListener(clickListener);
        rl_address.setOnClickListener(clickListener);
        rl_changepassword.setOnClickListener(clickListener);


    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back://回退键
                    ((MainActivity) getActivity()).showFragment_Mine();
                    break;
                case R.id.rl_myavatar://我的头像
//                    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    List<String> names = new ArrayList<>();
                    names.add("拍照");
                    names.add("相册");
                    showDialog(new SelectDialog.SelectDialogListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    takePhoto();
                                    break;
                                case 1:
                                    selectPhoto();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, names);
                    break;
                case R.id.rl_username://用户名
                    startActivity(new Intent(getActivity(), ChangeUserNameActivity.class));
                    break;
                case R.id.rl_nickname://昵称
                    startActivity(new Intent(getActivity(), ChangeNickNameActivity.class));
                    break;
                case R.id.rl_birthday://出生年月
                    birthday=SharedPreferencesUtils.getBirthday(getContext());
                    Log.e("TAG","birthday="+birthday);
                    birth=birthday.replaceAll("-","/");
                    Log.e("TAG","birth="+birth);
                    date=Date.parse(birth);
                    Log.e("TAG","date="+date);
                    mDialogYearMonthDay = new TimePickerDialog.Builder()
                            .setType(Type.YEAR_MONTH_DAY)
                            .setTitleStringId("出生年月")
                            .setWheelItemTextSize(14)
                            .setCurrentMillseconds(date)
                            .setCallBack(dateSetListener)
                            .build();
                    mDialogYearMonthDay.show(getFragmentManager(), "year_month_day");
                    break;
                case R.id.rl_address://我的地址
                    startActivity(new Intent(getActivity(), ChangeAddressActivity.class));
                    break;
                case R.id.rl_changepassword://修改密码
                    startActivity(new Intent(getActivity(), ChangePassWordActivity.class));
                    break;
                default:
                    break;
            }
        }
    };

    private void takePhoto() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            }else {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启用摄像头
                startActivityForResult(intent, 100);
            }
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启用摄像头
            startActivityForResult(intent, 100);
        }
    }

    private void selectPhoto() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
//                intent = new Intent(Intent.ACTION_PICK);//启动图库
//                intent.setType("image/*");//取到底层所有图片
//                startActivityForResult(intent, 200);

                File outputImage = new File(Environment.getExternalStorageDirectory(),
                        "output_image.jpg");
                imageUri = Uri.fromFile(outputImage);

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent = new Intent(Intent.ACTION_PICK,null);
                //此处调用了图片选择器
                //如果直接写intent.setDataAndType("image/*");
                //调用的是系统图库
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 200);
            }
        }else {
//            intent = new Intent(Intent.ACTION_PICK);//启动图库
//            intent.setType("image/*");//取到底层所有图片
//            startActivityForResult(intent, 200);

            File outputImage = new File(Environment.getExternalStorageDirectory(),
                    "output_image.jpg");
            imageUri = Uri.fromFile(outputImage);

            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent = new Intent(Intent.ACTION_PICK,null);
            //此处调用了图片选择器
            //如果直接写intent.setDataAndType("image/*");
            //调用的是系统图库
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 200);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启用摄像头
            startActivityForResult(intent, 100);
        }else if(requestCode==2&& grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            intent = new Intent(Intent.ACTION_PICK);//启动图库
//            intent.setType("image/*");//取到底层所有图片
//            startActivityForResult(intent, 200);

            File outputImage = new File(Environment.getExternalStorageDirectory(),
                    "output_image.jpg");
            imageUri = Uri.fromFile(outputImage);

            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent = new Intent(Intent.ACTION_PICK,null);
            //此处调用了图片选择器
            //如果直接写intent.setDataAndType("image/*");
            //调用的是系统图库
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 200);
        }
    }

    /**
     * @param requestCode 100拍照，200图库选择
     * @param resultCode  RESULT_OK
     * @param data        图片
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//获取成功
            if (requestCode == 100) {//拍照
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                updataAvatar(bitmap);
            } else if (requestCode == 200) {//选择图片
//                Uri uri = data.getData();
//                Log.e("TAG", "uri=" + uri);
//                crop(uri);

                Log.e("TAG","requestCode="+requestCode);

                //此处启动裁剪程序
                intent = new Intent("com.android.camera.action.CROP");
                //此处注释掉的部分是针对android 4.4路径修改的一个测试
                //有兴趣的读者可以自己调试看看
//                String text=imageUri.toString();
//                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                intent.setDataAndType(data.getData(), "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 300);

            } else if (requestCode == 300) {//剪裁图片
                Log.e("TAG", "300");
//                bitmap = data.getParcelableExtra("data");
//                updataAvatar(bitmap);

                try {
                    //将output_image.jpg对象解析成Bitmap对象，然后设置到ImageView中显示出来
                    Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver()
                            .openInputStream(imageUri));
//                    image.setImageBitmap(bitmap);
                    updataAvatar(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("TAG","e="+e);
                }
            }
        }
    }

    //修改头像
    private void updataAvatar(Bitmap bitmap) {
        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
        String avatars;
        avatars = PictureUtil.bitmapToString(bitmap);
        HttpClient.post(CommonUtil.APPURL, "sc_updateAvatar"
                , new Gson().toJson(new Sc_updateAvatar.UpdateAvatarParams(token, projectCode, avatars))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","avatar="+avatar);
                        Sc_updateAvatar.UpdateAvatarResult updateAvatarResult = new Gson().fromJson(data, Sc_updateAvatar.UpdateAvatarResult.class);
                        int result = updateAvatarResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            SharedPreferencesUtils.saveAvatar(getContext(), avatars);
                            FragmentMenuLeft.iv_self.setImageBitmap(bitmap);
                            image.setImageBitmap(bitmap);
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            updataAvatar(bitmap);
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 103) {
                            Log.e("TAG", "103-avatar错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "图片上传失败");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id = 1;
                        ToastUtil.showToast(getContext(), "操作失败");
                    }
                });
    }

//    //剪裁图片
//    private void crop(Uri uri) {
//        intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        //剪裁框比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        //剪裁后的图片输出尺寸
//        intent.putExtra("outputX", 80);
//        intent.putExtra("outputY", 80);
//        intent.putExtra("outputFormat", "JPEG");//格式
//        intent.putExtra("noFaceDetection", true);//取消面部识别
//        intent.putExtra("return-data", true);//有返回图片
//        startActivityForResult(intent, 300);
//    }

    private void changeBirthday(String str) {
//        birthday = tv_birthday.getText().toString();
        birthday = str;
        HttpClient.post(CommonUtil.APPURL, "sc_updateAccount", new Gson().toJson(new Sc_updateAccount.UpdateAccountParams(token, projectCode
                        , new Sc_updateAccount.UpdateAccountParams.AccountInfo(nickName, gender, birthday, address, mobilePhone)))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+token);
//                                    Log.e("TAG","projectCode="+projectCode);
//                                    Log.e("TAG","realName="+realName);
//                                    Log.e("TAG","gender="+gender);
//                                    Log.e("TAG","birthday="+birthday);
//                                    Log.e("TAG","address="+address);
//                                    Log.e("TAG","mobilePhone="+mobilePhone);
                        Sc_updateAccount.UpdateAccountResult updateAccountResult = new Gson().fromJson(data, Sc_updateAccount.UpdateAccountResult.class);
                        int result = updateAccountResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-解析成功");
                            id = 1;
                            SharedPreferencesUtils.saveBirthday(getContext(),birthday);
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            changeBirthday(birthday);
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id = 1;
                        ToastUtil.showToast(getContext(), "操作失败");
                    }
                });
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    @Override
    public void onResume() {
        super.onResume();

        //设置数据
        setMyAccount();
    }


    //设置数据
    private void setMyAccount() {
        realName = SharedPreferencesUtils.getUserName(getContext());
        nickName = SharedPreferencesUtils.getNickName(getContext());
        avatar = SharedPreferencesUtils.getAvatar(getContext());
        gender = SharedPreferencesUtils.getGender(getContext());
        birthday = SharedPreferencesUtils.getBirthday(getContext());
        mobilePhone = SharedPreferencesUtils.getMobilePhone(getContext());
        address = SharedPreferencesUtils.getAddress(getContext());

        bitmap = PictureUtil.base64ToBitmap(avatar);
        image.setImageBitmap(bitmap);

        tv_username.setText(realName);
        tv_nickname.setText(nickName);
        tv_birthday.setText(birthday);
        tv_address.setText(address);

        isTrue = gender.toString().equals("男");
        sw_gender.setChecked(isTrue);
        if (isTrue == true) {
            tv_man.setVisibility(View.VISIBLE);
            tv_woman.setVisibility(View.GONE);
            SharedPreferencesUtils.saveGender(getContext(), "男");
        } else {
            tv_woman.setVisibility(View.VISIBLE);
            tv_man.setVisibility(View.GONE);
            SharedPreferencesUtils.saveGender(getContext(), "女");
        }

        sw_gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_man.setVisibility(View.VISIBLE);
                    tv_woman.setVisibility(View.GONE);
                    gender = "男";
                    isTrue = true;
                } else if (!isChecked) {
                    tv_woman.setVisibility(View.VISIBLE);
                    tv_man.setVisibility(View.GONE);
                    gender = "女";
                    isTrue = false;
                }
                changeGender();
            }
        });
    }

    private void changeGender() {
        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
        HttpClient.post(CommonUtil.APPURL, "sc_updateAccount", new Gson().toJson(new Sc_updateAccount.UpdateAccountParams(token, projectCode
                        , new Sc_updateAccount.UpdateAccountParams.AccountInfo(nickName, gender, birthday, address, mobilePhone)))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+token);
//                                    Log.e("TAG","projectCode="+projectCode);
//                                    Log.e("TAG","realName="+realName);
//                                    Log.e("TAG","gender="+gender);
//                                    Log.e("TAG","birthday="+birthday);
//                                    Log.e("TAG","address="+address);
//                                    Log.e("TAG","mobilePhone="+mobilePhone);
                        Sc_updateAccount.UpdateAccountResult updateAccountResult = new Gson().fromJson(data, Sc_updateAccount.UpdateAccountResult.class);
                        int result = updateAccountResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-解析成功");
                            SharedPreferencesUtils.saveGender(getContext(), gender);
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            changeGender();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id = 1;
                        ToastUtil.showToast(getContext(), "网络操作失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((MainActivity) getActivity()).showFragmentMain();
            }
        });
        builder.create().show();
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }
}
