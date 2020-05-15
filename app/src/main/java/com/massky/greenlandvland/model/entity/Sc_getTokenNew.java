package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-12-20.
 */

public class Sc_getTokenNew {
    private GetTokenNewParams getTokenNewParams;
    private GetTokenNewResult getTokenNewResult;

    public Sc_getTokenNew(GetTokenNewParams getTokenNewParams, GetTokenNewResult getTokenNewResult) {
        this.getTokenNewParams = getTokenNewParams;
        this.getTokenNewResult = getTokenNewResult;
    }

    public GetTokenNewParams getGetTokenNewParams() {
        return getTokenNewParams;
    }

    public void setGetTokenNewParams(GetTokenNewParams getTokenNewParams) {
        this.getTokenNewParams = getTokenNewParams;
    }

    public GetTokenNewResult getGetTokenNewResult() {
        return getTokenNewResult;
    }

    public void setGetTokenNewResult(GetTokenNewResult getTokenNewResult) {
        this.getTokenNewResult = getTokenNewResult;
    }

    @Override
    public String toString() {
        return "Sc_getTokenNew{" +
                "getTokenNewParams=" + getTokenNewParams +
                ", getTokenNewResult=" + getTokenNewResult +
                '}';
    }

    public static class GetTokenNewParams{
        private String loginAccount;
        private String timeStamp;
        private String signature;
        private int appCode;

        public GetTokenNewParams(String loginAccount, String timeStamp, String signature, int appCode) {
            this.loginAccount = loginAccount;
            this.timeStamp = timeStamp;
            this.signature = signature;
            this.appCode = appCode;
        }

        public String getLoginAccount() {
            return loginAccount;
        }

        public void setLoginAccount(String loginAccount) {
            this.loginAccount = loginAccount;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "GetTokenNewParams{" +
                    "loginAccount='" + loginAccount + '\'' +
                    ", timeStamp='" + timeStamp + '\'' +
                    ", signature='" + signature + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    public static class GetTokenNewResult{
        private int result;
        private String token;
        private String deadline;

        public GetTokenNewResult(String token, int result, String deadline) {
            this.token = token;
            this.result = result;
            this.deadline = deadline;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        @Override
        public String toString() {
            return "TokenResult{" +
                    "result=" + result +
                    ", token='" + token + '\'' +
                    ", deadline='" + deadline + '\'' +
                    '}';
        }
    }
}
