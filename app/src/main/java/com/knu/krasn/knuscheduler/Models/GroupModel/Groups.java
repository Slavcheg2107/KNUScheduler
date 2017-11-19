package com.knu.krasn.knuscheduler.Models.GroupModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmModel;

/**
 * Created by krasn on 9/26/2017.
 */

public class Groups implements RealmModel {
    @SerializedName("groups")
    @Expose
    private List<String> groups = null;

    public List<Group> getGroups() {
        List <Group> groupList = new ArrayList<>();
        for(String group: groups){
            groupList.add(new Group(group));
        }
        return groupList;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
