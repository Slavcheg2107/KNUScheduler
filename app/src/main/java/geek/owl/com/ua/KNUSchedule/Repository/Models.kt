package geek.owl.com.ua.KNUSchedule.Repository

import android.arch.persistence.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class FacultyResponse(var faculties :List<Faculty>)
@Entity
class Faculty(var id : Long, var title:String): Item {
    override fun getItemViewType(): ViewType = ViewType.FACULTY
}

class GroupResponse(var groups: List<Group>)

@Entity
class Group(var id: Long, var title: String) : Item {
    override fun getItemViewType(): ViewType = ViewType.GROUP
}

class ScheduleResponse(var scheduleList: List<Schedule>){
}
@Entity
class Schedule : Item{
    override fun getItemViewType(): ViewType {
        return ViewType.SCHEDULE
    }

    @SerializedName("teachers")
    @Expose
    private val teachers: String? = null
    @SerializedName("day")
    @Expose
    private val day: Int? = null
    @SerializedName("room")
    @Expose
    private val room: String? = null
    @SerializedName("discipline")
    @Expose
    private val discipline: String? = null
    @SerializedName("group")
    @Expose
    private val group: String? = null
    @SerializedName("week")
    @Expose
    private val week: Int? = null
    @SerializedName("lesson")
    @Expose
    private val lesson: Int? = null
    @SerializedName("lessontype")
    @Expose
    private val lessontype: String? = null
    @SerializedName("subgroup")
    @Expose
    private val subgroup: String? = null
    @SerializedName("class_begin")
    @Expose
    private val beginTime: String? = null
    @SerializedName("class_end")
    @Expose
    private val endTime: String? = null
    @SerializedName("corps")
    @Expose
    private val corps: String? = null

}
interface Item{
    fun getItemViewType(): ViewType
}
enum class ViewType{
    FACULTY, GROUP, SCHEDULE, WEEK,
}