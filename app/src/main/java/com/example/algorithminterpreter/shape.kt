
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity


@Composable
fun ScratchBlockShape(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 120.dp.toPx() }
    val blockHeight = with(density) { 56.dp.toPx() }
    val notchWidth = with(density) { 24.dp.toPx() }
    val notchHeight = with(density) { 8.dp.toPx() }
    val notchOffsetX = with(density) { 18.dp.toPx() }

    return GenericShape { size, _ ->
        // Размеры должны быть нормализованы относительно фактического размера
        val width = size.width
        val height = size.height
        val scaleX = width / blockWidth
        val scaleY = height / blockHeight

        // Старт: левый верхний угол
        moveTo(0f, 0f)

        // До начала верхней ямочки
        lineTo(notchOffsetX * scaleX, 0f)

        // Верхняя впадина \_/
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth) * scaleX, 0f)

        // До правого верхнего угла
        lineTo(width, 0f)

        // Правый нижний угол
        lineTo(width, height)

        // До начала нижней ямочки
        lineTo((notchOffsetX + notchWidth) * scaleX, height)

        // Нижняя впадина \_/
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, height - notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, height - notchHeight * scaleY)
        lineTo(notchOffsetX * scaleX, height)

        // До левого нижнего угла
        lineTo(0f, height)

        // Вверх к старту
        lineTo(0f, 0f)
        close()
    }
}