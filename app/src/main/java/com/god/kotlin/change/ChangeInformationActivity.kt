package com.god.kotlin.change

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.activity_information_change.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class ChangeInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_change)

        toolbar_title.text = "修改资料"
        toolbar_back.setOnClickListener { finish() }

        val viewModel = obtainViewModel(InformationViewModel::class.java)
        viewModel.information.observe(this, Observer {
            information_name.setText(it.name)
            information_sex.setText(if (it.sex == "1"){ "男" }else {"女"})
            information_id_type.setText(it.idType)
            information_card.setText(it.idCard)
            information_phone.setText(it.phone)
            information_post.setText(it.postCode)
            information_email.setText(it.email)
            information_address.setText(it.address)
        })

        information_submit.setOnClickListener {
            viewModel.post(
                information_id_type.text.toString(),
                information_card.text.toString(),
                information_phone.text.toString(),
                information_post.text.toString(),
                information_email.text.toString(),
                information_address.text.toString()
            )
        }

        viewModel.result.observe(this, Observer {
            it.toast(this)
        })

        viewModel.query()
    }
}