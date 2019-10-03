/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */
package com.mobvoi.android.common.ota;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * OTA更新数据结构实体类
 * Created by kal on 2015-03-28
 */
public class UpdateInfo implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;
    public String app;
    public String changelog;
    public String channel;
    public String compatibility;
    public int counter;
    public String created_at;
    public String description;
    public String diff_from;
    public boolean enabled;
    public boolean force_update;
    public String md5;
    public int number;
    public int size;
    public String upload_status;
    public String url;
    public boolean valid;
    public String version;

    public UpdateInfo() {
        // Use the builder
    }

    private UpdateInfo(Parcel in) {
        readFromParcel(in);
    }


    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
        public UpdateInfo createFromParcel(Parcel in) {
            return new UpdateInfo(in);
        }

        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };

    public static UpdateInfo parseFromJSONObject(JSONObject jsonObject) {
        UpdateInfo info = new UpdateInfo();
        try {
            info.app = jsonObject.getString("app");
            info.changelog = jsonObject.getString("changelog");
            info.channel = jsonObject.getString("channel");
            info.compatibility = jsonObject.getString("compatibility");
            info.counter = jsonObject.getInt("counter");
            info.created_at = jsonObject.getString("created_at");
            info.description = jsonObject.getString("description");
            info.diff_from = jsonObject.getString("diff_from");
            info.enabled = jsonObject.getBoolean("enabled");
            info.force_update = jsonObject.getBoolean("force_update");
            info.md5 = jsonObject.getString("md5");
            info.number = jsonObject.getInt("number");
            info.size = jsonObject.getInt("size");
            info.upload_status = jsonObject.getString("upload_status");
            info.url = jsonObject.getString("url");
            info.valid = jsonObject.getBoolean("valid");
            info.version = jsonObject.getString("version");
        } catch (Exception e) {
            Log.e("UpdateInfo", e.getMessage(), e);
            info = null;
        }
        return info;
    }

    public JSONObject serializeToJsonObject() {
        try {
            JSONObject info = new JSONObject();
            info.put("app", app);
            info.put("changelog", changelog);
            info.put("channel", channel);
            info.put("compatibility", compatibility);
            info.put("counter", counter);
            info.put("created_at", created_at);
            info.put("description", description);
            info.put("diff_from", diff_from);
            info.put("enabled", enabled);
            info.put("force_update", force_update);
            info.put("md5", md5);
            info.put("number", number);
            info.put("size", size);
            info.put("upload_status", upload_status);
            info.put("url", url);
            info.put("valid", valid);
            info.put("version", version);
            return info;
        } catch (Exception e) {
            Log.e("UpdateInfo", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(app);
        out.writeString(changelog);
        out.writeString(channel);
        out.writeString(compatibility);
        out.writeInt(counter);
        out.writeString(created_at);
        out.writeString(description);
        out.writeString(diff_from);
        out.writeInt(enabled ? 1 : 0);
        out.writeInt(force_update ? 1 : 0);
        out.writeString(md5);
        out.writeInt(number);
        out.writeInt(size);
        out.writeString(upload_status);
        out.writeString(url);
        out.writeInt(valid ? 1 : 0);
        out.writeString(version);
    }

    private void readFromParcel(Parcel in) {
        app = in.readString();
        changelog = in.readString();
        channel = in.readString();
        compatibility = in.readString();
        counter = in.readInt();
        created_at = in.readString();
        description = in.readString();
        diff_from = in.readString();
        enabled = (in.readInt() == 1);
        force_update = (in.readInt() == 1);
        md5 = in.readString();
        number = in.readInt();
        size = in.readInt();
        upload_status = in.readString();
        url = in.readString();
        valid = (in.readInt() == 1);
        version = in.readString();
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "app='" + app + '\'' +
                ", changelog='" + changelog + '\'' +
                ", channel='" + channel + '\'' +
                ", compatibility='" + compatibility + '\'' +
                ", counter=" + counter +
                ", created_at='" + created_at + '\'' +
                ", description='" + description + '\'' +
                ", diff_from='" + diff_from + '\'' +
                ", enabled=" + enabled +
                ", force_update=" + force_update +
                ", md5='" + md5 + '\'' +
                ", number=" + number +
                ", size=" + size +
                ", upload_status='" + upload_status + '\'' +
                ", url='" + url + '\'' +
                ", valid=" + valid +
                ", version='" + version + '\'' +
                '}';
    }
}
