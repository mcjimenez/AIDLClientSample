package com.telefonica.movistarhome.comms.service;

import android.os.Parcel;
import android.os.Parcelable;

public class CommsCall implements Parcelable {
    private int state;
    private String stateText;
    private String local;
    private String remote;
    private boolean isIncoming;
    private int connectedTime;
    private int totalTime;

    public CommsCall(int state,
                       String stateText,
                       String local,
                       String remote,
                       boolean isIncoming,
                       int connectedTime,
                       int totalTime) {
        this.state = state;
        this.stateText = stateText;
        this.local = local;
        this.remote = remote;
        this.isIncoming = isIncoming;
        this.connectedTime = connectedTime;
        this.totalTime = totalTime;
    }

    public int getState() {
        return state;
    }
    public String getStateText() {
        return stateText;
    }
    public String getLocal() {
        return local;
    }
    public String getRemote(){
        return remote;
    }
    public boolean isIncoming() {
        return this.isIncoming;
    }
    public int getConnectedTime() {
        return this.connectedTime;
    }
    public int getTotalTime() {
        return this.totalTime;
    }

    public void setState(int state) {
        this.state = state;
    }
    public void setStateText(String stateText) {
        this.stateText = stateText;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public void setRemote(String remote) {
        this.remote = remote;
    }
    public void setIsIncoming(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }
    public void setConnectedTime(int connectedTime) {
        this.connectedTime = connectedTime;
    }
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(state);
        parcel.writeString(stateText);
        parcel.writeString(local);
        parcel.writeString(remote);
        parcel.writeInt(isIncoming ? 1 : 0);
        parcel.writeInt(connectedTime);
        parcel.writeInt(totalTime);
    }

    public static final Parcelable.Creator<CommsCall> CREATOR = new Parcelable.Creator<CommsCall>() {
        @Override
        public CommsCall createFromParcel(Parcel source) {
            return new CommsCall(source.readInt(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readInt() == 1,
                    source.readInt(),
                    source.readInt());
        }

        @Override
        public CommsCall[] newArray(int size) {
            return new CommsCall[size];
        }
    };
}
