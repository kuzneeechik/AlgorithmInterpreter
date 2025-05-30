import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithminterpreter.R
import com.example.algorithminterpreter.TomorrowFont

@Composable
fun StartScreen(
    onNewProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF5F52F0))
            .navigationBarsPadding()

    ) {
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
                    .padding(horizontal = 70.dp)
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
            modifier = Modifier.align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(id = R.drawable.down_wave),
            contentDescription = "нижняя",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

