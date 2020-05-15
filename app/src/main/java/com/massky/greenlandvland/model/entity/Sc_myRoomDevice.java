package com.massky.greenlandvland.model.entity;



/**
 * Created by masskywcy on 2017-11-06.
 */

public class Sc_myRoomDevice {
    private MyRoomDeviceParams myRoomDeviceParams;
    private MyRoomDeviceResult myRoomDeviceResult;

    public Sc_myRoomDevice(MyRoomDeviceParams myRoomDeviceParams, MyRoomDeviceResult myRoomDeviceResult) {
        this.myRoomDeviceParams = myRoomDeviceParams;
        this.myRoomDeviceResult = myRoomDeviceResult;
    }

    public MyRoomDeviceParams getMyRoomDeviceParams() {
        return myRoomDeviceParams;
    }

    public void setMyRoomDeviceParams(MyRoomDeviceParams myRoomDeviceParams) {
        this.myRoomDeviceParams = myRoomDeviceParams;
    }

    public MyRoomDeviceResult getMyRoomDeviceResult() {
        return myRoomDeviceResult;
    }

    public void setMyRoomDeviceResult(MyRoomDeviceResult myRoomDeviceResult) {
        this.myRoomDeviceResult = myRoomDeviceResult;
    }

    @Override
    public String toString() {
        return "Sc_myRoomDevice{" +
                "myRoomDeviceParams=" + myRoomDeviceParams +
                ", myRoomDeviceResult=" + myRoomDeviceResult +
                '}';
    }
    public static class MyRoomDeviceParams{
        private String token;
        private String projectCode;
        private String deviceId;

        public MyRoomDeviceParams(String token, String projectCode, String deviceId) {
            this.token = token;
            this.projectCode = projectCode;
            this.deviceId = deviceId;
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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        @Override
        public String toString() {
            return "MyRoomDeviceParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    '}';
        }
    }
    public static class MyRoomDeviceResult{


        /**
         * result : 100
         * deviceInfo : {"status":0,"speed":"","name":"4039KEY8","number":24386,"dimmer":"","name1":"","type":1,"name2":"","temperature":"","mode":""}
         */

        private String result;
        private DeviceInfoBean deviceInfo;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public DeviceInfoBean getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(DeviceInfoBean deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        public static class DeviceInfoBean {
            /**
             * status : 0
             * speed :
             * name : 4039KEY8
             * number : 24386
             * dimmer :
             * name1 :
             * type : 1
             * name2 :
             * temperature :
             * mode :
             */

            private int status;
            private String speed;
            private String name;
            private int number;
            private String dimmer;
            private String name1;
            private int type;
            private String name2;
            private String temperature;
            private String mode;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getSpeed() {
                return speed;
            }

            public void setSpeed(String speed) {
                this.speed = speed;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getDimmer() {
                return dimmer;
            }

            public void setDimmer(String dimmer) {
                this.dimmer = dimmer;
            }

            public String getName1() {
                return name1;
            }

            public void setName1(String name1) {
                this.name1 = name1;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getName2() {
                return name2;
            }

            public void setName2(String name2) {
                this.name2 = name2;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }
        }
    }
}
