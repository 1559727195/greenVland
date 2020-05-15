package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-12-20.
 */

public class Sc_checkMobilePhoneNew {
    private CheckMobilePhoneNewParams checkMobilePhoneNewParams;
    private CheckMobilePhoneNewResult checkMobilePhoneNewResult;

    public Sc_checkMobilePhoneNew(CheckMobilePhoneNewParams checkMobilePhoneNewParams, CheckMobilePhoneNewResult checkMobilePhoneNewResult) {
        this.checkMobilePhoneNewParams = checkMobilePhoneNewParams;
        this.checkMobilePhoneNewResult = checkMobilePhoneNewResult;
    }

    public CheckMobilePhoneNewParams getCheckMobilePhoneNewParams() {
        return checkMobilePhoneNewParams;
    }

    public void setCheckMobilePhoneNewParams(CheckMobilePhoneNewParams checkMobilePhoneNewParams) {
        this.checkMobilePhoneNewParams = checkMobilePhoneNewParams;
    }

    public CheckMobilePhoneNewResult getCheckMobilePhoneNewResult() {
        return checkMobilePhoneNewResult;
    }

    public void setCheckMobilePhoneNewResult(CheckMobilePhoneNewResult checkMobilePhoneNewResult) {
        this.checkMobilePhoneNewResult = checkMobilePhoneNewResult;
    }

    @Override
    public String toString() {
        return "Sc_checkMobilePhoneNew{" +
                "checkMobilePhoneNewParams=" + checkMobilePhoneNewParams +
                ", checkMobilePhoneNewResult=" + checkMobilePhoneNewResult +
                '}';
    }
    public static class CheckMobilePhoneNewParams{
        private String mobilePhone;
        private int appCode;

        public CheckMobilePhoneNewParams(String mobilePhone, int appCode) {
            this.mobilePhone = mobilePhone;
            this.appCode = appCode;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "CheckMobilePhoneNewParams{" +
                    "mobilePhone='" + mobilePhone + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }
    public static class CheckMobilePhoneNewResult{
        private int result;

        public CheckMobilePhoneNewResult(int result) {
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
            return "CheckMobilePhoneNewResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
