package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-09-26.
 */

public class Sc_updateUserName {
    private UpdateUserNameParams updateUserNameParams;
    private UpdateUserNameResult updateUserNameResult;

    public Sc_updateUserName(UpdateUserNameParams updateUserNameParams, UpdateUserNameResult updateUserNameResult) {
        this.updateUserNameParams = updateUserNameParams;
        this.updateUserNameResult = updateUserNameResult;
    }

    public UpdateUserNameParams getUpdateUserNameParams() {
        return updateUserNameParams;
    }

    public void setUpdateUserNameParams(UpdateUserNameParams updateUserNameParams) {
        this.updateUserNameParams = updateUserNameParams;
    }

    public UpdateUserNameResult getUpdateUserNameResult() {
        return updateUserNameResult;
    }

    public void setUpdateUserNameResult(UpdateUserNameResult updateUserNameResult) {
        this.updateUserNameResult = updateUserNameResult;
    }

    @Override
    public String toString() {
        return "Sc_updateUserName{" +
                "updateUserNameParams=" + updateUserNameParams +
                ", updateUserNameResult=" + updateUserNameResult +
                '}';
    }

    public static class UpdateUserNameParams{
        private String token;
        private String projectCode;
        private String userName;

        public UpdateUserNameParams(String token, String projectCode, String userName) {
            this.token = token;
            this.projectCode = projectCode;
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "UpdateUserNameParams{" +
                    "token='" + token + '\'' +
                    ", projectCode=" + projectCode +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    public static class UpdateUserNameResult{
        private int result;

        public UpdateUserNameResult(int result) {
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
            return "UpdateUserNameResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
