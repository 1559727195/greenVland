package com.massky.greenlandvland.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.massky.greenlandvland.model.entity.Sc_getFamily;
import com.massky.greenlandvland.model.entity.Sc_getTokenNew;
import com.massky.greenlandvland.model.entity.Sc_index;
import com.massky.greenlandvland.model.entity.Sc_myGateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-10-12.
 */
//存储登录和注册信息
public class SharedPreferencesUtils {
    //存储账号
    public static void saveLoginAccount(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("LoginAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("loginAccount",str);
        editor.commit();
    }
    //获得账号
    public static String getLoginAccount(Context context){
        SharedPreferences sp=context.getSharedPreferences("LoginAccount", Context.MODE_PRIVATE);
        return sp.getString("loginAccount",null);
    }

    //存储密码
    public static void savePassWord(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("PassWord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("password",str);
        editor.commit();
    }
    //获得密码
    public static String getPassWord(Context context){
        SharedPreferences sp=context.getSharedPreferences("PassWord", Context.MODE_PRIVATE);
        return sp.getString("password",null);
    }

    /**
     *  token方法
     */
    //存储token令牌
    public static void saveToken(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("token",str);
        editor.commit();
    }

    //获取用户令牌token
    public static String getToken(Context context){
        SharedPreferences sp=context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        return sp.getString("token",null);
    }


    public static void savePhoneNumber(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("PhoneNumber", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("phoneNumber",str);
        editor.commit();
    }

    public static String getPhoneNumber(Context context){
        SharedPreferences sp=context.getSharedPreferences("PhoneNumber", Context.MODE_PRIVATE);
        return sp.getString("phoneNumber",null);
    }

    public static <T> void saveProjectRoomType(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("ProjectRoomType", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("projectRoomType",new Gson().toJson(datalist));
        editor.commit();
    }

    public static <T> List<T> getProjectRoomType(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("ProjectRoomType", Context.MODE_PRIVATE);
        String strJson = sp.getString("projectRoomType", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void savePhoneId(Context context,String str){
        SharedPreferences sp=context.getSharedPreferences("PhoneId",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("phoneId",str);
        editor.commit();
    }
    public static String getPhoneId(Context context){
        SharedPreferences sp=context.getSharedPreferences("PhoneId",Context.MODE_PRIVATE);
        return sp.getString("phoneId",null);
    }


    public static void saveRoomNo(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("RoomNo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("roomNo",str);
        editor.commit();
    }

    public static String getRoomNo(Context context){
        SharedPreferences sp=context.getSharedPreferences("RoomNo", Context.MODE_PRIVATE);
        return sp.getString("roomNo",null);
    }

    public static <T> void saveRoomNoAndTypeArray(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("RoomNoAndTypeArray", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("roomNoAndTypeArray",new Gson().toJson(datalist));
        editor.commit();
    }

    public static <T> List<T> getRoomNoAndTypeArray(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("RoomNoAndTypeArray", Context.MODE_PRIVATE);
        String strJson = sp.getString("roomNoAndTypeArray", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //存储权限
    public static void saveProjectCode(Context context, String projectCode){
        SharedPreferences sp=context.getSharedPreferences("Permission", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("projectCode",projectCode);
        editor.commit();
    }
    //获取projectCode
    public static String getProjectCode(Context context){
        SharedPreferences sp=context.getSharedPreferences("Permission", Context.MODE_PRIVATE);
        return sp.getString("projectCode",null);
    }


    /**
     * sc_index方法
     * @param context
     * @param indexResult
     */
    //存储sc_index信息
    public static void saveIndex(Context context, Sc_index.IndexResult indexResult){
        SharedPreferences sp=context.getSharedPreferences("Index", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
//        Set menuImage = new HashSet();
//        menuImage.add(indexResult.getAppMenuList().get(0).getMenuImage());
//        menuImage.add(indexResult.getAppMenuList().get(1).getMenuImage());
//        menuImage.add(indexResult.getAppMenuList().get(2).getMenuImage());
//        menuImage.add(indexResult.getAppMenuList().get(3).getMenuImage());
//        menuImage.add(indexResult.getAppMenuList().get(4).getMenuImage());
        editor.putInt("result",indexResult.getResult());
        editor.putString("appMenuList",indexResult.getAppMenuList().toString());
        editor.putString("appAd",indexResult.getAppAd().toString());
//        editor.putStringSet("menuImage",menuImage);
        editor.commit();
    }

    //获取IndexAppMenuList
    public static String getIndexAppMenuList(Context context){
        SharedPreferences sp=context.getSharedPreferences("Index", Context.MODE_PRIVATE);
        return sp.getString("appMenuList",null);
    }
//    //获取appMenuList中的menuImage中的信息
//    public static Set<String> getMenuImage(Context context){
//        SharedPreferences sp=context.getSharedPreferences("Index",Context.MODE_PRIVATE);
//        Set<String> menuImage = null;
//        menuImage=sp.getStringSet("menuImage",null);
//        return menuImage;
//    }

    public static void saveAvatar(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Avatar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("avatar",str);
        editor.commit();
    }
    public static String getAvatar(Context context){
        SharedPreferences sp=context.getSharedPreferences("Avatar", Context.MODE_PRIVATE);
        return sp.getString("avatar",null);
    }

    public static void saveUserName(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("userName",str);
        editor.commit();
    }
    public static String getUserName(Context context){
        SharedPreferences sp=context.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        return sp.getString("userName",null);
    }
    public static void saveNickName(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("NickName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("nickName",str);
        editor.commit();
    }
    public static String getNickName(Context context){
        SharedPreferences sp=context.getSharedPreferences("NickName", Context.MODE_PRIVATE);
        return sp.getString("nickName",null);
    }

    public static void saveGender(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Gender", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("gender",str);
        editor.commit();
    }
    public static String getGender(Context context){
        SharedPreferences sp=context.getSharedPreferences("Gender", Context.MODE_PRIVATE);
        return sp.getString("gender",null);
    }
    public static void saveBirthday(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Birthday", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("birthday",str);
        editor.commit();
    }
    public static String getBirthday(Context context){
        SharedPreferences sp=context.getSharedPreferences("Birthday", Context.MODE_PRIVATE);
        return sp.getString("birthday",null);
    }
    public static void saveMobilePhone(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("MobilePhone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("mobilePhone",str);
        editor.commit();
    }
    public static String getMobilePhone(Context context){
        SharedPreferences sp=context.getSharedPreferences("MobilePhone", Context.MODE_PRIVATE);
        return sp.getString("mobilePhone",null);
    }
    public static void saveAddress(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("address",str);
        editor.commit();
    }
    public static String getAddress(Context context){
        SharedPreferences sp=context.getSharedPreferences("Address", Context.MODE_PRIVATE);
        return sp.getString("address",null);
    }
    public static void saveFamily(Context context, int i){
        SharedPreferences sp=context.getSharedPreferences("Family", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("family",i);
        editor.commit();
    }
    public static int getFamily(Context context){
        SharedPreferences sp=context.getSharedPreferences("Family", Context.MODE_PRIVATE);
        return sp.getInt("family",0);
    }

    public static <T> void saveFamilyList(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("FamilyList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("familylist",new Gson().toJson(datalist));
        editor.commit();
    }

    public static <T> List<T> getFamilyList(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("FamilyList", Context.MODE_PRIVATE);
        String strJson = sp.getString("familylist", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  Sc_getFamily方法
     */
    //存储Sc_getFamily信息
    public static void saveGetFamily(Context context, Sc_getFamily.GetFamilyResult getFamilyResult){
        SharedPreferences sp=context.getSharedPreferences("Family", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("result",getFamilyResult.getResult());
        editor.putInt("number",getFamilyResult.getFamilyList().size());
        editor.putString("familyList",getFamilyResult.getFamilyList().toString());
        editor.commit();
    }

    //获得Family数量
    public static int getFamilyNumber(Context context){
        SharedPreferences sp=context.getSharedPreferences("Family", Context.MODE_PRIVATE);
        return sp.getInt("number",0);
    }

    public static String getFamilyList(Context context){
        SharedPreferences sp=context.getSharedPreferences("Family", Context.MODE_PRIVATE);
        return sp.getString("familyList",null);
    }

//    //存储摄像头信息
//    public static <T> void saveMyCamera(Context context, List<T> datalist){
//        SharedPreferences sp=context.getSharedPreferences("MyCameraList",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("CameraList",new Gson().toJson(datalist));
//        editor.commit();
//    }
//    public static <T> List<T> getMyCamera(Context context,Class<T> cls){
//        SharedPreferences sp=context.getSharedPreferences("MyCameraList",Context.MODE_PRIVATE);
//        String strJson = sp.getString("CameraList", null);
//        List<T> list = new ArrayList<T>();
//        try {
//            Gson gson = new Gson();
//            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
//            for (JsonElement jsonElement : arry) {
//                list.add(gson.fromJson(jsonElement, cls));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * sc_myGateway方法
     * @param context
     * @param myGateResult
     */
    //存储boxNumber
    public static void saveMyGateway(Context context, Sc_myGateway.MyGateResult myGateResult){
        SharedPreferences sp=context.getSharedPreferences("Gateway", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("boxNumber",myGateResult.getBoxNumber());
        editor.commit();
    }
    //获得boxNumber
    public static String getBoxNumber(Context context){
        SharedPreferences sp=context.getSharedPreferences("Gateway", Context.MODE_PRIVATE);
        return sp.getString("boxNumber",null);
    }




    //存储boxNumber
    public static void save_myAreaNumber(Context context, String area_number){
        SharedPreferences sp=context.getSharedPreferences("AreaNumber", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("areaNumber",area_number);
        editor.commit();
    }

    //获得boxNumber
    public static String get_myAreaNumber(Context context){
        SharedPreferences sp=context.getSharedPreferences("AreaNumber", Context.MODE_PRIVATE);
        return sp.getString("areaNumber",null);
    }

    //存储房间信息
    public static <T> void saveMyRoom(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("MyRoomList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("RoomList",new Gson().toJson(datalist));
        editor.commit();
    }
    public static <T> List<T> getMyRoom(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("MyRoomList", Context.MODE_PRIVATE);
        String strJson = sp.getString("RoomList", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //存储门禁信息
    public static <T> void saveDoorList(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("DoorList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("doorList",new Gson().toJson(datalist));
        editor.commit();
    }
    public static <T> List<T> getDoorList(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("DoorList", Context.MODE_PRIVATE);
        String strJson = sp.getString("doorList", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

//    public static void saveDeviceMac(Context context,String str){
//        SharedPreferences sp=context.getSharedPreferences("DeviceMac",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("deviceMac",str);
//        editor.commit();
//    }
//    public static String getDeviceMac(Context context){
//        SharedPreferences sp=context.getSharedPreferences("DeviceMac",Context.MODE_PRIVATE);
//        return sp.getString("deviceMac",null);
//    }
//    public static void saveParamId(Context context,String str){
//        SharedPreferences sp=context.getSharedPreferences("ParamId",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("paramId",str);
//        editor.commit();
//    }
//    public static String getParamId(Context context){
//        SharedPreferences sp=context.getSharedPreferences("ParamId",Context.MODE_PRIVATE);
//        return sp.getString("paramId",null);
//    }
//    public static void saveParamInout(Context context,String str){
//        SharedPreferences sp=context.getSharedPreferences("ParamInout",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("paramInout",str);
//        editor.commit();
//    }
//    public static String getParamInout(Context context){
//        SharedPreferences sp=context.getSharedPreferences("ParamInout",Context.MODE_PRIVATE);
//        return sp.getString("paramInout",null);
//    }
//    public static void saveParamFloor(Context context,String str){
//        SharedPreferences sp=context.getSharedPreferences("ParamFloor",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("paramFloor",str);
//        editor.commit();
//    }
//    public static String getParamFloor(Context context){
//        SharedPreferences sp=context.getSharedPreferences("ParamFloor",Context.MODE_PRIVATE);
//        return sp.getString("paramFloor",null);
//    }


    //存储我的场景
    public static <T> void saveMyScene(Context context, List<T> datalist){
        SharedPreferences sp=context.getSharedPreferences("MyScene", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("sceneList",new Gson().toJson(datalist));
        editor.commit();
    }
    public static <T> List<T> getMyScene(Context context, Class<T> cls){
        SharedPreferences sp=context.getSharedPreferences("MyScene", Context.MODE_PRIVATE);
        String strJson = sp.getString("sceneList", null);
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //存储调光灯数据
    public static void saveDimmer(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Dimmer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("dimmer",str);
        editor.commit();
    }
    //获取调光数据
    public static String getDimmer(Context context){
        SharedPreferences sp=context.getSharedPreferences("Dimmer", Context.MODE_PRIVATE);
        return sp.getString("dimmer",null);
    }

    public static void cleanToken(Context context){
        SharedPreferences sp=context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
    public static void cleanPassword(Context context){
        SharedPreferences sp=context.getSharedPreferences("PassWord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveAccountType(Context context, int i){
        SharedPreferences sp=context.getSharedPreferences("AccountType", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("accountType",i);
        editor.commit();
    }

    public static int getAccountType(Context context){
        SharedPreferences sp=context.getSharedPreferences("AccountType", Context.MODE_PRIVATE);
        return sp.getInt("accountType",0);
    }

    public static void saveType(Context context, String str){
        SharedPreferences sp=context.getSharedPreferences("Type", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Type",str);
        editor.commit();
    }

    public static String getType(Context context){
        SharedPreferences sp=context.getSharedPreferences("Type", Context.MODE_PRIVATE);
        return sp.getString("Type",null);
    }

    public static void cleanType(Context context){
        SharedPreferences sp=context.getSharedPreferences("Type", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
