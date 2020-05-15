package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-11-28.
 */

public class Sc_complaintCategory {
    private ComplaintCategoryParams complaintCategoryParams;
    private ComplaintCategoryResult complaintCategoryResult;

    public Sc_complaintCategory(ComplaintCategoryParams complaintCategoryParams, ComplaintCategoryResult complaintCategoryResult) {
        this.complaintCategoryParams = complaintCategoryParams;
        this.complaintCategoryResult = complaintCategoryResult;
    }

    public ComplaintCategoryParams getComplaintCategoryParams() {
        return complaintCategoryParams;
    }

    public void setComplaintCategoryParams(ComplaintCategoryParams complaintCategoryParams) {
        this.complaintCategoryParams = complaintCategoryParams;
    }

    public ComplaintCategoryResult getComplaintCategoryResult() {
        return complaintCategoryResult;
    }

    public void setComplaintCategoryResult(ComplaintCategoryResult complaintCategoryResult) {
        this.complaintCategoryResult = complaintCategoryResult;
    }

    @Override
    public String toString() {
        return "Sc_complaintCategory{" +
                "complaintCategoryParams=" + complaintCategoryParams +
                ", complaintCategoryResult=" + complaintCategoryResult +
                '}';
    }
    public static class ComplaintCategoryParams{
        private String token;
        private String projectCode;

        public ComplaintCategoryParams(String token, String projectCode) {
            this.token = token;
            this.projectCode = projectCode;
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

        @Override
        public String toString() {
            return "ComplaintCategoryParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }
    public static class ComplaintCategoryResult{
        private int result;
        private List<ComplaintCategory> complaintCategoryList;
        private String propertyPhone;

        public ComplaintCategoryResult(int result, List<ComplaintCategory> complaintCategoryList, String propertyPhone) {
            this.result = result;
            this.complaintCategoryList = complaintCategoryList;
            this.propertyPhone = propertyPhone;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<ComplaintCategory> getComplaintCategoryList() {
            return complaintCategoryList;
        }

        public void setComplaintCategoryList(List<ComplaintCategory> complaintCategoryList) {
            this.complaintCategoryList = complaintCategoryList;
        }

        public String getPropertyPhone() {
            return propertyPhone;
        }

        public void setPropertyPhone(String propertyPhone) {
            this.propertyPhone = propertyPhone;
        }

        @Override
        public String toString() {
            return "ComplaintCategoryResult{" +
                    "result=" + result +
                    ", complaintCategoryList=" + complaintCategoryList +
                    ", propertyPhone='" + propertyPhone + '\'' +
                    '}';
        }

        public static class ComplaintCategory{
            private int id;
            private String categoryName;

            public ComplaintCategory(int id, String categoryName) {
                this.id = id;
                this.categoryName = categoryName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            @Override
            public String toString() {
                return "ComplaintCategory{" +
                        "id=" + id +
                        ", categoryName='" + categoryName + '\'' +
                        '}';
            }
        }
    }
}
