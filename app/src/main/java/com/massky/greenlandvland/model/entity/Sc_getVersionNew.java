package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-12-28.
 */

public class Sc_getVersionNew {
    private GetVersionNewParams getVersionNewParams;
    private GetVersionNewResult getVersionNewResult;

    public Sc_getVersionNew(GetVersionNewParams getVersionNewParams, GetVersionNewResult getVersionNewResult) {
        this.getVersionNewParams = getVersionNewParams;
        this.getVersionNewResult = getVersionNewResult;
    }

    public GetVersionNewParams getGetVersionNewParams() {
        return getVersionNewParams;
    }

    public void setGetVersionNewParams(GetVersionNewParams getVersionNewParams) {
        this.getVersionNewParams = getVersionNewParams;
    }

    public GetVersionNewResult getGetVersionNewResult() {
        return getVersionNewResult;
    }

    public void setGetVersionNewResult(GetVersionNewResult getVersionNewResult) {
        this.getVersionNewResult = getVersionNewResult;
    }

    @Override
    public String toString() {
        return "Sc_getVersionNew{" +
                "getVersionNewParams=" + getVersionNewParams +
                ", getVersionNewResult=" + getVersionNewResult +
                '}';
    }

    public static class GetVersionNewParams{
        private String token;
        private int appCode;

        public GetVersionNewParams(String token, int appCode) {
            this.token = token;
            this.appCode = appCode;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "GetVersionNewParams{" +
                    "token='" + token + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    public static class GetVersionNewResult{
        private int result;
        private int versionCode;
        private String version;

        public GetVersionNewResult(int result, int versionCode, String version) {
            this.result = result;
            this.versionCode = versionCode;
            this.version = version;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "GetVersionNewResult{" +
                    "result=" + result +
                    ", versionCode=" + versionCode +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}
