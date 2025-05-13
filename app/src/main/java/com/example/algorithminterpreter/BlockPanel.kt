import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlockPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BlockButton("+", Color(0xFF2F860D))
        BlockButton("%", Color(0xFF2F860D))
        BlockButton("*", Color(0xFF2F860D))
        BlockButton("-", Color(0xFF2F860D))
        BlockButton("/", Color(0xFF2F860D))
        Spacer(Modifier.height(8.dp))
        BlockButton(">", Color(0xFFF89402))
        BlockButton("<", Color(0xFFF89402))
        BlockButton(">=", Color(0xFFF89402))
        BlockButton("<=", Color(0xFFF89402))
        BlockButton("==", Color(0xFFF89402))
        BlockButton("!=", Color(0xFFF89402))
        Spacer(Modifier.height(8.dp))
        BlockButton("[  ]", Color(0xFFBA68C8))
        Spacer(Modifier.height(8.dp))
        BlockButton("int x", Color(0xFF03A9F4))
        BlockButton("int x[]", Color(0xFF03A9F4))
        Spacer(Modifier.height(8.dp))
        BlockButton("x =", Color(0xFF71C94F))
        Spacer(Modifier.height(8.dp))
        BlockButton("if", Color(0xFFFFAD19))
        BlockButton("if ... else", Color(0xFFFFAD19))
        BlockButton("while", Color(0xFFFF5755))
        Spacer(Modifier.height(8.dp))
        BlockButton("console.read()", Color(0xFF9A66FF))
        BlockButton("console.write()", Color(0xFF9A66FF))
    }
}

@Composable
fun BlockButton(text: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        androidx.compose.material3.Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}