package com.god.kotlin.profile

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.data.entity.Account
import com.god.kotlin.util.getMarketType
import com.god.kotlin.util.inflate

class AccountAdapter(
    private val list: MutableList<Account>
    , private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val account = getItem(position)
        val rowView = convertView ?: context.inflate(R.layout.trade_holder_shareholders_item)

        with(rowView.findViewById<TextView>(R.id.stockholders_code)) {
            text = account.secuid
        }

        with(rowView.findViewById<TextView>(R.id.stockholders_name)) {
            text = account.name
        }

        with(rowView.findViewById<TextView>(R.id.market_type)) {
            text = getMarketType(account.market)
        }

        with(rowView.findViewById<TextView>(R.id.funds_account)) {
            text = account.custid
        }


        return rowView
    }

    override fun getItem(position: Int): Account {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}