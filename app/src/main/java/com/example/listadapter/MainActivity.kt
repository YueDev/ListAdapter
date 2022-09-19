package com.example.listadapter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val beansLiveData = MutableLiveData(
        List(100) {
            PagBean("$it", getRandomColor())
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 4)


        //classic adapter
//        val items = pagBeans.value?.toMutableList() ?: return
//
//        val adapter = PagAdapterClassic(items)
//        recyclerView.adapter = adapter
//
//        ItemTouchHelper(object :
//            SimpleCallback(ItemTouchHelper.START or ItemTouchHelper.END or ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
//            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                val srcIndex = viewHolder.adapterPosition
//                val dstIndex = target.adapterPosition
//                val removed = items.removeAt(srcIndex)
//                items.add(dstIndex, removed)
//                adapter.notifyItemMoved(srcIndex, dstIndex)
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//            }
//        }).attachToRecyclerView(recyclerView)


        //list adapter
        //list adapter牵扯到滑动的话，一定给bean一个唯一的index, 比如uuid
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        val adapter = PagAdapter(::clickItem)
        recyclerView.adapter = adapter
        ItemTouchHelper(object :
            SimpleCallback(ItemTouchHelper.START or ItemTouchHelper.END or ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val beans = beansLiveData.value ?: return false
                val srcIndex = viewHolder.adapterPosition
                val dstIndex = target.adapterPosition
                val newBeans = mutableListOf(*beans.toTypedArray())
                val newBean = newBeans.removeAt(srcIndex).copy()
                newBeans.add(dstIndex, newBean)
                beansLiveData.value = newBeans
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }).attachToRecyclerView(recyclerView)

        beansLiveData.observe(this) {
            adapter.submitList(it)
        }


    }


    private fun clickItem(postion: Int, clickBean: PagBean) {
        //先获取之前的选中状态
        val beans = beansLiveData.value ?: return
        val selectBean = beans.find { it.isSelect } ?: run {
            //之前没有选中
            val newClickBean = clickBean.copy(isSelect = true)
            val index = beans.indexOf(clickBean)
            val newBeans = mutableListOf(*beans.toTypedArray())
            newBeans.removeAt(index)
            newBeans.add(index, newClickBean)
            beansLiveData.value = newBeans
            return
        }
        if (selectBean.uuid == clickBean.uuid) {
            Toast.makeText(this, "do pag text: ${selectBean.text}", Toast.LENGTH_SHORT).show()
        } else {
            //新的选中
            val oldIndex = beans.indexOf(selectBean)
            val newIndex = beans.indexOf(clickBean)
            val oldCopy = selectBean.copy(isSelect = false)
            val newCopy = clickBean.copy(isSelect = true)
            val newBeans = mutableListOf(*beans.toTypedArray())
            newBeans.removeAt(oldIndex)
            newBeans.add(oldIndex, oldCopy)
            newBeans.removeAt(newIndex)
            newBeans.add(newIndex, newCopy)
            beansLiveData.value = newBeans
        }
    }


    private fun getRandomColor() = Color.argb(255, (0..255).random(), (0..255).random(), (0..255).random())

}