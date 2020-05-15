package com.massky.greenlandvland.Dao;

import java.io.Serializable;

/**
 * Created by zhu on 2017/8/9.
 */

public class AccountInfo implements Serializable {
    private static final long serialVersionUID = 2L;
    public String userName;
    public String nickName;
    public String avatar;
    public String gender;
    public String birthDay;
    public String mobilePhone;
    public String address;
    public String family;
    public String balance;

    //物业服务：我的投诉 -accountInfo账户信息
    public String realName;
    public String headIcon;
}
