package com.ajithkp.application.homepage

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ajithkp.application.R

import com.ajithkp.application.appdatabase.Market
import java.text.SimpleDateFormat
import android.text.Html
import android.os.Build
import android.text.Spanned



/**
 * Created by kpajith on 27-08-2018 on 16:20.
 */

class FlavorsAdapter(private val markets: List<Market>, private val context: Context) : RecyclerView.Adapter<FlavorsAdapter.MyViewHolder>() {
    val actualFormat = SimpleDateFormat("dd/MM/yyyy")
    val englishFont = Typeface.createFromAsset(context.getAssets(), "fonts/Neucha.ttf");
    val desiredFormat = SimpleDateFormat("dd MMM")

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var header_text: TextView

        var label_1: TextView
        var label_2: TextView
        var label_3: TextView
        var label_4: TextView

        var value_1: TextView
        var value_2: TextView
        var value_3: TextView
        var value_4: TextView

        init {
            header_text = view.findViewById(R.id.header_text) as TextView

            label_1 = view.findViewById(R.id.label_1) as TextView
            label_2 = view.findViewById(R.id.label_2) as TextView
            label_3 = view.findViewById(R.id.label_3) as TextView
            label_4 = view.findViewById(R.id.label_4) as TextView

            value_1 = view.findViewById(R.id.value_1) as TextView
            value_2 = view.findViewById(R.id.value_2) as TextView
            value_3 = view.findViewById(R.id.value_3) as TextView
            value_4 = view.findViewById(R.id.value_4) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val market = markets[position]
        val marketDateTemp = actualFormat.parse(market.marketDate)
        holder.header_text.typeface = englishFont
        holder.header_text?.text = market.varitey + " (" + desiredFormat.format(marketDateTemp) + ")"

        holder.label_1.typeface = englishFont
        holder.label_2.typeface = englishFont
        holder.label_3.typeface = englishFont
        holder.label_4.typeface = englishFont
        holder.value_1.typeface = englishFont
        holder.value_2.typeface = englishFont
        holder.value_3.typeface = englishFont
        holder.value_4.typeface = englishFont

        holder.label_1.text = fromHtml("Min (\u20B9)")
        holder.label_2.text = fromHtml("Modal (\u20B9)")
        holder.label_3.text = fromHtml("Max (\u20B9)")
        holder.label_4.text = "Arrival"

        holder.value_1.text = market.minPrice.toString()
        holder.value_2.text = market.modalPrice.toString()
        holder.value_3.text = market.maxPrice.toString()
        holder.value_4.text = market.arrival.toString()

    }

    override fun getItemCount(): Int {
        return markets.size
    }


    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }
}