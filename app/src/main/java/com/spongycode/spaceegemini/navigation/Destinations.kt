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

object ImageText : Destinations {
    override val route = "ImageText"
}

object Settings : Destinations {
    override val route = "Settings"
}

object SetApiScreen : Destinations {
    override val route = "SetApiScreen"
}


object About : Destinations {
    override val route = "About"
}