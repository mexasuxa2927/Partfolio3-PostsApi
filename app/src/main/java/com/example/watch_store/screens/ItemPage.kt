package com.example.watch_store.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.watch_store.R
import com.example.watch_store.data.postcomment.Comments
import com.example.watch_store.data.postdata.Post
import com.example.watch_store.utils.NavigationControl
import com.example.watch_store.viewmodel.MyViewModel
import com.example.watch_store.viewmodel.SharedViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ItemPage(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
    ScreenUi(sharedViewModel = sharedViewModel, navigationControl =navigationControl)
}

private lateinit var viewModel: MyViewModel
@Composable
private fun ScreenUi(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
    viewModel = hiltViewModel()
    var post:Post? by  remember{ mutableStateOf(null) }
    var commentList   =  remember{ mutableStateListOf<Comments>() }
    viewModel.getPostById(sharedViewModel.getProductId().observeAsState(initial = "").value!!).observeAsState(initial = Result.failure(Throwable())).value.onFailure {

    }.onSuccess {
        post = it
    }

    viewModel.getCommentById(post?.id?:"").observeAsState(initial = Result.failure(Throwable())).value!!.onSuccess {
        commentList.clear()
        commentList.addAll(it)
    }.onFailure {
        commentList.clear()
        commentList.addAll(emptyList())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.space_black))) {
        Column(modifier = Modifier
            .fillMaxWidth().height(250.dp)
            ) {
            GlideImage(imageModel = {post?.image},Modifier.wrapContentSize(), imageOptions = ImageOptions(contentScale = ContentScale.Crop), loading = {

            })
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_posts))
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(color = colorResource(
            id =R.color.white_gray
        )).fillMaxWidth()) {
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .clip(RoundedCornerShape(50)), verticalArrangement = Arrangement.Center) {
                GlideImage(imageModel = {post?.owner?.picture})
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = post?.text?:"", maxLines = 1, color = Color.White, fontSize = 13.sp)
                Text(text = post?.owner?.firstName?:"", fontSize = 11.sp)
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn{
            items(commentList.size){
                commentLazyItem(data = commentList[it])
                Spacer(modifier = Modifier.height(5.dp))
            }

        }


    }



}

@Composable
private fun commentLazyItem(data:Comments) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white_gray))
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .width(30.dp)
                .height(30.dp)
        ) {
            GlideImage(imageModel = { data.owner.picture })
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = data.owner.firstName)
            Text(text = data.message)
        }
    }
}