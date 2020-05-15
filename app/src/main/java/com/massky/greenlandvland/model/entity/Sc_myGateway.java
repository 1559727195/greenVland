package com.massky.greenlandvland.model.entity;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class Sc_myGateway {
    private MyGatewayParams myGatewayParams;
    private MyGateResult myGateResult;

    public Sc_myGateway(MyGatewayParams myGatewayParams, MyGateResult myGateResult) {
        this.myGatewayParams = myGatewayParams;
        this.myGateResult = myGateResult;
    }

    public MyGatewayParams getMyGatewayParams() {
        return myGatewayParams;
    }

    public void setMyGatewayParams(MyGatewayParams myGatewayParams) {
        this.myGatewayParams = myGatewayParams;
    }

    public MyGateResult getMyGateResult() {
        return myGateResult;
    }

    public void setMyGateResult(MyGateResult myGateResult) {
        this.myGateResult = myGateResult;
    }

    @Override
    public String toString() {
        return "Sc_myGateway{" +
                "myGatewayParams=" + myGatewayParams +
                ", myGateResult=" + myGateResult +
                '}';
    }

    public static class MyGatewayParams{
        private String token;
        private String projectCode;

        public MyGatewayParams(String token, String projectCode) {
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
            return "MyGatewayParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    '}';
        }
    }
    public static class MyGateResult{
        private int result;
        private String boxNumber;

        public MyGateResult(int result, String boxNumber) {
            this.result = result;
            this.boxNumber = boxNumber;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getBoxNumber() {
            return boxNumber;
        }

        public void setBoxNumber(String boxNumber) {
            this.boxNumber = boxNumber;
        }

        @Override
        public String toString() {
            return "MyGateResult{" +
                    "result=" + result +
                    ", boxNumber='" + boxNumber + '\'' +
                    '}';
        }
    }
}
