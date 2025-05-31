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


    override fun getHeightInPx(density: Density): Float {
        return getLowestPoint(density) + with(density) {(12.dp * 2).toPx()}
    }

    override fun getWidthInPx(density: Density): Float{
        var width = 0f
        for (block in blocks) {
            if (width < block.block.getWidthInPx(density))
                width = block.block.getWidthInPx(density)
        }
        return width + with(density) {(8.dp * 2).toPx()}
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
        (16.dp * 2 + 56.dp + 10.dp + 8.dp * 3 + 48.dp + 8.dp * 2)

    override fun getHeightInPx(density: Density): Float {
        var inBox = 0f
        inBox += with(density) {(8.dp * 2).toPx()}
        for (blockIn in blocksInIf.blocks) {
            inBox += blockIn.block.getHeightInPx(density)
            inBox += with(density) {(8.dp).toPx()}
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
    var condition: String = ""
    var ifBody = BlocOfBlock(UUID.randomUUID())
    var elseBody: String = ""
    var elseBlock = BlocOfBlock(UUID.randomUUID())
    
    var standardHeight =
        (56.dp + 10.dp + 8.dp * 2 + 8.dp * 2)

    var standardWidth = 332.dp

    var standardBottomRowHeight = (8.dp * 2 + 48.dp)


    override fun getHeightInPx(density: Density): Float {
        var inBox = 0f

        for (block in ifBody.blocks) {
            inBox += block.block.getHeightInPx(density)
            inBox += with(density) { (standardHeight).toPx() }
        }
        inBox += elseBlock.getHeightInPx(density)
        inBox += with(density) { (standardHeight).toPx() }
        inBox = max(inBox, super.getHeightInPx(density))
        return inBox
    }

    override fun getWidthInPx(density: Density): Float {
        var inBox = with(density) { (standardWidth).toPx() }
        inBox = max(inBox, elseBlock.getHeightInPx(density))
        for (block in ifBody.blocks) {
            inBox = max(inBox, block.block.getWidthInPx(density))
        }
        inBox += with(density) { (8.dp).toPx() }
        inBox = max(inBox, super.getWidthInPx(density))
        return inBox
    }

    
}

class While(id: UUID) : Block(id, Color(0xFFFF5755), heightInDP = 350.dp, widthInDp = 255.dp) {
    var condition: String =""
    var inBlocks = BlocOfBlock(UUID.randomUUID())

    var standardHeight = (16.dp * 2 + 8.dp * 2 + 8.dp + 56.dp)
    var standardWidth = 332.dp


    override fun getHeightInPx(density: Density): Float {
        var res = inBlocks.getHeightInPx(density)
        res += with(density) { (standardHeight).toPx() }
        res = max(res, super.getHeightInPx(density))
        return res
    }

    override fun getWidthInPx(density: Density): Float {
        var res = max(with(density) { (standardWidth).toPx() }, inBlocks.getWidthInPx(density))
        res += with(density) { (8.dp).toPx() }
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