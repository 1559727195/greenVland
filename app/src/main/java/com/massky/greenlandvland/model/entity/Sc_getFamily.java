package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-10-12.
 */

public class Sc_getFamily {
    private GetFamilyParams getFamilyParams;
    private GetFamilyResult getFamilyResult;

    public Sc_getFamily(GetFamilyParams getFamilyParams, GetFamilyResult getFamilyResult) {
        this.getFamilyParams = getFamilyParams;
        this.getFamilyResult = getFamilyResult;
    }

    public GetFamilyParams getGetFamilyParams() {
        return getFamilyParams;
    }

    public void setGetFamilyParams(GetFamilyParams getFamilyParams) {
        this.getFamilyParams = getFamilyParams;
    }

    public GetFamilyResult getGetFamilyResult() {
        return getFamilyResult;
    }

    public void setGetFamilyResult(GetFamilyResult getFamilyResult) {
        this.getFamilyResult = getFamilyResult;
    }

    @Override
    public String toString() {
        return "Sc_getFamily{" +
                "getFamilyParams=" + getFamilyParams +
                ", getFamilyResult=" + getFamilyResult +
                '}';
    }

    public static class GetFamilyParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public GetFamilyParams(String token, String projectCode, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.roomNo = roomNo;
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

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "GetFamilyParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }

    public static class GetFamilyResult{
        private int result;
        private List<Family> familyList;

        public GetFamilyResult(int result, List<Family> familyList) {
            this.result = result;
            this.familyList = familyList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Family> getFamilyList() {
            return familyList;
        }

        public void setFamilyList(List<Family> familyList) {
            this.familyList = familyList;
        }

        @Override
        public String toString() {
            return "GetFamilyResult{" +
                    "result='" + result + '\'' +
                    ", familyList=" + familyList +
                    '}';
        }

        public static class Family implements Serializable {
            private String name;
            private String mobilePhone;
            private int type;

            public Family(String name, String mobilePhone, int type) {
                this.name = name;
                this.mobilePhone = mobilePhone;
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Family{" +
                        "name='" + name + '\'' +
                        ", mobilePhone='" + mobilePhone + '\'' +
                        ", type=" + type +
                        '}';
            }
        }
    }
}
