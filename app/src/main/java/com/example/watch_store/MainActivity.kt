package com.example.watch_store

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.watch_store.navigation.NavigationGraph
import com.example.watch_store.navigation.NavigationScreens
import com.example.watch_store.ui.theme.Watch_storeTheme
import com.example.watch_store.utils.NavigationControl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
@AndroidEntryPoint
class MainActivity : ComponentActivity(),NavigationControl {

    lateinit var navigation:NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Watch_storeTheme {
                navigation  = rememberNavController()
                NavigationGraph(navController = navigation,this)
                LaunchedEffect(key1 = true ){
                    delay(5000)
                    gotoPage(NavigationScreens.SplashScreen.route,NavigationScreens.MainPage.route,true)
                }

            }
        }
    }

    override fun gotoPage(rout_from: String, rout_to: String, inclusiveScreen: Boolean) {
        navigation.navigate(rout_to){
            popUpTo(rout_from){
                inclusive =  inclusiveScreen
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Watch_storeTheme {

    }
}