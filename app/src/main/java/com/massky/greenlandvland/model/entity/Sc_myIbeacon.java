package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-10-24.
 */

public class Sc_myIbeacon {
    private MyIbeaconParams myIbeaconParams;
    private MyIbeaconResult myIbeaconResult;

    public Sc_myIbeacon(MyIbeaconParams myIbeaconParams, MyIbeaconResult myIbeaconResult) {
        this.myIbeaconParams = myIbeaconParams;
        this.myIbeaconResult = myIbeaconResult;
    }

    public MyIbeaconParams getMyIbeaconParams() {
        return myIbeaconParams;
    }

    public void setMyIbeaconParams(MyIbeaconParams myIbeaconParams) {
        this.myIbeaconParams = myIbeaconParams;
    }

    public MyIbeaconResult getMyIbeaconResult() {
        return myIbeaconResult;
    }

    public void setMyIbeaconResult(MyIbeaconResult myIbeaconResult) {
        this.myIbeaconResult = myIbeaconResult;
    }

    @Override
    public String toString() {
        return "Sc_myIbeacon{" +
                "myIbeaconParams=" + myIbeaconParams +
                ", myIbeaconResult=" + myIbeaconResult +
                '}';
    }

    public static class MyIbeaconParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyIbeaconParams(String token, String projectCode, String roomNo) {
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
            return "MyIbeaconParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }

    public static class MyIbeaconResult{
        private int result;
        private List<Ibeacon> ibeaconList;

        public MyIbeaconResult(int result, List<Ibeacon> ibeaconList) {
            this.result = result;
            this.ibeaconList = ibeaconList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Ibeacon> getIbeaconList() {
            return ibeaconList;
        }

        public void setIbeaconList(List<Ibeacon> ibeaconList) {
            this.ibeaconList = ibeaconList;
        }

        @Override
        public String toString() {
            return "MyIbeaconResult{" +
                    "result=" + result +
                    ", ibeaconList=" + ibeaconList +
                    '}';
        }
        public static class Ibeacon{
            private String blMac;
            private String deviceName;

            public Ibeacon(String blMac, String deviceName) {
                this.blMac = blMac;
                this.deviceName = deviceName;
            }

            public String getBlMac() {
                return blMac;
            }

            public void setBlMac(String blMac) {
                this.blMac = blMac;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            @Override
            public String toString() {
                return "IbeaconList{" +
                        "blMac='" + blMac + '\'' +
                        ", deviceName='" + deviceName + '\'' +
                        '}';
            }
        }
    }
}
