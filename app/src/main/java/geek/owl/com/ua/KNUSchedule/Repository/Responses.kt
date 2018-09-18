package geek.owl.com.ua.KNUSchedule.Repository

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo


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
