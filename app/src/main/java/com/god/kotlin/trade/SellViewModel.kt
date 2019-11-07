package com.god.kotlin.trade

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.god.kotlin.data.TradeRepository
import com.god.kotlin.data.entity.Avail
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.data.entity.TradeResultEntity
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.net.Error
import com.god.kotlin.net.OnResult

class SellViewModel(private val repository: TradeRepository) :
    ViewModel() {

    val stockEntity = MutableLiveData<TradeStockEntity>()
    val handStockList = MutableLiveData<MutableList<TradeHandEntity>>()
    val available = MutableLiveData<Avail>()
    val order = MutableLiveData<TradeResultEntity>()
    var serverealResult = MutableLiveData<String>()

    val tips = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    val currentHQ = MutableLiveData<TradeStockEntity>()

    fun search(code: String) {
        loading.value = true
        repository.searchStock(code, object : OnResult<TradeStockEntity> {
            override fun onSucceed(response: TradeStockEntity) {
                stockEntity.value = response
                loading.value = false
            }

            override fun onFailure(error: Error) {
                loading.value = false
            }

        })
    }

    fun getHandList(fundsId: String, count: Int = 100, offset: Int = 1) {
        repository.getHandStockList(fundsId, count, offset, object : OnResult<MutableList<TradeHandEntity>> {
            override fun onSucceed(response: MutableList<TradeHandEntity>) {
                handStockList.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun getAvailable( market: String,secuid: String,fundsId: String,code: String, price: Double, flag: Boolean) {
        repository.getAvailable( market,secuid,fundsId,
            code, price, flag, object : OnResult<Int> {
            override fun onSucceed(response: Int) {
                available.value = Avail(flag,response)
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
            }

        })
    }

    fun transaction(market: String, code: String,secuid: String,fundsId: String, price: Double, qty: Int, postFlag: String) {
        loading.value = true
        repository.transaction(market, code,secuid,fundsId, price, qty, postFlag, object : OnResult<TradeResultEntity> {
            override fun onSucceed(response: TradeResultEntity) {
                order.value = response
                loading.value = false
            }

            override fun onFailure(error: Error) {
                tips.value = error.szError
                loading.value = false
            }

        })
    }

    private var total: Int = 0
    private var temp = 0
    private var collection: String = ""
    fun transactionSeveral(market: String, code: String,secuid: String,fundsId: String, price: Double, qty: Int, postFlag: String,
    items: Int,remainder: Int) {
        loading.value = true
        temp = 0
        total = items
        collection = ""
        for (i in 0 until items) {
            repository.transaction(market, code, secuid, fundsId, price, qty, postFlag,
                object : OnResult<TradeResultEntity> {
                    override fun onSucceed(response: TradeResultEntity) {
                        collect("委托成功" + "\n" +
                                "委托序号：" + response.ordersno + "\n" +
                                "合同序号：" + response.orderid + "\n" +
                                "委托批号：" + response.ordergroup + "\n\n")
                    }

                    override fun onFailure(error: Error) {
                        collect("委托失败" + "\n" +
                                "失败原因：" + error.szError + "\n\n")
                    }

                })
        }

        if(remainder != 0){
            total = items + 1
            repository.transaction(market, code, secuid, fundsId, price, remainder, postFlag,
                object : OnResult<TradeResultEntity> {
                    override fun onSucceed(response: TradeResultEntity) {
                        collect("委托成功" + "\n" +
                                "委托序号：" + response.ordersno + "\n" +
                                "合同序号：" + response.orderid + "\n" +
                                "委托批号：" + response.ordergroup + "\n\n")
                    }

                    override fun onFailure(error: Error) {
                        collect("委托失败" + "\n" +
                                "失败原因：" + error.szError + "\n\n")
                    }

                })
        }
    }

    fun getHQ(market: String,code: String){
        repository.getHQ(market,code,object :OnResult<TradeStockEntity>{
            override fun onSucceed(response: TradeStockEntity) {
                currentHQ.value = response
            }

            override fun onFailure(error: Error) {
            }

        })
    }

    fun collect(str: String) {
        if (temp < total) {
            collection += str
            temp += 1
            if (temp == total) {
                serverealResult.value = collection
                loading.value = false
            }
        }
    }
}