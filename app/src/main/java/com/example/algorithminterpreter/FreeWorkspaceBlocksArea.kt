package com.example.algorithminterpreter
import GreenAssignmentBlocks
import IntBlock
import BlockIfElse
import IntBlockArray
import BlockIf

import BlockWhile
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    selectedBlock: Block?
) {
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

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
        blocks.forEachIndexed { index, positionedBlock ->
            val isDragging = draggingIndex == index
            Box(
                modifier = Modifier
                    .zIndex(if (isDragging) 1f else 0f)
                    .offset {
                        val pos = if (isDragging) positionedBlock.position + dragOffset else positionedBlock.position
                        IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                    }
                    .pointerInput(index) {
                        detectDragGestures(
                            onDragStart = {
                                draggingIndex = index
                                dragOffset = Offset.Zero
                            },
                            onDrag = { _, dragAmount ->
                                dragOffset += dragAmount
                            },
                            onDragEnd = {
                                if (draggingIndex != null) {
                                    onBlockMove(draggingIndex!!, dragOffset)
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
                        blocks[index] = positionedBlock.copy(inputValue = newValue)
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
    isEditable: Boolean = true,
    isSelected: Boolean = false,
    isInteractive: Boolean = true,
    onInputChange: (String) -> Unit = {},
) {
    val borderWidth = if (isSelected) 4.dp else 2.dp
    val cornerRadius = 6.dp
    when (block) {
        is ConsoleRead -> {
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(95.dp)
                        .background(block.color, RoundedCornerShape(cornerRadius))
                        .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = block.text,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(57.dp)
                                .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                        ) { }
                    }
                }

        }
        is ConsoleWrite -> {
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(95.dp)
                    .background(block.color, RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = block.text,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(57.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    ) { }
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
                        modifier = Modifier.padding(end = 2.dp,start = 10.dp)
                    )
                }
            }
        }
        is IntVariable -> {
            Box(
                modifier = Modifier
                    .background( shape = IntBlock(), color = Color(0xFF35C1FE))
                    .width(160.dp)
                    .height(80.dp),
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
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                            .background(Color(0xFF35C1FE))
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
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ifel",
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
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                onInputChange(newValue)

                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        singleLine = true,
                        enabled = isInteractive,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 25.sp,
                            color = Color(0xFFD0D0D0),
                            fontWeight = FontWeight.Normal,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        placeholder = {
                            Text(
                                text = "0",
                                color = Color(0xFFD0D0D0),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            disabledTextColor = Color.White,
                            disabledBorderColor = Color.Transparent
                        )
                    )
            }
        }
    is ArraY -> {

        Box(
            modifier = Modifier
                .width(140.dp) // Ширина всего блока
                .height(56.dp)
                .background(Color(0xFF35C1FE), RoundedCornerShape(6.dp))
                .border(2.dp, Color.White, RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp) // Отступы от краёв рамки
        ) {
            // Левая скобка
            Text(
                text = "[",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterStart) // Прижать к левому краю по центру вертикали
                    .padding(start = 0.dp) // Можно подкорректировать отступ
            )

            // Поле ввода
            OutlinedTextField(
                value = inputValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isLetterOrDigit() }) {
                        onInputChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 28.dp, end = 28.dp) // Отступы слева и справа, чтобы не налезать на скобки
                    .align(Alignment.Center), // Центрируем поле в Box
                singleLine = true,
                enabled = isInteractive,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    disabledTextColor = Color.White,
                    disabledBorderColor = Color.Transparent
                )
            )

            // Правая скобка
            Text(
                text = "]",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Прижать к правому краю по центру вертикали
                    .padding(end = 0.dp)
            )
        }

    }
        is Variable -> {
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(56.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->

                        if (newValue.isEmpty() || newValue.all { it.isLetter() && it.isEnglishLetter() }) {
                            onInputChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(56.dp),

                    singleLine = true,
                    enabled = isInteractive,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            text = "X",
                            color = Color(0xFFD0D0D0),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        disabledTextColor = Color.White,
                        disabledBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}




