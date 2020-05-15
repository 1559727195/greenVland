package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-06-20.
 */

public class Sc_deleteForum {
    private DeleteForumParams deleteForumParams;
    private DeleteForumResult deleteForumResult;

    public Sc_deleteForum(DeleteForumParams deleteForumParams, DeleteForumResult deleteForumResult) {
        this.deleteForumParams = deleteForumParams;
        this.deleteForumResult = deleteForumResult;
    }

    public DeleteForumParams getDeleteForumParams() {
        return deleteForumParams;
    }

    public void setDeleteForumParams(DeleteForumParams deleteForumParams) {
        this.deleteForumParams = deleteForumParams;
    }

    public DeleteForumResult getDeleteForumResult() {
        return deleteForumResult;
    }

    public void setDeleteForumResult(DeleteForumResult deleteForumResult) {
        this.deleteForumResult = deleteForumResult;
    }

    @Override
    public String toString() {
        return "Sc_deleteForum{" +
                "deleteForumParams=" + deleteForumParams +
                ", deleteForumResult=" + deleteForumResult +
                '}';
    }

    public static class DeleteForumParams{
        private String token;
        private String projectCode;
        private int forumId;

        public DeleteForumParams(String token, String projectCode, int forumId) {
            this.token = token;
            this.projectCode = projectCode;
            this.forumId = forumId;
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

        @Override
        public String toString() {
            return "DeleteForumParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", forumId='" + forumId + '\'' +
                    '}';
        }
    }
    public static class DeleteForumResult{
        private int result;

        public DeleteForumResult(int result) {
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
            return "DeleteForumResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
