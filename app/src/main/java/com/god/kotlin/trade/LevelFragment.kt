package com.god.kotlin.trade

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.util.inflate
import kotlinx.android.synthetic.main.fragment_level.*

class LevelFragment : Fragment() {

    private var list = mutableListOf<TradeStockEntity.Dang>()

    companion object {
        fun newInstance(): LevelFragment {
            return LevelFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_level, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(level_list_view) {
            level_list_view.adapter = LevelAdapter(list, context)
        }
    }

    private class LevelAdapter(
        private val list: MutableList<TradeStockEntity.Dang>
        , private val context: Context
    ) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val level = getItem(position)
            val rowView = convertView ?: context.inflate(R.layout.trade_item_five)

            //todo  findViewById 耗时
            with(rowView.findViewById<TextView>(R.id.level_name)) {
                text = LEVEL_NAMES[position]
            }

            with(rowView.findViewById<TextView>(R.id.level_price)) {
                text = level.fPrice.toString()
            }

            with(rowView.findViewById<TextView>(R.id.level_quantity)) {
                text = level.fOrder.toString()
            }
            return rowView
        }

        override fun getItem(position: Int): TradeStockEntity.Dang {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    fun refresh(entity: TradeStockEntity) {
        list.clear()
        list.addAll(entity.ask)
        list.addAll(entity.bid)
    }

}

private val LEVEL_NAMES = arrayOf("卖5", "卖4", "卖3", "卖2", "卖1", "买1", "买2", "买3", "买4", "买5")