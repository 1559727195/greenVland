package com.massky.greenlandvland.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-27.
 */

public class Sc_getForumCategory {
    private GetForumCategoryParams getForumCategoryParams;
    private GetForumCategoryResult getForumCategoryResult;

    public Sc_getForumCategory(GetForumCategoryParams getForumCategoryParams, GetForumCategoryResult getForumCategoryResult) {
        this.getForumCategoryParams = getForumCategoryParams;
        this.getForumCategoryResult = getForumCategoryResult;
    }

    public GetForumCategoryParams getGetForumCategoryParams() {
        return getForumCategoryParams;
    }

    public void setGetForumCategoryParams(GetForumCategoryParams getForumCategoryParams) {
        this.getForumCategoryParams = getForumCategoryParams;
    }

    public GetForumCategoryResult getGetForumCategoryResult() {
        return getForumCategoryResult;
    }

    public void setGetForumCategoryResult(GetForumCategoryResult getForumCategoryResult) {
        this.getForumCategoryResult = getForumCategoryResult;
    }

    @Override
    public String toString() {
        return "Sc_getForumCategory{" +
                "getForumCategoryParams=" + getForumCategoryParams +
                ", getForumCategoryResult=" + getForumCategoryResult +
                '}';
    }
    public static class GetForumCategoryParams{
        private String token;
        private String projectCode;

        public GetForumCategoryParams(String token, String projectCode) {
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
            return "GetForumCategoryParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }
    public static class GetForumCategoryResult{
        private int result;
        private List<BbsCategory> bbsCategoryList;

        public GetForumCategoryResult(int result, List<BbsCategory> bbsCategoryList) {
            this.result = result;
            this.bbsCategoryList = bbsCategoryList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<BbsCategory> getBbsCategoryList() {
            return bbsCategoryList;
        }

        public void setBbsCategoryList(List<BbsCategory> bbsCategoryList) {
            this.bbsCategoryList = bbsCategoryList;
        }

        @Override
        public String toString() {
            return "GetForumCategoryResult{" +
                    "result=" + result +
                    ", bbsCategoryList=" + bbsCategoryList +
                    '}';
        }
        public static class BbsCategory implements Serializable {
            private int id;
            private String categoryName;
            private String detail;
            private String imgUrl;

            public BbsCategory(int id, String categoryName, String detail, String imgUrl) {
                this.id = id;
                this.categoryName = categoryName;
                this.detail = detail;
                this.imgUrl = imgUrl;
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

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            @Override
            public String toString() {
                return "BbsCategory{" +
                        "id=" + id +
                        ", categoryName='" + categoryName + '\'' +
                        ", detail='" + detail + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        '}';
            }
        }
    }
}
