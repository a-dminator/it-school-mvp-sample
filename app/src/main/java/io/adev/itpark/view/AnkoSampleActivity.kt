package io.adev.itpark.view

import android.graphics.Color.BLACK
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.JsonParser
import io.adev.itpark.HolderViews
import io.adev.itpark.adapter
import io.adev.itpark.model.entity.New
import io.adev.itpark.model.okHttpClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView

class AnkoSampleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var data = emptyList<New>()

        recyclerView = recyclerView {

            lparams(matchParent, wrapContent) {
                bottomMargin = dip(10)
            }

            class Views : HolderViews() {
                lateinit var title: TextView
                lateinit var text: TextView
                lateinit var image: ImageView
            }

            adapter<New, Views>({ data }) {
                createView {
                    cardView {

                        cardElevation = 4f
                        useCompatPadding = true

                        verticalLayout {

                            image = imageView {
                                adjustViewBounds = true
                                scaleType = CENTER_CROP
                            }.lparams(matchParent, ((resources.displayMetrics.widthPixels-10)/16f*9f).toInt()) // чтобы соотношение было 16:9

                            title = textView {
                                textSize = 24f
                                textColor = BLACK
                            }.lparams(matchParent, wrapContent) {
                                leftMargin  = dip(16)
                                rightMargin = dip(16)
                                topMargin   = dip(16)
                            }

                            text = textView {
                                textSize = 14f
                                textColor = BLACK
                            }.lparams(matchParent, wrapContent) {
                                leftMargin   = dip(16)
                                rightMargin  = dip(16)
                                topMargin    = dip(16)
                                bottomMargin = dip(24)
                            }

                        }
                    }.lparams(matchParent, wrapContent) {
                        rightMargin  = dip(5)
                        leftMargin   = dip(5)
                        bottomMargin = dip(5)
                    }
                }
                bindView { new ->

                    title.text = new.title
                    text.text  = new.text

                    Glide.with(context)
                            .load(new.image)
                            .into(image)
                }
            }

            layoutManager = LinearLayoutManager(context)
        }

        Observable.create<List<New>>
                { emitter ->

                    val request = Request.Builder()
                            .url("https://api.myjson.com/bins/a829r")
                            .build()

                    val result = okHttpClient.newCall(request)
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
                    data = news
                    recyclerView.adapter.notifyDataSetChanged()
                }

    }

}