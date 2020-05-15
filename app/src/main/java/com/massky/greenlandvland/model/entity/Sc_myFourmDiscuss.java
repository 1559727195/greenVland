package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-07.
 */

public class Sc_myFourmDiscuss {
    private MyFourmDiscussParams myFourmDiscussParams;
    private MyFourmDiscussResult myFourmDiscussResult;

    public Sc_myFourmDiscuss(MyFourmDiscussParams myFourmDiscussParams, MyFourmDiscussResult myFourmDiscussResult) {
        this.myFourmDiscussParams = myFourmDiscussParams;
        this.myFourmDiscussResult = myFourmDiscussResult;
    }

    public MyFourmDiscussParams getMyFourmDiscussParams() {
        return myFourmDiscussParams;
    }

    public void setMyFourmDiscussParams(MyFourmDiscussParams myFourmDiscussParams) {
        this.myFourmDiscussParams = myFourmDiscussParams;
    }

    public MyFourmDiscussResult getMyFourmDiscussResult() {
        return myFourmDiscussResult;
    }

    public void setMyFourmDiscussResult(MyFourmDiscussResult myFourmDiscussResult) {
        this.myFourmDiscussResult = myFourmDiscussResult;
    }

    @Override
    public String toString() {
        return "Sc_myFourmDiscuss{" +
                "myFourmDiscussParams=" + myFourmDiscussParams +
                ", myFourmDiscussResult=" + myFourmDiscussResult +
                '}';
    }
    public static class MyFourmDiscussParams{
        private String token;
        private String projectCode;
        private String page;
        private String roomNo;

        public MyFourmDiscussParams(String token, String projectCode, String page, String roomNo) {
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
            return "MyFourmDiscussParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class MyFourmDiscussResult{
        private int result;
        private List<Huitie> huitieList;

        public MyFourmDiscussResult(int result, List<Huitie> huitieList) {
            this.result = result;
            this.huitieList = huitieList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<Huitie> getHuitieList() {
            return huitieList;
        }

        public void setHuitieList(List<Huitie> huitieList) {
            this.huitieList = huitieList;
        }

        @Override
        public String toString() {
            return "MyFourmDiscussResult{" +
                    "result=" + result +
                    ", huitieList=" + huitieList +
                    '}';
        }
        public static class Huitie implements Serializable{
            private int id;
            private int laudCount;
            private String forumCategory;
            private String userName;
            private String pushTime;
            private String forumTitle;
            private String forumContent;
            private List<ForumImage> forumImage;
            private int discussCount;

            public Huitie(int id, int laudCount, String forumCategory, String userName, String pushTime, String forumTitle, String forumContent, List<ForumImage> forumImage, int discussCount) {
                this.id = id;
                this.laudCount = laudCount;
                this.forumCategory = forumCategory;
                this.userName = userName;
                this.pushTime = pushTime;
                this.forumTitle = forumTitle;
                this.forumContent = forumContent;
                this.forumImage = forumImage;
                this.discussCount = discussCount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLaudCount() {
                return laudCount;
            }

            public void setLaudCount(int laudCount) {
                this.laudCount = laudCount;
            }

            public String getForumCategory() {
                return forumCategory;
            }

            public void setForumCategory(String forumCategory) {
                this.forumCategory = forumCategory;
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

            public String getForumTitle() {
                return forumTitle;
            }

            public void setForumTitle(String forumTitle) {
                this.forumTitle = forumTitle;
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

            public int getDiscussCount() {
                return discussCount;
            }

            public void setDiscussCount(int discussCount) {
                this.discussCount = discussCount;
            }

            @Override
            public String toString() {
                return "Huitie{" +
                        "id=" + id +
                        ", laudCount=" + laudCount +
                        ", forumCategory='" + forumCategory + '\'' +
                        ", userName='" + userName + '\'' +
                        ", pushTime='" + pushTime + '\'' +
                        ", forumTitle='" + forumTitle + '\'' +
                        ", forumContent='" + forumContent + '\'' +
                        ", forumImage=" + forumImage +
                        ", discussCount=" + discussCount +
                        '}';
            }
            public static class ForumImage implements Serializable{
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
