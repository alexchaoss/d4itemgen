package com.d4itemgenerator

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNewItem()
        refresh.setOnClickListener {
            createNewItem()
        }
    }

    private fun createNewItem() {
        stats.removeAllViews()
        val generateItem = GenerateItem()
        generateItem.generateItem()

        var height = 0

        var itemName = generateItem.rarity?.name + "\n" + generateItem.slot?.slot
        item_name.text = itemName

        setImageRarity(generateItem)
        addAffixToLayout(generateItem)

        height = getFrameLayoutHeight(generateItem, height)
        val params = middleframe.layoutParams
        params.height = height
        middleframe.layoutParams = params
    }

    private fun getFrameLayoutHeight(
        generateItem: GenerateItem,
        height: Int
    ): Int {
        var height1 = height
        for (affix in generateItem.affixList) {
            if (affix.length > 30) {
                height1 += getDPMetric(35)
            } else if (affix.length > 70) {
                height1 += getDPMetric(60)
            } else {
                height1 += getDPMetric(25)
            }
        }
        for (affix in generateItem.legAffixList) {
            if (affix.length > 30) {
                height1 += getDPMetric(35)
            } else if (affix.length > 70) {
                height1 += getDPMetric(60)
            } else {
                height1 += getDPMetric(25)
            }
        }
        height1 += getDPMetric(30)
        return height1
    }

    private fun addAffixToLayout(generateItem: GenerateItem) {
        for (affix in generateItem.affixList) {
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
        for (affix in generateItem.legAffixList) {
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

    private fun setImageRarity(generateItem: GenerateItem) {
        when (generateItem.rarity) {
            Rarity.MYTHIC -> rarity.setImageResource(R.drawable.legendary)
            Rarity.RARE -> rarity.setImageResource(R.drawable.rare)
            Rarity.MAGIC -> rarity.setImageResource(R.drawable.magic)
        }
    }

    private fun getDPMetric(size: Int): Int {
        val scale = this.resources.displayMetrics.density
        return (size * scale + 0.5f).toInt()
    }
}
