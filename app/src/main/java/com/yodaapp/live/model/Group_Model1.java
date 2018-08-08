package com.yodaapp.live.model;


public class Group_Model1 {

    String id;
    String name;
    String phone;
    Boolean status;
    Boolean isChecked=false;

    public Group_Model1(String id, String name, String phone, Boolean status, Boolean isChecked) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.isChecked = isChecked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
