package com.d4itemgenerator.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.d4itemgenerator.DeleteClickEvent
import com.d4itemgenerator.ItemClickEvent
import com.d4itemgenerator.R
import com.d4itemgenerator.item.GenerateItem
import com.d4itemgenerator.item.Item
import com.d4itemgenerator.item.Rarity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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
        stats.removeAllViews()
        addAffixToLayout(itemClickEvent.data)
        val height =
            getFrameLayoutHeight(itemClickEvent.data.affixes, itemClickEvent.data.legendaryAffixes)
        val params = middleframe.layoutParams
        params.height = height
        middleframe.layoutParams = params
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

        setImageRarity(item!!)
        addAffixToLayout(item!!)
        val height = getFrameLayoutHeight(generateItem.affixList, generateItem.legAffixList)
        val params = middleframe.layoutParams
        params.height = height
        middleframe.layoutParams = params
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
                if(editText.text.toString().length > 20){
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
        var name = editText.text.toString()
        if(name.length > 10){
            var tempString = name.substring(0..name.indexOf(" ", 4))
            tempString  += "\n" + name.subSequence(name.indexOf(" ", 4) + 1, name.length)
            name = tempString
        }
        item?.itemName = name
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
                affix.length > 30 -> {
                    getDPMetric(35)
                }
                affix.length > 70 -> {
                    getDPMetric(60)
                }
                else -> {
                    getDPMetric(25)
                }
            }
        }
        height += getDPMetric(30)
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
            dot.setImageResource(R.drawable.normbullet)
            statsLayout.addView(dot)
            statsLayout.addView(affixTextView)

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

            stats.addView(statsLayout)
        }
    }

    private fun setImageRarity(item: Item) {
        when (item.rarity) {
            Rarity.LEGENDARY -> rarity.setImageResource(R.drawable.legendary)
            Rarity.RARE -> rarity.setImageResource(R.drawable.rare)
            Rarity.MAGIC -> rarity.setImageResource(R.drawable.magic)
        }
    }

    private fun getDPMetric(size: Int): Int {
        val scale = this.resources.displayMetrics.density
        return (size * scale + 0.5f).toInt()
    }
}
