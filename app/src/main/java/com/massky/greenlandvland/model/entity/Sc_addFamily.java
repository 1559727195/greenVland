package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-10-23.
 */

public class Sc_addFamily {
    private AddFamilyParams addFamilyParams;
    private AddFamilyResult addFamilyResult;

    public Sc_addFamily(AddFamilyParams addFamilyParams, AddFamilyResult addFamilyResult) {
        this.addFamilyParams = addFamilyParams;
        this.addFamilyResult = addFamilyResult;
    }

    public AddFamilyParams getAddFamilyParams() {
        return addFamilyParams;
    }

    public void setAddFamilyParams(AddFamilyParams addFamilyParams) {
        this.addFamilyParams = addFamilyParams;
    }

    public AddFamilyResult getAddFamilyResult() {
        return addFamilyResult;
    }

    public void setAddFamilyResult(AddFamilyResult addFamilyResult) {
        this.addFamilyResult = addFamilyResult;
    }

    @Override
    public String toString() {
        return "Sc_addFamily{" +
                "addFamilyParams=" + addFamilyParams +
                ", addFamilyResult=" + addFamilyResult +
                '}';
    }

    public static class AddFamilyParams{
        private String token;
        private String projectCode;
        private String mobilePhone;
        private String familyName;

        public AddFamilyParams(String token, String projectCode, String mobilePhone, String familyName) {
            this.token = token;
            this.projectCode = projectCode;
            this.mobilePhone = mobilePhone;
            this.familyName = familyName;
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

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        @Override
        public String toString() {
            return "AddFamilyParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", mobilePhone='" + mobilePhone + '\'' +
                    ", familyName='" + familyName + '\'' +
                    '}';
        }
    }
    public static class AddFamilyResult{
        private int result;

        public AddFamilyResult(int result) {
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
            return "AddFamilyResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
