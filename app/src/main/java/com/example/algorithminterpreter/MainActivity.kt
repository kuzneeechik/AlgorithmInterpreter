
package com.example.algorithminterpreter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.algorithminterpreter.ui.theme.AlgorithmInterpreterTheme

val TomorrowFont = FontFamily(
    Font(R.font.tomorrow_light)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgorithmInterpreterTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "start"
                ) {
                    composable("start") {
                        StartScr(
                            onNewProject = {
                                navController.navigate("project")
                            },
                            onMyProjects = {},
                            onAbout = {}
                        )
                    }
                    composable("project") {
                        ProjectScreen()
                    }
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
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6D60F8))
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Лого step code"
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = onNewProject,
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
            painter = painterResource(id = R.drawable.deep_wave),
            contentDescription = "верхняя",
            modifier = Modifier
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(id = R.drawable.down_wave),
            contentDescription = "нижняя",
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun ProjectScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0DDFF))
    ) {
        Image(
            painter = painterResource(id = R.drawable.big_line),
            contentDescription = "Линия сверху консоли",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(id = R.drawable.smal_line),
            contentDescription = "Линия внизу консоли",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {

        }
    }
}