package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-11-30.
 */

public class Sc_deleteInviteRecord {
    private DeleteInviteRecordParams deleteInviteRecordParams;
    private DeleteInviteRecordResult deleteInviteRecordResult;

    public Sc_deleteInviteRecord(DeleteInviteRecordParams deleteInviteRecordParams, DeleteInviteRecordResult deleteInviteRecordResult) {
        this.deleteInviteRecordParams = deleteInviteRecordParams;
        this.deleteInviteRecordResult = deleteInviteRecordResult;
    }

    public DeleteInviteRecordParams getDeleteInviteRecordParams() {
        return deleteInviteRecordParams;
    }

    public void setDeleteInviteRecordParams(DeleteInviteRecordParams deleteInviteRecordParams) {
        this.deleteInviteRecordParams = deleteInviteRecordParams;
    }

    public DeleteInviteRecordResult getDeleteInviteRecordResult() {
        return deleteInviteRecordResult;
    }

    public void setDeleteInviteRecordResult(DeleteInviteRecordResult deleteInviteRecordResult) {
        this.deleteInviteRecordResult = deleteInviteRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteInviteRecord{" +
                "deleteInviteRecordParams=" + deleteInviteRecordParams +
                ", deleteInviteRecordResult=" + deleteInviteRecordResult +
                '}';
    }
    public static class DeleteInviteRecordParams{
        private String token;
        private String projectCode;
        private int recordId;

        public DeleteInviteRecordParams(String token, String projectCode, int recordId) {
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
            return "DeleteInviteRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", recordId=" + recordId +
                    '}';
        }
    }
    public static class DeleteInviteRecordResult{
        private int result;
        private int count;

        public DeleteInviteRecordResult(int result, int count) {
            this.result = result;
            this.count = count;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "DeleteInviteRecordResult{" +
                    "result=" + result +
                    ", count=" + count +
                    '}';
        }
    }
}
