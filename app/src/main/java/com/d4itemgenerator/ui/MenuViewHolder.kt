package com.d4itemgenerator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.d4itemgenerator.DeleteClickEvent
import com.d4itemgenerator.ItemClickEvent
import com.d4itemgenerator.R
import com.d4itemgenerator.item.Item
import com.d4itemgenerator.item.Rarity
import org.greenrobot.eventbus.EventBus


class MenuViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private var itemName: TextView? = null
    private var rarity: ImageView? = null
    private var layout: ConstraintLayout? = null
    private var delete: ImageView? = null

    init {
        itemName = itemView.findViewById(R.id.item_name)
        rarity = itemView.findViewById(R.id.rarity)
        layout = itemView.findViewById(R.id.item_layout)
        delete = itemView.findViewById(R.id.delete)
    }

    fun bind(item: Item, name: String, position: Int) {
        itemName?.text = name
        when (item.rarity) {
            Rarity.LEGENDARY -> rarity?.setImageResource(R.drawable.legendary)
            Rarity.RARE -> rarity?.setImageResource(R.drawable.rare)
            Rarity.MAGIC -> rarity?.setImageResource(R.drawable.magic)
        }

        layout?.setOnClickListener{
            EventBus.getDefault().post(ItemClickEvent(item))
        }

        delete?.setOnClickListener {
            EventBus.getDefault().post(DeleteClickEvent(position, item))
        }
    }
}