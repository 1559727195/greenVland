package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-04.
 */

public class Sc_notification {
    private NotificationParams notificationParams;
    private NotificationResult notificationResult;

    public Sc_notification(NotificationParams notificationParams, NotificationResult notificationResult) {
        this.notificationParams = notificationParams;
        this.notificationResult = notificationResult;
    }

    public NotificationParams getNotificationParams() {
        return notificationParams;
    }

    public void setNotificationParams(NotificationParams notificationParams) {
        this.notificationParams = notificationParams;
    }

    public NotificationResult getNotificationResult() {
        return notificationResult;
    }

    public void setNotificationResult(NotificationResult notificationResult) {
        this.notificationResult = notificationResult;
    }

    @Override
    public String toString() {
        return "Sc_notification{" +
                "notificationParams=" + notificationParams +
                ", notificationResult=" + notificationResult +
                '}';
    }
    public static class NotificationParams{
        private String token;
        private String projectCode;
        private String page;
        private String roomNo;

        public NotificationParams(String token, String projectCode, String page, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.page = page;
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

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "NotificationParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class NotificationResult{
        private int result;
        private List<Notifacation> notifacationList;

        public NotificationResult(int result, List<Notifacation> notifacationList) {
            this.result = result;
            this.notifacationList = notifacationList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Notifacation> getNotifacationList() {
            return notifacationList;
        }

        public void setNotifacationList(List<Notifacation> notifacationList) {
            this.notifacationList = notifacationList;
        }

        @Override
        public String toString() {
            return "NotificationResult{" +
                    "result=" + result +
                    ", notifacationList=" + notifacationList +
                    '}';
        }

        public static class Notifacation implements Serializable {
            private int id;
            private String notificationTitle;
            private String notificationContent;
            private String notificationTime;
            private int notificationSign;
            private String adminName;

            public Notifacation(int id, String notificationTitle, String notificationContent, String notificationTime, int notificationSign, String adminName) {
                this.id = id;
                this.notificationTitle = notificationTitle;
                this.notificationContent = notificationContent;
                this.notificationTime = notificationTime;
                this.notificationSign = notificationSign;
                this.adminName = adminName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNotificationTitle() {
                return notificationTitle;
            }

            public void setNotificationTitle(String notificationTitle) {
                this.notificationTitle = notificationTitle;
            }

            public String getNotificationContent() {
                return notificationContent;
            }

            public void setNotificationContent(String notificationContent) {
                this.notificationContent = notificationContent;
            }

            public String getNotificationTime() {
                return notificationTime;
            }

            public void setNotificationTime(String notificationTime) {
                this.notificationTime = notificationTime;
            }

            public int getNotificationSign() {
                return notificationSign;
            }

            public void setNotificationSign(int notificationSign) {
                this.notificationSign = notificationSign;
            }

            public String getAdminName() {
                return adminName;
            }

            public void setAdminName(String adminName) {
                this.adminName = adminName;
            }

            @Override
            public String toString() {
                return "Notification{" +
                        "id=" + id +
                        ", notificationTitle='" + notificationTitle + '\'' +
                        ", notificationContent='" + notificationContent + '\'' +
                        ", notificationTime='" + notificationTime + '\'' +
                        ", notificationSign=" + notificationSign +
                        ", adminName='" + adminName + '\'' +
                        '}';
            }
        }
    }
}
