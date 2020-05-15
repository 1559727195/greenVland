package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-05-21.
 */

public class Sc_repairCategory {
    private RepairCategoryParams repairCategoryParams;
    private RepairCategoryResult repairCategoryResult;

    public Sc_repairCategory(RepairCategoryParams repairCategoryParams, RepairCategoryResult repairCategoryResult) {
        this.repairCategoryParams = repairCategoryParams;
        this.repairCategoryResult = repairCategoryResult;
    }

    public RepairCategoryParams getRepairCategoryParams() {
        return repairCategoryParams;
    }

    public void setRepairCategoryParams(RepairCategoryParams repairCategoryParams) {
        this.repairCategoryParams = repairCategoryParams;
    }

    public RepairCategoryResult getRepairCategoryResult() {
        return repairCategoryResult;
    }

    public void setRepairCategoryResult(RepairCategoryResult repairCategoryResult) {
        this.repairCategoryResult = repairCategoryResult;
    }

    @Override
    public String toString() {
        return "Sc_repairCategory{" +
                "repairCategoryParams=" + repairCategoryParams +
                ", repairCategoryResult=" + repairCategoryResult +
                '}';
    }

    public static class RepairCategoryParams{
        private String token;
        private String projectCode;

        public RepairCategoryParams(String token, String projectCode) {
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
            return "RepairCategoryParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }

    public static class RepairCategoryResult{
        private int result;
        private List<RepairCategory> repairCategoryList;

        public RepairCategoryResult(int result, List<RepairCategory> repairCategoryList) {
            this.result = result;
            this.repairCategoryList = repairCategoryList;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<RepairCategory> getRepairCategoryList() {
            return repairCategoryList;
        }

        public void setRepairCategoryList(List<RepairCategory> repairCategoryList) {
            this.repairCategoryList = repairCategoryList;
        }

        @Override
        public String toString() {
            return "RepairCategoryResult{" +
                    "result=" + result +
                    ", repairCategoryList=" + repairCategoryList +
                    '}';
        }

        public static class RepairCategory{
            private String repairCategory;

            public RepairCategory(String repairCategory) {
                this.repairCategory = repairCategory;
            }

            public String getRepairCategory() {
                return repairCategory;
            }

            public void setRepairCategory(String repairCategory) {
                this.repairCategory = repairCategory;
            }

            @Override
            public String toString() {
                return "RepairCategory{" +
                        "repairCategory='" + repairCategory + '\'' +
                        '}';
            }
        }
    }
}
