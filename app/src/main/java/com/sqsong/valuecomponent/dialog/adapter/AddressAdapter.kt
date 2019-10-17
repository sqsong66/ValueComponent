package com.sqsong.valuecomponent.dialog.adapter

import android.content.Context
import android.widget.TextView
import com.sqsong.valuecomponent.bean.City
import com.sqsong.valuecomponent.bean.County
import com.sqsong.valuecomponent.bean.Province
import com.sqsong.valuecomponent.bean.Town
import com.sqsong.valuecomponent.common.OnItemClickListener

class ProvinceAdapter(
    context: Context, dataList: List<Province>,
    itemClickListener: OnItemClickListener<Province>?
) : AbstractAddressAdapter<Province>(context, dataList, itemClickListener) {
    override fun bindAddressData(data: Province, textView: TextView) {
        textView.text = data.name
    }
}

class CityAdapter(
    context: Context, dataList: List<City>,
    itemClickListener: OnItemClickListener<City>?
) : AbstractAddressAdapter<City>(context, dataList, itemClickListener) {
    override fun bindAddressData(data: City, textView: TextView) {
        textView.text = data.name
    }
}

class CountyAdapter(
    context: Context, dataList: List<County>,
    itemClickListener: OnItemClickListener<County>?
) : AbstractAddressAdapter<County>(context, dataList, itemClickListener) {
    override fun bindAddressData(data: County, textView: TextView) {
        textView.text = data.name
    }
}

class TownAdapter(
    context: Context, dataList: List<Town>,
    itemClickListener: OnItemClickListener<Town>?
) : AbstractAddressAdapter<Town>(context, dataList, itemClickListener) {
    override fun bindAddressData(data: Town, textView: TextView) {
        textView.text = data.name
    }
}