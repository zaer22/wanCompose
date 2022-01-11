package com.lolilicker.wanjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lolilicker.wanjetpackcompose.page.hatchingPage
import com.lolilicker.wanjetpackcompose.page.infantPage
import com.lolilicker.wanjetpackcompose.page.welcomePage
import com.lolilicker.wanjetpackcompose.widget.DatePicker
import com.rengwuxian.wecompose.ui.theme.WeComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeComposeTheme() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Welcome.route) {
                    composable(Screen.Welcome.route) {
                        welcomePage(navController = navController)
                    }
                    composable(Screen.Hatching.route) {
                        hatchingPage(navController = navController)
                    }
                    composable(Screen.Infant.route) {
                        infantPage(navController = navController)
                    }
                }
            }
        }
    }
}