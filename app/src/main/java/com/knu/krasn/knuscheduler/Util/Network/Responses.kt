package com.knu.krasn.knuscheduler.Util.Network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.SchedulePojo

class FacultyResponse {
    @SerializedName("faculties")
    @Expose
    var data: List<FacultyPojo> = emptyList()
}

class GroupsResponse {
    @SerializedName("groups")
    @Expose
    var groups: List<GroupPojo> = emptyList()
}

class ScheduleResponse {
    @SerializedName("schedule")
    @Expose
    var schedules: List<SchedulePojo> = emptyList()
}
