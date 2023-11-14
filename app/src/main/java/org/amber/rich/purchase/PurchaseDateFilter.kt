package org.amber.rich.purchase

import org.amber.rich.R
import org.amber.rich.RichApplication
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * create by colin
 * 2023/11/13
 */
interface PurchaseDateFilter {
    val endTime: Long
        get() = System.currentTimeMillis()

    val title: String

    fun calculateStartTime(): Long
}

class TodayFilter : PurchaseDateFilter {

    override val title: String = RichApplication.appContext.getString(R.string.date_filter_today)

    override fun calculateStartTime(): Long {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

class LastWeekFilter : PurchaseDateFilter {

    override val title: String =
        RichApplication.appContext.getString(R.string.date_filter_last_week)

    override fun calculateStartTime(): Long {
        val calendar = Calendar.getInstance() // 获取当前日期
        calendar.time = Date()
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == 1) {
            dayOfWeek += 7
        }
        calendar.add(Calendar.DATE, 2 - dayOfWeek)
        return calendar.timeInMillis
    }

}

class LastMonthFilter : PurchaseDateFilter {

    override val title: String =
        RichApplication.appContext.getString(R.string.date_filter_last_month)

    override fun calculateStartTime(): Long {
        val calendar = Calendar.getInstance() // 获取当前日期
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.YEAR, 0)
        calendar.add(Calendar.MONTH, 0)
        calendar[Calendar.DAY_OF_MONTH] = 1 // 设置为1号,当前日期既为本月第一天

        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

}

class AllFilter : PurchaseDateFilter {

    override val title: String = RichApplication.appContext.getString(R.string.date_filter_all)

    override fun calculateStartTime(): Long {
        return 0
    }
}
