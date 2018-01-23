package com.knu.krasn.knuscheduler.Presenter.Events;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;

import java.util.List;

/**
 * Created by krasn on 9/4/2017.
 */

public class GettingGroupsEvent {
    List<Group> groups;

    public GettingGroupsEvent(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
