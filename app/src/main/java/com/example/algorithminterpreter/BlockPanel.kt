package com.example.algorithminterpreter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.util.UUID
import kotlin.math.max


object BaseVal {

    val BlockHorizontalPadding = 8.dp
    val BlockVerticalPadding = 12.dp
    val ColumnPadding = 8.dp
    val ColumnHorizontalArrangement = 8.dp
    val ColumnVerticalArrangement = 8.dp
    val RowHorizontalArrangement = 8.dp
    val BlockWidth = 256.dp
    val BlockHeight = 80.dp
    val wideBlockWidth = 350.dp
    val wideBlockHeight = 250.dp
    val standardBoxPadding = 16.dp
    val standardInputFieldHeight = 56.dp
    val standardInputFieldWidth = 220.dp
    val standardSpacerHeight = 10.dp
    val standardAddElseBlockButtonHeight = 48.dp
    val standardAddElseBlockButtonWidth = 104.dp
    val standardIfLabelWidth = 60.dp
    val addBlockButtonWidth = 60.dp
    val rowWidth = standardIfLabelWidth + standardInputFieldWidth + addBlockButtonWidth
    val borderWidth = 2.dp
    val roundCornerShape = 16.dp
    val bodyBlockTransparencyIndex = 0.2f
    val borderTransparencyIndex = 0.5f
    object WhileBlock {
        val horizontalArrangement = 8.dp

        val inputTextFieldWidth = 146.dp
        val inputTextFieldHeight = 56.dp

        val labelWidth = 70.dp

        val rowPadding = 16.dp

        val rowWidth = labelWidth + inputTextFieldWidth + 4.dp * 2 + horizontalArrangement * 6 + addBlockButtonWidth
        val overallPadding = 8.dp * 2 + standardBoxPadding * 2
    }
}





open class Block(
    val id: UUID,
    val color: Color,
    var parent: Block? = null,
    var heightInDP : Dp = 56.dp,
    var widthInDp: Dp = 55.dp

){
    open fun getHeightInPx(density: Density) : Float{
        return with(density) {heightInDP.toPx()}
    }
    open fun getWidthInPx(density: Density) : Float{
        return with(density) {widthInDp.toPx()}
    }
    fun getHeightInDp(density: Density): Dp{
        return with(density) {getHeightInPx(density).toDp()}
    }
    fun getWidthInDp(density: Density): Dp{
        return with(density) {getWidthInPx(density).toDp()}
    }
}


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


class Const(id: UUID) : Block(id, Color(0xFF057CDE), heightInDP = 56.dp, widthInDp = 55.dp) {
    var value: String = ""
}

class Variable(id: UUID) : Block(id, Color(0xFF35C1FE), heightInDP = 56.dp, widthInDp = 55.dp) {
    var name: String = ""
}

class ArrayElem(id: UUID) : Block(id, Color(0xFF35C1FE), heightInDP = 56.dp, widthInDp = 55.dp) {
    var name: String = ""
    var index: String = ""
    var allInfo : String = "${this.name}[${this.index}]"
}

class IntVariable(id: UUID) : Block(id, Color(0xFF35C1FE), heightInDP = 95.dp, widthInDp = 150.dp) {
    var variable: String = ""
}
class IntArray(id: UUID, val text: String = "int[]") :
    Block(id, Color(0xFF35C1FE), heightInDP = 95.dp, widthInDp = 260.dp) {
    var variable: String = ""
    var size: String = ""
}

class Staples(id: UUID) : Block(id, Color(0xFFBA68C8), heightInDP = 80.dp, widthInDp = 180.dp) {
    var elem: String =""
}

class ArithmeticOperation(id: UUID, val text: String) :
    Block(id, Color(0xFF2F860D), heightInDP = 56.dp, widthInDp = 55.dp)

class ComparisonOperation(id: UUID, val text: String) :
    Block(id, Color(0xFFF89402), heightInDP = 80.dp, widthInDp = 180.dp) {
    var left: String = ""
    var right: String = ""
}

class Assignment(id: UUID, val text: String = "x =") :
    Block(id, Color(0xFF71C94F), heightInDP = 95.dp, widthInDp = 195.dp) {
    var variable: String =""
    var value: String =""
}

class BlocOfBlock(
    blockId: UUID,
) : Block(
    id = blockId,
    color = Color(0xFF45A3FF)
) {
    val blocks = mutableListOf<PositionedBlock>()

    fun addBlock(posBlock: PositionedBlock) {
        posBlock.block.parent = this
        blocks.add(posBlock)
    }

    fun getLowestPoint(density: Density): Float{
        var height = 0f
        for (block in blocks) {
            height += block.block.getHeightInPx(density)
        }
        return height
    }

    fun getPointToSpawn(density: Density): Float{
        if (blocks.isEmpty()){
            return 0f
        }
        return getLowestPoint(density) - blocks.last().block.getHeightInPx(density)
    }

    override fun getHeightInPx(density: Density): Float {
        return getLowestPoint(density) + with(density) {(BaseVal.BlockVerticalPadding * 2).toPx()}
    }

    override fun getWidthInPx(density: Density): Float{
        var width = 0f
        for (block in blocks) {
            if (width < block.block.getWidthInPx(density))
                width = block.block.getWidthInPx(density)
        }
        return width + with(density) {(BaseVal.BlockHorizontalPadding * 2).toPx()}
    }
    fun copy(): Block {
        return BlocOfBlock(UUID.randomUUID())
    }
}

class If(
    blockId: UUID
) : Block(
    id = blockId,
    color = Color(0xFFBD3FCB)
) {
    var condition : String = ""
    var blocksInIf = BlocOfBlock(blockId = UUID.randomUUID())

    var standardHeight =
        (BaseVal.standardBoxPadding * 2 + BaseVal.standardInputFieldHeight + BaseVal.standardSpacerHeight + BaseVal.ColumnHorizontalArrangement * 3 + BaseVal.standardAddElseBlockButtonHeight + BaseVal.ColumnPadding * 2)

    override fun getHeightInPx(density: Density): Float {
        var inBox = 0f
        inBox += with(density) {(BaseVal.ColumnPadding * 2).toPx()}
        for (blockIn in blocksInIf.blocks) {
            inBox += blockIn.block.getHeightInPx(density)
            inBox += with(density) {(BaseVal.ColumnHorizontalArrangement).toPx()}
        }
        return inBox
    }

    override fun getWidthInPx(density: Density): Float {
        var inBox = 0f
        for (blockIn in blocksInIf.blocks) {
            if (blockIn.block.getWidthInPx(density) > inBox){
                inBox = blockIn.block.getWidthInPx(density)
            }
        }
        return inBox
    }


    fun deepCopy(): Block {
        return If(UUID.randomUUID())
    }
}

class IfElse(id: UUID) : Block(id, Color(0xFFFFAD19), heightInDP = 95.dp, widthInDp = 250.dp) {
    var condition: List<Block> = emptyList()
    var ifBody: List<Block> = emptyList()
    var elseBody: List<Block> = emptyList()

    var inBlock = BlocOfBlock(UUID.randomUUID())
}

class While(id: UUID) : Block(id, Color(0xFFFF5755), heightInDP = 350.dp, widthInDp = 255.dp) {
    var condition: String =""
    var inBlocks = BlocOfBlock(UUID.randomUUID())

    var standardHeight = (16.dp * 2 + 8.dp * 2 + 8.dp + 56.dp)
    var standardWidth = BaseVal.WhileBlock.rowWidth


    override fun getHeightInPx(density: Density): Float {
        var res = inBlocks.getHeightInPx(density)
        res += with(density) { (standardHeight).toPx() }
        res = max(res, super.getHeightInPx(density))
        return res
    }

    override fun getWidthInPx(density: Density): Float {
        var res = max(with(density) { (standardWidth).toPx() }, inBlocks.getWidthInPx(density))
        res += with(density) { (BaseVal.WhileBlock.overallPadding).toPx() }
        res = max(res, super.getWidthInPx(density))
        return res
    }
}

class ConsoleRead(id: UUID, val text: String = "console.read") : Block(id, Color(0xFF9A66FF), heightInDP = 95.dp, widthInDp = 250.dp) {
    var elem: Block? = null
}

class ConsoleWrite(id: UUID, val text: String = "console.write") : Block(id, Color(0xFF9A66FF), heightInDP = 95.dp, widthInDp = 250.dp) {
    var elem: String = ""
}


@Composable
fun BlockPanel(onBlockClick: (Block) -> Unit) {
    val blocks = listOf(
        Variable(UUID.randomUUID()),
        Const(UUID.randomUUID()),
        ArrayElem(UUID.randomUUID()),
        ArithmeticOperation(UUID.randomUUID(), "+"),
        ArithmeticOperation(UUID.randomUUID(), "—"),
        ArithmeticOperation(UUID.randomUUID(), "*"),
        ArithmeticOperation(UUID.randomUUID(), "%"),
        ArithmeticOperation(UUID.randomUUID(), "/"),
        ComparisonOperation(UUID.randomUUID(), ">"),
        ComparisonOperation(UUID.randomUUID(), "<"),
        ComparisonOperation(UUID.randomUUID(), ">="),
        ComparisonOperation(UUID.randomUUID(), "<="),
        ComparisonOperation(UUID.randomUUID(), "=="),
        ComparisonOperation(UUID.randomUUID(), "!="),
        Staples(UUID.randomUUID()),
        IntVariable(UUID.randomUUID()),
        IntArray(UUID.randomUUID()),
        Assignment(UUID.randomUUID()),
        If(UUID.randomUUID()),
        IfElse(UUID.randomUUID()),
        While(UUID.randomUUID()),
        ConsoleRead(UUID.randomUUID()),
        ConsoleWrite(UUID.randomUUID())
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        blocks.forEach { block ->
            if (block is ComparisonOperation || block is Staples || block is IntVariable || block is IntArray || block is Assignment) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clickable { onBlockClick(block) }
                        .zIndex(2f)
                ) {
                    BlockView(
                        block = block,
                        onInputChange = {},
                        isInteractive = false,
                        onClick = {},
                        onSwapMenu = {}
                    )
                }
            }
        }

        // это третий уровень
        blocks.forEach { block ->
            if (block is If || block is IfElse || block is While || block is ConsoleRead || block is ConsoleWrite) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .clickable { onBlockClick(block) }
                        .zIndex(3f)
                ) {
                    BlockView(
                        block = block,
                        onInputChange = {},
                        isInteractive = false,
                        onClick = {},
                        onSwapMenu = {}
                    )
                }
            }
        }
    }
}