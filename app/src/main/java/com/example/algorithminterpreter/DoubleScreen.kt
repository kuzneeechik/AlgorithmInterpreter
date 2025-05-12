import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.algorithminterpreter.R

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