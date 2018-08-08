package com.yodaapp.live.model;

/**
 * Created by pf-05 on 8/26/2016.
 */
public class Group_Message_Receive_Model {

    String id="";
    String group_id="";
    String sent_by="";
    String sent_by_id="";
    String message="";
    String groupName="";
    String userName="";
    String created="";

    public String getSent_by_id() {
        return sent_by_id;
    }

    public void setSent_by_id(String sent_by_id) {
        this.sent_by_id = sent_by_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
