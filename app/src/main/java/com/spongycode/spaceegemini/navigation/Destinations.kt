package com.spongycode.spaceegemini.navigation

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route = "Home"
}
object MultiTurn : Destinations {
    override val route = "MultiTurn"
}

object About : Destinations {
    override val route = "About"
}