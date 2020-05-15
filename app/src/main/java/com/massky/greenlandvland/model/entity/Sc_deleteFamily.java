package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-10-24.
 */

public class Sc_deleteFamily {
    private DeleteFamilyParams deleteFamilyParams;
    private DeleteFamilyResult deleteFamilyResult;

    public Sc_deleteFamily(DeleteFamilyParams deleteFamilyParams, DeleteFamilyResult deleteFamilyResult) {
        this.deleteFamilyParams = deleteFamilyParams;
        this.deleteFamilyResult = deleteFamilyResult;
    }

    public DeleteFamilyParams getDeleteFamilyParams() {
        return deleteFamilyParams;
    }

    public void setDeleteFamilyParams(DeleteFamilyParams deleteFamilyParams) {
        this.deleteFamilyParams = deleteFamilyParams;
    }

    public DeleteFamilyResult getDeleteFamilyResult() {
        return deleteFamilyResult;
    }

    public void setDeleteFamilyResult(DeleteFamilyResult deleteFamilyResult) {
        this.deleteFamilyResult = deleteFamilyResult;
    }

    public static class DeleteFamilyParams{
        private String token;
        private String projectCode;
        private String mobilePhone;

        public DeleteFamilyParams(String token, String projectCode, String mobilePhone) {
            this.token = token;
            this.projectCode = projectCode;
            this.mobilePhone = mobilePhone;
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

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        @Override
        public String toString() {
            return "DeleteFamilyParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", mobilePhone='" + mobilePhone + '\'' +
                    '}';
        }
    }
    public static class DeleteFamilyResult{
        private int result;

        public DeleteFamilyResult(int result) {
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
            return "DeleteFamilyResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
