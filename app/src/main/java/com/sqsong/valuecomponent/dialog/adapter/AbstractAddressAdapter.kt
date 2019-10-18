package com.sqsong.valuecomponent.dialog.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.common.OnItemClickListener

abstract class AbstractAddressAdapter<T>(
    context: Context, private val dataList: List<T>,
    private val itemClickListener: OnItemClickListener<T>?
) : RecyclerView.Adapter<AbstractAddressAdapter<T>.AddressViewHolder>() {

    private var mSelectedPosition = -1
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(mInflater.inflate(R.layout.item_address, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    fun resetAdapter(index: Int = -1) {
        mSelectedPosition = index
        notifyDataSetChanged()
    }

    abstract fun bindAddressData(data: T, textView: TextView)

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTv = itemView.findViewById<TextView>(R.id.nameTv)
        private val selectIv = itemView.findViewById<ImageView>(R.id.selectIv)

        fun bindData(data: T, position: Int) {
            bindAddressData(data, nameTv)

            if (mSelectedPosition == position) {
                selectIv.visibility = View.VISIBLE
                nameTv.setTypeface(null, Typeface.BOLD)
            } else {
                selectIv.visibility = View.GONE
                nameTv.setTypeface(null, Typeface.NORMAL)
            }

            itemView.setOnClickListener {
                val lastPos = mSelectedPosition
                mSelectedPosition = position
                if (lastPos != -1) {
                    notifyItemChanged(lastPos)
                }
                notifyItemChanged(mSelectedPosition)
                itemClickListener?.onItemClick(data, position)
            }
        }
    }
}