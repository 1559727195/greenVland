package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-18.
 */

public class Sc_deleteComplaint {
    private DeleteComplaintParams deleteComplaintParams;
    private DeleteComplaintResult deleteComplaintResult;

    public Sc_deleteComplaint(DeleteComplaintParams deleteComplaintParams, DeleteComplaintResult deleteComplaintResult) {
        this.deleteComplaintParams = deleteComplaintParams;
        this.deleteComplaintResult = deleteComplaintResult;
    }

    public DeleteComplaintParams getDeleteComplaintParams() {
        return deleteComplaintParams;
    }

    public void setDeleteComplaintParams(DeleteComplaintParams deleteComplaintParams) {
        this.deleteComplaintParams = deleteComplaintParams;
    }

    public DeleteComplaintResult getDeleteComplaintResult() {
        return deleteComplaintResult;
    }

    public void setDeleteComplaintResult(DeleteComplaintResult deleteComplaintResult) {
        this.deleteComplaintResult = deleteComplaintResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteComplaint{" +
                "deleteComplaintParams=" + deleteComplaintParams +
                ", deleteComplaintResult=" + deleteComplaintResult +
                '}';
    }

    public static class DeleteComplaintParams{
        private String token;
        private String projectCode;
        private int complaintId;

        public DeleteComplaintParams(String token, String projectCode, int complaintId) {
            this.token = token;
            this.projectCode = projectCode;
            this.complaintId = complaintId;
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

        public int getComplaintId() {
            return complaintId;
        }

        public void setComplaintId(int complaintId) {
            this.complaintId = complaintId;
        }

        @Override
        public String toString() {
            return "DeleteComplaintParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", complaintId=" + complaintId +
                    '}';
        }
    }

    public static class DeleteComplaintResult{
        private int result;

        public DeleteComplaintResult(int result) {
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
            return "DeleteComplaintResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
