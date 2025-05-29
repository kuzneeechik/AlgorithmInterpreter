package com.example.algorithminterpreter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithminterpreter.BlockView
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
        ArithmeticOperation(UUID.randomUUID(), "+"),
        ArithmeticOperation(UUID.randomUUID(), "%"),
        ArithmeticOperation(UUID.randomUUID(), "*"),
        ArithmeticOperation(UUID.randomUUID(), "-"),
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
        ConsoleRead(UUID.randomUUID()) ,
        ConsoleWrite(UUID.randomUUID())
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val onRow = mutableListOf<Block>()
        blocks.forEach { block ->
            if ( block is Const ||  block is Variable)
            {
                onRow.add(block)
            }
        }
        var flag = true
        blocks.forEach { block ->

            if(flag) {
                flag = false
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(block.color, RoundedCornerShape(8.dp))
                            .clickable { onBlockClick(onRow[1]) }
                    ) {
                        BlockView(

                            block = onRow[1],
                            onInputChange = {},
                            isEditable = false,
                            isInteractive = false
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(block.color, RoundedCornerShape(8.dp))
                            .clickable { onBlockClick(onRow[0]) }
                    ) {
                        BlockView(
                            block = onRow[0],
                            onInputChange = {},
                            isEditable = false,
                            isInteractive = false
                        )
                    }
                }
            }

            if (block !is Const && block !is Variable) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(block.color, RoundedCornerShape(8.dp))
                        .clickable { onBlockClick(block) }
                ) {
                    BlockView(
                        block = block,
                        onInputChange = {},
                        isEditable = false,
                        isInteractive = false
                    )
                }
            }
        }
    }
}
