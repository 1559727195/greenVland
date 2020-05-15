package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-11-14.
 */

public class Sc_sceneControl {
    private SceneControlParams sceneControlParams;
    private SceneControlResult sceneControlResult;

    public Sc_sceneControl(SceneControlParams sceneControlParams, SceneControlResult sceneControlResult) {
        this.sceneControlParams = sceneControlParams;
        this.sceneControlResult = sceneControlResult;
    }

    public SceneControlParams getSceneControlParams() {
        return sceneControlParams;
    }

    public void setSceneControlParams(SceneControlParams sceneControlParams) {
        this.sceneControlParams = sceneControlParams;
    }

    public SceneControlResult getSceneControlResult() {
        return sceneControlResult;
    }

    public void setSceneControlResult(SceneControlResult sceneControlResult) {
        this.sceneControlResult = sceneControlResult;
    }

    @Override
    public String toString() {
        return "Sc_sceneControl{" +
                "sceneControlParams=" + sceneControlParams +
                ", sceneControlResult=" + sceneControlResult +
                '}';
    }
    public static class SceneControlParams{
        private String token;
        private String projectCode;
        private String boxNumber;
        private String sceneName;

        public SceneControlParams(String token, String projectCode, String boxNumber, String sceneName) {
            this.token = token;
            this.projectCode = projectCode;
            this.boxNumber = boxNumber;
            this.sceneName = sceneName;
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

        public String getBoxNumber() {
            return boxNumber;
        }

        public void setBoxNumber(String boxNumber) {
            this.boxNumber = boxNumber;
        }

        public String getSceneName() {
            return sceneName;
        }

        public void setSceneName(String sceneName) {
            this.sceneName = sceneName;
        }

        @Override
        public String toString() {
            return "SceneControlParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", boxNumber='" + boxNumber + '\'' +
                    ", sceneName='" + sceneName + '\'' +
                    '}';
        }
    }
    public static class SceneControlResult{
        private int result;

        public SceneControlResult(int result) {
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
            return "SceneControlResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
