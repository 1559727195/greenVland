package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-10-25.
 */

public class Sc_uploadCamera {
    private UploadCameraParams uploadCameraParams;
    private UploadCameraResult uploadCameraResult;

    public Sc_uploadCamera(UploadCameraParams uploadCameraParams, UploadCameraResult uploadCameraResult) {
        this.uploadCameraParams = uploadCameraParams;
        this.uploadCameraResult = uploadCameraResult;
    }

    public UploadCameraParams getUploadCameraParams() {
        return uploadCameraParams;
    }

    public void setUploadCameraParams(UploadCameraParams uploadCameraParams) {
        this.uploadCameraParams = uploadCameraParams;
    }

    public UploadCameraResult getUploadCameraResult() {
        return uploadCameraResult;
    }

    public void setUploadCameraResult(UploadCameraResult uploadCameraResult) {
        this.uploadCameraResult = uploadCameraResult;
    }

    @Override
    public String toString() {
        return "Sc_uploadCamera{" +
                "uploadCameraParams=" + uploadCameraParams +
                ", uploadCameraResult=" + uploadCameraResult +
                '}';
    }
    public static class UploadCameraParams{
        private String token;
        private String projecrtCode;
        private List<Camera> cameraList;
        private String roomNo;

        public UploadCameraParams(String token, String projecrtCode, List<Camera> cameraList, String roomNo) {
            this.token = token;
            this.projecrtCode = projecrtCode;
            this.cameraList = cameraList;
            this.roomNo = roomNo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getProjecrtCode() {
            return projecrtCode;
        }

        public void setProjecrtCode(String projecrtCode) {
            this.projecrtCode = projecrtCode;
        }

        public List<Camera> getCameraList() {
            return cameraList;
        }

        public void setCameraList(List<Camera> cameraList) {
            this.cameraList = cameraList;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "UploadCameraParams{" +
                    "token='" + token + '\'' +
                    ", projecrtCode='" + projecrtCode + '\'' +
                    ", cameraList=" + cameraList +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class Camera{
            private String strIP;
            private String strMac;
            private String strUsername;
            private String strPassword;
            private int nDevID;
            private String strName;
            private String strDomain;
            private String cameraImg;

            public Camera(String strIP, String strMac, String strUsername, String strPassword, int nDevID, String strName, String strDomain, String cameraImg) {
                this.strIP = strIP;
                this.strMac = strMac;
                this.strUsername = strUsername;
                this.strPassword = strPassword;
                this.nDevID = nDevID;
                this.strName = strName;
                this.strDomain = strDomain;
                this.cameraImg = cameraImg;
            }

            public String getStrIP() {
                return strIP;
            }

            public void setStrIP(String strIP) {
                this.strIP = strIP;
            }

            public String getStrMac() {
                return strMac;
            }

            public void setStrMac(String strMac) {
                this.strMac = strMac;
            }

            public String getStrUsername() {
                return strUsername;
            }

            public void setStrUsername(String strUsername) {
                this.strUsername = strUsername;
            }

            public String getStrPassword() {
                return strPassword;
            }

            public void setStrPassword(String strPassword) {
                this.strPassword = strPassword;
            }

            public int getnDevID() {
                return nDevID;
            }

            public void setnDevID(int nDevID) {
                this.nDevID = nDevID;
            }

            public String getStrName() {
                return strName;
            }

            public void setStrName(String strName) {
                this.strName = strName;
            }

            public String getStrDomain() {
                return strDomain;
            }

            public void setStrDomain(String strDomain) {
                this.strDomain = strDomain;
            }

            public String getCameraImg() {
                return cameraImg;
            }

            public void setCameraImg(String cameraImg) {
                this.cameraImg = cameraImg;
            }

            @Override
            public String toString() {
                return "Camera{" +
                        "strIP='" + strIP + '\'' +
                        ", strMac='" + strMac + '\'' +
                        ", strUsername='" + strUsername + '\'' +
                        ", strPassword='" + strPassword + '\'' +
                        ", nDevID='" + nDevID + '\'' +
                        ", strName='" + strName + '\'' +
                        ", strDomain='" + strDomain + '\'' +
                        ", cameraImg='" + cameraImg + '\'' +
                        '}';
            }
        }
    }
    public static class UploadCameraResult{
        private int result;

        public UploadCameraResult(int result) {
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
            return "UploadCameraResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
