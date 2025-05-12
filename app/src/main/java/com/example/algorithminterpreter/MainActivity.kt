
package com.example.algorithminterpreter

import ProjectScreen
import StartScr
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

