package io.adev.itpark.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.JsonParser
import io.adev.itpark.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        //создали в котлине список данных  который мы будем отображать(список новостей)

        Observable.create<List<New>>
                { emitter ->

                    val client = OkHttpClient.Builder()
                            .readTimeout(1000L,TimeUnit.MILLISECONDS)
                            .build()

                    val request = Request.Builder()
                            .url("https://api.myjson.com/bins/a829r")
                            .build()

                    val result = client.newCall(request)
                            .execute()
                            .body().string()

                    val resultJson = JsonParser().parse(result).asJsonObject
                    val code = resultJson["code"].asInt

                    val responseArray = resultJson["response"].asJsonArray
                    val news =
                            responseArray.map { el ->
                                val newJson = el.asJsonObject

                                val title = newJson["title"].asString
                                val text  = newJson["text"].asString
                                val image = newJson["image"].asString

                                New(title, text, image)
                            }

                    emitter.onNext(news)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { news ->
                    val adapter = NewsAdapter()
                    adapter.setData(news)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
                }


    }

}
