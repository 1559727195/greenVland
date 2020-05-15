package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2019-01-15.
 */

public class Sc_cancelInviteRecord {
    private CancelInviteRecordParams cancelInviteRecordParams;
    private CancelInviteRecordResult cancelInviteRecordResult;

    public Sc_cancelInviteRecord(CancelInviteRecordParams cancelInviteRecordParams, CancelInviteRecordResult cancelInviteRecordResult) {
        this.cancelInviteRecordParams = cancelInviteRecordParams;
        this.cancelInviteRecordResult = cancelInviteRecordResult;
    }

    public CancelInviteRecordParams getCancelInviteRecordParams() {
        return cancelInviteRecordParams;
    }

    public void setCancelInviteRecordParams(CancelInviteRecordParams cancelInviteRecordParams) {
        this.cancelInviteRecordParams = cancelInviteRecordParams;
    }

    public CancelInviteRecordResult getCancelInviteRecordResult() {
        return cancelInviteRecordResult;
    }

    public void setCancelInviteRecordResult(CancelInviteRecordResult cancelInviteRecordResult) {
        this.cancelInviteRecordResult = cancelInviteRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_cancelInviteRecord{" +
                "cancelInviteRecordParams=" + cancelInviteRecordParams +
                ", cancelInviteRecordResult=" + cancelInviteRecordResult +
                '}';
    }

    public static class CancelInviteRecordParams{
        private String token;
        private String projectCode;
        private int recordId;

        public CancelInviteRecordParams(String token, String projectCode, int recordId) {
            this.token = token;
            this.projectCode = projectCode;
            this.recordId = recordId;
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

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        @Override
        public String toString() {
            return "CancelInviteRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", recordId=" + recordId +
                    '}';
        }
    }

    public static class CancelInviteRecordResult{
        private int result;

        public CancelInviteRecordResult(int result) {
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
            return "CancelInviteRecordResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
