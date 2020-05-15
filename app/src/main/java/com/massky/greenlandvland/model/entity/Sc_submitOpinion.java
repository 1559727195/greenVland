package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2018-05-17.
 */

public class Sc_submitOpinion {
    private SubmitOptionParams submitOptionParams;
    private SubmitOptionResult submitOptionResult;

    public Sc_submitOpinion(SubmitOptionParams submitOptionParams, SubmitOptionResult submitOptionResult) {
        this.submitOptionParams = submitOptionParams;
        this.submitOptionResult = submitOptionResult;
    }

    public SubmitOptionParams getSubmitOptionParams() {
        return submitOptionParams;
    }

    public void setSubmitOptionParams(SubmitOptionParams submitOptionParams) {
        this.submitOptionParams = submitOptionParams;
    }

    public SubmitOptionResult getSubmitOptionResult() {
        return submitOptionResult;
    }

    public void setSubmitOptionResult(SubmitOptionResult submitOptionResult) {
        this.submitOptionResult = submitOptionResult;
    }

    @Override
    public String toString() {
        return "Sc_submitOpinion{" +
                "submitOptionParams=" + submitOptionParams +
                ", submitOptionResult=" + submitOptionResult +
                '}';
    }

    public static class SubmitOptionParams{
        private String token;
        private String projectCode;
        private String opinion;

        public SubmitOptionParams(String token, String projectCode, String opinion) {
            this.token = token;
            this.projectCode = projectCode;
            this.opinion = opinion;
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

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        @Override
        public String toString() {
            return "SubmitOptionParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", opinion='" + opinion + '\'' +
                    '}';
        }
    }
    public static class SubmitOptionResult{
        private int result;

        public SubmitOptionResult(int result) {
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
            return "SubmitOptionResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
