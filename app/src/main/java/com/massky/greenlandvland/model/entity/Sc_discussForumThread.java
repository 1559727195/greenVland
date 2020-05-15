package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-30.
 */

public class Sc_discussForumThread {
    private DiscussForumThreadParams discussForumThreadParams;
    private DiscussForumThreadResult discussForumThreadResult;

    public Sc_discussForumThread(DiscussForumThreadParams discussForumThreadParams, DiscussForumThreadResult discussForumThreadResult) {
        this.discussForumThreadParams = discussForumThreadParams;
        this.discussForumThreadResult = discussForumThreadResult;
    }

    public DiscussForumThreadParams getDiscussForumThreadParams() {
        return discussForumThreadParams;
    }

    public void setDiscussForumThreadParams(DiscussForumThreadParams discussForumThreadParams) {
        this.discussForumThreadParams = discussForumThreadParams;
    }

    public DiscussForumThreadResult getDiscussForumThreadResult() {
        return discussForumThreadResult;
    }

    public void setDiscussForumThreadResult(DiscussForumThreadResult discussForumThreadResult) {
        this.discussForumThreadResult = discussForumThreadResult;
    }

    @Override
    public String toString() {
        return "Sc_discussForumThread{" +
                "discussForumThreadParams=" + discussForumThreadParams +
                ", discussForumThreadResult=" + discussForumThreadResult +
                '}';
    }

    public static class DiscussForumThreadParams{
        private String token;
        private String projectCode;
        private int discussType;
        private int id;
        private String discussContent;
        private String roomNo;

        public DiscussForumThreadParams(String token, String projectCode, int discussType, int id, String discussContent, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.discussType = discussType;
            this.id = id;
            this.discussContent = discussContent;
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

        public int getDiscussType() {
            return discussType;
        }

        public void setDiscussType(int discussType) {
            this.discussType = discussType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDiscussContent() {
            return discussContent;
        }

        public void setDiscussContent(String discussContent) {
            this.discussContent = discussContent;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "DiscussForumThreadParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", discussType=" + discussType +
                    ", id=" + id +
                    ", discussContent='" + discussContent + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }

    public static class DiscussForumThreadResult{
        private int result;

        public DiscussForumThreadResult(int result) {
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
            return "DiscussForumThreadResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
