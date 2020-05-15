package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-21.
 */

public class Sc_submitRepair {
    private SubmitRepairParams submitRepairParams;
    private SubmitRepairResult submitRepairResult;

    public Sc_submitRepair(SubmitRepairParams submitRepairParams, SubmitRepairResult submitRepairResult) {
        this.submitRepairParams = submitRepairParams;
        this.submitRepairResult = submitRepairResult;
    }

    public SubmitRepairParams getSubmitRepairParams() {
        return submitRepairParams;
    }

    public void setSubmitRepairParams(SubmitRepairParams submitRepairParams) {
        this.submitRepairParams = submitRepairParams;
    }

    public SubmitRepairResult getSubmitRepairResult() {
        return submitRepairResult;
    }

    public void setSubmitRepairResult(SubmitRepairResult submitRepairResult) {
        this.submitRepairResult = submitRepairResult;
    }

    @Override
    public String toString() {
        return "Sc_submitRepair{" +
                "submitRepairParams=" + submitRepairParams +
                ", submitRepairResult=" + submitRepairResult +
                '}';
    }

    public static class SubmitRepairParams{
        private String token;
        private String projectCode;
        private String repairTitle;
        private String repairCategory;
        private String repairContent;
        private String repairImage;

        public SubmitRepairParams(String token, String projectCode, String repairTitle, String repairCategory, String repairContent, String repairImage) {
            this.token = token;
            this.projectCode = projectCode;
            this.repairTitle = repairTitle;
            this.repairCategory = repairCategory;
            this.repairContent = repairContent;
            this.repairImage = repairImage;
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

        public String getRepairTitle() {
            return repairTitle;
        }

        public void setRepairTitle(String repairTitle) {
            this.repairTitle = repairTitle;
        }

        public String getRepairCategory() {
            return repairCategory;
        }

        public void setRepairCategory(String repairCategory) {
            this.repairCategory = repairCategory;
        }

        public String getRepairContent() {
            return repairContent;
        }

        public void setRepairContent(String repairContent) {
            this.repairContent = repairContent;
        }

        public String getRepairImage() {
            return repairImage;
        }

        public void setRepairImage(String repairImage) {
            this.repairImage = repairImage;
        }

        @Override
        public String toString() {
            return "SubmitRepairParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", repairTitle='" + repairTitle + '\'' +
                    ", repairCategory='" + repairCategory + '\'' +
                    ", repairContent='" + repairContent + '\'' +
                    ", repairImage='" + repairImage + '\'' +
                    '}';
        }
    }
    public static class SubmitRepairResult{
        private int result;

        public SubmitRepairResult(int result) {
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
            return "SubmitRepairResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
