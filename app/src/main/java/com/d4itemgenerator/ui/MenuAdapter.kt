package com.d4itemgenerator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d4itemgenerator.item.Item

class MenuAdapter(var list: List<Item>, var nameList: List<String>)
    : RecyclerView.Adapter<MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenuViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item: Item = list[holder.adapterPosition]
        val name: String = nameList[holder.adapterPosition]
        holder.bind(item, name, holder.adapterPosition)

    }

    override fun getItemCount(): Int = list.size
}