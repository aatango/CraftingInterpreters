fun interpret(statements: List<Stmt>) {
    try {
        for (statement in statements) execute(statement)
    } catch (err: RuntimeError) {
        runtimeError(err)
    }
}

private fun execute(stmt: Stmt) {
    when (stmt) {
        is Expression -> evaluate(stmt.expression)
        is Print -> println(stringify(evaluate(stmt.expression)))
    }
}

private fun evaluate(expr: Expr): Any? {
    return when (expr) {
        is Grouping -> evaluate(expr.expression)
        is Literal -> expr.value
        is Binary -> {
            val left: Any? = evaluate(expr.left)
            val right: Any? = evaluate(expr.right)

            return when (expr.operator.type) {
                TokenType.BANG_EQUAL -> left != right
                TokenType.EQUAL_EQUAL -> left == right
                TokenType.GREATER -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) > (right as Double)
                }

                TokenType.GREATER_EQUAL -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) >= (right as Double)
                }

                TokenType.LESS -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) < (right as Double)
                }

                TokenType.LESS_EQUAL -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) <= (right as Double)
                }

                TokenType.MINUS -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) - (right as Double)
                }

                TokenType.SLASH -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) / (right as Double)
                }

                TokenType.STAR -> {
                    checkNumberOperands(expr.operator, left, right)
                    return (left as Double) * (right as Double)
                }

                TokenType.PLUS -> {
                    if (left is Double && right is Double) return left + right
                    if (left is String && right is String) return left + right
                    throw RuntimeError(
                        expr.operator,
                        "Operands must be two numbers, or two strings!"
                    )
                }

                else -> return null
            }
        }

        is Unary -> {
            val right: Any? = evaluate(expr.right)

            when (expr.operator.type) {
                TokenType.BANG -> !isTruthy(right)
                TokenType.MINUS -> {
                    checkNumberOperand(expr.operator, right)
                    return -(right as Double)
                }

                else -> {}
            }
        }
    }
}

private fun isTruthy(obj: Any?): Boolean {
    return obj as? Boolean ?: (obj != null)
}

private fun checkNumberOperand(operator: Token, operand: Any?) {
    if (operand !is Double) throw RuntimeError(operator, "Operand must be a number!")
}

private fun checkNumberOperands(operator: Token, lhs: Any?, rhs: Any?) {
    if (lhs !is Double || rhs !is Double) throw RuntimeError(operator, "Operands must be numbers!")
}

private fun stringify(obj: Any?): String {
    return if (obj == null) "nil" else {
        val text: String = obj.toString()
        return if (obj is Double && text.endsWith(".0")) text.substring(
            0,
            text.length - 2
        ) else text
    }
}