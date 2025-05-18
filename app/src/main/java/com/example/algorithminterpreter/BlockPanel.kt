import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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


data class Block(val id: Int, val text: String, val color: Color)

@Composable
fun BlockPanel(onBlockClick: (Block) -> Unit) {
    val blocks = listOf(
        Block(1, "+", Color(0xFF2F860D)),
        Block(2, "%", Color(0xFF2F860D)),
        Block(3, "*", Color(0xFF2F860D)),
        Block(4, "-", Color(0xFF2F860D)),
        Block(5, "/", Color(0xFF2F860D)),
        Block(6, ">", Color(0xFFF89402)),
        Block(7, "<", Color(0xFFF89402)),
        Block(8, ">=", Color(0xFFF89402)),
        Block(9, "<=", Color(0xFFF89402)),
        Block(10, "==", Color(0xFFF89402)),
        Block(11, "!=", Color(0xFFF89402)),
        Block(12, "[  ]", Color(0xFFBA68C8)),
        Block(13, "int x", Color(0xFF03A9F4)),
        Block(14, "int x[]", Color(0xFF03A9F4)),
        Block(15, "x =", Color(0xFF71C94F)),
        Block(16, "if", Color(0xFFFFAD19)),
        Block(17, "if ... else", Color(0xFFFFAD19)),
        Block(18, "while", Color(0xFFFF5755)),
        Block(19, "console.read()", Color(0xFF9A66FF)),
        Block(20, "console.write()", Color(0xFF9A66FF))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        blocks.forEach { block ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .clickable { onBlockClick(block.copy(id = System.currentTimeMillis().toInt())) }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                androidx.compose.material3.Text(
                    text = block.text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
    }
}