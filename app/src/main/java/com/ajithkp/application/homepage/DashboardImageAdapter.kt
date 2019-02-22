package com.ajithkp.application.homepage

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ajithkp.application.R


/**
 * Created by kpajith on 27-08-2018 on 18:10.
 */
class DashboardImageAdapter(private val mContext: Context, val dashboardImages: ArrayList<String>?) : PagerAdapter() {

    var array: Array<String>? = null

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.dashboard_image_view, collection, false) as ImageView
        val imageUrl = dashboardImages?.get(position)
        Glide.with(mContext).load(imageUrl).thumbnail(Glide.with(mContext).load(R.drawable.loading)).into(layout!!)
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return dashboardImages?.size!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}