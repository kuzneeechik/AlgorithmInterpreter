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
    menu : Boolean,
    menuForBlockOfBlock: Boolean = false,
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
                is Const, is Variable, is ArrayElem, is ArithmeticOperation -> 3
                is ComparisonOperation, is Staples, is IntVariable, is IntArray, is Assignment -> 2
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
                    BasicTextField(
                        value = block.left,
                        onValueChange = { block.left = it },
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
                        text = operator,
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    BasicTextField(
                        value = block.right,
                        onValueChange = { block.right = it
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
                    )

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
                            .height(block.blocksInIf.getHeightInDp(density))
                            .width(block.blocksInIf.getWidthInDp(density))
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
                        block.blocksInIf.blocks.forEach { blockInner ->
                            FreeWorkspaceBlocksArea(
                                blocks = block.blocksInIf.blocks,
                                selectedBlock = null,
                                onWorkspaceClick = {},
                                onBlockMove = { index, offset ->
                                    val old = block.blocksInIf.blocks[index]
                                    block.blocksInIf.blocks[index] = old.copy(position = old.position + offset)
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
                                onSwapMenu(block.blocksInIf)
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
                            Icon(Icons.Outlined.Create, contentDescription = null)
                        }
                    }
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
                        text = "else",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = BaseVal.BlockHorizontalPadding,
                            vertical = BaseVal.BlockVerticalPadding
                        )
                        .defaultMinSize(minHeight = 80.dp, minWidth = 252.dp)
                        .height(block.inBlock.getHeightInDp(density))
                        .width(block.inBlock.getWidthInDp(density))
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
                    block.inBlock.blocks.forEach { blockInner ->
                        FreeWorkspaceBlocksArea(
                            blocks = block.inBlock.blocks,
                            selectedBlock = null,
                            onWorkspaceClick = {},
                            onBlockMove = { index, offset ->
                                val old = block.inBlock.blocks[index]
                                block.inBlock.blocks[index] = old.copy(position = old.position + offset)
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
                            onSwapMenu(block.inBlock)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000),
                        contentColor = Color.White
                    ),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
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

                        Button(
                            onClick = {
                                if (!menu) {
                                    onSwapMenu(block.inBlocks)
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7943DE), contentColor = Color.White
                            ), modifier = Modifier.width(BaseVal.addBlockButtonWidth)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Outlined.Add, contentDescription = null)
                            }
                        }
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
                            .height(block.inBlocks.getHeightInDp(density))
                            .width(block.inBlocks.getWidthInDp(density))
                            .clickable { innerBlockWithDeleteShownId = null }
                            .background(
                                color = Color.White.copy(alpha = BaseVal.bodyBlockTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )
                            .border(
                                width = BaseVal.borderWidth,
                                color = Color.White.copy(alpha = BaseVal.borderTransparencyIndex),
                                shape = RoundedCornerShape(BaseVal.roundCornerShape)
                            )) {
                        block.inBlocks.blocks.forEach { blockInner ->
                            key(blockInner.block.id) {
                                FreeWorkspaceBlocksArea(
                                    blocks = block.inBlocks.blocks,
                                    selectedBlock = null,
                                    onWorkspaceClick = {},
                                    onBlockMove = { index, offset ->
                                        val old = block.inBlocks.blocks[index]
                                        block.inBlocks.blocks[index] = old.copy(position = old.position + offset)
                                    },
                                    menu = menu,
                                    changeMenuForBlockOfBlock = {}
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(BaseVal.standardSpacerHeight))
                    }
                }
            }
        }

        is ArrayElem -> {
            Box(
                modifier = Modifier
                    .width(150.dp)
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
    }
}




