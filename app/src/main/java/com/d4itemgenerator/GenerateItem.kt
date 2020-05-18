package com.d4itemgenerator

import kotlin.random.Random

class GenerateItem {

    var slot: Slot? = null
    var rarity: Rarity? = null
    var legAffixList = mutableListOf<String>()
    var affixList = mutableListOf<String>()
    var characterClass: CharacterClass? = null

    var amuletaffix = listOf(
        "+[1-5-10][50-40-30] All Resistances",
        "Cannot be frozen",
        "+[25-15-5][50-25-10]% Reduced Cooldown Time {timecond}",
        "+[1-1-1][25-12-5]% Reduced Cooldown Time",
        "+[5-5-5][25-20-15]% Health",
        "+[5-5-5][25-20-15]% Damage",
        "+[5-5-5][25-20-15]% {dmgtype} Damage",
        "+[1-1-2][10-8-5]% Critical Strike Chance",
        "+[10-10-10][100-75-50]% Critical Strike Damage",
        "+[1-1-1][3-2-1] to all Ranks",
        "+[1-1-1][3-2-1] to all {class} Ranks",
        "+[5-8-10][50-40-25]% Magic Find"
    )
    var amuletcasteraffix = listOf(
        "+[5-5-8][25-20-15]% Faster Cast Rate",
        "+[5-10-15][50-40-30]% Faster Cast Rate {timecond}"
    )
    var ringaffix = amuletaffix;
    var gloveaffix = listOf(
        "Increases attack speed by [1-3-3][10-8-5]%",
        "Increases attack speed by [8-5-5][20-15-10]% {timecond}",
        "+[5-5-5][25-20-15]% Damage",
        "+[5-5-5][25-20-15]% {dmgtype} Damage",
        "+[50-50-50][250-200-150]% Damage {timecond|monstercond}",
        "+[50-50-50][250-200-150]% {dmgtype} Damage {timecond|monstercond}"
    )
    var armoraffix = listOf(
        "+[5-5-5][25-20-15]% Health",
        "+[3-3-5][25-20-15]% Ranged Damage Reduction",
        "+[3-3-5][25-20-15]% Melee Damage Reduction",
        "+[5-3-2][25-12-8]% Damage Reduction"
    )
    var chestaffix = listOf<String>()
    var pantsaffix = listOf<String>()
    var helmetsaffix = listOf<String>()
    var bootsaffix = listOf(
        "+[5-5-10][25-20-15]% Movement Speed",
        "+[1-5-10][50-40-30]% Movement Speed {timecond}"
    )
    var weaponaffix = listOf(
        "+[5-5-5][25-20-15]% Damage",
        "+[5-5-5][25-20-15]% {dmgtype} Damage",
        "+[50-50-50][250-200-150]% Damage {timecond|monstercond}",
        "+[50-50-50][250-200-150]% {dmgtype} Damage {timecond|monstercond}"
    )
    var weaponaffixnoncaster = listOf(
        "Increases attack speed by [1-3-3][10-8-5]%",
        "Increases attack speed by [8-5-5][20-15-10]% {timecond}",
        "+[5-10-20][100-80-50]% Physical Damage converted to {restype}"
    )
    var weaponaffixcaster = listOf(
        "+[5-5-8][25-20-15]% Faster Cast Rate",
        "+[5-10-15][50-40-30]% Faster Cast Rate {timecond}"
    )
    var shieldaffix = listOf(
        "+[1-5-10][50-40-30] All Resistances",
        "Cannot be frozen",
        "+[5-5-10][50-40-30]% Damage after blocking an attack",
        "+[1-1-1][25-20-15]% Increased block chance"
    )
    var affix = listOf(
        "+[5-15-20][100-50-35]% Health Regeneration",
        "+[5-15-20][100-50-35]% Ressource Regeneration",
        "+[5-5-10][50-40-30] {restype} Resistance",
        "+[5-5-8][50-25-15]% experience gained",
        "Reduces the duration of control impairing effects by [5-5-5][20-15-10]%",
        "+[10-10-25][200-100-75] Health",
        "[1-1-1][3-2-1] Sockets",
        "+[1-1-1][5-3-2] to a {class} Skill",
        "+[1-1-1][5-4-3]% Leech Life"
    )
    var legendaryaffix = listOf(
        "Slain monsters rest in peace",
        "+[5-15-5][100-50-35]% Chance to split projectiles",
        "+[5-5-5][50-40-10] {restype} Absorb",
        "You leave a trail of fire in your wake",
        "Stunning enemies freeze them",
        "Your dash is twice as long",
        "Your dash cooldown is reduced by half",
        "Town portal casting time reduced by half",
        "Potion cooldown reduced by [1-1-1][3-2-3] seconds"
    )

    var monstercond = listOf(
        "against demons",
        "against undead",
        "against stunned targets",
        "against slowed targets",
        "against elite monsters",
        "against boss monsters",
        "against burning monsters",
        "against frozen monsters",
        "against poisoned monsters",
        "against monsters below [5-10-15][30-25-20]% health"
    )
    var timecond = listOf(
        "[1-1-1][4-3-2] seconds after using a skill",
        "[2-2-2][5-4-3]  seconds after exiting a stun effect",
        "[2-2-2][5-4-3] seconds after dashing",
        "[2-2-2][5-4-3] seconds after a critical strike",
        "[3-3-3][8-6-5] seconds after killing a monster",
        "[3-3-4][8-6-5] seconds after killing an elite monster",
        "[5-5-5][15-12-10] seconds after using a potion",
        "when full health",
        "when your ressource is full",
        "when under [5-10-15][30-25-20]% of your max health",
        "when slowed",
        "when you are unstoppable"
    )
    var timecondbarb = listOf(
        "[1-1-1][4-3-2] seconds after switching arsenal",
        "[1-1-1][4-3-2] seconds after using a war cry"
    )
    var timeconddruid = listOf(
        "[1-1-1][4-3-2] seconds after shapeshifting",
        "[3-3-3][8-6-5] seconds after one of your pet dies"
    )

    fun generateItem() {
        rarity = Rarity.values()[(0..2).random()]
        slot = Slot.values()[(0..8).random()]
        characterClass = CharacterClass.values()[(0..4).random()]

        var affixCount = intArrayOf(1, 2, 2, 2, 3).random()
        var legCount = 0
        if (rarity == Rarity.LEGENDARY) {
            affixCount = 4
            legCount = intArrayOf(1, 2, 2, 2, 3).random()
        }

        val affixToChoose = affix.toMutableList()
        val legAffixToChoose: MutableList<String> = mutableListOf()

        getAffixes(affixToChoose, characterClass!!)
        getLegAffixes(legAffixToChoose, characterClass!!)

        amuletaffix = amuletaffix + gloveaffix
        amuletaffix = amuletaffix + chestaffix
        amuletaffix = amuletaffix + bootsaffix
        amuletaffix = amuletaffix + weaponaffix
        amuletaffix = amuletaffix + legendaryaffix

        for (i in 1..affixCount) {
            affixList.add(affixToChoose.random())
        }

        for (i in 1..legCount) {
            legAffixList.add(legAffixToChoose.random())
        }

        chooseAffixRange()
        chooseLegAffixRange()

    }

    private fun chooseAffixRange() {
        for (affix in affixList) {
            val index = affixList.indexOf(affix)
            val results = "\\[.+?]".toRegex().find(affix)
            if (results != null) {
                val min = results.value.replace("[", "").replace("]", "")
                val max = results.next()?.value?.replace("[", "")?.replace("]", "")

                var rarityIndex = 0
                when (rarity) {
                    Rarity.RARE -> rarityIndex = 1
                    Rarity.LEGENDARY -> rarityIndex = 2
                }

                val minRange = min.split("-".toRegex())[rarityIndex].toInt()
                val maxrange = max!!.split("-".toRegex())[rarityIndex].toInt()
                affixList[affixList.indexOf(affix)] = affixList[affixList.indexOf(affix)].replace(
                    affix.substring(affix.indexOf("["), affix.lastIndexOf("]") + 1),
                    Random.nextInt(minRange, maxrange + 1).toString()
                )
                replaceValues(index, affixList)
            }
        }
    }

    private fun chooseLegAffixRange() {
        for (affix in legAffixList) {
            val index = legAffixList.indexOf(affix)
            val results = "\\[.+?]".toRegex().find(affix)
            if (results != null) {
                val min = results.value.replace("[", "").replace("]", "")
                val max = results.next()?.value?.replace("[", "")?.replace("]", "")

                var rarityIndex = 0
                when (rarity) {
                    Rarity.RARE -> rarityIndex = 1
                    Rarity.LEGENDARY -> rarityIndex = 2
                }

                val minRange = min.split("-".toRegex())[rarityIndex].toInt()
                val maxrange = max!!.split("-".toRegex())[rarityIndex].toInt()
                legAffixList[index] = legAffixList[index].replace(
                    affix.substring(affix.indexOf("["), affix.lastIndexOf("]") + 1),
                    Random.nextInt(minRange, maxrange + 1).toString()
                )
                replaceValues(index, legAffixList)
            }
        }
    }

    private fun replaceValues(index: Int, affixList: MutableList<String>) {
        affixList[index] =
            affixList[index].replace("{dmgtype}", DamageType.values().random().damage)
                .replace("{restype}", Resistance.values().random().res)
                .replace("{class}", CharacterClass.values().random().classType)

        if (affixList[index].contains("{timecond}") || affixList[index].contains("{timecond|monstercond}")) {
            val monsterCond = affixList[index].contains("{timecond|monstercond}")
            var timecondRandom = getRandomTimeCond(monsterCond)
            val results = "\\[.+?]".toRegex().find(timecondRandom)
            if (results != null) {
                val min = results.value.replace("[", "").replace("]", "")
                val max = results.next()?.value?.replace("[", "")?.replace("]", "")

                var rarityIndex = 0
                when (rarity) {
                    Rarity.RARE -> rarityIndex = 1
                    Rarity.LEGENDARY -> rarityIndex = 2
                }

                val minRange = min.split("-".toRegex())[rarityIndex].toInt()
                val maxrange = max!!.split("-".toRegex())[rarityIndex].toInt()

                timecondRandom = timecondRandom.replace(
                    timecondRandom.substring(
                        timecondRandom.indexOf("["),
                        timecondRandom.lastIndexOf("]") + 1
                    ),
                    Random.nextInt(minRange, maxrange + 1).toString()
                )
            }
            if (monsterCond) {
                affixList[index] =
                    affixList[index].replace("{timecond|monstercond}", timecondRandom)
            } else
                affixList[index] = affixList[index].replace("{timecond}", timecondRandom)
        }
    }

    private fun getRandomTimeCond(monsterCondition: Boolean): String {
        val timecondList = timecond.toMutableList()
        if (monsterCondition) {
            timecondList += monstercond
        }
        if (characterClass == CharacterClass.BARB) {
            timecondList += timecondbarb
        } else if (characterClass == CharacterClass.DRUID) {
            timecondList += timeconddruid
        }
        return timecondList.random()
    }

    private fun getAffixes(affixToChoose: MutableList<String>, characterClass: CharacterClass) {
        when (slot) {
            Slot.AMULET -> {
                affixToChoose += amuletaffix
                if (characterClass == CharacterClass.SORC || characterClass == CharacterClass.PALADIN) {
                    affixToChoose += amuletcasteraffix
                }
            }
            Slot.RING -> affixToChoose += ringaffix
            Slot.GLOVES -> {
                affixToChoose += gloveaffix
                affixToChoose += armoraffix
            }
            Slot.CHEST_ARMOR, Slot.HELMET, Slot.PANTS -> affixToChoose += armoraffix
            Slot.BOOTS -> {
                affixToChoose += bootsaffix
                affixToChoose += armoraffix
            }
            Slot.SHIELD -> {
                affixToChoose += shieldaffix
                affixToChoose += armoraffix
            }
            Slot.WEAPON -> {
                affixToChoose += weaponaffix
                if (characterClass == CharacterClass.SORC || characterClass == CharacterClass.PALADIN) {
                    affixToChoose += weaponaffixcaster
                }
                if (characterClass != CharacterClass.SORC) {
                    affixToChoose += weaponaffixnoncaster
                }
            }
        }
    }

    private fun getLegAffixes(
        legAffixToChoose: MutableList<String>,
        characterClass: CharacterClass
    ) {
        if (slot != Slot.AMULET && slot != Slot.RING) {
            legAffixToChoose += amuletaffix

            if (characterClass == CharacterClass.SORC || characterClass == CharacterClass.PALADIN) {
                legAffixToChoose += amuletcasteraffix
            }
        }
        if (slot != Slot.WEAPON) {
            legAffixToChoose += weaponaffix
        }
        if (slot != Slot.BOOTS) {
            legAffixToChoose += bootsaffix
        }
        if (slot != Slot.GLOVES) {
            legAffixToChoose += gloveaffix
        }
        if (slot != Slot.SHIELD) {
            legAffixToChoose += shieldaffix
        }
        if (slot == Slot.AMULET || slot == Slot.RING || slot == Slot.WEAPON) {
            legAffixToChoose += armoraffix
        }
    }
}