package com.example.algorithminterpreter

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
import com.example.algorithminterpreter.ui.theme.AlgorithmInterpreterTheme
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.geometry.Offset

data class PositionedBlock(
    val block: Block,
    var position: Offset,
    var inputValue: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen() {
    var blocksVisible by remember { mutableStateOf(false) }
    var consoleVisible by remember { mutableStateOf(false) }
    val checkConsoleBord by animateDpAsState(
        targetValue = if (consoleVisible) (-298).dp else (0).dp
    )
    val workspaceBlocks = remember { mutableStateListOf<PositionedBlock>()}
    var consoleInputText by remember { mutableStateOf("") }

    val check by animateDpAsState(
        targetValue = if (blocksVisible) 0.dp else (-300).dp
    )
    val checkButton by animateDpAsState(
        targetValue = if (blocksVisible) 300.dp else 0.dp
    )



    fun addBlockInOrder(block: Block) {
        val baseX = 400f
        val arrayBlocks = workspaceBlocks.size+1
        workspaceBlocks.add(PositionedBlock(block, Offset(baseX, 250f* arrayBlocks), ""))

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0DDFF))
    ) {

        FreeWorkspaceBlocksArea(

            blocks = workspaceBlocks,
            selectedBlock = null,
            onWorkspaceClick = {},
            onBlockMove = { index, offset ->
                val old = workspaceBlocks[index]
                workspaceBlocks[index] = old.copy(position = old.position + offset)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.block),
            contentDescription = "Blocks",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 150.dp)
                .width(35.dp)
                .height(150.dp)
                .clickable { if (!consoleVisible) blocksVisible = !blocksVisible }
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
                .offset(y = checkConsoleBord)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF5F52F0))
                .navigationBarsPadding()
                .height(85.dp)
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF5F52F0))
                    .height(85.dp),
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
                        containerColor = Color(0xFF5F52F0),
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
                    onClick = {
                        workspaceBlocks.clear()

                    },
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

        if (blocksVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA868599))
                    .clickable { blocksVisible = false }
            )
        }
        Box(
            modifier = Modifier
                .offset(x = check)
                .width(300.dp)
                .fillMaxHeight()
                .background(Color(0xFFE0DDFF)),
        ) {
            BlockPanel { block ->
                addBlockInOrder(block)
                blocksVisible = false
            }
            Image(
                painter = painterResource(id = R.drawable.block),
                contentDescription = "Blocks",
                modifier = Modifier
                    .offset(x = checkButton)
                    .padding(top = 150.dp)
                    .width(35.dp)
                    .height(150.dp)
                    .clickable { if (!consoleVisible) blocksVisible = !blocksVisible }
            )
        }

        if (consoleVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(298.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF8685C7))
            ) {
                TextField(
                    value = consoleInputText,
                    onValueChange = { consoleInputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                        .align(Alignment.TopCenter),
                    placeholder = { Text("Введите текст:", fontSize = 22.sp,
                        color = Color.White,letterSpacing = 2.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(

                        fontSize = 22.sp,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                        letterSpacing = 2.sp
                    ),
                    singleLine = false,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.White
                    )
                )
            }
        }

        if (consoleVisible )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF8685C7))
            ) {}
        }

        if (!consoleVisible && !blocksVisible)
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF5F52F0))
            ) {}
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