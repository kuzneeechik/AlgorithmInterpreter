package com.example.algorithminterpreter
import BlockIf
import BlockIfElse
import BlockWhile
import ConsoleReadBlocks
import ConsoleWriteBlocks
import GreenAssignmentBlocks
import IntBlock
import IntBlockArray
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

private fun Char.isEnglishLetter(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z'
}
@Composable
fun FreeWorkspaceBlocksArea(
    blocks: MutableList<PositionedBlock>,
    onWorkspaceClick: (Offset) -> Unit,
    onBlockMove: (Int, Offset) -> Unit,
    selectedBlock: Block?,
    leftPanelWidth: Dp = 140.dp,
    topBoundaryDp: Dp = 90.dp,
    bottomBoundaryDp: Dp = 190.dp,
    blockWidth: Dp = 270.dp
) {
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val leftPanelWidthPx = with(density) { leftPanelWidth.toPx() }
    val topBoundaryPx = with(density) { topBoundaryDp.toPx() }
    val bottomBoundaryPx = with(density) { bottomBoundaryDp.toPx() }
    val halfBlockWidthPx = with(density) { blockWidth.toPx() / 2 }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFD6EAF8), RoundedCornerShape(12.dp))
            .pointerInput(selectedBlock) {
                detectTapGestures { offset ->
                    if (selectedBlock != null) {
                        onWorkspaceClick(offset)
                    }
                }
            }
    ) {
        //сортировка блоков по zIndex перывй ур сверхуууууу
        val sortedBlocks = blocks.sortedByDescending {
            when (it.block) {
                is Const, is Variable, is ArrayElem, is ArithmeticOperation -> 3
                is ComparisonOperation, is Staples, is IntVariable, is IntArray, is Assignment -> 2
                is If, is IfElse, is While, is ConsoleRead, is ConsoleWrite -> 1
                else -> 0
            }
        }

        sortedBlocks.forEachIndexed { sortedIndex, positionedBlock ->
            val originalIndex = blocks.indexOf(positionedBlock)
            val isDragging = draggingIndex == originalIndex

            Box(
                modifier = Modifier
                    .zIndex(
                        when {
                            isDragging -> 10f // перетаскиваемый  ВСЕГДА сверху
                            positionedBlock.block is Const || positionedBlock.block is Variable ||
                                    positionedBlock.block is ArrayElem || positionedBlock.block is ArithmeticOperation -> 3f
                            positionedBlock.block is ComparisonOperation || positionedBlock.block is Staples ||
                                    positionedBlock.block is IntVariable || positionedBlock.block is IntArray ||
                                    positionedBlock.block is Assignment -> 2f
                            else -> 1f
                        }
                    )
                    .offset {
                        val currentPosition = positionedBlock.position
                        var newPosition = if (isDragging) currentPosition + dragOffset else currentPosition

                        newPosition = newPosition.copy(
                            x = newPosition.x.coerceAtLeast(leftPanelWidthPx - halfBlockWidthPx),
                            y = newPosition.y.coerceIn(
                                topBoundaryPx,
                                screenHeightPx - bottomBoundaryPx
                            )
                        )

                        IntOffset(newPosition.x.roundToInt(), newPosition.y.roundToInt())
                    }
                    .pointerInput(originalIndex) {
                        detectDragGestures(
                            onDragStart = {
                                draggingIndex = originalIndex
                                dragOffset = Offset.Zero
                            },
                            onDrag = { _, dragAmount ->
                                dragOffset += dragAmount
                            },
                            onDragEnd = {
                                draggingIndex?.let { idx ->
                                    var finalPosition = blocks[idx].position + dragOffset

                                    finalPosition = finalPosition.copy(
                                        x = finalPosition.x.coerceAtLeast(leftPanelWidthPx - halfBlockWidthPx),
                                        y = finalPosition.y.coerceIn(
                                            topBoundaryPx,
                                            screenHeightPx - bottomBoundaryPx
                                        )
                                    )

                                    onBlockMove(idx, finalPosition - blocks[idx].position)
                                }
                                draggingIndex = null
                                dragOffset = Offset.Zero
                            }
                        )
                    }
            ) {
                BlockView(
                    block = positionedBlock.block,
                    inputValue = positionedBlock.inputValue,
                    onInputChange = { newValue ->
                        blocks[originalIndex] = positionedBlock.copy(inputValue = newValue)
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockView(
    block: Block,
    inputValue: String = "",
    isInteractive: Boolean = true,
    onInputChange: (String) -> Unit = {},
) {
    val cornerRadius = 6.dp
    when (block) {
        is ConsoleRead -> {
            Box(
                modifier = Modifier
                    .background(shape = ConsoleReadBlocks(), color = Color(0xFF9A66FF))
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "console.read",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        is ConsoleWrite -> {
            Box(
                modifier = Modifier
                    .background(shape = ConsoleWriteBlocks(), color = Color(0xFF9A66FF))
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "console.write",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        }
        is ArithmeticOperation -> {
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 5.dp)

                ) {
                    Text(
                        text = block.text,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp,
                        modifier = Modifier


                    )
                }
            }
        }

        is ComparisonOperation -> {
            val operator = when (block.text) {
                ">" -> ">"
                "<" -> "<"
                ">=" -> ">="
                "<=" -> "<="
                "==" -> "=="
                "!=" -> "!="
                else -> ""
            }
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp)
                    .background(block.color, RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }
                    Text(
                        text = operator,
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }

                }
            }
        }

        is Staples -> {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp)
                    .background(Color(0xFFD25AE0), RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
                    .padding(horizontal = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(
                        text = "(",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 10.dp, start = 2.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }

                    Text(
                        text = ")",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 2.dp, start = 10.dp)
                    )
                }
            }
        }

        is IntVariable -> {
            Box(
                modifier = Modifier
                    .background(shape = IntBlock(), color = Color(0xFF35C1FE))
                    .width(150.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "int",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }

        }

        is IntArray -> {
            Box(
                modifier = Modifier
                    .background(shape = IntBlockArray(), color = Color(0xFF35C1FE))
                    .width(260.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                )

                {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "int",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "[",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = "]",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
            }
        }

        is Assignment -> {
            Box(
                modifier = Modifier
                    .background(shape = GreenAssignmentBlocks(), color = Color(0xFF71C94F))
                    .width(195.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .background(Color(0xFF35C1FE), RoundedCornerShape(8.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }
                    Text(
                        text = "=",
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }
                }
            }
        }

        is If -> {
            Box(
                modifier = Modifier
                    .background(shape = BlockIf(), color = Color(0xFFFFAD19))
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "if",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        is IfElse -> {
            Box(
                modifier = Modifier
                    .background(shape = BlockIfElse(), color = Color(0xFFFFAD19))
                    .width(180.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "else",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        is While -> {
            Box(
                modifier = Modifier
                    .background(shape = BlockWhile(), color = Color(0xFFFF5755))
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "while",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(115.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        is Const -> {

            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(56.dp)
                    .background(Color(0xFF057CDE), RoundedCornerShape(6.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            )
            {
                BasicTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        if (newValue.all { ( it.isDigit()) }) {
                            onInputChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    singleLine = true,
                    enabled = isInteractive,
                    textStyle = TextStyle(
                        fontSize = 25.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (inputValue.isEmpty()) {
                                Text(
                                    text = "0",
                                    color = Color(0xFFD0D0D0),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()//отображение
                        }
                    }
                )
            }
        }

        is ArrayElem -> {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(56.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(6.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTextField(
                        value = block.name,
                        onValueChange = { newName ->
                            if (isInteractive && newName.all { it.isLetter() }) {
                                block.name = newName
                                onInputChange("${newName}[${block.index}]")
                            }
                        },
                        modifier = Modifier
                            .width(60.dp)
                            .padding(start = 8.dp),
                        singleLine = true,
                        enabled = isInteractive,
                        cursorBrush = SolidColor(Color.White),
                        textStyle = TextStyle(
                            fontSize = 25.sp,
                            color = if (block.name.isEmpty()) Color(0xFFD0D0D0) else Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (block.name.isEmpty()) {
                                    Text(
                                        text = "name",
                                        color = Color(0xFFD0D0D0),
                                        fontSize = 17.sp,
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "[",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(2.dp))
                    BasicTextField(
                        value = block.index,
                        onValueChange = { newIndex ->
                            if (isInteractive && newIndex.all { it.isEnglishLetter() || it.isDigit() }) {
                                block.index = newIndex
                                onInputChange("${block.name}[${newIndex}]")
                            }
                        },
                        modifier = Modifier
                            .width(40.dp)
                            .padding(horizontal = 2.dp),
                        singleLine = true,
                        enabled = isInteractive,
                        cursorBrush = SolidColor(Color.White),
                        textStyle = TextStyle(
                            fontSize = 25.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (block.index.isEmpty()) {
                                    Text(
                                        text = "i",
                                        color = Color(0xFFD0D0D0),
                                        fontSize = 25.sp,
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = "]",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }



        is Variable -> {
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(56.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(6.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            )
            {  Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        if (newValue.all { (it.isEnglishLetter()) }) {
                            onInputChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    singleLine = true,
                    enabled = isInteractive,
                    textStyle = TextStyle(
                        fontSize = 25.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (inputValue.isEmpty()) {
                                Text(
                                    text = "X",
                                    color = Color(0xFFD0D0D0),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}




