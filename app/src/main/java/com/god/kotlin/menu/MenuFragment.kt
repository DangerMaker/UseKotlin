package com.god.kotlin.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.god.kotlin.R
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {

    private lateinit var recyclerViewH: RecyclerView

    companion object {
        fun newInstance(): MenuFragment {
            return MenuFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val data: MutableList<ImageMenu> = mutableListOf()
        data.add(ImageMenu("买入", R.drawable.menu_buy))
        data.add(ImageMenu("卖出", R.drawable.menu_sell))
        data.add(ImageMenu("撤单", R.drawable.menu_cancel))
        data.add(ImageMenu("持仓", R.drawable.menu_holder))
        data.add(ImageMenu("市价买卖", R.drawable.meun_market_price))
        data.add(ImageMenu("对买对卖", R.drawable.menu_buy_sell))
        data.add(ImageMenu("批量委托", R.drawable.menu_batch))
        data.add(ImageMenu("查询", R.drawable.menu_query))

        val dataV: MutableList<StringMenu> = mutableListOf()
        dataV.add(StringMenu("新股申购"))
        dataV.add(StringMenu("银行转账"))
        dataV.add(StringMenu("预埋单"))
        dataV.add(StringMenu("预受要约"))
//        dataV.add(StringMenu("网络投票"))
        dataV.add(StringMenu("转股回售"))
        dataV.add(StringMenu("股东资料"))
        dataV.add(StringMenu("客户风险级别查询"))
//        dataV.add(StringMenu("客户风险级别测评"))
        dataV.add(StringMenu("修改资料"))
        dataV.add(StringMenu("修改密码"))
        dataV.add(StringMenu("退出登录"))

        recyclerViewH = RecyclerView(context!!)
        with(recyclerViewH) {
            layoutParams = ViewGroup.LayoutParams(-1, -2)
            layoutManager = GridLayoutManager(context, 4)
            adapter = MenuHorizontalAdapter(data)
            setPadding(20, 20, 20, 20)
        }

        with(menu_recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = MenuAdapter(recyclerViewH, dataV)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_light_1px)!!)
            addItemDecoration(divider)
        }

    }
}
