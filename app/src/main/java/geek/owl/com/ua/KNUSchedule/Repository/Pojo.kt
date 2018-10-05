package geek.owl.com.ua.KNUSchedule.Repository

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter


@Entity
data class GroupPojo(var name: String) : SimpleAdapter.ItemModel {
  @PrimaryKey
  var id: Long = 0

  var facultyId: String = ""
  override fun getType(): Int {
    return ItemType.GROUP.ordinal
  }
}

@Entity
data class FacultyPojo(var name: String, @PrimaryKey var id: Long) : SimpleAdapter.ItemModel {
  override fun getType(): Int {
    return ItemType.FACULTY.ordinal
  }

}

@Entity
data class SchedulePojo(@PrimaryKey var id: Long) : SimpleAdapter.ItemModel {
  override fun getType(): Int {
    return ItemType.SCHEDULE.ordinal
  }

  @SerializedName("teachers")
  @Expose
  var teachers: String = ""
  @SerializedName("day")
  @Expose
  var day: Int = -1
  @SerializedName("room")
  @Expose
  var room: String = ""
  @SerializedName("discipline")
  @Expose
  var discipline: String = ""
  @SerializedName("group")
  @Expose
  var group: String = ""
  @SerializedName("week")
  @Expose
  var week: Int = -1
  @SerializedName("lesson")
  @Expose
  var lesson: Int = -1
  @SerializedName("lessontype")
  @Expose
  var lessontype: String = ""
  @SerializedName("subgroup")
  @Expose
  var subgroup: String? = null
  @SerializedName("class_begin")
  @Expose
  var beginTime: String = ""
  @SerializedName("class_end")
  @Expose
  var endTime: String = ""
  @SerializedName("corps")
  @Expose
  var corps: String = ""

}

data class DayPojo(val number: Int) : SimpleAdapter.ItemModel {
  override fun getType(): Int {
    return ItemType.DAY.ordinal
  }

  var scheduleList: List<SchedulePojo> = emptyList()
  var weekNumber: Int = 0

}

data class WeekPojo(val list: ArrayList<DayPojo>, val weekNumber: Int) : SimpleAdapter.ItemModel {
  var title = ""

  override fun getType(): Int {
    return ItemType.WEEK.ordinal
  }
}

enum class ItemType {
  FACULTY, GROUP, SCHEDULE, WEEK, DAY
}