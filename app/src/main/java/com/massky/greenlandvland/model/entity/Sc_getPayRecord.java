package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2019-01-06.
 */

public class Sc_getPayRecord {
    private GetPayRecordParams getPayRecordParams;
    private GetPayRecordResult getPayRecordResult;

    public Sc_getPayRecord(GetPayRecordParams getPayRecordParams, GetPayRecordResult getPayRecordResult) {
        this.getPayRecordParams = getPayRecordParams;
        this.getPayRecordResult = getPayRecordResult;
    }

    public GetPayRecordParams getGetPayRecordParams() {
        return getPayRecordParams;
    }

    public void setGetPayRecordParams(GetPayRecordParams getPayRecordParams) {
        this.getPayRecordParams = getPayRecordParams;
    }

    public GetPayRecordResult getGetPayRecordResult() {
        return getPayRecordResult;
    }

    public void setGetPayRecordResult(GetPayRecordResult getPayRecordResult) {
        this.getPayRecordResult = getPayRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_getPayRecord{" +
                "getPayRecordParams=" + getPayRecordParams +
                ", getPayRecordResult=" + getPayRecordResult +
                '}';
    }

    public static class GetPayRecordParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public GetPayRecordParams(String token, String projectCode, String roomNo) {
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
            return "GetPayRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }

    public static class GetPayRecordResult{
        private int result;
        private List<PayRecordList> payRecordList;

        public GetPayRecordResult(int result, List<PayRecordList> payRecordList) {
            this.result = result;
            this.payRecordList = payRecordList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<PayRecordList> getPayRecordList() {
            return payRecordList;
        }

        public void setPayRecordList(List<PayRecordList> payRecordList) {
            this.payRecordList = payRecordList;
        }

        @Override
        public String toString() {
            return "GetPayRecordResult{" +
                    "result=" + result +
                    ", payRecordList=" + payRecordList +
                    '}';
        }

        public static class PayRecordList{
            private int id;
            private int type;
            private String money;
            private String dt;

            public PayRecordList(int id, int type, String money, String dt) {
                this.id = id;
                this.type = type;
                this.money = money;
                this.dt = dt;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getDt() {
                return dt;
            }

            public void setDt(String dt) {
                this.dt = dt;
            }

            @Override
            public String toString() {
                return "PayRecordList{" +
                        "id=" + id +
                        ", type=" + type +
                        ", money='" + money + '\'' +
                        ", dt='" + dt + '\'' +
                        '}';
            }
        }
    }
}
