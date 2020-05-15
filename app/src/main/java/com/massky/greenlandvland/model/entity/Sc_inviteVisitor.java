package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

public class Sc_inviteVisitor {
    private InviteVisitorParams inviteVisitorParams;
    private InviteVisitorResoult inviteVisitorResoult;

    public Sc_inviteVisitor(InviteVisitorParams inviteVisitorParams, InviteVisitorResoult inviteVisitorResoult) {
        this.inviteVisitorParams = inviteVisitorParams;
        this.inviteVisitorResoult = inviteVisitorResoult;
    }

    public InviteVisitorParams getInviteVisitorParams() {
        return inviteVisitorParams;
    }

    public void setInviteVisitorParams(InviteVisitorParams inviteVisitorParams) {
        this.inviteVisitorParams = inviteVisitorParams;
    }

    public InviteVisitorResoult getInviteVisitorResoult() {
        return inviteVisitorResoult;
    }

    public void setInviteVisitorResoult(InviteVisitorResoult inviteVisitorResoult) {
        this.inviteVisitorResoult = inviteVisitorResoult;
    }

    @Override
    public String toString() {
        return "Sc_inviteVisitor{" +
                "inviteVisitorParams=" + inviteVisitorParams +
                ", inviteVisitorResoult=" + inviteVisitorResoult +
                '}';
    }

    public static class InviteVisitorParams{
        private String token;
        private List<VisitorName> visitorName;
        private List<VisitorPhone> visitorPhone;
        private String useTime;
        private String beginTime;
        private String endTime;
        private List<Quanxian> quanxian;
        private String projectCode;
        private String roomNo;

        public InviteVisitorParams(String token, List<VisitorName> visitorName, List<VisitorPhone> visitorPhone, String useTime, String beginTime, String endTime, List<Quanxian> quanxian, String projectCode, String roomNo) {
            this.token = token;
            this.visitorName = visitorName;
            this.visitorPhone = visitorPhone;
            this.useTime = useTime;
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.quanxian = quanxian;
            this.projectCode = projectCode;
            this.roomNo = roomNo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<VisitorName> getVisitorName() {
            return visitorName;
        }

        public void setVisitorName(List<VisitorName> visitorName) {
            this.visitorName = visitorName;
        }

        public List<VisitorPhone> getVisitorPhone() {
            return visitorPhone;
        }

        public void setVisitorPhone(List<VisitorPhone> visitorPhone) {
            this.visitorPhone = visitorPhone;
        }

        public String getUseTime() {
            return useTime;
        }

        public void setUseTime(String useTime) {
            this.useTime = useTime;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<Quanxian> getQuanxian() {
            return quanxian;
        }

        public void setQuanxian(List<Quanxian> quanxian) {
            this.quanxian = quanxian;
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
            return "InviteVisitorParams{" +
                    "token='" + token + '\'' +
                    ", visitorName=" + visitorName +
                    ", visitorPhone=" + visitorPhone +
                    ", useTime='" + useTime + '\'' +
                    ", beginTime='" + beginTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", quanxian=" + quanxian +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class VisitorName{
            private String name;

            public VisitorName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "VisitorName{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }
        public static class VisitorPhone{
            private String phone;

            public VisitorPhone(String phone) {
                this.phone = phone;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            @Override
            public String toString() {
                return "VisitorPhone{" +
                        "phone='" + phone + '\'' +
                        '}';
            }
        }
        public static class Quanxian{
            private int doorId;

            public Quanxian(int doorId) {
                this.doorId = doorId;
            }

            public int getDoorId() {
                return doorId;
            }

            public void setDoorId(int doorId) {
                this.doorId = doorId;
            }

            @Override
            public String toString() {
                return "Quanxian{" +
                        "doorId='" + doorId + '\'' +
                        '}';
            }
        }
    }
    public static class InviteVisitorResoult{
        private int result;

        public InviteVisitorResoult(int result) {
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
            return "InviteVisitorResoult{" +
                    "result=" + result +
                    '}';
        }
    }
}

