package com.god.kotlin.transfer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.util.getTime
import com.god.kotlin.util.getTransferStatus
import com.god.kotlin.util.inflate

class RecordAdapter(
    private val list: MutableList<TransferRecord>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val transfer = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_transfer)

        with(rowView.findViewById<TextView>(R.id.record_time)) {
            text = transfer.operdate + " " + getTime(transfer.opertime)
        }

        with(rowView.findViewById<TextView>(R.id.record_price)) {
            text = transfer.fundeffect
        }

        with(rowView.findViewById<TextView>(R.id.record_status)) {
            text = getTransferStatus(transfer.status)
        }

        return rowView
    }

    override fun getItem(position: Int): TransferRecord {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}