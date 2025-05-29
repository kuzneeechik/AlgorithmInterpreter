package com.example.algorithminterpreter

class BlockToText {
    fun convertBlockToText(block: Block): String {
        when (block) {
            is Const -> return block.value + ' '

            is Variable -> return block.name + ' '

            is ArrayElem -> return "${block.name}[${block.index}] "

            is IntVariable -> return "int ${block.variable} "

            is IntArray -> return "int ${block.variable}[${block.size}] "

            is ArithmeticOperation -> return "${block.text} "

            is Staples -> {
                val elem = block.elem.joinToString(" ") { convertBlockToText(it) }

                return "($elem) "
            }

            is ComparisonOperation -> {
                val left = block.left.joinToString(" ") { convertBlockToText(it) }
                val right = block.right.joinToString(" ") { convertBlockToText(it) }

                return "$left ${block.text} $right "
            }

            is Assignment -> {
                val variable = block.variable?.let { convertBlockToText(it) } ?: ""
                val value = block.value.joinToString(" ") { convertBlockToText(it) }

                return "$variable = $value "
            }

            is If -> {
                val condition = block.condition.joinToString(" ") { convertBlockToText(it) }
                val body = block.body.joinToString(" ") { convertBlockToText(it) }

                return "if $condition {$body} "
            }

            is IfElse -> {
                val condition = block.condition
                    .joinToString(" ") { convertBlockToText(it) }
                val ifBody = block.ifBody
                    .joinToString(" ") { convertBlockToText(it) }
                val elseBody = block.elseBody
                    .joinToString(" ") { convertBlockToText(it) }

                return "if $condition {$ifBody} else {$elseBody} "
            }

            is While -> {
                val condition = block.condition
                    .joinToString(" ") { convertBlockToText(it) }
                val body = block.body.joinToString(" ") { convertBlockToText(it) }

                return "while $condition {$body} "
            }

            is ConsoleRead -> {
                val elem = block.elem?.let { convertBlockToText(it) } ?: ""

                return "console.read $elem "
            }

            is ConsoleWrite -> {
                val elem = block.elem?.let { convertBlockToText(it) } ?: ""

                return "console.write $elem "
            }

            else -> throw IllegalArgumentException("Unknown block type: ${block.javaClass.simpleName}")
        }
    }

    fun convertBlocksToCode(blocks: List<Block>): String {
        return blocks.joinToString { convertBlockToText(it) }
    }
} 