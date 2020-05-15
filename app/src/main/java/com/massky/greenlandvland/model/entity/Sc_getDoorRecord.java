package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-12-04.
 */

public class Sc_getDoorRecord {
    private GetDoorRecordParams getDoorRecordParams;
    private GetDoorRecordResult getDoorRecordResult;

    public Sc_getDoorRecord(GetDoorRecordParams getDoorRecordParams, GetDoorRecordResult getDoorRecordResult) {
        this.getDoorRecordParams = getDoorRecordParams;
        this.getDoorRecordResult = getDoorRecordResult;
    }

    public GetDoorRecordParams getGetDoorRecordParams() {
        return getDoorRecordParams;
    }

    public void setGetDoorRecordParams(GetDoorRecordParams getDoorRecordParams) {
        this.getDoorRecordParams = getDoorRecordParams;
    }

    public GetDoorRecordResult getGetDoorRecordResult() {
        return getDoorRecordResult;
    }

    public void setGetDoorRecordResult(GetDoorRecordResult getDoorRecordResult) {
        this.getDoorRecordResult = getDoorRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_getDoorRecord{" +
                "getDoorRecordParams=" + getDoorRecordParams +
                ", getDoorRecordResult=" + getDoorRecordResult +
                '}';
    }
    public static class GetDoorRecordParams{
        private String token;
        private String projectCode;
        private String page;
        private String roomNo;

        public GetDoorRecordParams(String token, String projectCode, String page, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.page = page;
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

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "GetDoorRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class GetDoorRecordResult{
        private int result;
        private List<Open> openList;

        public GetDoorRecordResult(int result, List<Open> openList) {
            this.result = result;
            this.openList = openList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Open> getOpenList() {
            return openList;
        }

        public void setOpenList(List<Open> openList) {
            this.openList = openList;
        }

        @Override
        public String toString() {
            return "GetDoorRecordResult{" +
                    "result=" + result +
                    ", openList=" + openList +
                    '}';
        }
        public static class Open{
            private String openTime;
            private String doorName;
            private String floor;
            private String userName;
            private String openResult;
            private String RowNumber;

            public Open(String openTime, String doorName, String floor, String userName, String openResult, String rowNumber) {
                this.openTime = openTime;
                this.doorName = doorName;
                this.floor = floor;
                this.userName = userName;
                this.openResult = openResult;
                RowNumber = rowNumber;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getDoorName() {
                return doorName;
            }

            public void setDoorName(String doorName) {
                this.doorName = doorName;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getOpenResult() {
                return openResult;
            }

            public void setOpenResult(String openResult) {
                this.openResult = openResult;
            }

            public String getRowNumber() {
                return RowNumber;
            }

            public void setRowNumber(String rowNumber) {
                RowNumber = rowNumber;
            }

            @Override
            public String toString() {
                return "Open{" +
                        "openTime='" + openTime + '\'' +
                        ", doorName='" + doorName + '\'' +
                        ", floor='" + floor + '\'' +
                        ", userName='" + userName + '\'' +
                        ", openResult='" + openResult + '\'' +
                        ", RowNumber='" + RowNumber + '\'' +
                        '}';
            }
        }
    }
}
