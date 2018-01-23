package com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by krasn on 9/3/2017.
 */

public class Faculty extends RealmObject {
    private RealmList<Group> groups;
    private String name;
    private String id;

    public Faculty() {
    }

    public Faculty(String id, String name, List<Group> groups) {
        this.name = name;
        this.id = id;
        this.groups.addAll(groups);
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

    public List<Group> getGroups() {
        return groups == null ? new RealmList<>() : groups;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public void setGroups(List<Group> groups) {
        this.groups.addAll(groups);
    }

    public void setName(String name) {
        this.name = name;
    }
}
