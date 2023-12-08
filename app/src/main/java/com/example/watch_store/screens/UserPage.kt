package com.example.watch_store.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.watch_store.R
import com.example.watch_store.data.postdata.Post
import com.example.watch_store.navigation.NavigationScreens
import com.example.watch_store.utils.NavigationControl
import com.example.watch_store.viewmodel.MyViewModel
import com.example.watch_store.viewmodel.SharedViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UserPage(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
    ScreeUi(sharedViewModel,navigationControl)
}


private lateinit var viewModel: MyViewModel
private lateinit var stringId : MutableLiveData<String>
@Composable
private fun ScreeUi(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
    var user  = sharedViewModel.getUserId().observeAsState(initial = null).value!!
    var posts =remember { mutableStateListOf<Post>() }
    
    viewModel = hiltViewModel()
    
    viewModel.getPostsByUserId(user.id).observeAsState(initial = Result.failure(Throwable())).value.onFailure { 
        posts.clear()
        posts.addAll(emptyList())
        
    }.onSuccess { 
        posts.clear()
        posts.addAll(it)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.space_black)), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(40.dp))
        Column(modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(50))){
          GlideImage (imageModel = {user.picture})
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text =user.firstName +" "+user.lastName, fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(modifier = Modifier.padding(horizontal = 5.dp),columns = GridCells.Fixed(3), content ={
            items(count = posts.size){
                GridPostItem(post = posts[it],sharedViewModel,navigationControl)
            }
        } )



    }
}


@Composable
private fun GridPostItem(post: Post,sharedViewModel: SharedViewModel,control: NavigationControl) {
    Column(modifier = Modifier.height(200.dp).clickable {
        sharedViewModel.setProductId(post.id)
        control.gotoPage(NavigationScreens.UserPage.route,NavigationScreens.ItemPage.route,false)
        
    }) {
        GlideImage (imageModel = {post.image}, imageOptions = ImageOptions(alignment = Alignment.Center, contentScale = ContentScale.Crop),loading = {
            val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.loading_posts))

            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        })
    }
}













