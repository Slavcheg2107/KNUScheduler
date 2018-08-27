package geek.owl.com.ua.KNUSchedule.Util.Network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.owl.krasn.KNUSchedule.Repository.FacultyPojo
import com.owl.krasn.KNUSchedule.Repository.SchedulePojo

class FacultyResponse {
    @SerializedName("faculties")
    @Expose
    var data: List<FacultyPojo> = emptyList()
}

class GroupsResponse {
    @SerializedName("groups")
    @Expose
    var groups: List<String> = emptyList()
}

class ScheduleResponse {
    @SerializedName("schedule")
    @Expose
    var schedules: List<SchedulePojo> = emptyList()
}
