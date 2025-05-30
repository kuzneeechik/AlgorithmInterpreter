package com.example.algorithminterpreter

import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.Continuation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.algorithminterpreter.ui.theme.DarkPurple
import com.example.algorithminterpreter.ui.theme.LightLightPurple
import com.example.algorithminterpreter.ui.theme.LightPurple
import com.example.algorithminterpreter.ui.theme.PurpleGrey
import kotlinx.coroutines.delay

data class PositionedBlock(
    val block: Block,
    var position: Offset,
    var inputValue: String = "",
    val zIndex: Float = when (block) {
        is Const, is Variable, is ArrayElem, is ArithmeticOperation -> 3f  //выше всех
        is ComparisonOperation, is Staples, is IntVariable, is IntArray, is Assignment -> 2f
        is If, is IfElse, is While, is ConsoleRead, is ConsoleWrite -> 1f
        else -> 0f
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen() {
    val consoleOutput = remember { mutableStateListOf<String>() }
    var blocksVisible by remember { mutableStateOf(false) }
    var consoleVisible by remember { mutableStateOf(false) }
    val checkConsoleBord by animateDpAsState(
        targetValue = if (consoleVisible) (-298).dp else (0).dp //открывание закрывание консоли
    )
    var workspaceBlocks = remember { mutableStateListOf<PositionedBlock>() } //изм блоков
    var consoleInputText by remember { mutableStateOf("") } //сохранение текста введенного в консоль

    val check by animateDpAsState(
        targetValue = if (blocksVisible) 0.dp else (-300).dp
    )
    val checkButton by animateDpAsState(
        targetValue = if (blocksVisible) 300.dp else 0.dp
    )
    // открыта закрыта панель блоков

    var inputContinuation: Continuation<String>? by remember { mutableStateOf(null) }
    var waitingForStartInput by remember { mutableStateOf(false) }
    val inputQueue = remember { mutableStateListOf<String>() }  // очередь введённых строк
    var currentPrompt by remember { mutableStateOf<String?>(null) }

    val code = "int n " +
            "n = 7 " +
            "int mas[n] " +
            "int i " +
            "while i < n " +
            "console.read mas[i] " +
            "i = i + 1 endwhile " +
            "int j " +
            "int k " +
            "int b " +
            "while j < n " +
            "k = 0 " +
            "while k < n - 1 " +
            "if mas[k] > mas[k + 1] " +
            "b = mas[k] " +
            "mas[k] = mas[k + 1] " +
            "mas[k + 1] = b endif k = k + 1 endwhile j = j + 1 endwhile " +
            "i = 0 " +
            "while i < n " +
            "console.write mas[i] i = i + 1 endwhile"

    fun output(text: String) {
        consoleOutput.add(text)
    }

    suspend fun awaitInput(variableName: String): String {
        return suspendCoroutine { continuation ->
            currentPrompt = "Введите $variableName:"
            inputContinuation = continuation
            waitingForStartInput = true
        }
    }

    val coroutineScope = rememberCoroutineScope()

    fun startInterpreter() {
        consoleOutput.clear()
        inputQueue.clear()


        coroutineScope.launch {
            try {
                val lexer = Lexer(code)
                lexer.lexAnalysis()
                val parser = Parser(lexer.tokens, ::output)

                parser.getInput = { awaitInput(it) }

                val rootNode = parser.parseCode()
                parser.run(rootNode)
            } catch (e: Exception) {
                output("Error occurred: ${e.message}")
            }
        }
    }

    fun addBlockInOrder(block: Block) {
        val baseX = 400f
        val arrayBlocks = workspaceBlocks.size + 1
        workspaceBlocks.add(PositionedBlock(block, Offset(baseX, 100f * arrayBlocks), ""))

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightLightPurple)
    ) {

        FreeWorkspaceBlocksArea(

            blocks = workspaceBlocks,
            selectedBlock = null,
            onWorkspaceClick = {},
            onBlockMove = { index, offset -> //обновляет позицию блока в списке при перетаскивании
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
                .clickable {
                    if (!consoleVisible) {
                        blocksVisible = !blocksVisible
                    }
                }
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .height(40.dp)
                .fillMaxWidth()
                .background(color = DarkPurple),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {}

        Box(
            modifier = Modifier
                .offset(y = checkConsoleBord) //по у меняется открытие закрытие
                .align(Alignment.BottomCenter)
                .background(DarkPurple)
                .navigationBarsPadding()
                .height(85.dp)
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkPurple)
                    .height(85.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        consoleOutput.clear()
                        consoleInputText = ""
                        inputQueue.clear()
                        waitingForStartInput = false

                        startInterpreter()
                    },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .height(55.dp)
                        .border(2.dp, Color.White, RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkPurple,
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
                    .background(PurpleGrey)
                    .clickable { blocksVisible = false }
            )
        }
        Box(
            modifier = Modifier
                .offset(x = check)
                .width(300.dp)
                .fillMaxHeight()
                .background(LightLightPurple),
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

        var cursorVisible by remember { mutableStateOf(true) }
        val cursorAlpha by animateFloatAsState(
            targetValue = if (cursorVisible) {
                1f
            } else {
                0f
            }, //(видим/невидим)
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse //это то что повтор
            )
        )
// это запуск мигания
        LaunchedEffect(Unit) {
            while (true) {
                delay(500)
                cursorVisible = !cursorVisible
            }
        }
        if (consoleVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(298.dp)
                    .align(Alignment.BottomCenter)
                    .background(LightPurple)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                        .align(Alignment.TopCenter)
                ) {
                    if (currentPrompt != null) {
                        Text(
                            text = currentPrompt!!,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    BasicTextField(
                        value = consoleInputText,
                        onValueChange = { consoleInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp), // отступ вниз после ввода
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            letterSpacing = 2.sp
                        ),
                        cursorBrush = SolidColor(Color.White.copy(alpha = cursorAlpha)),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val input = consoleInputText.trim()
                                if (input.isNotEmpty() && waitingForStartInput) {
                                    val inputs = input.split(Regex("\\s+"))
                                    inputQueue.clear()
                                    inputQueue.addAll(inputs)
                                    inputContinuation?.resume(inputs[0])
                                    inputContinuation = null
                                    consoleInputText = ""
                                    waitingForStartInput = false
                                    currentPrompt = null
                                }
                            }
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                innerTextField()
                                if (consoleInputText.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(24.dp)
                                            .background(Color.White.copy(alpha = cursorAlpha))
                                    )
                                }
                            }
                        }
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(consoleOutput) { line ->
                            Text(
                                text = line,
                                fontSize = 22.sp,
                                color = Color.White,
                                textAlign = TextAlign.Start,
                                letterSpacing = 2.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (consoleVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .align(Alignment.BottomCenter)
                    .background(LightPurple)
            ) {}
        }

        if (!consoleVisible && !blocksVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .align(Alignment.BottomCenter)
                    .background(DarkPurple)
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