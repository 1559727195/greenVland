package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-30.
 */

public class Sc_laudForumThread {
    private LaudForumThreadParams laudForumThreadParams;
    private LaudForumThreadResult laudForumThreadResult;

    public Sc_laudForumThread(LaudForumThreadParams laudForumThreadParams, LaudForumThreadResult laudForumThreadResult) {
        this.laudForumThreadParams = laudForumThreadParams;
        this.laudForumThreadResult = laudForumThreadResult;
    }

    public LaudForumThreadParams getLaudForumThreadParams() {
        return laudForumThreadParams;
    }

    public void setLaudForumThreadParams(LaudForumThreadParams laudForumThreadParams) {
        this.laudForumThreadParams = laudForumThreadParams;
    }

    public LaudForumThreadResult getLaudForumThreadResult() {
        return laudForumThreadResult;
    }

    public void setLaudForumThreadResult(LaudForumThreadResult laudForumThreadResult) {
        this.laudForumThreadResult = laudForumThreadResult;
    }

    @Override
    public String toString() {
        return "Sc_laudForumThread{" +
                "laudForumThreadParams=" + laudForumThreadParams +
                ", laudForumThreadResult=" + laudForumThreadResult +
                '}';
    }

    public static class LaudForumThreadParams{
        private String token;
        private String projectCode;
        private int discussType;
        private int id;

        public LaudForumThreadParams(String token, String projectCode, int discussType, int id) {
            this.token = token;
            this.projectCode = projectCode;
            this.discussType = discussType;
            this.id = id;
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

        @Override
        public String toString() {
            return "LaudForumThreadParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", discussType=" + discussType +
                    ", id=" + id +
                    '}';
        }
    }

    public static class LaudForumThreadResult{
        private int result;

        public LaudForumThreadResult(int result) {
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
            return "LaudForumThreadResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
