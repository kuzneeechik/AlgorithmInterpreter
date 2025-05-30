import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun IntBlock(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 150.dp.toPx() } // конвертирует в пиксели учитывает плотность
    val blockHeight = with(density) { 95.dp.toPx() }
    val notchWidth = with(density) { 40.dp.toPx() } // это все выступы
    val notchHeight = with(density) { 10.dp.toPx() }
    val notchOffsetX = with(density) { 24.dp.toPx() } //отступ от левого

    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val scaleX = width / blockWidth //для масштаба
        val scaleY = height / blockHeight

        moveTo(0f, 0f)
        lineTo(notchOffsetX * scaleX, 0f)
        lineTo(
            (notchOffsetX + notchWidth * 0.25f) * scaleX,
            notchHeight * scaleY
        ) //левая точка выреза
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, notchHeight * scaleY)//правая
        lineTo((notchOffsetX + notchWidth) * scaleX, 0f)//конец выреза
        lineTo(width, 0f)//прсото линия сверху
        lineTo(width, height)
        lineTo((notchOffsetX + notchWidth) * scaleX, height)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, height - -notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, height - -notchHeight * scaleY)
        lineTo(notchOffsetX * scaleX, height)
        lineTo(0f, height)
        close()
    }
}

@Composable
fun IntBlockArray(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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

@Composable
fun ConsoleReadBlocks(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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

@Composable
fun ConsoleWriteBlocks(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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

@Composable
fun GreenAssignmentBlocks(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 195.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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

@Composable
fun BlockIf(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
    val notchWidth = with(density) { 40.dp.toPx() }
    val notchHeight = with(density) { 10.dp.toPx() }
    val notchOffsetX = with(density) { 24.dp.toPx() }

    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val scaleX = width / blockWidth
        val scaleY = height / blockHeight

        // Верхний пазл (основной блок if)
        moveTo(0f, 0f)
        lineTo(notchOffsetX * scaleX, 0f)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth) * scaleX, 0f)
        lineTo(width, 0f)
        lineTo(width, height * 0.7f) // Оставляем место для нижнего пазла

        // Нижний пазл (поменьше)
        lineTo(width, height)
        lineTo((notchOffsetX + notchWidth) * scaleX, height)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, height - notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, height - notchHeight * scaleY)
        lineTo(notchOffsetX * scaleX, height)
        lineTo(0f, height)
        close()
    }
}

@Composable
fun BlockIfLower(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 30.dp.toPx() } // Меньшая высота
    val notchWidth = with(density) { 40.dp.toPx() }
    val notchHeight = with(density) { 10.dp.toPx() }
    val notchOffsetX = with(density) { 24.dp.toPx() }

    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val scaleX = width / blockWidth
        val scaleY = height / blockHeight

        // Верхний выступ (для соединения с основным блоком)
        moveTo(0f, 0f)
        lineTo(notchOffsetX * scaleX, 0f)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, -notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, -notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth) * scaleX, 0f)

        // Правая сторона
        lineTo(width, 0f)
        lineTo(width, height)

        // Нижняя часть
        lineTo((notchOffsetX + notchWidth) * scaleX, height)
        lineTo((notchOffsetX + notchWidth * 0.75f) * scaleX, height + notchHeight * scaleY)
        lineTo((notchOffsetX + notchWidth * 0.25f) * scaleX, height + notchHeight * scaleY)
        lineTo(notchOffsetX * scaleX, height)
        lineTo(0f, height)
        close()
    }
}
@Composable
fun BlockIfElse(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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

@Composable
fun BlockWhile(): Shape {
    val density = LocalDensity.current
    val blockWidth = with(density) { 250.dp.toPx() }
    val blockHeight = with(density) { 95.dp.toPx() }
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
