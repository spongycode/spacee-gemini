package com.spongycode.spaceegemini.navigation

import com.spongycode.spaceegemini.App
import com.spongycode.spaceegemini.R

data class DrawerNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
)

val items = listOf(
    DrawerNavigationItem(
        title = App.getInstance().getString(R.string.multi_turn_mode),
        selectedIcon = R.drawable.multi_turn_icon,
        unselectedIcon = R.drawable.multi_turn_icon,
    ),
    DrawerNavigationItem(
        title = App.getInstance().getString(R.string.image_mode),
        selectedIcon = R.drawable.image_mode_icon,
        unselectedIcon = R.drawable.image_mode_icon,
    ),
    DrawerNavigationItem(
        title = App.getInstance().getString(R.string.single_turn_mode),
        selectedIcon = R.drawable.single_turn_icon,
        unselectedIcon = R.drawable.single_turn_icon,
    ),
    DrawerNavigationItem(
        title = App.getInstance().getString(R.string.settings),
        selectedIcon = R.drawable.settings_icon,
        unselectedIcon = R.drawable.settings_icon,
    ),
    DrawerNavigationItem(
        title = App.getInstance().getString(R.string.about),
        selectedIcon = R.drawable.about_icon,
        unselectedIcon = R.drawable.about_icon,
    )
)
