package com.example.androidtest.adapter

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import com.example.androidtest.model.DataListModel
import kotlinx.android.synthetic.main.list_item.view.*


class RecyclerAdapter(
    val list: ArrayList<DataListModel>,
    val context: Context,
    val listner: OnSelect
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                com.example.androidtest.R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTitle.text = list[position].title
        holder.textdate.text = list[position].created_at
        if (list[position].isSelected) {
            // The switch is enabled/checked
            holder.layItem.setBackgroundColor(Color.GRAY)
            holder.switch.isChecked = true
        } else {
            // The switch is disabled
            holder.layItem.setBackgroundColor(Color.WHITE)
            holder.switch.isChecked = false
        }

        holder.switch.setOnClickListener { view ->
            if (!list[position].isSelected) {
                // The switch is enabled/checked
                holder.layItem.setBackgroundColor(Color.GRAY)
                list[position].isSelected = true;
                listner.onSelect(true)
            } else {
                // The switch is disabled
                holder.layItem.setBackgroundColor(Color.WHITE)
                listner.onSelect(false)
                list[position].isSelected = false
            }
        }
//        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                // The switch is enabled/checked
//                holder.layItem.setBackgroundColor(Color.GRAY)
//                list[position].isSelected = 1;
//                listner.onSelect(isChecked)
//            } else {
//                // The switch is disabled
//                holder.layItem.setBackgroundColor(Color.WHITE)
//                listner.onSelect(isChecked)
//                list[position].isSelected = 0
//            }
//        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layItem = itemView.layItem
        var textTitle = itemView.txtTitle
        var textdate = itemView.txtDate
        var switch = itemView.selectionSwitch


    }

    interface OnSelect {
        fun onSelect(b: Boolean)
    }
}