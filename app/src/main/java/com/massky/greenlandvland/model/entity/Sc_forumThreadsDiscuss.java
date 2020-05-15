package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-05-28.
 */

public class Sc_forumThreadsDiscuss {
    private ForumThreadsDiscussParams forumThreadsDiscussParams;
    private ForumThreadsDiscussResult forumThreadsDiscussResult;

    public Sc_forumThreadsDiscuss(ForumThreadsDiscussParams forumThreadsDiscussParams, ForumThreadsDiscussResult forumThreadsDiscussResult) {
        this.forumThreadsDiscussParams = forumThreadsDiscussParams;
        this.forumThreadsDiscussResult = forumThreadsDiscussResult;
    }

    public ForumThreadsDiscussParams getForumThreadsDiscussParams() {
        return forumThreadsDiscussParams;
    }

    public void setForumThreadsDiscussParams(ForumThreadsDiscussParams forumThreadsDiscussParams) {
        this.forumThreadsDiscussParams = forumThreadsDiscussParams;
    }

    public ForumThreadsDiscussResult getForumThreadsDiscussResult() {
        return forumThreadsDiscussResult;
    }

    public void setForumThreadsDiscussResult(ForumThreadsDiscussResult forumThreadsDiscussResult) {
        this.forumThreadsDiscussResult = forumThreadsDiscussResult;
    }

    @Override
    public String toString() {
        return "Sc_forumThreadsDiscuss{" +
                "forumThreadsDiscussParams=" + forumThreadsDiscussParams +
                ", forumThreadsDiscussResult=" + forumThreadsDiscussResult +
                '}';
    }

    public static class ForumThreadsDiscussParams{
        private String token;
        private String projectCode;
        private int forumId;
        private String page;

        public ForumThreadsDiscussParams(String token, String projectCode, int forumId, String page) {
            this.token = token;
            this.projectCode = projectCode;
            this.forumId = forumId;
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

        public int getForumId() {
            return forumId;
        }

        public void setForumId(int forumId) {
            this.forumId = forumId;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "ForumThreadsDiscussParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", forumId=" + forumId +
                    ", page='" + page + '\'' +
                    '}';
        }
    }

    public static class ForumThreadsDiscussResult{
        private int result;
        private List<ForumDiscuss> forumDiscussList;

        public ForumThreadsDiscussResult(int result, List<ForumDiscuss> forumDiscussList) {
            this.result = result;
            this.forumDiscussList = forumDiscussList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<ForumDiscuss> getForumDiscussList() {
            return forumDiscussList;
        }

        public void setForumDiscussList(List<ForumDiscuss> forumDiscussList) {
            this.forumDiscussList = forumDiscussList;
        }

        @Override
        public String toString() {
            return "ForumThreadsDiscussResult{" +
                    "result=" + result +
                    ", forumDiscussList=" + forumDiscussList +
                    '}';
        }

        public static class ForumDiscuss{
            private int id;
            private String discusser;
            private String discussContent;
            private String discussTime;
            private String subDiscussCount;
            private String discussLaudCount;
            private String discusserHead;

            public ForumDiscuss(int id, String discusser, String discussContent, String discussTime, String subDiscussCount, String discussLaudCount, String discusserHead) {
                this.id = id;
                this.discusser = discusser;
                this.discussContent = discussContent;
                this.discussTime = discussTime;
                this.subDiscussCount = subDiscussCount;
                this.discussLaudCount = discussLaudCount;
                this.discusserHead = discusserHead;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDiscusser() {
                return discusser;
            }

            public void setDiscusser(String discusser) {
                this.discusser = discusser;
            }

            public String getDiscussContent() {
                return discussContent;
            }

            public void setDiscussContent(String discussContent) {
                this.discussContent = discussContent;
            }

            public String getDiscussTime() {
                return discussTime;
            }

            public void setDiscussTime(String discussTime) {
                this.discussTime = discussTime;
            }

            public String getSubDiscussCount() {
                return subDiscussCount;
            }

            public void setSubDiscussCount(String subDiscussCount) {
                this.subDiscussCount = subDiscussCount;
            }

            public String getDiscussLaudCount() {
                return discussLaudCount;
            }

            public void setDiscussLaudCount(String discussLaudCount) {
                this.discussLaudCount = discussLaudCount;
            }

            public String getDiscusserHead() {
                return discusserHead;
            }

            public void setDiscusserHead(String discusserHead) {
                this.discusserHead = discusserHead;
            }

            @Override
            public String toString() {
                return "ForumDiscuss{" +
                        "id=" + id +
                        ", discusser='" + discusser + '\'' +
                        ", discussContent='" + discussContent + '\'' +
                        ", discussTime='" + discussTime + '\'' +
                        ", subDiscussCount='" + subDiscussCount + '\'' +
                        ", discussLaudCount='" + discussLaudCount + '\'' +
                        ", discusserHead='" + discusserHead + '\'' +
                        '}';
            }
        }
    }
}
