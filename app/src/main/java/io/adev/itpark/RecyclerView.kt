package io.adev.itpark

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.recyclerview.v7._RecyclerView
import kotlin.reflect.KClass

abstract class HolderViews : AnkoContext<Context> {
    lateinit var itemView: View
    lateinit var impl: AnkoContext<Context>
    override val ctx get() = impl.ctx
    override val view get() = impl.view
    override val owner get() = impl.owner
    override fun addView(p0: View?, p1: ViewGroup.LayoutParams?) = impl.addView(p0, p1)
}

class _RecyclerViewHolder<out V : HolderViews>(
        itemView: View,
        val views: V) : RecyclerView.ViewHolder(itemView)

class _RecyclerViewAdapter<M, V : HolderViews>(
        private val context: Context,
        private val viewsClass: KClass<V>) : RecyclerView.Adapter<_RecyclerViewHolder<V>>() {

    private var setData: List<M>? = null

    private var getData = { emptyList<M>() }
    fun data(getData: () -> List<M>) {
        this.getData = getData
    }

    var data: List<M>
        get() {
            return if (setData != null) {
                setData!!
            } else {
                getData()
            }
        }
        set(value) {
            setData = value
            notifyDataSetChanged()
        }

    private var _createView: (V.() -> View)? = null
    fun createView(createView: V.() -> View) {
        _createView = createView
    }

    private var _bindView: (V.(M) -> Unit)? = null
    fun bindView(bindView: V.(M) -> Unit) {
        _bindView = bindView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: _RecyclerViewHolder<V>?, position: Int) {
        _bindView?.let { holder?.views?.it(data[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): _RecyclerViewHolder<V> {
        val views = viewsClass.constructors.first().call()
        views.impl = AnkoContext.create(context, context)
        val itemView = _createView?.let { views.it() }!!
        if (itemView.parent != null) (itemView.parent as? ViewGroup)?.removeView(itemView)
        views.itemView = itemView
        return _RecyclerViewHolder(itemView, views)
    }

}

inline fun <M, reified V : HolderViews> _RecyclerView.adapter(init: _RecyclerViewAdapter<M, V>.() -> Unit): _RecyclerViewAdapter<M, V> {

    val result =
            _RecyclerViewAdapter<M, V>(context, V::class).apply {
                init()
            }

    adapter = result

    return result
}