package com.massky.greenlandvland.model.entity;

public class Sc_sc_myAreaNumber {


    /**
     * token : 809jkasdfiopadfiuqwpeqwe890123
     * projectCode : 000001
     * roomNo : 12-12-12
     */

    public static class Sc_myAreaNumber {


        public Sc_myAreaNumber(
                String token,
                String projectCode,
                String roomNo
        ) {

            this.token = token;
            this.projectCode = projectCode;
            this.roomNo = roomNo;

        }


        private String token;
        private String projectCode;
        private String roomNo;


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
    }

    public static class Sc_myAreaNumberResult {

        public Sc_myAreaNumberResult(String result, String areaNumber) {
            this.result = result;
            this.areaNumber = areaNumber;
        }

        /**
         * result : 100
         * areaNumber : 2
         */

        private String result;
        private String areaNumber;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getAreaNumber() {
            return areaNumber;
        }

        public void setAreaNumber(String areaNumber) {
            this.areaNumber = areaNumber;
        }
    }
}

