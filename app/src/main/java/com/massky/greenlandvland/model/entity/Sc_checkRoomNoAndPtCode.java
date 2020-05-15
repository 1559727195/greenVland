package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2019-01-18.
 */

public class Sc_checkRoomNoAndPtCode {
    private CheckRoomNoAndPtCodeParams checkRoomNoAndPtCodeParams;
    private CheckRoomNoAndPtCodeResult checkRoomNoAndPtCodeResult;

    public Sc_checkRoomNoAndPtCode(CheckRoomNoAndPtCodeParams checkRoomNoAndPtCodeParams, CheckRoomNoAndPtCodeResult checkRoomNoAndPtCodeResult) {
        this.checkRoomNoAndPtCodeParams = checkRoomNoAndPtCodeParams;
        this.checkRoomNoAndPtCodeResult = checkRoomNoAndPtCodeResult;
    }

    public CheckRoomNoAndPtCodeParams getCheckRoomNoAndPtCodeParams() {
        return checkRoomNoAndPtCodeParams;
    }

    public void setCheckRoomNoAndPtCodeParams(CheckRoomNoAndPtCodeParams checkRoomNoAndPtCodeParams) {
        this.checkRoomNoAndPtCodeParams = checkRoomNoAndPtCodeParams;
    }

    public CheckRoomNoAndPtCodeResult getCheckRoomNoAndPtCodeResult() {
        return checkRoomNoAndPtCodeResult;
    }

    public void setCheckRoomNoAndPtCodeResult(CheckRoomNoAndPtCodeResult checkRoomNoAndPtCodeResult) {
        this.checkRoomNoAndPtCodeResult = checkRoomNoAndPtCodeResult;
    }

    @Override
    public String toString() {
        return "Sc_checkRoomNoAndPtCode{" +
                "checkRoomNoAndPtCodeParams=" + checkRoomNoAndPtCodeParams +
                ", checkRoomNoAndPtCodeResult=" + checkRoomNoAndPtCodeResult +
                '}';
    }

    public static class CheckRoomNoAndPtCodeParams{
        private String roomNo;
        private String projectCode;

        public CheckRoomNoAndPtCodeParams(String roomNo, String projectCode) {
            this.roomNo = roomNo;
            this.projectCode = projectCode;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        @Override
        public String toString() {
            return "CheckRoomNoAndPtCodeParams{" +
                    "roomNo='" + roomNo + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }

    public static class CheckRoomNoAndPtCodeResult{
        private int result;

        public CheckRoomNoAndPtCodeResult(int result) {
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
            return "CheckRoomNoAndPtCodeResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
