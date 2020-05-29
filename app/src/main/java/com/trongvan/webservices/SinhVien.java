package com.trongvan.webservices;

public class SinhVien {
    private int mID;
    private String mName;
    private int mAge;
    private String mAddress;

    public SinhVien(int mID, String mName, int mAge, String mAddress) {
        this.mID = mID;
        this.mName = mName;
        this.mAge = mAge;
        this.mAddress = mAddress;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
