package geek.owl.com.ua.KNUSchedule.Util.Adapters

import com.owl.krasn.KNUSchedule.Repository.SchedulePojo


interface OnItemClick {
    fun onClick(item: SimpleAdapter.ItemModel)
}

interface OnScheduleClick {
    fun onScheduleClick(schedulePojo: SchedulePojo)
}

interface OnDayClick