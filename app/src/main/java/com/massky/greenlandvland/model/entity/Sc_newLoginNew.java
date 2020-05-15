package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-12-25.
 */

public class Sc_newLoginNew {
    private NewLoginNewParams newLoginNewParams;
    private NewLoginNewResult newLoginNewResult;

    public Sc_newLoginNew(NewLoginNewParams newLoginNewParams, NewLoginNewResult newLoginNewResult) {
        this.newLoginNewParams = newLoginNewParams;
        this.newLoginNewResult = newLoginNewResult;
    }

    public NewLoginNewParams getNewLoginNewParams() {
        return newLoginNewParams;
    }

    public void setNewLoginNewParams(NewLoginNewParams newLoginNewParams) {
        this.newLoginNewParams = newLoginNewParams;
    }

    public NewLoginNewResult getNewLoginNewResult() {
        return newLoginNewResult;
    }

    public void setNewLoginNewResult(NewLoginNewResult newLoginNewResult) {
        this.newLoginNewResult = newLoginNewResult;
    }

    @Override
    public String toString() {
        return "Sc_newLoginNew{" +
                "newLoginNewParams=" + newLoginNewParams +
                ", newLoginNewResult=" + newLoginNewResult +
                '}';
    }

    public static class NewLoginNewParams{
        private String token;
        private String regId;
        private String phoneId;
        private int appCode;

        public NewLoginNewParams(String token, String regId, String phoneId, int appCode) {
            this.token = token;
            this.regId = regId;
            this.phoneId = phoneId;
            this.appCode = appCode;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRegId() {
            return regId;
        }

        public void setRegId(String regId) {
            this.regId = regId;
        }

        public String getPhoneId() {
            return phoneId;
        }

        public void setPhoneId(String phoneId) {
            this.phoneId = phoneId;
        }

        public int getAppCode() {
            return appCode;
        }

        public void setAppCode(int appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "NewLoginNewParams{" +
                    "token='" + token + '\'' +
                    ", regId='" + regId + '\'' +
                    ", phoneId='" + phoneId + '\'' +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    public static class NewLoginNewResult{
        private int result;
        private String avatar;
        private String userName;
        private String phoneNumber;
        private List<ProjectRoomType> projectRoomType;

        public NewLoginNewResult(int result, String avatar, String userName, String phoneNumber, List<ProjectRoomType> projectRoomType) {
            this.result = result;
            this.avatar = avatar;
            this.userName = userName;
            this.phoneNumber = phoneNumber;
            this.projectRoomType = projectRoomType;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public List<ProjectRoomType> getProjectRoomType() {
            return projectRoomType;
        }

        public void setProjectRoomType(List<ProjectRoomType> projectRoomType) {
            this.projectRoomType = projectRoomType;
        }

        @Override
        public String toString() {
            return "NewLoginNewResult{" +
                    "result=" + result +
                    ", avatar='" + avatar + '\'' +
                    ", userName='" + userName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", projectRoomType=" + projectRoomType +
                    '}';
        }

        public static class ProjectRoomType{
            private String projectCode;
            private String projectName;
            private List<RoomNoAndTypeArray> roomNoAndTypeArray;

            public ProjectRoomType(String projectCode, String projectName, List<RoomNoAndTypeArray> roomNoAndTypeArray) {
                this.projectCode = projectCode;
                this.projectName = projectName;
                this.roomNoAndTypeArray = roomNoAndTypeArray;
            }

            public String getProjectCode() {
                return projectCode;
            }

            public void setProjectCode(String projectCode) {
                this.projectCode = projectCode;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public List<RoomNoAndTypeArray> getRoomNoAndTypeArray() {
                return roomNoAndTypeArray;
            }

            public void setRoomNoAndTypeArray(List<RoomNoAndTypeArray> roomNoAndTypeArray) {
                this.roomNoAndTypeArray = roomNoAndTypeArray;
            }

            @Override
            public String toString() {
                return "ProjectRoomType{" +
                        "projectCode='" + projectCode + '\'' +
                        ", projectName='" + projectName + '\'' +
                        ", roomNoAndTypeArray=" + roomNoAndTypeArray +
                        '}';
            }

            public static class RoomNoAndTypeArray{
                private String roomNo;
                private String roomNoName;
                private int accountType;

                public RoomNoAndTypeArray(String roomNo, String roomNoName, int accountType) {
                    this.roomNo = roomNo;
                    this.roomNoName = roomNoName;
                    this.accountType = accountType;
                }

                public String getRoomNo() {
                    return roomNo;
                }

                public void setRoomNo(String roomNo) {
                    this.roomNo = roomNo;
                }

                public String getRoomNoName() {
                    return roomNoName;
                }

                public void setRoomNoName(String roomNoName) {
                    this.roomNoName = roomNoName;
                }

                public int getAccountType() {
                    return accountType;
                }

                public void setAccountType(int accountType) {
                    this.accountType = accountType;
                }

                @Override
                public String toString() {
                    return "RoomNoAndTypeArray{" +
                            "roomNo='" + roomNo + '\'' +
                            ", roomNoName='" + roomNoName + '\'' +
                            ", accountType=" + accountType +
                            '}';
                }
            }
        }
    }
}
