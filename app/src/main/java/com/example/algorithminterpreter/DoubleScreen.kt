import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithminterpreter.R
import com.example.algorithminterpreter.TomorrowFont
import com.example.algorithminterpreter.ui.theme.AlgorithmInterpreterTheme
import androidx.compose.animation.core.animateDpAsState
import Block
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun ProjectScreen() {
    var blocksVisible by remember { mutableStateOf(false) }
    var consoleVisible by remember { mutableStateOf(false) }
    val checkConsoleBord by animateDpAsState(
        targetValue = if (consoleVisible) (-298).dp else (0).dp
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0DDFF))
    ) {
        Image(
            painter = painterResource(id = R.drawable.block),
            contentDescription = "Blocks",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 150.dp)
                .width(35.dp)
                .height(150.dp)
                .clickable { if (!consoleVisible) blocksVisible = !blocksVisible  }
        )
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .height(40.dp)
                .fillMaxWidth()
                .background(color = Color(0xFF5F52F0)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {}

        Box(
            modifier = Modifier
                .offset(y=checkConsoleBord)
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .height(75.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF5F52F0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .height(55.dp)
                        .border(2.dp, Color.White, RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6D60F8),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Start",
                            fontFamily = TomorrowFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
                Text(
                    text = "StepCode",
                    color = Color.White,
                    fontFamily = TomorrowFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .height(55.dp)
                        .border(2.dp, Color.White, RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Clear",
                            fontFamily = TomorrowFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.console),
                contentDescription = "Console",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-38).dp)
                    .width(160.dp)
                    .height(40.dp)
                    .clickable { consoleVisible = !consoleVisible }
            )
        }
        //затемнение основного экрана
        if (blocksVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA868599))
                    .clickable { blocksVisible = false }
            )
        }
        // панель с блоками поверх затемнения
        if (blocksVisible) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(330.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .background(Color(0xFFE0DDFF))
                    .align(Alignment.TopStart)
                    .padding(start = 50.dp, end = 50.dp, top = 50.dp, bottom = 70.dp)
            ) {
                BlockPanel()
            }
        }
        if (consoleVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(298.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF8685C7))
            )
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    AlgorithmInterpreterTheme {
        ProjectScreen()
    }
}

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
    val cornerRadius = 6.dp // Унифицированное закругление для всех кнопок

    if (block.id == 23) {
        if (!isEditable) {
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(56.dp)
                    .background(Color(0xFF057CDE), RoundedCornerShape(6.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "0",
                    color = Color(0xFFD0D0D0),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        } else {
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
                        fontSize = 22.sp,
                        color = Color(0xFFD0D0D0),
                        fontWeight = FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            text = "0",
                            color = Color(0xFFD0D0D0),
                            fontSize = 22.sp,
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
    }
    else if (block.id == 22) {
        if (!isEditable) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(54.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "X",
                        color = Color(0xFFD0D0D0),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(54.dp)
                    .background(Color(0xFF35C1FE), RoundedCornerShape(cornerRadius))
                    .border(2.dp, Color.White, RoundedCornerShape(cornerRadius))
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        // Разрешаем только английские буквы
                        if (newValue.isEmpty() || newValue.all { it.isLetter() && it.isEnglishLetter() }) {
                            onInputChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(56.dp),

                    singleLine = true,
                    enabled = isInteractive,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            text = "X",
                            color = Color(0xFFD0D0D0),
                            fontSize = 22.sp,
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
    }
    // ... далее остальные блоки (арифметические, условия, скобки и т.д.) ...
}