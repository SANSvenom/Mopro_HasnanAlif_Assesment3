package com.hasnan0062.assesment3.ui.screen


import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hasnan0062.assesment3.R
import com.hasnan0062.assesment3.model.Kucing
import com.hasnan0062.assesment3.network.ApiStatus
import com.hasnan0062.assesment3.ui.theme.Assesment3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    val column1 = data.filterIndexed { index, _ -> index % 2 == 0 }
    val column2 = data.filterIndexed { index, _ -> index % 2 != 0 }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            Row(
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    column1.forEach { kucing ->
                        ListItem(kucing = kucing)
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    column2.forEach { kucing ->
                        ListItem(kucing = kucing)
                    }
                }
            }
        }
        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData() },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal=32.dp, vertical=16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }

    }

}


@Composable
fun ListItem(kucing: Kucing) {
    val aspectRatio = if (kucing.height != 0) {
        kucing.width.toFloat() / kucing.height.toFloat()
    } else {
        1f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(kucing.url)
                .crossfade(true)
                .build(),
            contentDescription = "Gambar kucing",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assesment3Theme {
        MainScreen()
    }
}