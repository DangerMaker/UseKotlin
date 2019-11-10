package com.god.kotlin.menu

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ez08.trade.net.Client
import com.god.kotlin.R
import com.god.kotlin.bank.BankListActivity
import com.god.kotlin.change.ChangeInformationActivity
import com.god.kotlin.change.ChangePwdActivity
import com.god.kotlin.ipo.IpoOrderActivity
import com.god.kotlin.pre.TradeOrderActivity
import com.god.kotlin.profile.*
import com.god.kotlin.query.QueryMenuActivity
import com.god.kotlin.trade.*
import com.god.kotlin.trade.agree.TradeAgreeActivity
import com.god.kotlin.util.Constant
import com.god.kotlin.util.inflate
import com.god.kotlin.util.startActivity
import kotlinx.android.synthetic.main.trade_holder_options.view.*
import kotlinx.android.synthetic.main.trade_holder_other.view.*
import java.lang.Exception

class MenuAdapter(private val header: View, private val list: List<StringMenu>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val HEAD = 0
    val NORMAL = 1

    private fun logout() {
        Client.getInstance().logout()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEAD -> {
                ViewHolder(header)
            }

            NORMAL -> {
                val view = parent.inflate(R.layout.trade_holder_other)
                StringHolder(view).apply {
                    itemView.setOnClickListener {
                        when (list[adapterPosition - 1].name) {
                            "客户风险级别查询" -> view.startActivity(RiskQueryActivity::class.java)
                            "预受要约" -> view.startActivity(TradeAgreeActivity::class.java)
                            "新股申购" -> view.startActivity(IpoOrderActivity::class.java)
                            "客户风险级别测评" -> view.startActivity(RiskTestActivity::class.java)
                            "股东资料" -> view.startActivity(AccountListActivity::class.java)
                            "修改资料" -> view.startActivity(ChangeInformationActivity::class.java)
                            "修改密码" -> view.startActivity(ChangePwdActivity::class.java)
                            "转股回售" -> view.startActivity(TradeActivity::class.java){
                                putExtra(Constant.TRADE_TYPE,3)
                                putExtra(Constant.TRADE_MAIN_PAGE,0)
                            }
                            "预埋单" -> view.startActivity(TradeOrderActivity::class.java)
//                            "预埋单" -> view.startActivity(PreActivity::class.java)
//                            "银行转账" -> view.startActivity(TransferMenuActivity::class.java)
                            "银行转账" -> view.startActivity(BankListActivity::class.java)
                            "退出登录" -> logout()
                            else -> throw Exception("router err")
                        }
                    }
                }
            }

            else -> throw Exception("Adapter err")
        }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (getItemViewType(position)) {
            HEAD -> {

            }

            NORMAL -> {
                val stringHolder: StringHolder = holder as StringHolder
                stringHolder.title.text = list[position - 1].name
            }

            else -> throw Exception("Adapter err")
        }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> HEAD
        else -> NORMAL
    }

}

class MenuHorizontalAdapter(private val list: List<ImageMenu>) : RecyclerView.Adapter<ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trade_holder_options, parent, false)
        return ImageHolder(view).apply {
            itemView.setOnClickListener {
                when (list[adapterPosition].name) { //tradeType 0限价 1市价 2批量 3可转债 4对买
                    "买入" -> view.startActivity(TradeActivity::class.java) {
                        putExtra(Constant.TRADE_TYPE, 0)
                        putExtra(Constant.TRADE_MAIN_PAGE, 0)
                    }
                    "卖出" -> view.startActivity(TradeActivity::class.java) {
                        putExtra(Constant.TRADE_TYPE, 0)
                        putExtra(Constant.TRADE_MAIN_PAGE, 1)
                    }
                    "撤单" -> view.startActivity(TradeActivity::class.java) {
                        putExtra(Constant.TRADE_TYPE, 0)
                        putExtra(Constant.TRADE_MAIN_PAGE, 2)
                    }
                    "持仓" -> view.startActivity(TradeActivity::class.java) {
                        putExtra(Constant.TRADE_TYPE, 0)
                        putExtra(Constant.TRADE_MAIN_PAGE, 3)
                    }
                    "市价买卖" -> view.startActivity(TradeActivity::class.java) {
                        putExtra(Constant.TRADE_TYPE, 1)
                        putExtra(Constant.TRADE_MAIN_PAGE, 0)
                    }
                    "对买对卖" -> view.startActivity(TradeActivity::class.java){
                        putExtra(Constant.TRADE_TYPE, 4)
                        putExtra(Constant.TRADE_MAIN_PAGE, 0)
                    }
                    "批量委托" -> view.startActivity(TradeActivity::class.java){
                        putExtra(Constant.TRADE_TYPE, 2)
                        putExtra(Constant.TRADE_MAIN_PAGE, 0)
                    }
                    "查询" -> view.startActivity(QueryMenuActivity::class.java)
                    else -> throw Exception("router err")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.subTitle.text = list[position].name
        holder.image.setImageResource(list[position].resId)
    }
}

class ImageHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    val subTitle: TextView = parent.sub_title
    val image: ImageView = parent.icon

    init {
        parent.setOnClickListener {
            parent.startActivity(TradeActivity::class.java)
        }
    }
}

class StringHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    val title: TextView = parent.title

    init {

    }
}

class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent)

data class ImageMenu(val name: String, val resId: Int)
data class StringMenu(val name: String)