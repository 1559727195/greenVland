package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-09-26.
 */

public class Sc_updateAccount {
    private UpdateAccountParams updateAccountParams;
    private UpdateAccountResult updateAccountResult;

    public Sc_updateAccount(UpdateAccountParams updateAccountParams, UpdateAccountResult updateAccountResult) {
        this.updateAccountParams = updateAccountParams;
        this.updateAccountResult = updateAccountResult;
    }

    public UpdateAccountParams getUpdateAccountParams() {
        return updateAccountParams;
    }

    public void setUpdateAccountParams(UpdateAccountParams updateAccountParams) {
        this.updateAccountParams = updateAccountParams;
    }

    public UpdateAccountResult getUpdateAccountResult() {
        return updateAccountResult;
    }

    public void setUpdateAccountResult(UpdateAccountResult updateAccountResult) {
        this.updateAccountResult = updateAccountResult;
    }

    @Override
    public String toString() {
        return "Sc_updateAccount{" +
                "updateAccountParams=" + updateAccountParams +
                ", updateAccountResult=" + updateAccountResult +
                '}';
    }

    public static class UpdateAccountParams {
        private String token;
        private String projectCode;
        private AccountInfo accountInfo;

        public UpdateAccountParams(String token, String projectCode, AccountInfo accountInfo) {
            this.token = token;
            this.projectCode = projectCode;
            this.accountInfo = accountInfo;
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

        public AccountInfo getAccountInfo() {
            return accountInfo;
        }

        public void setAccountInfo(AccountInfo accountInfo) {
            this.accountInfo = accountInfo;
        }

        @Override
        public String toString() {
            return "UpdateAccountParams{" +
                    "token='" + token + '\'' +
                    ", projectCode=" + projectCode +
                    ", accountInfo=" + accountInfo +
                    '}';
        }

        public static class AccountInfo{
            private String realName;
            private String gender;
            private String birthDay;
            private String address;
            private String mobilePhone;

            public AccountInfo(String realName, String gender, String birthDay, String address, String mobilePhone) {
                this.realName = realName;
                this.gender = gender;
                this.birthDay = birthDay;
                this.address = address;
                this.mobilePhone = mobilePhone;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBirthDay() {
                return birthDay;
            }

            public void setBirthDay(String birthDay) {
                this.birthDay = birthDay;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            @Override
            public String toString() {
                return "AccountInfo{" +
                        "realName='" + realName + '\'' +
                        ", gender='" + gender + '\'' +
                        ", birthDay='" + birthDay + '\'' +
                        ", address='" + address + '\'' +
                        ", mobilePhone='" + mobilePhone + '\'' +
                        '}';
            }
        }
    }

    public static class UpdateAccountResult{
        private int result;

        public UpdateAccountResult(int result) {
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
            return "UpdateAccountResult{" +
                    "result=" + result +
                    '}';
        }
    }
}
