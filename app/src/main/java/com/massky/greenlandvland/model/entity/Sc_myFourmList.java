package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-06.
 */

public class Sc_myFourmList {
    private MyFourmListParams myFourmListParams;
    private MyFourmListResult myFourmListResult;

    public Sc_myFourmList(MyFourmListParams myFourmListParams, MyFourmListResult myFourmListResult) {
        this.myFourmListParams = myFourmListParams;
        this.myFourmListResult = myFourmListResult;
    }

    public MyFourmListParams getMyFourmListParams() {
        return myFourmListParams;
    }

    public void setMyFourmListParams(MyFourmListParams myFourmListParams) {
        this.myFourmListParams = myFourmListParams;
    }

    public MyFourmListResult getMyFourmListResult() {
        return myFourmListResult;
    }

    public void setMyFourmListResult(MyFourmListResult myFourmListResult) {
        this.myFourmListResult = myFourmListResult;
    }

    @Override
    public String toString() {
        return "Sc_myFourmList{" +
                "myFourmListParams=" + myFourmListParams +
                ", myFourmListResult=" + myFourmListResult +
                '}';
    }
    public static class MyFourmListParams{
        private String token;
        private String projectCode;
        private String page;
        private String roomNo;

        public MyFourmListParams(String token, String projectCode, String page, String roomNo) {
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
            return "MyFourmListParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyFourmListResult{
        private int result;
        private List<MyFourm> myFourmList;

        public MyFourmListResult(int result, List<MyFourm> myFourmList) {
            this.result = result;
            this.myFourmList = myFourmList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<MyFourm> getMyFourmList() {
            return myFourmList;
        }

        public void setMyFourmList(List<MyFourm> myFourmList) {
            this.myFourmList = myFourmList;
        }

        @Override
        public String toString() {
            return "MyFourmListResult{" +
                    "result=" + result +
                    ", myFourmList=" + myFourmList +
                    '}';
        }
        public static class MyFourm implements Serializable {
            private int id;
            private String forumTitle;
            private String forumCategory;
            private String forumContent;
            private List<ForumImage> forumImage;
            private String userName;
            private String pushTime;
            private int discussCount;
            private int laudCount;

            public MyFourm(int id, String forumTitle, String forumCategory, String forumContent, List<ForumImage> forumImage, String userName, String pushTime, int discussCount, int laudCount) {
                this.id = id;
                this.forumTitle = forumTitle;
                this.forumCategory = forumCategory;
                this.forumContent = forumContent;
                this.forumImage = forumImage;
                this.userName = userName;
                this.pushTime = pushTime;
                this.discussCount = discussCount;
                this.laudCount = laudCount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getForumTitle() {
                return forumTitle;
            }

            public void setForumTitle(String forumTitle) {
                this.forumTitle = forumTitle;
            }

            public String getForumCategory() {
                return forumCategory;
            }

            public void setForumCategory(String forumCategory) {
                this.forumCategory = forumCategory;
            }

            public String getForumContent() {
                return forumContent;
            }

            public void setForumContent(String forumContent) {
                this.forumContent = forumContent;
            }

            public List<ForumImage> getForumImage() {
                return forumImage;
            }

            public void setForumImage(List<ForumImage> forumImage) {
                this.forumImage = forumImage;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPushTime() {
                return pushTime;
            }

            public void setPushTime(String pushTime) {
                this.pushTime = pushTime;
            }

            public int getDiscussCount() {
                return discussCount;
            }

            public void setDiscussCount(int discussCount) {
                this.discussCount = discussCount;
            }

            public int getLaudCount() {
                return laudCount;
            }

            public void setLaudCount(int laudCount) {
                this.laudCount = laudCount;
            }

            @Override
            public String toString() {
                return "MyFourm{" +
                        "id=" + id +
                        ", forumTitle='" + forumTitle + '\'' +
                        ", forumCategory='" + forumCategory + '\'' +
                        ", forumContent='" + forumContent + '\'' +
                        ", forumImage=" + forumImage +
                        ", userName='" + userName + '\'' +
                        ", pushTime='" + pushTime + '\'' +
                        ", discussCount=" + discussCount +
                        ", laudCount=" + laudCount +
                        '}';
            }

            public static class ForumImage implements Serializable {
                private String imageUrl;

                public ForumImage(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                @Override
                public String toString() {
                    return "ForumImage{" +
                            "imageUrl='" + imageUrl + '\'' +
                            '}';
                }
            }
        }
    }
}
