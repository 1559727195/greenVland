package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-03-12.
 */

public class Sc_submitComplaint {
    private SubmitComplaintParams submitComplaintParams;
    private SubmitComplaintResult submitComplaintResult;

    public Sc_submitComplaint(SubmitComplaintParams submitComplaintParams, SubmitComplaintResult submitComplaintResult) {
        this.submitComplaintParams = submitComplaintParams;
        this.submitComplaintResult = submitComplaintResult;
    }

    public SubmitComplaintParams getSubmitComplaintParams() {
        return submitComplaintParams;
    }

    public void setSubmitComplaintParams(SubmitComplaintParams submitComplaintParams) {
        this.submitComplaintParams = submitComplaintParams;
    }

    public SubmitComplaintResult getSubmitComplaintResult() {
        return submitComplaintResult;
    }

    public void setSubmitComplaintResult(SubmitComplaintResult submitComplaintResult) {
        this.submitComplaintResult = submitComplaintResult;
    }

    @Override
    public String toString() {
        return "Sc_submitComplaint{" +
                "submitComplaintParams=" + submitComplaintParams +
                ", submitComplaintResult=" + submitComplaintResult +
                '}';
    }

    public static class SubmitComplaintParams{
        private String token;
        private String projectCode;
        private String complaintTitle;
        private String complaintCategory;
        private String complaintContent;
        private List<ComplaintImage> complaintImageList;
        private String roomNo;

        public SubmitComplaintParams(String token, String projectCode, String complaintTitle, String complaintCategory, String complaintContent, List<ComplaintImage> complaintImageList, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.complaintTitle = complaintTitle;
            this.complaintCategory = complaintCategory;
            this.complaintContent = complaintContent;
            this.complaintImageList = complaintImageList;
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

        public List<ComplaintImage> getComplaintImageList() {
            return complaintImageList;
        }

        public void setComplaintImageList(List<ComplaintImage> complaintImageList) {
            this.complaintImageList = complaintImageList;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "SubmitComplaintParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", complaintTitle='" + complaintTitle + '\'' +
                    ", complaintCategory='" + complaintCategory + '\'' +
                    ", complaintContent='" + complaintContent + '\'' +
                    ", complaintImageList=" + complaintImageList +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }

        public static class ComplaintImage{
            private String imageBase64;

            public ComplaintImage(String imageBase64) {
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
                return "ComplaintImage{" +
                        "imageBase64='" + imageBase64 + '\'' +
                        '}';
            }
        }
    }

    public static class SubmitComplaintResult{
        private int result;

        public SubmitComplaintResult(int result) {
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
            return "SubmitComplaintResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
