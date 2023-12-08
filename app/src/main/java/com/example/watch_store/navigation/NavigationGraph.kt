package com.example.watch_store.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.watch_store.screens.ItemPage
import com.example.watch_store.screens.MainPage
import com.example.watch_store.screens.SplashScreen
import com.example.watch_store.screens.UserPage
import com.example.watch_store.utils.NavigationControl
import com.example.watch_store.viewmodel.SharedViewModel

@Composable
fun NavigationGraph (navController: NavHostController,navigationControl: NavigationControl){
    var sharedViewModel :SharedViewModel  = viewModel()

    NavHost(navController = navController, startDestination = NavigationScreens.SplashScreen.route){

        composable(NavigationScreens.SplashScreen.route){
            SplashScreen(sharedViewModel,navigationControl)
        }
        composable(NavigationScreens.MainPage.route){
            MainPage(sharedViewModel,navigationControl)
        }
        composable(NavigationScreens.ItemPage.route){
            ItemPage(sharedViewModel,navigationControl)
        }
        composable(NavigationScreens.UserPage.route){
            UserPage(sharedViewModel,navigationControl)
        }
    }
}