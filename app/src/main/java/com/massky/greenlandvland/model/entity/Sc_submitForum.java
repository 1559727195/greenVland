package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-06-19.
 */

public class Sc_submitForum {
    private SubmitForumParam submitForumParam;
    private SubmitForumResult submitForumResult;

    public Sc_submitForum(SubmitForumParam submitForumParam, SubmitForumResult submitForumResult) {
        this.submitForumParam = submitForumParam;
        this.submitForumResult = submitForumResult;
    }

    public SubmitForumParam getSubmitForumParam() {
        return submitForumParam;
    }

    public void setSubmitForumParam(SubmitForumParam submitForumParam) {
        this.submitForumParam = submitForumParam;
    }

    public SubmitForumResult getSubmitForumResult() {
        return submitForumResult;
    }

    public void setSubmitForumResult(SubmitForumResult submitForumResult) {
        this.submitForumResult = submitForumResult;
    }

    @Override
    public String toString() {
        return "Sc_submitForum{" +
                "submitForumParam=" + submitForumParam +
                ", submitForumResult=" + submitForumResult +
                '}';
    }

    public static class SubmitForumParam{
        private String token;
        private String projectCode;
        private String forumTitle;
        private String forumCategory;
        private String forumContent;
        private List<ForumImage> forumImage;
        private String roomNo;

        public SubmitForumParam(String token, String projectCode, String forumTitle, String forumCategory, String forumContent, List<ForumImage> forumImage, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.forumTitle = forumTitle;
            this.forumCategory = forumCategory;
            this.forumContent = forumContent;
            this.forumImage = forumImage;
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

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "SubmitForumParam{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", forumTitle='" + forumTitle + '\'' +
                    ", forumCategory='" + forumCategory + '\'' +
                    ", forumContent='" + forumContent + '\'' +
                    ", forumImage=" + forumImage +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class ForumImage {
            private String imageBase64;

            public ForumImage(String imageBase64) {
                this.imageBase64 = imageBase64;
            }

            public String getImageBase64() {
                return imageBase64;
            }

            public void setImageBase64(String imageBase64) {
                this.imageBase64 = imageBase64;
            }

            @Override
            public String toString() {
                return "ForumImage{" +
                        "imageBase64='" + imageBase64 + '\'' +
                        '}';
            }
        }
    }

    public static class SubmitForumResult{
        private int result;

        public SubmitForumResult(int result) {
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
            return "SubmitForumResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
