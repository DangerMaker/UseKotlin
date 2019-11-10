package com.god.kotlin.transfer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.profile.AccountListActivity
import com.god.kotlin.profile.AccountListViewModel
import com.god.kotlin.util.inflate
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.widget.TitleArrayAdapter
import kotlinx.android.synthetic.main.activity_transfer_menu.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import java.text.SimpleDateFormat
import java.util.*

class TransferRecordActivity : AppCompatActivity() {

    val list = mutableListOf<TransferRecord>()
    private lateinit var arrayAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_menu)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "转账记录"

        with(transfer_list) {
            addHeaderView(inflate(R.layout.trade_holder_transfer_title))
            arrayAdapter = RecordAdapter(list, this@TransferRecordActivity)
            adapter = arrayAdapter
        }

        val viewModel = obtainViewModel(TransferRecodViewModel::class.java)
        viewModel.recordList.observe(this, Observer {
            list.clear()
            Collections.sort(it,DateComparator())
            list.addAll(it)
            arrayAdapter.notifyDataSetChanged()
        })

        viewModel.query("fundid")
    }

    class DateComparator : Comparator<TransferRecord> {

        override fun compare(transferEntity: TransferRecord, t1: TransferRecord): Int {
            val time1 = convertTimeToLong(transferEntity.operdate + transferEntity.opertime)
            val time2 = convertTimeToLong(t1.operdate + t1.opertime)
            return when {
                time1 > time2 -> -1
                time1 < time2 -> 1
                else -> 0
            }
        }

        companion object {

            fun convertTimeToLong(time: String): Long {
                var date: Date?
                return try {
                    val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                    date = sdf.parse(time)
                    date!!.time
                } catch (e: Exception) {
                    e.printStackTrace()
                    0L
                }

            }
        }


    }
}

