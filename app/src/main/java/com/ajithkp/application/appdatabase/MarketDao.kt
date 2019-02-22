package com.ajithkp.application.appdatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import java.util.*

/**
 * Created by kpajith on 23-08-2018 on 15:33.
 */


@Dao
interface MarketDao {

    @get:Query("SELECT * from market")
    val allMarkets: kotlin.collections.List<Market>

    @get:Query("SELECT distinct marketName from market order by marketName")
    val allMarketNames: kotlin.collections.List<String>

    @Query("SELECT count(*) from market where marketName = :marketName and marketDate = :marketDate and varitey = :varitey")
    fun checkMarketUniqueness(marketName: String, marketDate:String, varitey:String): Integer


    @Query("SELECT * from market where marketName = :marketName order by marketName")
    fun allCommodities(marketName: String): kotlin.collections.List<Market>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMarket(market: Market)

    @Query("DELETE FROM market")
    fun deleteAll()
}