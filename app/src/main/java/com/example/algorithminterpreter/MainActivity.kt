package com.example.algorithminterpreter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
                        StartScreen(
                            onNewProject = {
                                navController.navigate("project")
                            },
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

