package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29 0029.
 */

public class Sc_myLocation {
    private MyLocationParams myLocationParams;
    private MyLocationResoult myLocationResoult;

    public Sc_myLocation(MyLocationParams myLocationParams, MyLocationResoult myLocationResoult) {
        this.myLocationParams = myLocationParams;
        this.myLocationResoult = myLocationResoult;
    }

    public MyLocationParams getMyLocationParams() {
        return myLocationParams;
    }

    public void setMyLocationParams(MyLocationParams myLocationParams) {
        this.myLocationParams = myLocationParams;
    }

    public MyLocationResoult getMyLocationResoult() {
        return myLocationResoult;
    }

    public void setMyLocationResoult(MyLocationResoult myLocationResoult) {
        this.myLocationResoult = myLocationResoult;
    }

    @Override
    public String toString() {
        return "Sc_myLocation{" +
                "myLocationParams=" + myLocationParams +
                ", myLocationResoult=" + myLocationResoult +
                '}';
    }

    public static class MyLocationParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyLocationParams(String token, String projectCode, String roomNo) {
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
            return "MyLocationParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyLocationResoult{
        private int result;
        private String backgroundImg;
        private List<Location> locationList;

        public MyLocationResoult(int result, String backgroundImg, List<Location> locationList) {
            this.result = result;
            this.backgroundImg = backgroundImg;
            this.locationList = locationList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
        }

        public List<Location> getLocationList() {
            return locationList;
        }

        public void setLocationList(List<Location> locationList) {
            this.locationList = locationList;
        }

        @Override
        public String toString() {
            return "MyLocationResoult{" +
                    "result=" + result +
                    ", backgroundImg='" + backgroundImg + '\'' +
                    ", locationList=" + locationList +
                    '}';
        }
        public static class Location implements Serializable {
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
                        ", readX='" + readX + '\'' +
                        ", readY='" + readY + '\'' +
                        '}';
            }
        }
    }
}
