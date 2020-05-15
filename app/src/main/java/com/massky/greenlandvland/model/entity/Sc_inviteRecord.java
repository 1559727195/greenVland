package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-11-16.
 */

public class Sc_inviteRecord {
    private InviteRecordParams inviteRecordParams;
    private InviteRecordResult inviteRecordResult;

    public Sc_inviteRecord(InviteRecordParams inviteRecordParams, InviteRecordResult inviteRecordResult) {
        this.inviteRecordParams = inviteRecordParams;
        this.inviteRecordResult = inviteRecordResult;
    }

    public InviteRecordParams getInviteRecordParams() {
        return inviteRecordParams;
    }

    public void setInviteRecordParams(InviteRecordParams inviteRecordParams) {
        this.inviteRecordParams = inviteRecordParams;
    }

    public InviteRecordResult getInviteRecordResult() {
        return inviteRecordResult;
    }

    public void setInviteRecordResult(InviteRecordResult inviteRecordResult) {
        this.inviteRecordResult = inviteRecordResult;
    }

    @Override
    public String toString() {
        return "Sc_inviteRecord{" +
                "inviteRecordParams=" + inviteRecordParams +
                ", inviteRecordResult=" + inviteRecordResult +
                '}';
    }

    public static class InviteRecordParams{
        private String token;
        private String projectCode;
        private String page;
        private String roomNo;

        public InviteRecordParams(String token, String projectCode, String page, String roomNo) {
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
            return "InviteRecordParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class InviteRecordResult{
        private int result;
        private List<Invite> inviteList;

        public InviteRecordResult(int result, List<Invite> inviteList) {
            this.result = result;
            this.inviteList = inviteList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Invite> getInviteList() {
            return inviteList;
        }

        public void setInviteList(List<Invite> inviteList) {
            this.inviteList = inviteList;
        }

        @Override
        public String toString() {
            return "InviteRecordResult{" +
                    "result=" + result +
                    ", inviteList=" + inviteList +
                    '}';
        }

        public static class Invite{
            private int id;
            private String inviter;
            private List<Visitor> visitor;
            private List<VisitorPhone> visitorPhone;
            private String useTime;
            private String beginTime;
            private String endTime;
            private int status;
            private List<Quanxian> quanxian;
            private String inviteTime;

            public Invite(int id, String inviter, List<Visitor> visitor, List<VisitorPhone> visitorPhone, String useTime, String beginTime, String endTime, int status, List<Quanxian> quanxian, String inviteTime) {
                this.id = id;
                this.inviter = inviter;
                this.visitor = visitor;
                this.visitorPhone = visitorPhone;
                this.useTime = useTime;
                this.beginTime = beginTime;
                this.endTime = endTime;
                this.status = status;
                this.quanxian = quanxian;
                this.inviteTime = inviteTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getInviter() {
                return inviter;
            }

            public void setInviter(String inviter) {
                this.inviter = inviter;
            }

            public List<Visitor> getVisitor() {
                return visitor;
            }

            public void setVisitor(List<Visitor> visitor) {
                this.visitor = visitor;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<Quanxian> getQuanxian() {
                return quanxian;
            }

            public void setQuanxian(List<Quanxian> quanxian) {
                this.quanxian = quanxian;
            }

            public String getInviteTime() {
                return inviteTime;
            }

            public void setInviteTime(String inviteTime) {
                this.inviteTime = inviteTime;
            }

            @Override
            public String toString() {
                return "Invite{" +
                        "id=" + id +
                        ", inviter='" + inviter + '\'' +
                        ", visitor=" + visitor +
                        ", visitorPhone=" + visitorPhone +
                        ", useTime='" + useTime + '\'' +
                        ", beginTime='" + beginTime + '\'' +
                        ", endTime='" + endTime + '\'' +
                        ", status=" + status +
                        ", quanxian=" + quanxian +
                        ", inviteTime='" + inviteTime + '\'' +
                        '}';
            }

            public static class Visitor{
                private String name;

                public Visitor(String name) {
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
                    return "Visitor{" +
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
                private String doorName;

                public Quanxian(String doorName) {
                    this.doorName = doorName;
                }

                public String getDoorName() {
                    return doorName;
                }

                public void setDoorName(String doorName) {
                    this.doorName = doorName;
                }

                @Override
                public String toString() {
                    return "Quanxian{" +
                            "doorName='" + doorName + '\'' +
                            '}';
                }
            }
        }
    }
}
