package com.god.kotlin

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ez08.trade.*
import com.ez08.trade.client.SimpleSocketClient
import com.ez08.trade.request.KeyExchangeRequest
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //获取验证码
        button.setOnClickListener {
            createVerityClientAndSend()
        }

        //握手
        button1.setOnClickListener {
            createBizClient()
        }
    }


    private fun createBizClient() {
        val bizClient = ClientHelper.get()
        bizClient.setOnConnectListener(object : ConnectListener{
            override fun connectSuccess(client: Client) {
                sendHandShakePackage(client)
            }

            override fun connectFail(client: Client) {
            }

            override fun connectLost(client: Client) {
            }
        })
        bizClient.connect()
    }

    private fun sendHandShakePackage(client: Client) {
        val request = KeyExchangeRequest()
        request.setCallback { client, data ->
            if (data.isSucceed) {
                Log.e(TAG, data.data)
//                setLoginPackage(client)
            } else {

            }
        }
        client.send(request)
    }


    var szId: ByteArray? = null
    var reserve = "0"

    private fun createVerityClientAndSend() { //获取验证码图片 type = 0  验证验证码 type = 1
        val verityClient = SimpleSocketClient(Constant.SERVER_IP, Constant.VERITY_SERVER_PORT)
        verityClient.setCallback { _, data ->
            if (data.isSucceed) {
                runOnUiThread {
                    try {
                        val jsonObject = JSONObject(data.data)
                        val pic = jsonObject.getString("bufPic")
                        val picReal = Base64.decode(pic, Base64.DEFAULT)
                        szId = jsonObject.getString("szId").toByteArray(charset("GB2312"))
                        reserve = jsonObject.getString("reserve")
                        val decodedByte = BitmapFactory.decodeByteArray(picReal, 0, picReal.size)
                        safe_code_iv.setImageBitmap(decodedByte)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            } else {
                data.data.toast(application)
            }
        }
        verityClient.send(null)
    }
}

private const val TAG = "test_socket"