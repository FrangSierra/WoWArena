package durdinstudios.wowarena.data.models.warcraft


sealed class WoWClass {
    abstract val id: Int
    inline fun <reified T> specs() : List<Spec<T : WoWClass>>

    class Warrior(override val id: Int = 1) : WoWClass<Warrior>() {
        override val specs: List<Spec<Warrior>> = listOf(Spec.Arms(), Spec.Fury(), Spec.ProtectionWarrior())
    }

    class Paladin(override val id: Int = 2) : WoWClass<Paladin>(){
        override val specs: List<Spec<Paladin>> = listOf(Spec.HolyPaladin(), Spec.ProtectionPaladin(), Spec.Retribution())
    }
    class Hunter(override val id: Int = 3) : WoWClass<Hunter>(){
        override val specs: List<Spec<Hunter>> = listOf(Spec.BeastMastery(), Spec.Marksmanship(), Spec.Survival())
    }
    class Rogue(override val id: Int = 4) : WoWClass<Rogue>(){
        override val specs: List<Spec<Rogue>> = listOf(Spec.Assassination(), Spec.Outlaw(), Spec.Subtetly())
    }
    class Priest(override val id: Int = 5) : WoWClass<Priest>(){
        override val specs: List<Spec<Priest>> = listOf(Spec.Discipline(), Spec.HolyPriest(), Spec.Shadow())
    }
    class DeathKnight(override val id: Int = 6) : WoWClass<DeathKnight>(){
        override val specs: List<Spec<DeathKnight>> = listOf(Spec.Blood(), Spec.FrostDk(), Spec.Unholy())
    }
    class Shaman(override val id: Int = 7) : WoWClass<Shaman>(){
        override val specs: List<Spec<Shaman>> = listOf(Spec.Elemental(), Spec.Enhancement(), Spec.RestorationShamn())
    }
    class Mague(override val id: Int = 8) : WoWClass<Mague>(){
        override val specs: List<Spec<Mague>> = listOf(Spec.FrostMague(), Spec.Fire(), Spec.Arcane())
    }
    class Warlock(override val id: Int = 9) : WoWClass<Warlock>(){
        override val specs: List<Spec<Warlock>> = listOf(Spec.Affliction(), Spec.Demonology(), Spec.Destruction())
    }
    class Monk (override val id: Int = 10): WoWClass<Monk>(){
        override val specs: List<Spec<Monk>> = listOf(Spec.Brewmaster(), Spec.Mistweaver(), Spec.Windwalker())
    }
    class Druid(override val id: Int = 11): WoWClass<Druid>(){
        override val specs: List<Spec<Druid>> = listOf(Spec.Balance(), Spec.Guardian(), Spec.Feral(), Spec.Restoration())
    }
    class DemonHunter (override val id: Int = 12): WoWClass<DemonHunter>(){
        override val specs: List<Spec<DemonHunter>> = listOf(Spec.Havoc(), Spec.Vengeance())
    }
}
sealed class Spec<T : WoWClass<T>> {
    abstract val id: Int

    class Arms(override val id: Int = 71) : Spec<WoWClass.Warrior>()
    class Fury(override val id: Int = 72) : Spec<WoWClass.Warrior>()
    class ProtectionWarrior(override val id: Int = 73) : Spec<WoWClass.Warrior>()

    class HolyPaladin(override val id: Int = 65) : Spec<WoWClass.Paladin>()
    class ProtectionPaladin(override val id: Int = 66) : Spec<WoWClass.Paladin>()
    class Retribution(override val id: Int = 70) : Spec<WoWClass.Paladin>()

    class BeastMastery(override val id: Int = 253) : Spec<WoWClass.Hunter>()
    class Marksmanship(override val id: Int = 254) : Spec<WoWClass.Hunter>()
    class Survival(override val id: Int = 255) : Spec<WoWClass.Hunter>()

    class Assassination(override val id: Int = 259) : Spec<WoWClass.Rogue>()
    class Outlaw(override val id: Int = 260) : Spec<WoWClass.Rogue>()
    class Subtetly(override val id: Int = 261) : Spec<WoWClass.Rogue>()

    class Discipline(override val id: Int = 256) : Spec<WoWClass.Priest>()
    class HolyPriest(override val id: Int = 257) : Spec<WoWClass.Priest>()
    class Shadow(override val id: Int = 258) : Spec<WoWClass.Priest>()

    class Blood(override val id: Int = 250) : Spec<WoWClass.DeathKnight>()
    class FrostDk(override val id: Int = 251) : Spec<WoWClass.DeathKnight>()
    class Unholy(override val id: Int = 252) : Spec<WoWClass.DeathKnight>()

    class Elemental(override val id: Int = 262) : Spec<WoWClass.Shaman>()
    class Enhancement(override val id: Int = 263) : Spec<WoWClass.Shaman>()
    class RestorationShamn(override val id: Int = 264) : Spec<WoWClass.Shaman>()

    class Arcane(override val id: Int = 62) : Spec<WoWClass.Mague>()
    class Fire(override val id: Int = 63) : Spec<WoWClass.Mague>()
    class FrostMague(override val id: Int = 64) : Spec<WoWClass.Mague>()

    class Affliction(override val id: Int = 265) : Spec<WoWClass.Warlock>()
    class Demonology(override val id: Int = 266) : Spec<WoWClass.Warlock>()
    class Destruction(override val id: Int = 267) : Spec<WoWClass.Warlock>()

    class Brewmaster(override val id: Int = 268) : Spec<WoWClass.Monk>()
    class Mistweaver(override val id: Int = 270) : Spec<WoWClass.Monk>()
    class Windwalker(override val id: Int = 269) : Spec<WoWClass.Monk>()


    class Balance(override val id: Int = 102) : Spec<WoWClass.Druid>()
    class Feral(override val id: Int = 103) : Spec<WoWClass.Druid>()
    class Guardian(override val id: Int = 104) : Spec<WoWClass.Druid>()
    class Restoration(override val id: Int = 105) : Spec<WoWClass.Druid>()

    class Havoc(override val id: Int = 577) : Spec<WoWClass.DemonHunter>()
    class Vengeance(override val id: Int = 581) : Spec<WoWClass.DemonHunter>()
}


