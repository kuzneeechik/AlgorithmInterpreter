
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithminterpreter.TomorrowFont
import com.example.algorithminterpreter.ui.theme.AlgorithmInterpreterTheme

@Composable
fun ProjectScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0DDFF))
    ) {

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .height(40.dp)
                .fillMaxWidth()
                .background(color = Color(0xFF5F52F0)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {}



        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ///////////


            /////
        }



        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .height(75.dp)
                .fillMaxWidth()
                .background(Color(0xFF5F52F0)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(45.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D60F8),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp)  //внутренние отступы
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)  //отступ между иконкой и текстом
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Start",
                        fontFamily = TomorrowFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }


            Text(
                text = "StepCode",
                color = Color.White,
                fontFamily = TomorrowFont,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )



            Button(
                onClick = {},
                modifier = Modifier
                    .padding(end = 16.dp)
                    .height(36.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)  //отступ между иконкой и текстом
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Start",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Clear",
                        fontFamily = TomorrowFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
@Preview
@Composable
fun GreetingPreview() {
    AlgorithmInterpreterTheme {
        ProjectScreen()
    }
}