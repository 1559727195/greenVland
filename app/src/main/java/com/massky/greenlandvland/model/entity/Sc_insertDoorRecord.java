package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2019-01-15.
 */

public class Sc_insertDoorRecord {
    private InsertDoorRecordParams insertDoorRecordParams;
    private InsertDoorRecordResult insertDoorRecordResult;

    public Sc_insertDoorRecord(InsertDoorRecordParams insertDoorRecordParams, InsertDoorRecordResult insertDoorRecordResult) {
        this.insertDoorRecordParams = insertDoorRecordParams;
        this.insertDoorRecordResult = insertDoorRecordResult;
    }

    public InsertDoorRecordParams getInsertDoorRecordParams() {
        return insertDoorRecordParams;
    }

    public void setInsertDoorRecordParams(InsertDoorRecordParams insertDoorRecordParams) {
        this.insertDoorRecordParams = insertDoorRecordParams;
    }

    public InsertDoorRecordResult getInsertDoorRecordResult() {
        return insertDoorRecordResult;
    }

    public void setInsertDoorRecordResult(InsertDoorRecordResult insertDoorRecordResult) {
        this.insertDoorRecordResult = insertDoorRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_insertDoorRecord{" +
                "insertDoorRecordParams=" + insertDoorRecordParams +
                ", insertDoorRecordResult=" + insertDoorRecordResult +
                '}';
    }

    public static class InsertDoorRecordParams{
        private String token;
        private String projectCode;
        private List<Open> openList;
        private String roomNo;

        public InsertDoorRecordParams(String token, String projectCode, List<Open> openList, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.openList = openList;
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

        public List<Open> getOpenList() {
            return openList;
        }

        public void setOpenList(List<Open> openList) {
            this.openList = openList;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "InsertDoorRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", openList=" + openList +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class Open{
            private int doorId;
            private String floor;
            private String openTime;
            private String openResult;

            public Open(int doorId, String floor, String openTime, String openResult) {
                this.doorId = doorId;
                this.floor = floor;
                this.openTime = openTime;
                this.openResult = openResult;
            }

            public int getDoorId() {
                return doorId;
            }

            public void setDoorId(int doorId) {
                this.doorId = doorId;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getOpenResult() {
                return openResult;
            }

            public void setOpenResult(String openResult) {
                this.openResult = openResult;
            }

            @Override
            public String toString() {
                return "Open{" +
                        "doorId=" + doorId +
                        ", floor='" + floor + '\'' +
                        ", openTime='" + openTime + '\'' +
                        ", openResult='" + openResult + '\'' +
                        '}';
            }
        }
    }

    public static class InsertDoorRecordResult{
        private int result;

        public InsertDoorRecordResult(int result) {
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
            return "InsertDoorRecordResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
