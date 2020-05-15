package com.massky.greenlandvland.model.entity;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-06.
 */

public class Sc_myRoom {
    private MyRoomParams myRoomParams;
    private MyRoomResult myRoomResult;

    public Sc_myRoom(MyRoomParams myRoomParams, MyRoomResult myRoomResult) {
        this.myRoomParams = myRoomParams;
        this.myRoomResult = myRoomResult;
    }

    public MyRoomParams getMyRoomParams() {
        return myRoomParams;
    }

    public void setMyRoomParams(MyRoomParams myRoomParams) {
        this.myRoomParams = myRoomParams;
    }

    public MyRoomResult getMyRoomResult() {
        return myRoomResult;
    }

    public void setMyRoomResult(MyRoomResult myRoomResult) {
        this.myRoomResult = myRoomResult;
    }

    @Override
    public String toString() {
        return "Sc_myRoom{" +
                "myRoomParams=" + myRoomParams +
                ", myRoomResult=" + myRoomResult +
                '}';
    }

    public static class MyRoomParams{
        private String token;
        private String projectCode;
        private String roomNo;
        private String  areaNumber;

        public MyRoomParams(String token, String projectCode, String roomNo, String areaNumber) {
            this.token = token;
            this.projectCode = projectCode;
            this.roomNo = roomNo;
            this.areaNumber = areaNumber;
        }

        public void setAreaNumber(String areaNumber) {
            this.areaNumber = areaNumber;
        }

        public String getAreaNumber() {
            return areaNumber;
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



        @Override
        public String toString() {
            return "MyRoomParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", boxNumber='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyRoomResult implements Serializable {


        /**
         * result : 100
         * roomList : [{"roomName":"客厅","deviceList":[{"deviceId":"12","deviceName":"客厅空调"}]}]
         */

        private String result;
        private List<RoomListBean> roomList;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<RoomListBean> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<RoomListBean> roomList) {
            this.roomList = roomList;
        }

        public static class RoomListBean  implements Serializable {
            /**
             * roomName : 客厅
             * deviceList : [{"deviceId":"12","deviceName":"客厅空调"}]
             */

            private String roomName;
            private List<DeviceListBean> deviceList;




            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            public List<DeviceListBean> getDeviceList() {
                return deviceList;
            }

            public void setDeviceList(List<DeviceListBean> deviceList) {
                this.deviceList = deviceList;
            }

            public static class DeviceListBean {
                /**
                 * deviceId : 12
                 * deviceName : 客厅空调
                 */

                private String deviceId;
                private String deviceName;

                public String getDeviceId() {
                    return deviceId;
                }

                public void setDeviceId(String deviceId) {
                    this.deviceId = deviceId;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }
            }
        }



    }
}
