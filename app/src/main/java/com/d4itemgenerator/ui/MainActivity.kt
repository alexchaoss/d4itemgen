package com.d4itemgenerator.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.d4itemgenerator.DeleteClickEvent
import com.d4itemgenerator.ItemClickEvent
import com.d4itemgenerator.R
import com.d4itemgenerator.item.GenerateItem
import com.d4itemgenerator.item.Item
import com.d4itemgenerator.item.Rarity
import com.d4itemgenerator.item.Slot
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferences? = null
    private val gson = Gson()
    private val items = mutableListOf<Item>()
    private val itemNames = mutableListOf<String>()
    private var item: Item? = Item()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        recyclerviewmenu.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerviewmenu.apply {
            try {
                val itemMap = sharedPreferences!!.all
                itemNames += itemMap.keys.toList()
                for (item in itemMap.values){
                    val tempItem = gson.fromJson(item.toString(), Item::class.java)
                    items.add(tempItem)
                }

                adapter = MenuAdapter(items, itemNames)
            }catch (e: Exception){
                Log.e("Error", e.message)
            }
        }


        createNewItem()

        refresh.setOnClickListener {
            createNewItem()
        }

        menu.setOnClickListener {
            saveditemmenu.visibility = View.VISIBLE
        }

        close.setOnClickListener {
            saveditemmenu.visibility = View.GONE
        }

        save.setOnClickListener {
            openPrompt()
        }

        share.setOnClickListener {
            val itemBitmap = getBitmapFromView()
            val imageURI = saveImageExternal(itemBitmap)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            val bytes = ByteArrayOutputStream()
            itemBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageURI)
            startActivity(Intent.createChooser(shareIntent, "Select"))
        }
    }

    private fun getBitmapFromView(): Bitmap {
        val itemToShare =
            Bitmap.createBitmap(itemlayout.width, itemlayout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(itemToShare)
        itemlayout.draw(canvas)
        return itemToShare
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun itemClickEventReceived(itemClickEvent: ItemClickEvent) {
        item = itemClickEvent.data
        item_name.text = itemClickEvent.data.itemName
        setImageRarity(itemClickEvent.data)
        setItemSlot(itemClickEvent.data)
        stats.removeAllViews()
        addAffixToLayout(itemClickEvent.data)
        val height =
            getFrameLayoutHeight(itemClickEvent.data.affixes, itemClickEvent.data.legendaryAffixes)
        val paramsLeft = left_border.layoutParams
        paramsLeft.height = height
        left_border.layoutParams = paramsLeft
        val paramsRight = right_border.layoutParams
        paramsRight.height = height
        right_border.layoutParams = paramsRight
        saveditemmenu.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun deleteClickEventReceived(deleteClickEvent: DeleteClickEvent) {
        val builder = AlertDialog.Builder(this,
            R.style.Theme_AppCompat_DayNight_Dialog
        )
            .setTitle("Remove Item?")
            .setPositiveButton("Yes") { dialog, _ ->
                removeItem(deleteClickEvent.item, deleteClickEvent.position)
                dialog.cancel()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .create()
        builder.show()

    }

    private fun createNewItem() {
        stats.removeAllViews()
        val generateItem = GenerateItem()
        generateItem.generateItem()
        val itemName = generateItem.rarity?.name + "\n" + generateItem.slot?.slot
        item_name.text = itemName
        item = Item()
        item?.itemName = itemName
        item?.affixes = generateItem.affixList as ArrayList<String>
        item?.legendaryAffixes = generateItem.legAffixList as ArrayList<String>
        item?.rarity = generateItem.rarity
        item?.slot = generateItem.slot

        setImageRarity(item!!)
        setItemSlot(item!!)
        addAffixToLayout(item!!)
        val height = getFrameLayoutHeight(generateItem.affixList, generateItem.legAffixList)
        val paramsLeft = left_border.layoutParams
        paramsLeft.height = height
        left_border.layoutParams = paramsLeft
        val paramsRight = right_border.layoutParams
        paramsRight.height = height
        right_border.layoutParams = paramsRight
    }

    private fun setItemSlot(item: Item) {
        when (item.slot) {
            Slot.AMULET -> itemslot.setImageResource(R.drawable.amulet)
            Slot.BRACERS -> itemslot.setImageResource(R.drawable.bracer)
            Slot.WEAPON -> itemslot.setImageResource(R.drawable.weapon)
            Slot.HELMET -> itemslot.setImageResource(R.drawable.helm)
            Slot.CHEST_ARMOR -> itemslot.setImageResource(R.drawable.chest)
            Slot.PANTS -> itemslot.setImageResource(R.drawable.pants)
            Slot.BOOTS -> itemslot.setImageResource(R.drawable.boots)
            Slot.RING -> itemslot.setImageResource(R.drawable.ring)
            Slot.SHIELD -> itemslot.setImageResource(R.drawable.shield)
        }
    }

    private fun openPrompt() {
        val editText = EditText(this)
        editText.setTextColor(Color.WHITE)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(getDPMetric(20),0,getDPMetric(20),0)
        params.gravity = Gravity.CENTER
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
            .setTitle("Add New item")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                if (editText.text.toString().length > 15) {
                    Toast.makeText(this, "Name is too long.", Toast.LENGTH_SHORT).show()
                }else {
                    saveItem(editText)
                    dialog.cancel()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create()
        builder.show()
        editText.layoutParams = params
    }

    private fun removeItem(item: Item, position: Int) {
        itemNames.removeAt(position)
        items.removeAt(position)
        Log.i("SIZE", items.size.toString())
        sharedPreferences?.edit()?.remove(item.itemName)?.apply()
        Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show()
        recyclerviewmenu.adapter?.notifyDataSetChanged()
    }

    private fun saveItem(editText: EditText) {
        item?.itemName = editText.text.toString() + " ${item?.slot?.slot}"
        items.add(item!!)
        itemNames.add(item?.itemName!!)
        recyclerviewmenu.adapter?.notifyDataSetChanged()
        val gsonItem = gson.toJson(item)
        sharedPreferences?.edit()?.putString(item?.itemName, gsonItem)?.apply()
        Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show()
    }

    private fun getFrameLayoutHeight(affixList: List<String>, legAffixList: List<String>): Int {
        var height = 0
        val affixes = mutableListOf<String>()
        affixes += affixList
        affixes += legAffixList
        for (affix in affixes) {
            height += when {
                affix.length <= 38 -> {
                    if (affix.matches(" [0-9]+ Defense".toRegex()) || affix.matches(" [0-9]+ Weapon Attack".toRegex())) {
                        getDPMetric(30)
                    } else {
                        getDPMetric(28)
                    }
                }
                affix.length > 65 -> {
                    getDPMetric(55)
                }
                affix.length > 85 -> {
                    getDPMetric(70)
                }
                affix.length > 120 -> {
                    getDPMetric(80)
                }
                else -> {
                    getDPMetric(52)
                }
            }
        }
        return height
    }

    private fun addAffixToLayout(item: Item) {
        for (affix in item.affixes) {
            val statsLayout = LinearLayout(this)
            statsLayout.orientation = LinearLayout.HORIZONTAL

            val affixTextView = TextView(this)
            affixTextView.text = affix
            affixTextView.setTextColor(Color.parseColor("#faebcc"))
            affixTextView.textSize = 17f


            val dot = ImageView(this)
            if (affix.matches(" [0-9]+ Defense".toRegex())) {
                dot.setImageResource(R.drawable.defense_bullet)
                affixTextView.textSize = 19f
            } else if (affix.matches(" [0-9]+ Weapon Attack".toRegex())) {
                dot.setImageResource(R.drawable.attack_bullet)
                affixTextView.textSize = 19f
            } else {
                dot.setImageResource(R.drawable.normbullet)
            }

            statsLayout.addView(dot)
            statsLayout.addView(affixTextView)

            val imageParams = dot.layoutParams as LinearLayout.LayoutParams
            imageParams.setMargins(getDPMetric(5), getDPMetric(0), getDPMetric(5), 0)
            dot.layoutParams = imageParams

            stats.addView(statsLayout)
        }
        for (affix in item.legendaryAffixes) {
            val statsLayout = LinearLayout(this)
            statsLayout.orientation = LinearLayout.HORIZONTAL

            val affixTextView = TextView(this)
            affixTextView.text = affix
            affixTextView.setTextColor(Color.parseColor("#ff3e11"))
            affixTextView.textSize = 17f

            val dot = ImageView(this)
            dot.setImageResource(R.drawable.legbullet)

            statsLayout.addView(dot)
            statsLayout.addView(affixTextView)

            val imageParams = dot.layoutParams as LinearLayout.LayoutParams
            imageParams.setMargins(getDPMetric(5), getDPMetric(0), getDPMetric(5), 0)
            dot.layoutParams = imageParams

            stats.addView(statsLayout)
        }
    }

    private fun setImageRarity(item: Item) {
        when (item.rarity) {
            Rarity.LEGENDARY -> rarity.setImageResource(R.drawable.legendary_new)
            Rarity.RARE -> rarity.setImageResource(R.drawable.new_rare)
            Rarity.MAGIC -> rarity.setImageResource(R.drawable.magic_new)
        }
    }

    private fun getDPMetric(size: Int): Int {
        val scale = this.resources.displayMetrics.density
        return (size * scale + 0.5f).toInt()
    }

    private fun saveImageExternal(image: Bitmap): Uri? {
        var uri: Uri? = null
        try {
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                packageName + "to-share.png"
            )
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.close()
            uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        } catch (e: IOException) {
            Log.d(
                "EXTERNAL IMG SAVE",
                "IOException while trying to write file for sharing: " + e.message.toString()
            )
        }
        return uri
    }
}
