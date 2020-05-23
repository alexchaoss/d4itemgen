package com.d4itemgenerator.item

import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("affixes")
    var affixes: ArrayList<String> = ArrayList()
    @SerializedName("legendary_affixes")
    var legendaryAffixes: ArrayList<String> = ArrayList()
    @SerializedName("item_name")
    var itemName: String? = ""
    @SerializedName("rarity")
    var rarity: Rarity? = Rarity.MAGIC
    @SerializedName("slot")
    var slot: Slot? = Slot.AMULET
}