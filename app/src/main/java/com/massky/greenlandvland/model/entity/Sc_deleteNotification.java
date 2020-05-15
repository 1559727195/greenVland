package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-25.
 */

public class Sc_deleteNotification {
    private DeleteNotificationParams deleteNotificationParams;
    private DeleteNotificationResult deleteNotificationResult;

    public Sc_deleteNotification(DeleteNotificationParams deleteNotificationParams, DeleteNotificationResult deleteNotificationResult) {
        this.deleteNotificationParams = deleteNotificationParams;
        this.deleteNotificationResult = deleteNotificationResult;
    }

    public DeleteNotificationParams getDeleteNotificationParams() {
        return deleteNotificationParams;
    }

    public void setDeleteNotificationParams(DeleteNotificationParams deleteNotificationParams) {
        this.deleteNotificationParams = deleteNotificationParams;
    }

    public DeleteNotificationResult getDeleteNotificationResult() {
        return deleteNotificationResult;
    }

    public void setDeleteNotificationResult(DeleteNotificationResult deleteNotificationResult) {
        this.deleteNotificationResult = deleteNotificationResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteNotification{" +
                "deleteNotificationParams=" + deleteNotificationParams +
                ", deleteNotificationResult=" + deleteNotificationResult +
                '}';
    }

    public static class DeleteNotificationParams{
        private String token;
        private String projectCode;
        private String id;

        public DeleteNotificationParams(String token, String projectCode, String id) {
            this.token = token;
            this.projectCode = projectCode;
            this.id = id;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DeleteNotificationParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
    public static class DeleteNotificationResult{
        private int result;

        public DeleteNotificationResult(int result) {
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
            return "DeleteNotificationResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
