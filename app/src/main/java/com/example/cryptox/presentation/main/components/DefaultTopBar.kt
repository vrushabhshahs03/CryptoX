package com.example.cryptox.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onClick: () -> Unit,
    image: String?,
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
            actionIconContentColor = Color.White
        ),
        title = {
            if(!showSearch) {
                Text(
                    text = "CryptoX",
                    color = Color.White
                )
            } else {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f),
                    value = searchQuery,
                    onValueChange = { value ->
                        searchQuery = value
                        onQueryChange(value)
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    placeholder = {
                        Text("Search Coins...")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            "Search Icon",
                            tint = Color.White
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Blue,
                        unfocusedContainerColor = Color.Blue,
                        focusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = {
                searchQuery = ""
                onQueryChange("")
                showSearch = !showSearch
            }) {
                Icon(
                    if(showSearch) Icons.Default.Close else Icons.Default.Search,
                    "Search Icon"
                )
            }
            IconButton(onClick = {
                onClick()
            }) {
                if(image != null) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon"
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun DefaultTopBarPreview() {
    DefaultTopBar(
        onQueryChange = {},
        onClick = {},
        image = null,
    )
}