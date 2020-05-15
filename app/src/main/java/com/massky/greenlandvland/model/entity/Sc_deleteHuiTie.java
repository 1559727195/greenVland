package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-06-20.
 */

public class Sc_deleteHuiTie {
    private DeleteHuiTieParams deleteHuiTieParams;
    private DeleteHuiTieResult deleteHuiTieResult;

    public Sc_deleteHuiTie(DeleteHuiTieParams deleteHuiTieParams, DeleteHuiTieResult deleteHuiTieResult) {
        this.deleteHuiTieParams = deleteHuiTieParams;
        this.deleteHuiTieResult = deleteHuiTieResult;
    }

    public DeleteHuiTieParams getDeleteHuiTieParams() {
        return deleteHuiTieParams;
    }

    public void setDeleteHuiTieParams(DeleteHuiTieParams deleteHuiTieParams) {
        this.deleteHuiTieParams = deleteHuiTieParams;
    }

    public DeleteHuiTieResult getDeleteHuiTieResult() {
        return deleteHuiTieResult;
    }

    public void setDeleteHuiTieResult(DeleteHuiTieResult deleteHuiTieResult) {
        this.deleteHuiTieResult = deleteHuiTieResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteHuiTie{" +
                "deleteHuiTieParams=" + deleteHuiTieParams +
                ", deleteHuiTieResult=" + deleteHuiTieResult +
                '}';
    }

    public static class DeleteHuiTieParams{
        private String token;
        private String projectCode;
        private int discussId;

        public DeleteHuiTieParams(String token, String projectCode, int discussId) {
            this.token = token;
            this.projectCode = projectCode;
            this.discussId = discussId;
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

        public int getDiscussId() {
            return discussId;
        }

        public void setDiscussId(int discussId) {
            this.discussId = discussId;
        }

        @Override
        public String toString() {
            return "DeleteHuiTieParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", discussId='" + discussId + '\'' +
                    '}';
        }
    }

    public static class DeleteHuiTieResult{
        private int result;

        public DeleteHuiTieResult(int result) {
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
            return "DeleteHuiTieResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
