package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-10-25.
 */

public class Sc_myCamera  {
    private MyCameraParams myCameraParams;
    private MyCameraResult myCameraResult;

    public Sc_myCamera(MyCameraParams myCameraParams, MyCameraResult myCameraResult) {
        this.myCameraParams = myCameraParams;
        this.myCameraResult = myCameraResult;
    }

    public MyCameraParams getMyCameraParams() {
        return myCameraParams;
    }

    public void setMyCameraParams(MyCameraParams myCameraParams) {
        this.myCameraParams = myCameraParams;
    }

    public MyCameraResult getMyCameraResult() {
        return myCameraResult;
    }

    public void setMyCameraResult(MyCameraResult myCameraResult) {
        this.myCameraResult = myCameraResult;
    }

    @Override
    public String toString() {
        return "Sc_myCamera{" +
                "myCameraParams=" + myCameraParams +
                ", myCameraResult=" + myCameraResult +
                '}';
    }
    public static class MyCameraParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyCameraParams(String token, String projectCode, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
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

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "MyCameraParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyCameraResult{
        private int result;
        private List<Camera> cameraList;

        public MyCameraResult(int result, List<Camera> cameraList) {
            this.result = result;
            this.cameraList = cameraList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Camera> getCameraList() {
            return cameraList;
        }

        public void setCameraList(List<Camera> cameraList) {
            this.cameraList = cameraList;
        }

        @Override
        public String toString() {
            return "MyCameraResult{" +
                    "result=" + result +
                    ", cameraList=" + cameraList +
                    '}';
        }
        public static class Camera implements Serializable {
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
}
