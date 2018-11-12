package com.telefonica.movistarhome.comms.service;

import android.os.Parcel;
import android.os.Parcelable;

public class CommsLastRegisterState implements Parcelable {
    //status : String (0 error, 1 success)
    //id : String (accountId)
    //error : String (pjsip_status_code)
    //connectionDuration: Long (de momento será timestamp de la última vez que se produjo un registro)

    private String id;
    private String status;
    private String error;
    private long lastUpdate;

    public CommsLastRegisterState(String id,
                     String status,
                     String error,
                     long lastUpdate) {
        this.id = id;
        this.status = status;
        this.error = error;
        this.lastUpdate = lastUpdate;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(status);
        parcel.writeString(error);
        parcel.writeLong(lastUpdate);
    }

    public static final Parcelable.Creator<CommsLastRegisterState> CREATOR = new Parcelable.Creator<CommsLastRegisterState>() {
        @Override
        public CommsLastRegisterState createFromParcel(Parcel source) {
            return new CommsLastRegisterState(source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readLong());
        }

        @Override
        public CommsLastRegisterState[] newArray(int size) {
            return new CommsLastRegisterState[size];
        }
    };
}

