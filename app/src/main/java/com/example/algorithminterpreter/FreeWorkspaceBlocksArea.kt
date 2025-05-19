package com.example.algorithminterpreter

import Block
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

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
    onInputChange: (String) -> Unit = {},
) {
    val borderWidth = if (isSelected) 4.dp else 2.dp
    if (block.id == 23) {
        Box(
            modifier = Modifier
                .width(54.dp)
                .height(54.dp)
                .background(Color(0xFF057CDE), RoundedCornerShape(10.dp))
                .border(2.dp, Color.White, RoundedCornerShape(10.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                // Тонкий овал (контур нуля)
                drawRoundRect(
                    color = Color(0xFFD0D0D0),
                    topLeft = Offset(size.width * 0.30f, size.height * 0.12f),
                    size = Size(size.width * 0.40f, size.height * 0.76f),
                    cornerRadius = CornerRadius(size.width * 0.20f, size.width * 0.20f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = size.width * 0.07f)
                )
                // Тонкая наклонная черта
                drawLine(
                    color = Color(0xFFD0D0D0),
                    start = Offset(size.width * 0.42f, size.height * 0.22f),
                    end = Offset(size.width * 0.58f, size.height * 0.78f),
                    strokeWidth = size.width * 0.05f
                )
            }
        }
    }
    else if (block.id == 22) {
        if (!isEditable) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(54.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(10.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "X",
                        color = Color(0xFFD0D0D0),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal // потоньше
                    )
                }
            }
        } else {
            // На главном экране — поле для ввода
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(54.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(10.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 32.sp,
                        color = Color(0xFFD0D0D0),
                        fontWeight = FontWeight.Normal
                    ),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color(0xFFD0D0D0),
                        unfocusedTextColor = Color(0xFFD0D0D0),
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }


    else if (block.id in 1..5) {
        if (!isEditable) {
            // Только визуализация (панель блоков)
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = block.text,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                            .padding(start = 8.dp)
                    )
                }
            }
        } else {
            // На главном экране — поле для ввода
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = block.text,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = onInputChange,
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                            .padding(start = 8.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    } else if (block.id in 6..11) {
        val parts = inputValue.split("|", limit = 2)
        val left = parts.getOrNull(0) ?: ""
        val right = parts.getOrNull(1) ?: ""
        val operator = when (block.id) {
            6 -> ">"
            7 -> "<"
            8 -> ">="
            9 -> "<="
            10 -> "=="
            11 -> "!="
            else -> ""
        }
        if (!isEditable) {
            // Только визуализация (панель блоков)
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(40.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = operator,
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(40.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        } else {
            // На главном экране — два поля для ввода
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        value = left,
                        onValueChange = { newLeft -> onInputChange("$newLeft|$right") },
                        modifier = Modifier
                            .width(110.dp)
                            .height(40.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Text(
                        text = operator,
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    OutlinedTextField(
                        value = right,
                        onValueChange = { newRight -> onInputChange("$left|$newRight") },
                        modifier = Modifier
                            .width(110.dp)
                            .height(40.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
    else if (block.id == 12) {
        val parts = inputValue.split("|", limit = 2)
        val left = parts.getOrNull(0) ?: ""
        val right = parts.getOrNull(1) ?: ""
        Box(
            modifier = Modifier
                .width(200.dp) // уменьшили ширину
                .height(56.dp)
                .background(Color(0xFFD25AE0), RoundedCornerShape(8.dp))
                .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 2.dp) // минимальный паддинг
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // Левая круглая скобка
                Text(
                    text = "(",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 2.dp)
                )
                // Первое поле
                OutlinedTextField(
                    value = left,
                    onValueChange = { newLeft -> onInputChange("$newLeft|$right") },
                    modifier = Modifier
                        .width(78.dp)
                        .height(40.dp),
                    singleLine = true,
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFD25AE0),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                // Второе поле
                OutlinedTextField(
                    value = right,
                    onValueChange = { newRight -> onInputChange("$left|$newRight") },
                    modifier = Modifier
                        .width(78.dp)
                        .height(40.dp),
                    singleLine = true,
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFD25AE0),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                // Правая круглая скобка
                Text(
                    text = ")",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
    else if(block.id  == 16) {
        // Блок if с кастомным оформлением
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(80.dp)
                .background(Color(0xFFF89402), RoundedCornerShape(8.dp))
        ) {
            // Рисуем выступы и левую полосу
            Canvas(modifier = Modifier.matchParentSize()) {
                // Левая вертикальная полоса
                drawRoundRect(
                    color = Color(0xFFF89402),
                    topLeft = Offset(0f, 0f),
                    size = Size(16.dp.toPx(), size.height),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                // Нижний выступ
                drawRect(
                    color = Color(0xFFF89402),
                    topLeft = Offset(16.dp.toPx(), size.height - 18.dp.toPx()),
                    size = Size(36.dp.toPx(), 18.dp.toPx())
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 22.dp, top = 12.dp)
            ) {
                Text(
                    text = "if",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .width(110.dp)
                        .height(36.dp),
                    singleLine = true,
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }





    else if(block.id in 17..21) {
        if (!isEditable) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                    Spacer(modifier = Modifier.width(1.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(block.color, RoundedCornerShape(8.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(56.dp)
                    .background(block.color, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                    Spacer(modifier = Modifier.width(1.dp))
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = onInputChange,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(block.color, RoundedCornerShape(8.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                        singleLine = true,
                        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )
                }
            }
        }
    }
}
  /*  else if(block.id in 9..11){

    }
    else if(block.id in 16..21){

    }
}*/