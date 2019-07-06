package com.god.kotlin.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.god.kotlin.R
import com.god.kotlin.util.inflate
import kotlinx.android.synthetic.main.fragment_buy.*

class BuyFragment :Fragment(){

    companion object {
        fun newInstance(): BuyFragment {
            return BuyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_buy, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(buy_recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = HandAdapter()
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_light_1px)!!)
            addItemDecoration(divider)
        }
    }


    private class HandAdapter : RecyclerView.Adapter<HandHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandHolder {
            return HandHolder(parent.inflate(R.layout.holder_hold_stock))
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: HandHolder, position: Int) {

        }

    }

    class HandHolder(parent: View) : RecyclerView.ViewHolder(parent)
}
