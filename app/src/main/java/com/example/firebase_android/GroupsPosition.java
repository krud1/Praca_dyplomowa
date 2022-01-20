package com.example.firebase_android;

import android.net.Uri;

public class GroupsPosition {
    String item_name;
    String additional_info;
    String path;
    Uri photo_uri;
    String tableId;
    String status;
    String notificationDate;

    public GroupsPosition(){

    }

    public GroupsPosition(String item_name, String additional_info, Uri photo_uri, String tableId, String status, String notificationDate) {
        this.item_name = item_name;
        this.additional_info = additional_info;
        this.photo_uri = photo_uri;
        this.tableId = tableId;
        this.status = status;
        this.notificationDate = notificationDate;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public Uri getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(Uri photo_uri) {
        this.photo_uri = photo_uri;
    }

    public Uri getUri(){

        return photo_uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTableId() { return tableId;}

    public void setTableId(String tableId) { this.tableId = tableId; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
