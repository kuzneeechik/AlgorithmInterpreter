package com.example.algorithminterpreter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.util.UUID

open class Block(val id: UUID, val color: Color)

class Const(id: UUID) : Block(id, Color(0xFF057CDE)) {
    var value: String = ""
}

class Variable(id: UUID) : Block(id, Color(0xFF35C1FE)) {
    var name: String = ""
}

class ArrayElem(id: UUID) : Block(id, Color(0xFF35C1FE)) {
    var name: String = ""
    var index: String = ""
}

class IntVariable(id: UUID) : Block(id, Color(0xFF35C1FE)) {
    var variable: String = ""
}
class IntArray(id: UUID, val text: String = "int[]") :
    Block(id, Color(0xFF35C1FE)) {
    var variable: String = ""
    var size: String = ""
}

class Staples(id: UUID) : Block(id, Color(0xFFBA68C8)) {
    var elem: List<Block> = emptyList()
}

class ArithmeticOperation(id: UUID, val text: String) :
    Block(id, Color(0xFF2F860D))

class ComparisonOperation(id: UUID, val text: String) :
    Block(id, Color(0xFFF89402)) {
    var left: List<Block> = emptyList()
    var right: List<Block> = emptyList()
}

class Assignment(id: UUID, val text: String = "x =") :
    Block(id, Color(0xFF71C94F)) {
    var variable: Variable? = null
    var value: List<Block> = emptyList()
}

class If(id: UUID) : Block(id, Color(0xFFFFAD19)) {
    var condition: List<Block> = emptyList()
    var body: List<Block> = emptyList()
}

class IfElse(id: UUID) : Block(id, Color(0xFFFFAD19)) {
    var condition: List<Block> = emptyList()
    var ifBody: List<Block> = emptyList()
    var elseBody: List<Block> = emptyList()
}

class While(id: UUID) : Block(id, Color(0xFFFF5755)) {
    var condition: List<Block> = emptyList()
    var body: List<Block> = emptyList()
}

class ConsoleRead(id: UUID, val text: String = "console.read") :
    Block(id, Color(0xFF9A66FF)) {
    var elem: Block? = null
}

class ConsoleWrite(id: UUID, val text: String = "console.write") :
    Block(id, Color(0xFF9A66FF)) {
    var elem: Block? = null
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
        val onRow = mutableListOf<Block>()
        blocks.forEach { block ->
            if (block is Const || block is Variable || block is ArrayElem)
            {
                onRow.add(block)
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .background(onRow[1].color, RoundedCornerShape(8.dp))
                    .clickable { onBlockClick(onRow[1]) }
                    .zIndex(3f) // первый уровень
            ) {
                BlockView(
                    block = onRow[1],
                    onInputChange = {},
                    isInteractive = false
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .background(onRow[0].color, RoundedCornerShape(8.dp))
                    .clickable { onBlockClick(onRow[0]) }
                    .zIndex(3f)
            ) {
                BlockView(
                    block = onRow[0],
                    onInputChange = {},
                    isInteractive = false
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .background(onRow[2].color, RoundedCornerShape(8.dp))
                    .clickable { onBlockClick(onRow[2]) }
                    .zIndex(3f)
            ) {
                BlockView(
                    block = onRow[2],
                    onInputChange = {},
                    isInteractive = false
                )
            }
        }
        val arithmeticBlocks = blocks.filterIsInstance<ArithmeticOperation>()//это тоже первый
        val chunkedArithmetic = arithmeticBlocks.chunked(2)
        chunkedArithmetic.forEach { rowBlocks ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                rowBlocks.forEach { arithmeticBlock ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .background(arithmeticBlock.color, RoundedCornerShape(8.dp))
                            .clickable { onBlockClick(arithmeticBlock) }
                            .zIndex(3f)
                    ) {
                        BlockView(
                            block = arithmeticBlock,
                            onInputChange = {},
                            isInteractive = false
                        )
                    }
                }
            }
        }

        //это второй уровень
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
                        isInteractive = false
                    )
                }
            }
        }

        // это третий уровень
        blocks.forEach { block ->
            if (block is If || block is IfElse || block is While || block is ConsoleRead || block is ConsoleWrite) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clickable { onBlockClick(block) }
                        .zIndex(1f)
                ) {
                    BlockView(
                        block = block,
                        onInputChange = {},
                        isInteractive = false
                    )
                }
            }
        }
    }
}