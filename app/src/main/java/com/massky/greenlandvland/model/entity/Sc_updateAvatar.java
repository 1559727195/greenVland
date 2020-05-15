package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-09-26.
 */

public class Sc_updateAvatar {
    private UpdateAvatarParams updateAvatarParams;
    private UpdateAvatarResult updateAvatarResult;

    public Sc_updateAvatar(UpdateAvatarParams updateAvatarParams, UpdateAvatarResult updateAvatarResult) {
        this.updateAvatarParams = updateAvatarParams;
        this.updateAvatarResult = updateAvatarResult;
    }

    public UpdateAvatarParams getUpdateAvatarParams() {
        return updateAvatarParams;
    }

    public void setUpdateAvatarParams(UpdateAvatarParams updateAvatarParams) {
        this.updateAvatarParams = updateAvatarParams;
    }

    public UpdateAvatarResult getUpdateAvatarResult() {
        return updateAvatarResult;
    }

    public void setUpdateAvatarResult(UpdateAvatarResult updateAvatarResult) {
        this.updateAvatarResult = updateAvatarResult;
    }

    @Override
    public String toString() {
        return "Sc_updateAvatar{" +
                "updateAvatarParams=" + updateAvatarParams +
                ", updateAvatarResult=" + updateAvatarResult +
                '}';
    }

    public static class UpdateAvatarParams{
        private String token;
        private String projectCode;
        private String avatar;

        public UpdateAvatarParams(String token, String projectCode, String avatar) {
            this.token = token;
            this.projectCode = projectCode;
            this.avatar = avatar;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public String toString() {
            return "UpdateAvatarParams{" +
                    "token='" + token + '\'' +
                    ", projectCode=" + projectCode +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
    }

    public static class UpdateAvatarResult{
        private int result;

        public UpdateAvatarResult(int result) {
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
            return "UpdateAvatarResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
