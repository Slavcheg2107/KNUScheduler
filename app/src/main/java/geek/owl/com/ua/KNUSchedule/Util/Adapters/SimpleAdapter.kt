package geek.owl.com.ua.KNUSchedule.Util.Adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.AppClass
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.*
import geek.owl.com.ua.KNUSchedule.Util.KNUDiffUtil


class SimpleAdapter(var data: MutableList<ItemModel>, private val itemClickListener: OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  var itemOffset = 0
  var isFiltering: Boolean = false
  private var filteredData: MutableList<ItemModel> = ArrayList()

  interface ItemModel {
    fun getType(): Int
  }

  private val renderer: ViewRenderer = ViewRenderer()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return renderer.createViewHolder(parent, viewType)

  }

  fun filterGroups(filteredText: String) {
    data as MutableList<GroupPojo>
    if (filteredText.isNotEmpty()) {
      isFiltering = true
      filteredData = data.filter {
        it as GroupPojo
        it.name.contains(filteredText, ignoreCase = true) || it.name.equals(filteredText, ignoreCase = true)
      }.toMutableList()
      val groupDiffUtil = KNUDiffUtil(data, filteredData)
      val result = DiffUtil.calculateDiff(groupDiffUtil, true)
      result.dispatchUpdatesTo(this)
    } else {
      isFiltering = false
      this.notifyDataSetChanged()

    }

  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item: ItemModel = if (isFiltering) filteredData[position] else data[position]
    when (holder.itemViewType) {
      ItemType.FACULTY.ordinal -> {
        item as FacultyPojo
        holder as ViewRenderer.FacultyViewHolder
        holder.bind(item, itemClickListener)
      }
      ItemType.GROUP.ordinal -> {
        item as GroupPojo
        holder as ViewRenderer.GroupViewHolder
        holder.bind(item, itemClickListener)
      }

      ItemType.WEEK.ordinal -> {
        item as DayPojo
        holder as ViewRenderer.WeekViewHolder
        holder.bind("${AppClass.INSTANCE.getString(R.string.week)} ${item.week}", itemClickListener)
      }

      ItemType.SCHEDULE.ordinal -> {
        item as SchedulePojo
        holder as ViewRenderer.ScheduleViewHolder
        holder.bind(item, itemClickListener)
      }
    }
  }


  override fun getItemCount(): Int {
    return if (!isFiltering) {
      data.size
    } else filteredData.size
  }

  override fun getItemViewType(position: Int): Int {

    return if (!isFiltering) {
      val type = data[position].getType()
      if (type == ItemType.DAY.ordinal) {
        data.let { list ->
          list as MutableList<DayPojo>
          list.sortByDescending { dayPojo -> dayPojo.week }
          return when {
            position == 0 -> ItemType.WEEK.ordinal
            list[position].week != list[position + 1].week -> ItemType.DAY.ordinal
            else -> ItemType.WEEK.ordinal
          }
        }
      } else {
        type
      }
    } else filteredData[position].getType()
  }




}