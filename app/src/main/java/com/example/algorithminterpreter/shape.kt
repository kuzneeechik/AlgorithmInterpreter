import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text


@Composable
fun ScratchBlockShape(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 160.dp.toPx() }
    val blockHeight = with(density) { 80.dp.toPx() }
    val notchWidth = with(density) { 40.dp.toPx() }
    val notchHeight = with(density) { 10.dp.toPx() }
    val notchOffsetX = with(density) { 24.dp.toPx() }

    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val scaleX = width / blockWidth
        val scaleY = height / blockHeight

        moveTo(0f, 0f)
        lineTo(notchOffsetX * scaleX, 0f)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth) * scaleX, 0f)
        lineTo(width, 0f)
        lineTo(width, height)
        lineTo((notchOffsetX + notchWidth) * scaleX, height)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, height - -notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, height - -notchHeight * scaleY)
        lineTo(notchOffsetX * scaleX, height)
        lineTo(0f, height)
        close()
    }
}
