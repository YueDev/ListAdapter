package com.example.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Yue on 2022/8/8.
 */
class PagAdapter(
    private val itemClick: (position: Int, clickBean: PagBean) -> Unit
) : ListAdapter<PagBean, PagHolder>(PagDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagHolder {
        val holder = PagHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pag, parent, false))
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            itemClick(position, getItem(position))
        }
        holder.itemView.setOnLongClickListener {
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: PagHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}


class PagHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textView = itemView.findViewById<TextView>(R.id.textView)
    private val selectView = itemView.findViewById<View>(R.id.selectView)

    fun onBind(pagBean: PagBean) {
        textView.text = pagBean.text
        textView.setBackgroundColor(pagBean.color)
        selectView.visibility = if (pagBean.isSelect) View.VISIBLE else View.GONE
    }
}




object PagDiffCallback : DiffUtil.ItemCallback<PagBean>() {

    override fun areItemsTheSame(oldItem: PagBean, newItem: PagBean) = oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(oldItem: PagBean, newItem: PagBean) = oldItem == newItem

}


class PagAdapterClassic(private val items: List<PagBean>) : RecyclerView.Adapter<PagHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagHolder {
        return PagHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pag, parent, false))
    }

    override fun onBindViewHolder(holder: PagHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

}
