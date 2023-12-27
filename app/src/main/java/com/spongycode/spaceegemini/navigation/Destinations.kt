package com.spongycode.spaceegemini.navigation

import com.spongycode.spaceegemini.App
import com.spongycode.spaceegemini.R

interface Destinations {
    val route: String
}

object SingleTurnMode : Destinations {
    override val route: String = App.getInstance().getString(R.string.single_turn_mode)
}

object MultiTurnMode : Destinations {
    override val route = App.getInstance().getString(R.string.multi_turn_mode)
}

object ImageMode : Destinations {
    override val route = App.getInstance().getString(R.string.image_mode)
}

object Settings : Destinations {
    override val route = App.getInstance().getString(R.string.settings)
}

object SetApi : Destinations {
    override val route = App.getInstance().getString(R.string.set_api)
}

object About : Destinations {
    override val route = App.getInstance().getString(R.string.about)
}