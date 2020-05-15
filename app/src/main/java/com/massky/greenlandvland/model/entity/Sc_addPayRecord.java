package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2019-01-06.
 */

public class Sc_addPayRecord {
    private AddPayRecordParams addPayRecordParams;
    private AddPayRecordResult addPayRecordResult;

    public Sc_addPayRecord(AddPayRecordParams addPayRecordParams, AddPayRecordResult addPayRecordResult) {
        this.addPayRecordParams = addPayRecordParams;
        this.addPayRecordResult = addPayRecordResult;
    }

    public AddPayRecordParams getAddPayRecordParams() {
        return addPayRecordParams;
    }

    public void setAddPayRecordParams(AddPayRecordParams addPayRecordParams) {
        this.addPayRecordParams = addPayRecordParams;
    }

    public AddPayRecordResult getAddPayRecordResult() {
        return addPayRecordResult;
    }

    public void setAddPayRecordResult(AddPayRecordResult addPayRecordResult) {
        this.addPayRecordResult = addPayRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_addPayRecord{" +
                "addPayRecordParams=" + addPayRecordParams +
                ", addPayRecordResult=" + addPayRecordResult +
                '}';
    }

    public static class AddPayRecordParams{
        private String token;
        private String projectCode;
        private String roomNo;
        private int type;
        private String money;

        public AddPayRecordParams(String token, String projectCode, String roomNo, int type, String money) {
            this.token = token;
            this.projectCode = projectCode;
            this.roomNo = roomNo;
            this.type = type;
            this.money = money;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Override
        public String toString() {
            return "AddPayRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    ", type=" + type +
                    ", money='" + money + '\'' +
                    '}';
        }
    }

    public static class AddPayRecordResult{
        private int result;

        public AddPayRecordResult(int result) {
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
            return "AddPayRecordResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
