package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-27.
 */

public class Sc_forumThreads {
    private ForumThreadsParams forumThreadsParams;
    private ForumThreadsResult forumThreadResult;

    public Sc_forumThreads(ForumThreadsParams forumThreadsParams, ForumThreadsResult forumThreadResult) {
        this.forumThreadsParams = forumThreadsParams;
        this.forumThreadResult = forumThreadResult;
    }

    public ForumThreadsParams getForumThreadsParams() {
        return forumThreadsParams;
    }

    public void setForumThreadsParams(ForumThreadsParams forumThreadsParams) {
        this.forumThreadsParams = forumThreadsParams;
    }

    public ForumThreadsResult getForumThreadResult() {
        return forumThreadResult;
    }

    public void setForumThreadResult(ForumThreadsResult forumThreadResult) {
        this.forumThreadResult = forumThreadResult;
    }

    @Override
    public String toString() {
        return "Sc_forumThreads{" +
                "forumThreadsParams=" + forumThreadsParams +
                ", forumThreadResult=" + forumThreadResult +
                '}';
    }
    public static class ForumThreadsParams{
        private String token;
        private String projectCode;
        private String forumType;
        private String page;
        private String roomNo;

        public ForumThreadsParams(String token, String projectCode, String forumType, String page, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.forumType = forumType;
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

        public String getForumType() {
            return forumType;
        }

        public void setForumType(String forumType) {
            this.forumType = forumType;
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
            return "ForumThreadsParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", forumType='" + forumType + '\'' +
                    ", page='" + page + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }
    public static class ForumThreadsResult{
        private int result;
        private List<ForumThreads> forumThreadsList;

        public ForumThreadsResult(int result, List<ForumThreads> forumThreadsList) {
            this.result = result;
            this.forumThreadsList = forumThreadsList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<ForumThreads> getForumThreadsList() {
            return forumThreadsList;
        }

        public void setForumThreadsList(List<ForumThreads> forumThreadsList) {
            this.forumThreadsList = forumThreadsList;
        }

        @Override
        public String toString() {
            return "ForumThreadsResult{" +
                    "result=" + result +
                    ", forumThreadsList=" + forumThreadsList +
                    '}';
        }

        public static class ForumThreads implements Serializable {
            private int id;
            private int laudCount;
            private String userName;
            private String pushTime;
            private String forumTitle;
            private String forumCategory;
            private String forumContent;
            private int discussCount;
            private List<ForumImage> forumImage;

            public ForumThreads(int id, int laudCount, String userName, String pushTime, String forumTitle, String forumCategory, String forumContent, int discussCount, List<ForumImage> forumImage) {
                this.id = id;
                this.laudCount = laudCount;
                this.userName = userName;
                this.pushTime = pushTime;
                this.forumTitle = forumTitle;
                this.forumCategory = forumCategory;
                this.forumContent = forumContent;
                this.discussCount = discussCount;
                this.forumImage = forumImage;
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

            public int getDiscussCount() {
                return discussCount;
            }

            public void setDiscussCount(int discussCount) {
                this.discussCount = discussCount;
            }

            public List<ForumImage> getForumImage() {
                return forumImage;
            }

            public void setForumImage(List<ForumImage> forumImage) {
                this.forumImage = forumImage;
            }

            @Override
            public String toString() {
                return "ForumThreads{" +
                        "id=" + id +
                        ", laudcount=" + laudCount +
                        ", userName='" + userName + '\'' +
                        ", pushTime='" + pushTime + '\'' +
                        ", forumTitle='" + forumTitle + '\'' +
                        ", forumCategory='" + forumCategory + '\'' +
                        ", forumContent='" + forumContent + '\'' +
                        ", discussCount=" + discussCount +
                        ", forumImage=" + forumImage +
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
