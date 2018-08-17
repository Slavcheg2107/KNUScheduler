package com.knu.krasn.knuscheduler.Util.Adapters

import com.knu.krasn.knuscheduler.Repository.SchedulePojo


interface OnItemClick {
    fun onClick(item: SimpleAdapter.ItemModel)
}

interface OnScheduleClick {
    fun onScheduleClick(schedulePojo: SchedulePojo)
}

interface OnDayClick