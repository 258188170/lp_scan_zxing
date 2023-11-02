package com.card.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.card.myapplication.ui.theme.MyApplicationTheme
import com.google.zxing.client.android.CaptureActivity
import com.google.zxing.client.android.Intents
import com.google.zxing.client.android.Intents.Scan.ACTION
import com.google.zxing.client.android.Intents.Scan.QR_CODE_MODE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        Button(
            modifier = Modifier.padding(all = 16.dp),
            onClick = {
                val intent = Intent(context, CaptureActivity::class.java)
                intent.putExtra(Intents.Scan.FORMATS,QR_CODE_MODE)
                intent.putExtra(Intents.Scan.ACTION,ACTION)
                context.startActivity(intent)
            }, content = {
                Text(text = "connect")
            })

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}