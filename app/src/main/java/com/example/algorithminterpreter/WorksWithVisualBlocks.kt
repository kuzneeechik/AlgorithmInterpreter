package com.example.algorithminterpreter
import BlockIf
import BlockIfElse
import BlockWhile
import ConsoleReadBlocks
import ConsoleWriteBlocks
import GreenAssignmentBlocks
import IntBlock
import IntBlockArray
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreeWorkspaceBlocksArea(
    blocks: MutableList<PositionedBlock>,
    onWorkspaceClick: (Offset) -> Unit,
    onBlockMove: (Int, Offset) -> Unit,
    selectedBlock: Block?,
    leftPanelWidth: Dp = 140.dp,
    topBoundaryDp: Dp = 90.dp,
    bottomBoundaryDp: Dp = 190.dp,
    blockWidth: Dp = 270.dp,
    menu : Boolean = false,
    changeMenuForBlockOfBlock: ()-> Unit
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

    var middle = remember {mutableStateListOf<PositionedBlock>()}
    var currentBlockOfBlock by remember { mutableStateOf<BlocOfBlock?>(null) }


    fun addBlock(blockIn: BlocOfBlock){
        currentBlockOfBlock = blockIn
        middle.clear()
        middle.addAll(currentBlockOfBlock!!.blocks)
        changeMenuForBlockOfBlock()
    }

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
                is IntVariable, is IntArray, is Assignment -> 2
                is If, is IfElse, is While, is ConsoleRead, is ConsoleWrite -> 1 // 3 ниже всех
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
                            isDragging -> 10f // перетаскиваемый всегда сверху
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
                    onClick = {},
                    menu = menu,
                    onInputChange = { blocks[originalIndex] = positionedBlock.copy() },
                    onSwapMenu = {blockOfBlock -> addBlock(blockOfBlock)}
                )
            }
        }
    }
}
@Composable
fun BlockView(
    block: Block,
    isInteractive: Boolean = true,
    menu : Boolean = true,
    onClick: ()-> Unit,
    onInputChange: (String) -> Unit = {},
    onSwapMenu: (BlocOfBlock)-> Unit
) {
    val density = LocalDensity.current
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
                    BasicTextField(
                        value = block.elem,
                        onValueChange = { block.elem = it },
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
                    BasicTextField(
                        value = block.elem,
                        onValueChange = { block.elem = it },
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
                    BasicTextField(
                        value = block.variable,
                        onValueChange = { block.variable = it },
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
                    BasicTextField(
                        value = block.variable,
                        onValueChange = { block.variable = it },
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
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "[",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    BasicTextField(
                        value = block.size,
                        onValueChange = { block.size = it },
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
                    BasicTextField(
                        value = block.variable,
                        onValueChange = { block.variable = it },
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
                    )
                    Text(
                        text = "=",
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    BasicTextField(
                        value = block.value,
                        onValueChange = { block.value = it },
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
                    )
                }
            }
        }

        is If -> {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(shape = BlockIf(), color = Color(0xFFFFAD19))
                    .height(block.getHeightInDp(density))
                    .width(block.getWidthInDp(density))
                    .defaultMinSize(minHeight = 95.dp, minWidth = 190.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(BaseVal.ColumnPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(BaseVal.ColumnHorizontalArrangement),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "if",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TomorrowFont,
                        )
                        OutlinedTextField(
                            value = block.condition,
                            onValueChange = {
                                block.condition = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(56.dp),
                            enabled = !menu,
                            singleLine = true,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = BaseVal.BlockHorizontalPadding,
                                vertical = BaseVal.BlockVerticalPadding
                            )
                            .defaultMinSize(minHeight = 80.dp, minWidth = 252.dp)
                            .height(block.body.getHeightInDp(density))
                            .width(block.body.getWidthInDp(density))
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        block.body.blocks.forEach { blockInner ->
                            FreeWorkspaceBlocksArea(
                                blocks = block.body.blocks,
                                selectedBlock = null,
                                onWorkspaceClick = {},
                                onBlockMove = { index, offset ->
                                    val old = block.body.blocks[index]
                                    block.body.blocks[index] =
                                        old.copy(position = old.position + offset)
                                },
                                menu = menu,
                                changeMenuForBlockOfBlock = {}
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            if (!menu) {
                                onSwapMenu(block.body)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0C0B0C),
                            contentColor = Color.White
                        ),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Outlined.Favorite, contentDescription = null)
                        }
                    }
                }
            }
        }

        is IfElse -> {
            Box(
                modifier = Modifier
                    .background(shape = BlockIf(), color = Color(0xFFFFAD19))
                    .width(250.dp)
                    .height(95.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(BaseVal.ColumnPadding),
                    verticalArrangement = Arrangement.spacedBy(BaseVal.ColumnVerticalArrangement)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(BaseVal.ColumnHorizontalArrangement),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            text = "if",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TomorrowFont,
                            modifier = Modifier.width(BaseVal.standardIfLabelWidth)
                        )
                        OutlinedTextField(
                            value = block.condition,
                            onValueChange = {
                                block.condition = it
                            },
                            modifier = Modifier
                                .width(BaseVal.standardInputFieldWidth)
                                .height(BaseVal.standardInputFieldHeight),
                            enabled = !menu,
                            singleLine = true,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = BaseVal.BlockHorizontalPadding,
                                vertical = BaseVal.BlockVerticalPadding
                            )
                            .defaultMinSize(
                                minHeight = BaseVal.BlockHeight,
                                minWidth = BaseVal.BlockWidth
                            )
                            .height(block.ifBody.getHeightInDp(density))
                            .width(block.ifBody.getWidthInDp(density))
                            .background(
                                color = Color.White.copy(alpha = BaseVal.bodyBlockTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )
                            .border(
                                width = BaseVal.borderWidth,
                                color = Color.White.copy(alpha = BaseVal.borderTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )
                    ) {
                        block.ifBody.blocks.forEach { blockInner ->
                            key(blockInner.block.id) {
                                FreeWorkspaceBlocksArea(
                                    blocks = block.ifBody.blocks,
                                    selectedBlock = null,
                                    onWorkspaceClick = {},
                                    onBlockMove = { index, offset ->
                                        val old = block.ifBody.blocks[index]
                                        block.ifBody.blocks[index] =
                                            old.copy(position = old.position + offset)
                                    },
                                    menu = menu,
                                    changeMenuForBlockOfBlock = {}
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(BaseVal.standardSpacerHeight))
                    }

                    Button(
                        onClick = {
                            if (!menu) {
                                onSwapMenu(block.ifBody)
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        ), modifier = Modifier.width(BaseVal.addBlockButtonWidth)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Outlined.Favorite, contentDescription = null)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(unbounded = true)
                            .defaultMinSize(
                                minHeight = BaseVal.BlockHeight,
                                minWidth = BaseVal.BlockWidth
                            ),
                        verticalArrangement = Arrangement.spacedBy(BaseVal.ColumnVerticalArrangement)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "else",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = TomorrowFont,
                                modifier = Modifier.width(BaseVal.standardIfLabelWidth)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(
                                    horizontal = BaseVal.BlockHorizontalPadding,
                                    vertical = BaseVal.BlockVerticalPadding
                                )
                                .defaultMinSize(
                                    minHeight = BaseVal.BlockHeight,
                                    minWidth = BaseVal.BlockWidth
                                )
                                .height(block.elseBody.getHeightInDp(density))
                                .width(block.elseBody.getWidthInDp(density))
                                .background(
                                    color = Color.White.copy(alpha = BaseVal.bodyBlockTransparencyIndex),
                                    shape = RoundedCornerShape(BaseVal.roundCornerShape)
                                )
                                .border(
                                    width = BaseVal.borderWidth,
                                    color = Color.White.copy(alpha = BaseVal.borderTransparencyIndex),
                                    shape = RoundedCornerShape(BaseVal.roundCornerShape)
                                )
                        ) {
                            block.elseBody.blocks.forEach { blockFor ->
                                key(blockFor.block.id) {
                                    FreeWorkspaceBlocksArea(
                                        blocks = block.elseBody.blocks,
                                        selectedBlock = null,
                                        onWorkspaceClick = {},
                                        onBlockMove = { index, offset ->
                                            val old = block.elseBody.blocks[index]
                                            block.elseBody.blocks[index] =
                                                old.copy(position = old.position + offset)
                                        },
                                        menu = menu,
                                        changeMenuForBlockOfBlock = {}
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(BaseVal.standardSpacerHeight))
                        }
                        Button(
                            onClick = {
                                if (!menu) {
                                    onSwapMenu(block.elseBody)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Yellow,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.width(BaseVal.addBlockButtonWidth)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Outlined.Favorite, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }

        is While -> {
            Box(
                modifier = Modifier
                    .background(shape = BlockWhile(), color = Color(0xFFFF5755))
                    .width(block.getWidthInDp(density))
                    .height(block.getHeightInDp(density)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(BaseVal.ColumnPadding),
                    verticalArrangement = Arrangement.spacedBy(BaseVal.ColumnVerticalArrangement)
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(BaseVal.WhileBlock.horizontalArrangement),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "while",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TomorrowFont,
                            modifier = Modifier.width(BaseVal.WhileBlock.labelWidth)
                        )

                        Text(
                            text = "(",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TomorrowFont,
                            modifier = Modifier.width(4.dp)
                        )

                        OutlinedTextField(
                            value = block.condition,
                            onValueChange = { block.condition = it },
                            modifier = Modifier
                                .width(BaseVal.WhileBlock.inputTextFieldWidth)
                                .height(BaseVal.WhileBlock.inputTextFieldHeight),
                            enabled = !menu,
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = TomorrowFont
                            )
                        )

                        Text(
                            text = ")",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TomorrowFont,
                            modifier = Modifier.width(4.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = BaseVal.BlockHorizontalPadding,
                                vertical = BaseVal.BlockVerticalPadding
                            )
                            .defaultMinSize(
                                minHeight = BaseVal.BlockHeight,
                                minWidth = BaseVal.BlockWidth
                            )
                            .height(block.WhileBody.getHeightInDp(density))
                            .width(block.WhileBody.getWidthInDp(density))
                            .background(
                                color = Color.White.copy(alpha = BaseVal.bodyBlockTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )
                            .border(
                                width = BaseVal.borderWidth,
                                color = Color.White.copy(alpha = BaseVal.borderTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )
                    ) {
                        block.WhileBody.blocks.forEach { blockInner ->
                            key(blockInner.block.id) {
                                FreeWorkspaceBlocksArea(
                                    blocks = block.WhileBody.blocks,
                                    selectedBlock = null,
                                    onWorkspaceClick = {},
                                    onBlockMove = { index, offset ->
                                        val old = block.WhileBody.blocks[index]
                                        block.WhileBody.blocks[index] =
                                            old.copy(position = old.position + offset)
                                    },
                                    menu = menu,
                                    changeMenuForBlockOfBlock = {}
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(BaseVal.standardSpacerHeight))
                    }

                    Button(
                        onClick = {
                            if (!menu) {
                                onSwapMenu(block.WhileBody)
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7943DE), contentColor = Color.White
                        ), modifier = Modifier.width(BaseVal.addBlockButtonWidth)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Outlined.Favorite, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}




