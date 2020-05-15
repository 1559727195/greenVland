package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-17.
 */

public class Sc_deleteCamera {
    private DeleteCameraParams deleteCameraParams;
    private DeleteCameraResult deleteCameraResult;

    public Sc_deleteCamera(DeleteCameraParams deleteCameraParams, DeleteCameraResult deleteCameraResult) {
        this.deleteCameraParams = deleteCameraParams;
        this.deleteCameraResult = deleteCameraResult;
    }

    public DeleteCameraParams getDeleteCameraParams() {
        return deleteCameraParams;
    }

    public void setDeleteCameraParams(DeleteCameraParams deleteCameraParams) {
        this.deleteCameraParams = deleteCameraParams;
    }

    public DeleteCameraResult getDeleteCameraResult() {
        return deleteCameraResult;
    }

    public void setDeleteCameraResult(DeleteCameraResult deleteCameraResult) {
        this.deleteCameraResult = deleteCameraResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteCamera{" +
                "deleteCameraParams=" + deleteCameraParams +
                ", deleteCameraResult=" + deleteCameraResult +
                '}';
    }

    public static class DeleteCameraParams{
        private String token;
        private String projectCode;
        private int nDevID;

        public DeleteCameraParams(String token, String projectCode, int nDevID) {
            this.token = token;
            this.projectCode = projectCode;
            this.nDevID = nDevID;
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

        public int getnDevID() {
            return nDevID;
        }

        public void setnDevID(int nDevID) {
            this.nDevID = nDevID;
        }

        @Override
        public String toString() {
            return "DeleteCameraParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", nDevID='" + nDevID + '\'' +
                    '}';
        }
    }

    public static class DeleteCameraResult{
        private int result;

        public DeleteCameraResult(int result) {
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
            return "DeleteCameraResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
