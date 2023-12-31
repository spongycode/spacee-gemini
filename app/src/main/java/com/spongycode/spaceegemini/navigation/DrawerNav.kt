package com.spongycode.spaceegemini.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.App
import com.spongycode.spaceegemini.R

@Composable
fun DrawerNav(
    selectedItemIndex: Int,
    onItemSelect: (Int) -> Unit,
    onCloseDrawer: () -> Unit,
    navController: NavHostController
) {
    ModalDrawerSheet(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                },
                selected = selectedItemIndex == index,
                onClick = {
                    onItemSelect(index)
                    onCloseDrawer()
                    navController.navigate(item.title) {
                        popUpTo(item.title) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(5.dp)
                    ) {
                        Icon(
                            painterResource(
                                id = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                }
                            ),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = item.title
                        )
                    }
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(60.dp)
            )
        }
    }
}

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

