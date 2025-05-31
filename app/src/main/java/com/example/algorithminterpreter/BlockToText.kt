package com.example.algorithminterpreter

class BlockToText {
    fun convertBlockToText(block: Block): String {
        when (block) {
            is IntVariable -> return "int ${block.variable} "

            is IntArray -> return "int ${block.variable}[${block.size}] "

            is Assignment -> return "${block.variable} = ${block.value} "

            is If -> {
                val condition = block.condition

                val body = block.body.blocks
                    .joinToString(" ") { convertBlockToText(it.block) }

                return "if $condition {$body} "
            }

            is IfElse -> {
                val condition = block.condition

                val ifBody = block.ifBody.blocks
                    .joinToString(" ") { convertBlockToText(it.block) }

                val elseBody = block.elseBody.blocks
                    .joinToString(" ") { convertBlockToText(it.block) }

                return "if $condition {$ifBody} else {$elseBody} "
            }

            is While -> {
                val condition = block.condition

                val body = block.WhileBody.blocks
                    .joinToString(" ") { convertBlockToText(it.block) }

                return "while $condition {$body} "
            }

            is ConsoleRead -> return "console.read ${block.elem} "

            is ConsoleWrite -> return "console.write ${block.elem} "

            else -> throw IllegalArgumentException("Unknown block type: ${block.javaClass.simpleName}")
        }
    }

    fun convertBlocksToCode(blocks: List<Block>): String {
        return blocks.joinToString { convertBlockToText(it) }
    }
} 