@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.watch_store.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.watch_store.R
import com.example.watch_store.data.User
import com.example.watch_store.data.postcomment.Comments
import com.example.watch_store.data.postdata.Post
import com.example.watch_store.navigation.NavigationScreens
import com.example.watch_store.utils.NavigationControl
import com.example.watch_store.viewmodel.MyViewModel
import com.example.watch_store.viewmodel.SharedViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainPage(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
ScreenUI(sharedViewModel,navigationControl)
}

private lateinit var viewmodel:MyViewModel
private val userPage:Int  = (0..4).random()
private var page: MutableLiveData<Int> = MutableLiveData<Int>((1..25).random())

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScreenUI(sharedViewModel: SharedViewModel,navigationControl: NavigationControl) {
    viewmodel= hiltViewModel()

    val dataList  = remember { mutableStateListOf<User>() }
    val scope  = rememberCoroutineScope()

    val postlist =  remember { mutableStateListOf<Post>() }
    var searchtext by remember { mutableStateOf("")      }

    searchtext =  sharedViewModel.getTag().observeAsState(initial = "").value


    if(searchtext.trim()!=""){
        viewmodel.getPostByTag(searchtext).observeAsState(initial = Result.failure(Throwable())).value.onFailure {
            Log.d("@@@@@", "ScreenUI: $it")
            postlist.clear()
            postlist.addAll(emptyList())
        }.onSuccess {
            postlist.clear()
            postlist.addAll(it)
        }
    }
    else
    {
        fetchPost(postlist = postlist, pages = page.observeAsState(initial = 1).value)
    }

    var isrefreshing:Boolean by remember{ mutableStateOf(false) }
    val refreshstate  = rememberPullRefreshState(refreshing =isrefreshing , onRefresh = {
        page.postValue((0..25).random())
        scope.launch {
            delay(1000)
            isrefreshing = false
        }
    })


    viewmodel.fetchUser(userPage).observeAsState(initial = Result.failure(Throwable())).value!!.onSuccess {
        dataList.clear()
        dataList.addAll(it)
    }.onFailure {
        dataList.clear()
        dataList.addAll(emptyList())
    }


   Column(modifier = Modifier
       .fillMaxSize()
       .background(color = colorResource(id = R.color.space_black))) {

        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.white_gray),
                shape = RoundedCornerShape(50)
            )
            .clip(shape = RoundedCornerShape(50))
            .height(50.dp)

            .weight(
                fill = true,
                weight = 1f
            ), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
        {
            Spacer(modifier = Modifier.width(10.dp))
            Image(painter = painterResource(id = R.drawable.search), modifier = Modifier
                .height(24.dp)
                .width(24.dp), contentDescription = "")
            TextField(value = searchtext, onValueChange ={searchtext =it
                                                         sharedViewModel.setTag(it)
                                                         }, placeholder = { Text(text = "Search by tags", color = Color.Gray)}, maxLines = 1, modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent), colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.white_gray), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.width(20.dp))

     }

       Box(modifier = Modifier.fillMaxWidth())
       {
           LazyRow{
               items(count = dataList.size){
                   userItem(dataList[it],sharedViewModel,navigationControl)
               }
           }

           Column(
               Modifier
                   .align(Alignment.CenterStart)
                   .height(110.dp)
                   .width(20.dp)
                   .background(
                       brush = Brush.horizontalGradient(
                           listOf(colorResource(id = R.color.space_black), Color.Transparent)
                       )
                   )) {

           }
           Column(
               Modifier
                   .align(alignment = Alignment.CenterEnd)
                   .height(110.dp)
                   .width(20.dp)
                   .background(
                       brush = Brush.horizontalGradient(
                           listOf(Color.Transparent, colorResource(id = R.color.space_black))
                       )
                   )) {

           }
       }
       Box(modifier = Modifier.pullRefresh(refreshstate)){

           LazyColumn(modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 5.dp)){
               item{
                   Spacer(modifier = Modifier.height(20.dp))
               }
               items(count = postlist.size){
                   postItem(data = postlist[it],sharedViewModel,navigationControl)
                   Spacer(modifier = Modifier.height(10.dp))
               }
           }

           Column(
               Modifier
                   .align(Alignment.TopCenter)
                   .fillMaxWidth()
                   .height(30.dp)
                   .background(
                       brush = Brush.verticalGradient(
                           listOf(colorResource(id = R.color.space_black), Color.Transparent)
                       )
                   )) {

           }
           Column(
               Modifier
                   .align(Alignment.BottomCenter)
                   .fillMaxWidth()
                   .height(30.dp)
                   .background(
                       brush = Brush.verticalGradient(
                           listOf(Color.Transparent, colorResource(id = R.color.space_black))
                       )
                   )) {

           }
           PullRefreshIndicator(isrefreshing, refreshstate, Modifier.align(Alignment.TopCenter))
       }

   }

}
@Composable
fun fetchPost(postlist: SnapshotStateList<Post>,pages:Int) {
    viewmodel.fetchPosts(pages).observeAsState(initial = Result.failure(Throwable())).value!!.onSuccess {
        postlist.clear()
        postlist.addAll(it)
    }.onFailure {
        postlist.clear()
        postlist.addAll(emptyList())
    }
}

@Composable
fun userItem(data: User,sharedViewModel: SharedViewModel,navigationControl: NavigationControl) {
    Column(modifier = Modifier
        .width(80.dp).clickable {
            sharedViewModel.setUserId(data)
            navigationControl.gotoPage(NavigationScreens.MainPage.route,NavigationScreens.UserPage.route,false)
        }
        .height(110.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier
            .height(60.dp)
            .width(60.dp)
            .background(color = Color.Transparent, shape = RoundedCornerShape(50))
            .clip(
                RoundedCornerShape(50)
            )) {
            GlideImage(imageModel = {data.picture})
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = data.firstName, maxLines = 1, color = Color.White)

    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun postItem(
    data: Post,
    sharedViewModel: SharedViewModel,
    navigationControl: NavigationControl,
) {
    var showSheet  by remember {  mutableStateOf(false) }
    var postId  by remember {  mutableStateOf("") }
    var commentsList  = remember { mutableStateListOf<Comments>() }
    
    if(showSheet){
        viewmodel.getCommentById(postId).observeAsState(initial = Result.failure(Throwable())).value!!.onSuccess { 
            commentsList.clear()
            commentsList.addAll(it)
        }.onFailure { 
            commentsList.clear()
            commentsList.addAll(emptyList())
        }
        
        
    }



    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            sharedViewModel.setProductId(data.id)
            navigationControl.gotoPage(
                NavigationScreens.MainPage.route,
                NavigationScreens.ItemPage.route,
                false
            )
        }
        .padding(horizontal = 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(color = colorResource(id = R.color.post_itemColor)) ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .clip(RoundedCornerShape(50)), verticalArrangement = Arrangement.Center) {
                GlideImage(imageModel = {data.owner.picture}, imageOptions = ImageOptions(contentScale = ContentScale.Inside, alignment = Alignment.Center))
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = data.text, maxLines = 1, color = Color.White, fontSize = 13.sp)
                Text(text = data.owner.firstName, fontSize = 11.sp)
            }

        }
        Column (
            Modifier
                .fillMaxWidth()
                .height(200.dp)){
           GlideImage (imageModel = {data.image}, imageOptions = ImageOptions(alignment = Alignment.Center, contentScale = ContentScale.Crop), loading = {val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_posts))

               LottieAnimation(
                   modifier = Modifier.fillMaxSize(),
                   composition = composition,
                   iterations = LottieConstants.IterateForever,
               )})

        }


        Spacer(modifier = Modifier.height(10.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Spacer(modifier = Modifier.width(5.dp))
            Icon(painter = painterResource(id =R.drawable.like_13), contentDescription ="" , tint = Color.White, modifier = Modifier
                .width(24.dp)
                .height(24.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text(text =data.likes.toString(),color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(painter = painterResource(id =R.drawable.comment_25), contentDescription ="" , tint = Color.White, modifier = Modifier
                .width(24.dp)
                .clickable {
                    showSheet = true
                    postId = data.id

                }
                .height(24.dp))

        }



        Spacer(modifier = Modifier.height(5.dp))
        Row {
            data.tags.forEach{
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "#$it", modifier = Modifier
                    .background(color = Color.Magenta, shape = RoundedCornerShape(5))
                    .clip(
                        RoundedCornerShape(5)
                    )
                    .clickable {
                        sharedViewModel.setTag(it)
                    }, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        if(showSheet){

            ModalBottomSheet(onDismissRequest = { showSheet = false }, modifier = Modifier.padding(top = 50.dp)) {
                Box(Modifier.fillMaxSize()) {
                    if (commentsList.size>0){
                        LazyColumn{
                            items(commentsList.size){
                                commentLazyItem(data = commentsList[it])
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                    else{
                        Image(painter = painterResource(id = R.drawable.no_comment), contentDescription = "", modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .align(
                                Alignment.TopCenter
                            )
                            .padding(top = 30.dp))

                    }
                }

            }
        }




    }




}

@Composable
private fun commentLazyItem(data:Comments) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.white_gray))
        .padding(10.dp)) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(50))
            .width(30.dp)
            .height(30.dp)) {
            GlideImage(imageModel = {data.owner.picture})
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = data.owner.firstName)
            Text(text = data.message)
        }
    }

}


















