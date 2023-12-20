package com.spongycode.spaceegemini.navigation

interface Destinations
{
    val route:String
}
object Home: Destinations {
    override val route="Home"
}
object About: Destinations {
    override val route="About"
}