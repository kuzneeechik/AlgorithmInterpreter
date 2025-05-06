package com.example.algorithminterpreter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.algorithminterpreter.ui.theme.AlgorithmInterpreterTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.offset


val TomorrowFont = FontFamily(
    Font(R.font.tomorrow_light)
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgorithmInterpreterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StartScr(
                        onNewProject = {},
                        onMyProjects = {},
                        onAbout = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun StartScr(
    onNewProject: () -> Unit,
    onMyProjects: () -> Unit,
    onAbout: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF6D60F8))
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {


        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Лого step code",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .border(4.dp, Color.White, RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6D60F8),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Start",
                fontFamily = TomorrowFont,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )

        }


    }
    Image(
        painter = painterResource(id = R.drawable.down_wave),
        contentDescription = "нижняя",
        modifier = Modifier
        .offset(x = 112.dp, y = 650.dp)
    )
    Image(
        painter = painterResource(id = R.drawable.deep_wave),
        contentDescription = "верхняя",
    )

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlgorithmInterpreterTheme {
        StartScr(
            onNewProject = {},
            onMyProjects = {},
            onAbout = {}
        )
    }
}
