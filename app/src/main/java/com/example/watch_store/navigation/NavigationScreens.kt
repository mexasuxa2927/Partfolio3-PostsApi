package com.example.watch_store.navigation

sealed class NavigationScreens(val route:String){

    object SplashScreen:NavigationScreens("splash-screen")
    object MainPage:NavigationScreens("main-screen")
    object ItemPage:NavigationScreens("item-screen")
    object UserPage:NavigationScreens("user-page")

}