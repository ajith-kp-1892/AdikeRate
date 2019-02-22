package com.ajithkp.application


import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.ajithkp.application.appdatabase.AppDatabase
import com.ajithkp.application.appdatabase.Market
import com.ajithkp.application.homepage.DashboardImageAdapter
import com.ajithkp.application.homepage.FlavorsAdapter
import com.ajithkp.application.util.Util
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by kpajith on 08-08-2018 on 15:11.
 */

class DashboardActivity : AppCompatActivity() {

    private val PREFS_FILENAME = "com.obopay.io.prefs"
    private val MARKET_NAME = "MARKET_NAME"
    private val DEFAULT_MARKET_NAME = "Hosanagar"
    private val actualFormat = SimpleDateFormat("dd/MM/yyyy")


    private var mDb: AppDatabase? = null
    private var recycler_view: RecyclerView? = null
    private var viewPager: ViewPager? = null
    private var inflater: LayoutInflater? = null
    private var todayDate: Date? = null
    private var levelDialog: AlertDialog? = null

    private var prefs: SharedPreferences? = null

    //var dateSelector: Button? = null
    private var marketName: TextView? = null
    private var dialog: ProgressDialog? = null
    private var market_change_button: ImageButton? = null
    private var marketNames: List<String>? = null
    private var flavoursList: ArrayList<Market>? = null
    private var flavoursAdapter: FlavorsAdapter? = null
    private var dashboardImages: ArrayList<String>? = null
    private var countdownTimer: CountDownTimer? = null
    private var util: Util? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDb = AppDatabase.getAppDatabase(this)
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        recycler_view = findViewById(R.id.recycler_view) as RecyclerView
        //dateSelector = findViewById(R.id.date_selector) as Button
        marketName = findViewById(R.id.market_name) as TextView
        market_change_button = findViewById(R.id.market_change_button) as ImageButton

        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        todayDate = Date()
        util = Util(this)

        val desiredFormat = SimpleDateFormat("dd MMM")
        //dateSelector?.setText(desiredFormat.format(todayDate))

        marketName?.setText(getSelectedMarketName() + " Market")
        dialog = ProgressDialog(this)
        flavoursList = ArrayList()

        dashboardImages = ArrayList()
        dashboardImages?.add("https://www.plantheunplanned.com/wp-content/uploads/2017/12/Kodachadri-2.jpg")
        dashboardImages?.add("https://www.plantheunplanned.com/wp-content/uploads/2017/12/Kodachadri-4.jpg")
        dashboardImages?.add("https://www.plantheunplanned.com/wp-content/uploads/2017/12/Kodachadri-3.jpg")

        viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager?.adapter = DashboardImageAdapter(this, dashboardImages)
        viewPager?.currentItem = 0
        market_change_button?.setOnClickListener { showMarketSelector() }

        LongOperation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun marketSelectorInit() {
        marketNames = mDb?.marketDao()?.allMarketNames
        var items = arrayOf<CharSequence>()

        if (marketNames != null) {
            (0..marketNames!!.indexOf(DEFAULT_MARKET_NAME)).forEach {
                Collections.swap(marketNames, 0, it)
            }

            for (marketName in marketNames!!) {
                items = items.plus(marketName)
            }
        }

        var list = items.toList()

        items = arrayOf<CharSequence>()
        for (name in list) {
            items = items.plus(util?.getLangaueKeyValue(name.toString()).toString())
        }


        val builder = AlertDialog.Builder(this)
        builder.setTitle(util!!.getLangaueKeyValue("Choose Market"))
        builder.setSingleChoiceItems(items, 0) { dialog, item ->
            updateMarketOrientedData(marketNames!!.get(item).toString())
            levelDialog?.dismiss()
        }
        levelDialog = builder.create()
    }


    private fun showMarketSelector() {
        if (marketNames != null && marketNames?.size != 0) {
            levelDialog?.show()
        } else {
            marketSelectorInit()
            levelDialog?.show()
        }
    }

    private fun updateMarketOrientedData(item: String) {
        val editor = prefs!!.edit()
        editor.putString(MARKET_NAME, item)
        editor.apply()
        updateCommodityView()
    }

    private inner class LongOperation : AsyncTask<String, Void, String>() {
        var isInternetCheck = true

        override fun doInBackground(vararg params: String): String? {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, -1);
            val lastMonthDate = actualFormat.format(cal.time)

            try {
                val baseURL = "https://www.krishimaratavahini.kar.nic.in/MainPage/DailyMrktPriceRep2.aspx?Rep=Com&CommCode=140&VarCode=1&Date=" + lastMonthDate + "&CommName=Arecanut%20/%20%E0%B2%85%E0%B2%A1%E0%B2%BF%E0%B2%95%E0%B3%86&VarName=Red%20/%20%E0%B2%95%E0%B3%86%E0%B2%82%E0%B2%AA%E0%B3%81"
                Log.e("obopay", "url--" + baseURL)
                val doc = Jsoup.connect(baseURL).get()
                val content = doc.getElementById("_ctl0_content5_Table1")
                val links = content.getElementsByTag("tr")
                for (link in links) {
                    try {
                        var i = 0
                        var marketName: String? = null
                        var marketDate: String? = null
                        var varitey: String? = null
                        var arrival: String? = null
                        var minPrice: Long? = null
                        var maxPrice: Long? = null
                        var modalPrice: Long? = null

                        val flavours = link.getElementsByTag("td")
                        for (flv in flavours) {
                            when (i) {
                                0 -> {
                                    marketName = flv.text()
                                    marketName = marketName.substring(0, 1).toUpperCase() + marketName.substring(1).toLowerCase()
                                }
                                1 -> {
                                    marketDate = flv.text()
                                }
                                2 -> {
                                    varitey = flv.text()
                                }
                                3 -> {
                                    arrival = flv.text()
                                }
                                4 -> {
                                    try {
                                        minPrice = flv.text().toLong()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                5 -> {
                                    try {
                                        maxPrice = flv.text().toLong()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                6 -> {
                                    try {
                                        modalPrice = flv.text().toLong()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            i++
                        }

                        val uniqueKey = marketName + "_" + varitey + "_" + marketDate
                        val market = Market(marketName = marketName, marketDate = marketDate, varitey = varitey, arrival = arrival, minPrice = minPrice, maxPrice = maxPrice, modalPrice = modalPrice, key = uniqueKey)
                        val checkness = mDb?.marketDao()?.checkMarketUniqueness(market?.marketName!!, market?.marketDate!!, market?.varitey!!)
                        if (checkness != null && checkness.equals(0)) {
                            mDb?.marketDao()?.insertMarket(market)
                        } else {
                            Log.e("OBOPAY", "EXIST-- " + uniqueKey)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (ex: Exception) {
                isInternetCheck = false
                ex.printStackTrace()
            }
            return ""
        }

        override fun onPostExecute(result: String) {
            updateCommodityView()
            if (dialog != null && dialog!!.isShowing()) {
                dialog?.dismiss()
            }
            marketSelectorInit()
            if (!isInternetCheck) {
                try {
                    Toast.makeText(this@DashboardActivity, "Please Check Internet Connection", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                }
            }
        }

        override fun onPreExecute() {
            try {
                dialog?.setMessage("Fetching Data.Please Wait")
                if (!dialog!!.isShowing) {
                    dialog?.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onProgressUpdate(vararg values: Void) {}
    }

    private fun updateCommodityView() {

        flavoursList = ArrayList()

        var name = getSelectedMarketName()
        marketName?.text = util?.fromHtml(name + " Market")

        if (name != null) {
            var flavours = mDb?.marketDao()?.allCommodities(name)
            if (flavours != null) {
                flavours.sortedWith(compareBy(Market::marketDate, Market::marketDate))
                flavoursList?.addAll(flavours)
            }
        }

        flavoursAdapter = FlavorsAdapter(flavoursList!!, this)
        recycler_view?.setLayoutManager(LinearLayoutManager(this))

        recycler_view!!.adapter = flavoursAdapter
        flavoursAdapter!!.notifyDataSetChanged()
    }

    private fun getSelectedMarketName(): String {
        var marketName = DEFAULT_MARKET_NAME
        val tempName = prefs!!.getString(MARKET_NAME, "")
        if (!tempName.isEmpty()) {
            marketName = tempName
        }
        return marketName
    }


    private fun startCountDownTimer() {
        countdownTimer = object : CountDownTimer(20000, 2000) {

            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val nextCount = (viewPager?.currentItem?.plus(1))
                val total = viewPager?.childCount!!
                val selectable = nextCount!! % total
                viewPager?.currentItem = selectable
                countdownTimer?.cancel()
                startCountDownTimer()
            }

        }
        countdownTimer?.start()
    }

    private fun stopCountdownTimer() {
        if (countdownTimer != null) {
            countdownTimer?.cancel()
        }
    }


    override fun onResume() {
        super.onResume()
        startCountDownTimer()
    }


    override fun onPause() {
        super.onPause()
        stopCountdownTimer()
    }


    public override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing()) {
            dialog?.cancel()
        }

        if (levelDialog != null && levelDialog!!.isShowing()) {
            levelDialog?.cancel()
        }
    }


    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return true
    }*/


    /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.getItemId()) {
             R.id.item_selector -> {
                 showMarketSelector()
                 return true
             }
             else -> return super.onOptionsItemSelected(item)
         }
     }*/

}
