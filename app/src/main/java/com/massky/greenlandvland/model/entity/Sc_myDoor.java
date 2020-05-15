package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class Sc_myDoor {
    private MyDoorParams myDoorParams;
    private MyDoorResult myDoorResult;

    public Sc_myDoor(MyDoorParams myDoorParams, MyDoorResult myDoorResult) {
        this.myDoorParams = myDoorParams;
        this.myDoorResult = myDoorResult;
    }

    public MyDoorParams getMyDoorParams() {
        return myDoorParams;
    }

    public void setMyDoorParams(MyDoorParams myDoorParams) {
        this.myDoorParams = myDoorParams;
    }

    public MyDoorResult getMyDoorResult() {
        return myDoorResult;
    }

    public void setMyDoorResult(MyDoorResult myDoorResult) {
        this.myDoorResult = myDoorResult;
    }

    @Override
    public String toString() {
        return "Sc_myDoor{" +
                "myDoorParams=" + myDoorParams +
                ", myDoorResult=" + myDoorResult +
                '}';
    }
    public static class MyDoorParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyDoorParams(String token, String projectCode, String roomNo) {
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
            return "MyDoorParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyDoorResult{
        private int result;
        private List<Door> doorList;

        public MyDoorResult(int result, List<Door> doorList) {
            this.result = result;
            this.doorList = doorList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Door> getDoorList() {
            return doorList;
        }

        public void setDoorList(List<Door> doorList) {
            this.doorList = doorList;
        }

        @Override
        public String toString() {
            return "MyDoorResult{" +
                    "result=" + result +
                    ", doorList=" + doorList +
                    '}';
        }
        public static class Door{
            private int id;
            private String doorName;
            private int doorStatus;
            private String floor;
            private String projectCode;
            private String mac;
            private int type;

            public Door(int id, String doorName, int doorStatus, String floor, String projectCode, String mac, int type) {
                this.id = id;
                this.doorName = doorName;
                this.doorStatus = doorStatus;
                this.floor = floor;
                this.projectCode = projectCode;
                this.mac = mac;
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDoorName() {
                return doorName;
            }

            public void setDoorName(String doorName) {
                this.doorName = doorName;
            }

            public int getDoorStatus() {
                return doorStatus;
            }

            public void setDoorStatus(int doorStatus) {
                this.doorStatus = doorStatus;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public String getProjectCode() {
                return projectCode;
            }

            public void setProjectCode(String projectCode) {
                this.projectCode = projectCode;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Door{" +
                        "id=" + id +
                        ", doorName='" + doorName + '\'' +
                        ", doorStatus=" + doorStatus +
                        ", floor='" + floor + '\'' +
                        ", projectCode='" + projectCode + '\'' +
                        ", mac='" + mac + '\'' +
                        ", type=" + type +
                        '}';
            }
        }
    }
}
