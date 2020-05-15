package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-12-20.
 */

public class Sc_isLoginNew {
    private LoginNewParams loginNewParams;
    private LoginNewResult loginNewResult;

    public Sc_isLoginNew(LoginNewParams loginNewParams, LoginNewResult loginNewResult) {
        this.loginNewParams = loginNewParams;
        this.loginNewResult = loginNewResult;
    }

    public LoginNewParams getLoginNewParams() {
        return loginNewParams;
    }

    public void setLoginNewParams(LoginNewParams loginNewParams) {
        this.loginNewParams = loginNewParams;
    }

    public LoginNewResult getLoginNewResult() {
        return loginNewResult;
    }

    public void setLoginNewResult(LoginNewResult loginNewResult) {
        this.loginNewResult = loginNewResult;
    }

    @Override
    public String toString() {
        return "Sc_isLoginNew{" +
                "loginNewParams=" + loginNewParams +
                ", loginNewResult=" + loginNewResult +
                '}';
    }

    public static class LoginNewParams{
        private String mobilePhone;
        private String phoneId;
        private int appCode;

        public LoginNewParams(String mobilePhone, String phoneId, int appCode) {
            this.mobilePhone = mobilePhone;
            this.phoneId = phoneId;
            this.appCode = appCode;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getPhoneId() {
            return phoneId;
        }

        public void setPhoneId(String phoneId) {
            this.phoneId = phoneId;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "LoginNewParams{" +
                    "mobilePhone='" + mobilePhone + '\'' +
                    ", phoneId='" + phoneId + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    public static class LoginNewResult{
        private int result;

        public LoginNewResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "LoginNewResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
