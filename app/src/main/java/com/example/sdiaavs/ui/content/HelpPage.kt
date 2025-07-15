package com.example.sdiaavs.ui.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun HelpPage(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Details")

        // Scrollable block
        Box(
            modifier = Modifier
                .height(150.dp) // Set desired height
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .verticalScroll(rememberScrollState()) // Enable scrolling
                .padding(8.dp)
        ) {
            Column {
                repeat(20) { index ->
                    Text(text = "Item #$index")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Other details")
    }
}
