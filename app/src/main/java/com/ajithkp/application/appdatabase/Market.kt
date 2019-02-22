package com.ajithkp.application.appdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "market")
class Market(var marketName: String?,
        /*@field:TypeConverters(DateConverter::class) */ var marketDate: String?, var varitey: String?, var arrival: String?, var minPrice: Long?, var maxPrice: Long?, var modalPrice: Long?, var key: String?) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}