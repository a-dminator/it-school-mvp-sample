package io.adev.itpark.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.JsonParser
import io.adev.itpark.R
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class NewsActivity : AppCompatActivity() {

    class New(
        val title: String,//заголовок новости
        val text: String,
        val image: String)//текст новости

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val client = OkHttpClient.Builder()
                .readTimeout(1000L,TimeUnit.MILLISECONDS)
                .build()

        val task = Runnable {

            val request = Request.Builder()
                    .url("https://api.myjson.com/bins/a829r")
                    .build()

            val result = client.newCall(request)
                    .execute()
                    .body().string()

            val resultJson = JsonParser().parse(result).asJsonObject
            val code = resultJson["code"].asInt

            val news = mutableListOf<New>()

            val responseArray = resultJson["response"].asJsonArray
            for (el in responseArray) {
                val newJson = el.asJsonObject

//                val ПОЛЕ = нужныйJson["ПОЛЕ"].asНужныйТип

                // пример
                val title = newJson["title"].asString
                val text  = newJson["text"].asString
                val image = newJson["image"].asString

                news.add(
                        New(title, text, image))
            }

            runOnUiThread {
                val adapter = NewsAdapter()
                adapter.setData(news)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }

            Log.e("NewsActivity", result)
        }

        Thread(task).start()

        //создали в котлине список данных  который мы будем отображать(список новостей)

    }

}
