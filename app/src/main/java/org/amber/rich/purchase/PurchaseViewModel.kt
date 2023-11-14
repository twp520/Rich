package org.amber.rich.purchase

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.amber.rich.R
import org.amber.rich.RichApplication
import org.amber.rich.data.PurchaseRepository
import java.text.SimpleDateFormat
import java.util.Date

/**
 * create by colin
 * 2023/11/2
 */
class PurchaseViewModel : ViewModel() {

    private val repository = PurchaseRepository()


    val purchaseDateFilters =
        mutableListOf(AllFilter(), TodayFilter(), LastWeekFilter(), LastMonthFilter())
    private val purchaseDataFilterState = MutableStateFlow(purchaseDateFilters[1])
    val filterState: StateFlow<PurchaseDateFilter> = purchaseDataFilterState.asStateFlow()

    val purchaseState =
        purchaseDataFilterState.transform {
            val list = withContext(Dispatchers.IO) {
                repository.loadPurchase(it)
            }
            emit(list)
        }

    val profitState = purchaseState.transform {
        val sumProfit = it.sumOf { item ->
            item.calculateProfit()
        }
        val sumPrice = it.sumOf { item ->
            item.purchase.purchasePrice
        }
        val result = RichApplication.appContext.getString(
            R.string.purchase_title,
            sumPrice.toString(),
            sumProfit.toString()
        )
        emit(result)
    }


    fun changeDateFilter(dateFilter: PurchaseDateFilter) {
        purchaseDataFilterState.update {
            dateFilter
        }
    }

    fun formatDate(date: Long): String {
        return SimpleDateFormat.getDateTimeInstance().format(Date(date))
    }
}