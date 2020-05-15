package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-11-01.
 */

public class Sc_myLocationReload {
    private MyLocationReloadParams myLocationReloadParams;
    private MyLocationReloadResult myLocationReloadResult;

    public Sc_myLocationReload(MyLocationReloadParams myLocationReloadParams, MyLocationReloadResult myLocationReloadResult) {
        this.myLocationReloadParams = myLocationReloadParams;
        this.myLocationReloadResult = myLocationReloadResult;
    }

    public MyLocationReloadParams getMyLocationReloadParams() {
        return myLocationReloadParams;
    }

    public void setMyLocationReloadParams(MyLocationReloadParams myLocationReloadParams) {
        this.myLocationReloadParams = myLocationReloadParams;
    }

    public MyLocationReloadResult getMyLocationReloadResult() {
        return myLocationReloadResult;
    }

    public void setMyLocationReloadResult(MyLocationReloadResult myLocationReloadResult) {
        this.myLocationReloadResult = myLocationReloadResult;
    }

    @Override
    public String toString() {
        return "Sc_myLocationReload{" +
                "myLocationReloadParams=" + myLocationReloadParams +
                ", myLocationReloadResult=" + myLocationReloadResult +
                '}';
    }
    public static class MyLocationReloadParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyLocationReloadParams(String token, String projectCode, String roomNo) {
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
            return "MyLocationReloadParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyLocationReloadResult{
        private int result;
        private List<Location> locationList;

        public MyLocationReloadResult(int result, List<Location> locationList) {
            this.result = result;
            this.locationList = locationList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Location> getLocationList() {
            return locationList;
        }

        public void setLocationList(List<Location> locationList) {
            this.locationList = locationList;
        }

        @Override
        public String toString() {
            return "MyLocationReloadResult{" +
                    "result=" + result +
                    ", locationList=" + locationList +
                    '}';
        }
        public static class Location{
            private String ibeaconMac;
            private String ibeaconName;
            private int readX;
            private int readY;

            public Location(String ibeaconMac, String ibeaconName, int readX, int readY) {
                this.ibeaconMac = ibeaconMac;
                this.ibeaconName = ibeaconName;
                this.readX = readX;
                this.readY = readY;
            }

            public String getIbeaconMac() {
                return ibeaconMac;
            }

            public void setIbeaconMac(String ibeaconMac) {
                this.ibeaconMac = ibeaconMac;
            }

            public String getIbeaconName() {
                return ibeaconName;
            }

            public void setIbeaconName(String ibeaconName) {
                this.ibeaconName = ibeaconName;
            }

            public int getReadX() {
                return readX;
            }

            public void setReadX(int readX) {
                this.readX = readX;
            }

            public int getReadY() {
                return readY;
            }

            public void setReadY(int readY) {
                this.readY = readY;
            }

            @Override
            public String toString() {
                return "Location{" +
                        "ibeaconMac='" + ibeaconMac + '\'' +
                        ", ibeaconName='" + ibeaconName + '\'' +
                        ", readX=" + readX +
                        ", readY=" + readY +
                        '}';
            }
        }
    }
}
