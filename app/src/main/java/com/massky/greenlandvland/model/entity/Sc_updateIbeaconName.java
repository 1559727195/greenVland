package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-04.
 */

public class Sc_updateIbeaconName {
    private UpdateIbeaconNameParams updateIbeaconNameParams;
    private UpdateIbeaconNameResult updateIbeaconNameResult;

    public Sc_updateIbeaconName(UpdateIbeaconNameParams updateIbeaconNameParams, UpdateIbeaconNameResult updateIbeaconNameResult) {
        this.updateIbeaconNameParams = updateIbeaconNameParams;
        this.updateIbeaconNameResult = updateIbeaconNameResult;
    }

    public UpdateIbeaconNameParams getUpdateIbeaconNameParams() {
        return updateIbeaconNameParams;
    }

    public void setUpdateIbeaconNameParams(UpdateIbeaconNameParams updateIbeaconNameParams) {
        this.updateIbeaconNameParams = updateIbeaconNameParams;
    }

    public UpdateIbeaconNameResult getUpdateIbeaconNameResult() {
        return updateIbeaconNameResult;
    }

    public void setUpdateIbeaconNameResult(UpdateIbeaconNameResult updateIbeaconNameResult) {
        this.updateIbeaconNameResult = updateIbeaconNameResult;
    }

    @Override
    public String toString() {
        return "Sc_updateIbeaconName{" +
                "updateIbeaconNameParams=" + updateIbeaconNameParams +
                ", updateIbeaconNameResult=" + updateIbeaconNameResult +
                '}';
    }
    public static class UpdateIbeaconNameParams{
        private String token;
        private String projectCode;
        private List<Ibeacon> ibeaconList;
        private String roomNo;

        public UpdateIbeaconNameParams(String token, String projectCode, List<Ibeacon> ibeaconList, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.ibeaconList = ibeaconList;
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

        public List<Ibeacon> getIbeaconList() {
            return ibeaconList;
        }

        public void setIbeaconList(List<Ibeacon> ibeaconList) {
            this.ibeaconList = ibeaconList;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "UpdateIbeaconNameParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", ibeaconList=" + ibeaconList +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class Ibeacon implements Serializable {
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
                return "Ibeacon{" +
                        "blMac='" + blMac + '\'' +
                        ", deviceName='" + deviceName + '\'' +
                        '}';
            }
        }
    }
    public static class UpdateIbeaconNameResult{
        private int result;

        public UpdateIbeaconNameResult(int result) {
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
            return "UpdateIbeaconNameResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
