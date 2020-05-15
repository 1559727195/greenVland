package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2019-01-23.
 */

public class Sc_myAccount {
    private MyAccountParams myAccountParams;
    private MyAccountResult myAccountResult;

    public Sc_myAccount(MyAccountParams myAccountParams, MyAccountResult myAccountResult) {
        this.myAccountParams = myAccountParams;
        this.myAccountResult = myAccountResult;
    }

    public MyAccountParams getMyAccountParams() {
        return myAccountParams;
    }

    public void setMyAccountParams(MyAccountParams myAccountParams) {
        this.myAccountParams = myAccountParams;
    }

    public MyAccountResult getMyAccountResult() {
        return myAccountResult;
    }

    public void setMyAccountResult(MyAccountResult myAccountResult) {
        this.myAccountResult = myAccountResult;
    }

    @Override
    public String toString() {
        return "Sc_myAccount{" +
                "myAccountParams=" + myAccountParams +
                ", myAccountResult=" + myAccountResult +
                '}';
    }

    public static class MyAccountParams{
        private String token;
        private String projectCode;
        private String roomNo;

        public MyAccountParams(String token, String projectCode, String roomNo) {
            this.token = token;
            this.projectCode = projectCode;
            this.roomNo = roomNo;
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

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return "MyAccountParams{" +
                    "token='" + token + '\'' +
                    ", projectCode='" + projectCode + '\'' +
                    ", roomNo='" + roomNo + '\'' +
                    '}';
        }
    }

    public static class MyAccountResult{
        private int result;
        private AccountInfo accountInfo;

        public MyAccountResult(int result, AccountInfo accountInfo) {
            this.result = result;
            this.accountInfo = accountInfo;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public AccountInfo getAccountInfo() {
            return accountInfo;
        }

        public void setAccountInfo(AccountInfo accountInfo) {
            this.accountInfo = accountInfo;
        }

        @Override
        public String toString() {
            return "MyAccountResult{" +
                    "result=" + result +
                    ", accountInfo=" + accountInfo +
                    '}';
        }

        public static class AccountInfo{
            private String userName;
            private String nickName;
            private String avatar;
            private String gender;
            private String birthday;
            private String mobilePhone;
            private String address;
            private int family;

            public AccountInfo(String userName, String nickName, String avatar, String gender, String birthday, String mobilePhone, String address, int family) {
                this.userName = userName;
                this.nickName = nickName;
                this.avatar = avatar;
                this.gender = gender;
                this.birthday = birthday;
                this.mobilePhone = mobilePhone;
                this.address = address;
                this.family = family;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getFamily() {
                return family;
            }

            public void setFamily(int family) {
                this.family = family;
            }

            @Override
            public String toString() {
                return "AccountInfo{" +
                        "userName='" + userName + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", gender='" + gender + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", mobilePhone='" + mobilePhone + '\'' +
                        ", address='" + address + '\'' +
                        ", family=" + family +
                        '}';
            }
        }
    }
}
