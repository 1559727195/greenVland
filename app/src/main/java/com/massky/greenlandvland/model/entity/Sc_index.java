package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2017-09-25.
 */

public class Sc_index {
    private IndexParams indexParams;
    private IndexResult indexResult;

    public Sc_index(IndexParams indexParams, IndexResult indexResult) {
        this.indexParams = indexParams;
        this.indexResult = indexResult;
    }

    public IndexParams getIndexParams() {
        return indexParams;
    }

    public void setIndexParams(IndexParams indexParams) {
        this.indexParams = indexParams;
    }

    public IndexResult getIndexResult() {
        return indexResult;
    }

    public void setIndexResult(IndexResult indexResult) {
        this.indexResult = indexResult;
    }

    @Override
    public String toString() {
        return "Sc_index{" +
                "indexParams=" + indexParams +
                ", indexResult=" + indexResult +
                '}';
    }
    public static class IndexParams {
        private String token;
        private String projectCode;

        public IndexParams(String token, String projectCode) {
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
            return "IndexParams{" +
                    "token='" + token + '\'' +
                    ", projectCode=" + projectCode +
                    '}';
        }
    }

    public static class IndexResult {
        private int result;
        private List<AppMenu> appMenuList;
        private List<AppAd> appAd;

        public IndexResult(int result, List<AppMenu> appMenuList, List<AppAd> appAd) {
            this.result = result;
            this.appMenuList = appMenuList;
            this.appAd = appAd;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<AppMenu> getAppMenuList() {
            return appMenuList;
        }

        public void setAppMenuList(List<AppMenu> appMenuList) {
            this.appMenuList = appMenuList;
        }

        public List<AppAd> getAppAd() {
            return appAd;
        }

        public void setAppAd(List<AppAd> appAd) {
            this.appAd = appAd;
        }

        @Override
        public String toString() {
            return "IndexResult{" +
                    "result=" + result +
                    ", appMenuList=" + appMenuList +
                    ", appAd=" + appAd +
                    '}';
        }
        public static class AppMenu {
            private int id;
            private String menuName;
            private String menuImage;
            private String menuLink;
            private String projectCode;

            public AppMenu(int id, String menuName, String menuImage, String menuLink, String projectCode) {
                this.id = id;
                this.menuName = menuName;
                this.menuImage = menuImage;
                this.menuLink = menuLink;
                this.projectCode = projectCode;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public String getMenuImage() {
                return menuImage;
            }

            public void setMenuImage(String menuImage) {
                this.menuImage = menuImage;
            }

            public String getMenuLink() {
                return menuLink;
            }

            public void setMenuLink(String menuLink) {
                this.menuLink = menuLink;
            }

            public String getProjectCode() {
                return projectCode;
            }

            public void setProjectCode(String projectCode) {
                this.projectCode = projectCode;
            }

            @Override
            public String toString() {
                return "AppMenu{" +
                        "id=" + id +
                        ", menuName='" + menuName + '\'' +
                        ", menuImage='" + menuImage + '\'' +
                        ", menuLink='" + menuLink + '\'' +
                        ", projectCode='" + projectCode + '\'' +
                        '}';
            }
        }

        public static class AppAd {
            private int id;
            private String projectCode;
            private String adName;
            private String adUrl;
            private String adLink;

            public AppAd(int id, String projectCode, String adName, String adUrl, String adLink) {
                this.id = id;
                this.projectCode = projectCode;
                this.adName = adName;
                this.adUrl = adUrl;
                this.adLink = adLink;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getProjectCode() {
                return projectCode;
            }

            public void setProjectCode(String projectCode) {
                this.projectCode = projectCode;
            }

            public String getAdName() {
                return adName;
            }

            public void setAdName(String adName) {
                this.adName = adName;
            }

            public String getAdUrl() {
                return adUrl;
            }

            public void setAdUrl(String adUrl) {
                this.adUrl = adUrl;
            }

            public String getAdLink() {
                return adLink;
            }

            public void setAdLink(String adLink) {
                this.adLink = adLink;
            }

            @Override
            public String toString() {
                return "AppAd{" +
                        "id=" + id +
                        ", projectCode='" + projectCode + '\'' +
                        ", adName='" + adName + '\'' +
                        ", adUrl='" + adUrl + '\'' +
                        ", adLink='" + adLink + '\'' +
                        '}';
            }
        }

    }
}
