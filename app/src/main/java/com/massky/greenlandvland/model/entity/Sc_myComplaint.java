package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/3 0003.
 */

public class Sc_myComplaint {
    private MyComplaintParams myComplaintParams;
    private MyComplaintResult myComplaintResult;

    public Sc_myComplaint(MyComplaintParams myComplaintParams, MyComplaintResult myComplaintResult) {
        this.myComplaintParams = myComplaintParams;
        this.myComplaintResult = myComplaintResult;
    }

    public MyComplaintParams getMyComplaintParams() {
        return myComplaintParams;
    }

    public void setMyComplaintParams(MyComplaintParams myComplaintParams) {
        this.myComplaintParams = myComplaintParams;
    }

    public MyComplaintResult getMyComplaintResult() {
        return myComplaintResult;
    }

    public void setMyComplaintResult(MyComplaintResult myComplaintResult) {
        this.myComplaintResult = myComplaintResult;
    }

    @Override
    public String toString() {
        return "Sc_myComplaint{" +
                "myComplaintParams=" + myComplaintParams +
                ", myComplaintResult=" + myComplaintResult +
                '}';
    }
    public static class MyComplaintParams{
        private String token;
        private String projectCode;
        private String page;

        public MyComplaintParams(String token, String projectCode, String page) {
            this.token = token;
            this.projectCode = projectCode;
            this.page = page;
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

        @Override
        public String toString() {
            return "MyComplaintParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    '}';
        }
    }
    public static class MyComplaintResult{
        private int result;
        private List<Complaint> complaintList;

        public MyComplaintResult(int result, List<Complaint> complaintList) {
            this.result = result;
            this.complaintList = complaintList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Complaint> getComplaintList() {
            return complaintList;
        }

        public void setComplaintList(List<Complaint> complaintList) {
            this.complaintList = complaintList;
        }

        @Override
        public String toString() {
            return "MyComplaintResult{" +
                    "result=" + result +
                    ", complaintList=" + complaintList +
                    '}';
        }

        public static class Complaint implements Serializable{
            private int id;
            private String complaintTitle;
            private String complaintCategory;
            private String complaintContent;
            private String complaintTime;
            private List<List<ComplaintImage>> complaintImage;
            private AccountInfo accountInfo;
            private String handleResult;

            public Complaint(int id, String complaintTitle, String complaintCategory, String complaintContent, String complaintTime, List<List<ComplaintImage>> complaintImage, AccountInfo accountInfo, String handleResult) {
                this.id = id;
                this.complaintTitle = complaintTitle;
                this.complaintCategory = complaintCategory;
                this.complaintContent = complaintContent;
                this.complaintTime = complaintTime;
                this.complaintImage = complaintImage;
                this.accountInfo = accountInfo;
                this.handleResult = handleResult;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getComplaintTitle() {
                return complaintTitle;
            }

            public void setComplaintTitle(String complaintTitle) {
                this.complaintTitle = complaintTitle;
            }

            public String getComplaintCategory() {
                return complaintCategory;
            }

            public void setComplaintCategory(String complaintCategory) {
                this.complaintCategory = complaintCategory;
            }

            public String getComplaintContent() {
                return complaintContent;
            }

            public void setComplaintContent(String complaintContent) {
                this.complaintContent = complaintContent;
            }

            public String getComplaintTime() {
                return complaintTime;
            }

            public void setComplaitnTime(String complaintTime) {
                this.complaintTime = complaintTime;
            }

            public List<List<ComplaintImage>> getComplaintImage() {
                return complaintImage;
            }

            public void setComplaintImage(List<List<ComplaintImage>> complaintImage) {
                this.complaintImage = complaintImage;
            }

            public AccountInfo getAccountInfo() {
                return accountInfo;
            }

            public void setAccountInfo(AccountInfo accountInfo) {
                this.accountInfo = accountInfo;
            }

            public String getHandleResult() {
                return handleResult;
            }

            public void setHandleResult(String handleResult) {
                this.handleResult = handleResult;
            }

            @Override
            public String toString() {
                return "Complaint{" +
                        "id=" + id +
                        ", complaintTitle='" + complaintTitle + '\'' +
                        ", complaintCategory='" + complaintCategory + '\'' +
                        ", complaintContent='" + complaintContent + '\'' +
                        ", complaitnTime='" + complaintTime + '\'' +
                        ", complaintImage=" + complaintImage +
                        ", accountInfo=" + accountInfo +
                        ", handleResult='" + handleResult + '\'' +
                        '}';
            }
            public static class AccountInfo implements Serializable{
                private String headIcon;
                private String realName;

                public AccountInfo(String headIcon, String realName) {
                    this.headIcon = headIcon;
                    this.realName = realName;
                }

                public String getHeadIcon() {
                    return headIcon;
                }

                public void setHeadIcon(String headIcon) {
                    this.headIcon = headIcon;
                }

                public String getRealName() {
                    return realName;
                }

                public void setRealName(String realName) {
                    this.realName = realName;
                }

                @Override
                public String toString() {
                    return "AccountInfo{" +
                            "headIcon='" + headIcon + '\'' +
                            ", realName='" + realName + '\'' +
                            '}';
                }
            }
            public static class ComplaintImage implements Serializable{
                private int id;
                private String imageName;
                private String imageUrl;
                private int complaintId;

                public ComplaintImage(int id, String imageName, String imageUrl, int complaintId) {
                    this.id = id;
                    this.imageName = imageName;
                    this.imageUrl = imageUrl;
                    this.complaintId = complaintId;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getImageName() {
                    return imageName;
                }

                public void setImageName(String imageName) {
                    this.imageName = imageName;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public int getComplaintId() {
                    return complaintId;
                }

                public void setComplaintId(int complaintId) {
                    this.complaintId = complaintId;
                }

                @Override
                public String toString() {
                    return "ComplaintImage{" +
                            "id=" + id +
                            ", imageName='" + imageName + '\'' +
                            ", imageUrl='" + imageUrl + '\'' +
                            ", complaintId=" + complaintId +
                            '}';
                }
            }
        }
    }
}
