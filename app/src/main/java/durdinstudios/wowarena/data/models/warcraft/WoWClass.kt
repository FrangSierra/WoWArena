package durdinstudios.wowarena.data.models.warcraft


sealed class WoWClass<T : WoWClass<T>>(clazz: Class<T>) {
    abstract val id: Int
    val specs : List<Spec<*>> = Spec::class.nestedClasses.filterIsInstance(Spec)

    class Warrior(override val id: Int = 1) : WoWClass<Warrior>()
    class Paladin(override val id: Int = 2) : WoWClass<Paladin>()
    class Hunter(override val id: Int = 3) : WoWClass<Hunter>()
    class Rogue(override val id: Int = 4) : WoWClass<Rogue>()
    class Priest(override val id: Int = 5) : WoWClass<Priest>()
    class DeathKnight(override val id: Int = 6) : WoWClass<DeathKnight>()
    class Shaman(override val id: Int = 7) : WoWClass<Shaman>()
    class Mague(override val id: Int = 8) : WoWClass<Mague>()
    class Warlock(override val id: Int = 9) : WoWClass<Warlock>()
    class Monk (override val id: Int = 10): WoWClass<Monk>()
    class Druid(override val id: Int = 11): WoWClass<Druid>()
    class DemonHunter (override val id: Int = 12): WoWClass<DemonHunter>()
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

    class Balance(override val id: Int = 102) : Spec<WoWClass.Druid>()
    class Feral(override val id: Int = 103) : Spec<WoWClass.Druid>()
    class Guardian(override val id: Int = 104) : Spec<WoWClass.Druid>()
    class Restoration(override val id: Int = 105) : Spec<WoWClass.Druid>()

    class Havoc(override val id: Int = 577) : Spec<WoWClass.DemonHunter>()
    class Vengeance(override val id: Int = 581) : Spec<WoWClass.DemonHunter>()


}


