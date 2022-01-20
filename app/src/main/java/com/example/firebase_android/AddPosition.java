package com.example.firebase_android;

public class AddPosition {
    String item_name;
    String additional_info;
    String phone_number;
    String notificationDate;
    String createDate;
    String photo_path;
    String tableId;
    String status;
    int uniqueId;
    boolean phone;
    boolean popup;
    public AddPosition(){

    }

    public AddPosition(String item_name, String additional_info,
                       String phone_number, String notificationDate,
                       boolean phone, boolean popup, String createDate,
                       String photo_path, String tableId, String status, int uniqueId) {
        this.item_name = item_name;
        this.additional_info = additional_info;
        this.phone_number = phone_number;
        this.notificationDate = notificationDate;
        this.phone = phone;
        this.popup = popup;
        this.createDate = createDate;
        this.photo_path = photo_path;
        this.tableId = tableId;
        this.status = status;
        this.uniqueId = uniqueId;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
