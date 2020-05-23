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
import com.d4itemgenerator.item.Slot
import org.greenrobot.eventbus.EventBus


class MenuViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private var itemName: TextView? = null
    private var rarity: ImageView? = null
    private var layout: ConstraintLayout? = null
    private var delete: ImageView? = null
    private var itemslot: ImageView? = null

    init {
        itemName = itemView.findViewById(R.id.item_name)
        rarity = itemView.findViewById(R.id.rarity)
        layout = itemView.findViewById(R.id.item_layout)
        delete = itemView.findViewById(R.id.delete)
        itemslot = itemView.findViewById(R.id.itemslot)
    }

    fun bind(item: Item, name: String, position: Int) {
        itemName?.text = name
        setItemSlot(item)
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

    private fun setItemSlot(item: Item) {
        when (item.slot) {
            Slot.AMULET -> itemslot?.setImageResource(R.drawable.amulet)
            Slot.BRACERS -> itemslot?.setImageResource(R.drawable.bracer)
            Slot.WEAPON -> itemslot?.setImageResource(R.drawable.weapon)
            Slot.HELMET -> itemslot?.setImageResource(R.drawable.helm)
            Slot.CHEST_ARMOR -> itemslot?.setImageResource(R.drawable.chest)
            Slot.PANTS -> itemslot?.setImageResource(R.drawable.pants)
            Slot.BOOTS -> itemslot?.setImageResource(R.drawable.boots)
            Slot.RING -> itemslot?.setImageResource(R.drawable.ring)
            Slot.SHIELD -> itemslot?.setImageResource(R.drawable.shield)
        }
    }
}