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


open class Block(val id: UUID, val text: String, val color: Color)
class PrintBlock(id: UUID, ) :
    Block(id = id, text = "print", color = Color(0xFF9A66FF)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части
    fun createId(): Block {
        return PrintBlock(UUID.randomUUID())
    }
}

class Operation(id: UUID, text: String) :
    Block(id = id, text = text, color = Color(0xFF2F860D)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части
    fun createId(): Block {
        return Operation(UUID.randomUUID(),text)
    }
}

class СomparisonOperation(id: UUID, text: String) :
    Block(id = id, text = text, color = Color(0xFFF89402)) {
    var valueInput:  String = ""
    //сюда нужно  писать функцию связывания вашей части
    var leftInput: String = ""
    var rightInput: String = ""
    fun createId(): Block {
        return СomparisonOperation(UUID.randomUUID(),text)
    }
}

class While(id: UUID) :
    Block(id = id, text = "while", color = Color(0xFFFF5755)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части
    fun createId(): Block {
        return While(UUID.randomUUID())
    }
}


class Staples(id: UUID, ) :
    Block(id = id, text = "", color = Color(0xFFBA68C8)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return Staples(UUID.randomUUID())
    }
}


class If(id: UUID, ) :
    Block(id = id, text = "if", color = Color(0xFFFFAD19)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return If(UUID.randomUUID())
    }
}

class Const(id: UUID, ) :
    Block(id = id, text = "0", color = Color(0xFF057CDE)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return Const(UUID.randomUUID())
    }
}
class Variable(id: UUID, ) :
    Block(id = id, text = "X", color = Color(0xFF35C1FE)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return Variable(UUID.randomUUID())
    }
}

class IfElse(id: UUID, ) :
    Block(id = id, text = "ifElsee", color = Color(0xFFFFAD19)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return IfElse(UUID.randomUUID())
    }
}
class BlueInt(id: UUID, ) :
    Block(id = id, text = "int", color = Color(0xFF35C1FE)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части
    fun createId(): Block {
        return BlueInt(UUID.randomUUID())
    }
}
class BlueIntArray(id: UUID, ) :
    Block(id = id, text = "int[]", color = Color(0xFF35C1FE)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return BlueIntArray(UUID.randomUUID())
    }
}
class GreenAssignment(id: UUID, ) :
    Block(id = id, text = "X=", color = Color(0xFF71C94F)) {
    var valueInput: String = ""
    //сюда нужно  писать функцию связывания вашей части

    fun createId(): Block {
        return GreenAssignment(UUID.randomUUID())
    }
}
@Composable
fun BlockPanel(onBlockClick: (Block) -> Unit) {
    val blocks = listOf(
        Variable(UUID.randomUUID()),
        Const(UUID.randomUUID()),
        Operation(UUID.randomUUID(), "+"),
        Operation(UUID.randomUUID(), "%"),
        Operation(UUID.randomUUID(), "*"),
        Operation(UUID.randomUUID(), "-"),
        Operation(UUID.randomUUID(), "/"),
        СomparisonOperation(UUID.randomUUID(), ">"),
        СomparisonOperation(UUID.randomUUID(), "<"),
        СomparisonOperation(UUID.randomUUID(), ">="),
        СomparisonOperation(UUID.randomUUID(), "<="),
        СomparisonOperation(UUID.randomUUID(), "=="),
        СomparisonOperation(UUID.randomUUID(), "!="),
        Staples(UUID.randomUUID()),
        BlueInt(UUID.randomUUID()),
        BlueIntArray(UUID.randomUUID()),
        GreenAssignment(UUID.randomUUID()),
        If(UUID.randomUUID()),
        IfElse(UUID.randomUUID()),
        While(UUID.randomUUID()),
        PrintBlock(UUID.randomUUID()) ,

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
