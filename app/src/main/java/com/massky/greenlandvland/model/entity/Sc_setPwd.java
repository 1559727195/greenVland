package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-09-26.
 */

public class Sc_setPwd {
    private SetPwdParams setPwdParams;
    private SetPwdResult setPwdResult;

    public Sc_setPwd(SetPwdParams setPwdParams, SetPwdResult setPwdResult) {
        this.setPwdParams = setPwdParams;
        this.setPwdResult = setPwdResult;
    }

    public SetPwdParams getSetPwdParams() {
        return setPwdParams;
    }

    public void setSetPwdParams(SetPwdParams setPwdParams) {
        this.setPwdParams = setPwdParams;
    }

    public SetPwdResult getSetPwdResult() {
        return setPwdResult;
    }

    public void setSetPwdResult(SetPwdResult setPwdResult) {
        this.setPwdResult = setPwdResult;
    }

    @Override
    public String toString() {
        return "Sc_setPwd{" +
                "setPwdParams=" + setPwdParams +
                ", setPwdResult=" + setPwdResult +
                '}';
    }

    public static class SetPwdParams{
        private String token;
        private String oldPwd;
        private String newPwd;
        private String projectCode;

        public SetPwdParams(String token, String oldPwd, String newPwd, String projectCode) {
            this.token = token;
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
            this.projectCode = projectCode;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOldPwd() {
            return oldPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        @Override
        public String toString() {
            return "SetPwdParams{" +
                    "token='" + token + '\'' +
                    ", oldPwd='" + oldPwd + '\'' +
                    ", newPwd='" + newPwd + '\'' +
                    ", projectCode=" + projectCode +
                    '}';
        }
    }
    public static class SetPwdResult{
        private int result;

        public SetPwdResult(int result) {
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
            return "SetPwdResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
