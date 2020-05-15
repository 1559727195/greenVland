package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class Sc_myForum {
    private MyForumParams myForumParams;
    private MyForumResult myForumResult;

    public Sc_myForum(MyForumParams myForumParams, MyForumResult myForumResult) {
        this.myForumParams = myForumParams;
        this.myForumResult = myForumResult;
    }

    public MyForumParams getMyForumParams() {
        return myForumParams;
    }

    public void setMyForumParams(MyForumParams myForumParams) {
        this.myForumParams = myForumParams;
    }

    public MyForumResult getMyForumResult() {
        return myForumResult;
    }

    public void setMyForumResult(MyForumResult myForumResult) {
        this.myForumResult = myForumResult;
    }

    @Override
    public String toString() {
        return "Sc_myForum{" +
                "myForumParams=" + myForumParams +
                ", myForumResult=" + myForumResult +
                '}';
    }

    public static class MyForumParams{
        private String token;
        private String projectCode;
        private String accountId;
        private int page;

        public MyForumParams(String token, String projectCode, String accountId, int page) {
            this.token = token;
            this.projectCode = projectCode;
            this.accountId = accountId;
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

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "MyForumParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", accountId='" + accountId + '\'' +
                    ", page=" + page +
                    '}';
        }
    }
    public static class MyForumResult{
        private int result;
        private List<ForumThreads> forumThreadsList;

        public MyForumResult(int result, List<ForumThreads> forumThreadsList) {
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
            return "MyForumResult{" +
                    "result=" + result +
                    ", forumThreadsList=" + forumThreadsList +
                    '}';
        }
        public static class ForumThreads{
            private int id;
            private String forumTitle;
            private String forumCategory;
            private String forumContent;
            private List<ForumImage> forumImage;
            private String discussCount;
            private String laudCount;

            public ForumThreads(int id, String forumTitle, String forumCategory, String forumContent, List<ForumImage> forumImage, String discussCount, String laudCount) {
                this.id = id;
                this.forumTitle = forumTitle;
                this.forumCategory = forumCategory;
                this.forumContent = forumContent;
                this.forumImage = forumImage;
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

            public String getDiscussCount() {
                return discussCount;
            }

            public void setDiscussCount(String discussCount) {
                this.discussCount = discussCount;
            }

            public String getLaudCount() {
                return laudCount;
            }

            public void setLaudCount(String laudCount) {
                this.laudCount = laudCount;
            }

            @Override
            public String toString() {
                return "ForumThreads{" +
                        "id=" + id +
                        ", forumTitle='" + forumTitle + '\'' +
                        ", forumCategory='" + forumCategory + '\'' +
                        ", forumContent='" + forumContent + '\'' +
                        ", forumImage=" + forumImage +
                        ", discussCount='" + discussCount + '\'' +
                        ", laudcount='" + laudCount + '\'' +
                        '}';
            }
            public static class ForumImage{
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
