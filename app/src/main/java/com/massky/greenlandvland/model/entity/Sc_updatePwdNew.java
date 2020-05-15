package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-12-20.
 */

public class Sc_updatePwdNew {
    private UpdatePwdNewParams updatePwdNewParams;
    private UpdatePwdNewResult updatePwdNewResult;

    public Sc_updatePwdNew(UpdatePwdNewParams updatePwdNewParams, UpdatePwdNewResult updatePwdNewResult) {
        this.updatePwdNewParams = updatePwdNewParams;
        this.updatePwdNewResult = updatePwdNewResult;
    }

    public UpdatePwdNewParams getUpdatePwdNewParams() {
        return updatePwdNewParams;
    }

    public void setUpdatePwdNewParams(UpdatePwdNewParams updatePwdNewParams) {
        this.updatePwdNewParams = updatePwdNewParams;
    }

    public UpdatePwdNewResult getUpdatePwdNewResult() {
        return updatePwdNewResult;
    }

    public void setUpdatePwdNewResult(UpdatePwdNewResult updatePwdNewResult) {
        this.updatePwdNewResult = updatePwdNewResult;
    }

    @Override
    public String toString() {
        return "Sc_updatePwdNew{" +
                "updatePwdNewParams=" + updatePwdNewParams +
                ", updatePwdNewResult=" + updatePwdNewResult +
                '}';
    }

    public static class UpdatePwdNewParams{
        private String mobilePhone;
        private String newPwd;
        private int appCode;

        public UpdatePwdNewParams(String mobilePhone, String newPwd, int appCode) {
            this.mobilePhone = mobilePhone;
            this.newPwd = newPwd;
            this.appCode = appCode;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "UpdatePwdNewParams{" +
                    "mobilePhone='" + mobilePhone + '\'' +
                    ", newPwd='" + newPwd + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    public static class UpdatePwdNewResult{
        private int result;

        public UpdatePwdNewResult(int result) {
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
            return "UpdatePwdNewResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
