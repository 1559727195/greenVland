package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-07-20.
 */

public class Sc_newRegister {
    private NewRegisterParams newRegisterParams;
    private NewRegisterResult newRegisterResult;

    public Sc_newRegister(NewRegisterParams newRegisterParams, NewRegisterResult newRegisterResult) {
        this.newRegisterParams = newRegisterParams;
        this.newRegisterResult = newRegisterResult;
    }

    public NewRegisterParams getNewRegisterParams() {
        return newRegisterParams;
    }

    public void setNewRegisterParams(NewRegisterParams newRegisterParams) {
        this.newRegisterParams = newRegisterParams;
    }

    public NewRegisterResult getNewRegisterResult() {
        return newRegisterResult;
    }

    public void setNewRegisterResult(NewRegisterResult newRegisterResult) {
        this.newRegisterResult = newRegisterResult;
    }

    @Override
    public String toString() {
        return "Sc_newRegister{" +
                "newRegisterParams=" + newRegisterParams +
                ", newRegisterResult=" + newRegisterResult +
                '}';
    }

    public static class NewRegisterParams{
        private String mobilePhone;
        private String loginPwd;
        private String roomNo;
        private String projectCode;

        public NewRegisterParams(String mobilePhone, String loginPwd, String roomNo, String projectCode) {
            this.mobilePhone = mobilePhone;
            this.loginPwd = loginPwd;
            this.roomNo = roomNo;
            this.projectCode = projectCode;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getLoginPwd() {
            return loginPwd;
        }

        public void setLoginPwd(String loginPwd) {
            this.loginPwd = loginPwd;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        @Override
        public String toString() {
            return "NewRegisterParams{" +
                    "mobilePhone='" + mobilePhone + '\'' +
                    ", loginPwd='" + loginPwd + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }

    public static class NewRegisterResult{
        private int result;

        public NewRegisterResult(int result) {
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
            return "NewRegisterResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
