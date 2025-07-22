fun interpret(statements: List<Stmt>) {
    try {
        for (statement in statements) execute(statement)
    } catch (err: RuntimeError) {
        runtimeError(err)
    }
}

var environment: Environment = Environment()

private fun execute(stmt: Stmt) {
    when (stmt) {
        is Block -> executeBlock(stmt.statements, Environment(environment))
        is Expression -> evaluate(stmt.expression)
        is Print -> println(stringify(evaluate(stmt.expression)))
        is Var -> stmt.initializer?.let(::evaluate)
            .also { environment.define(stmt.name.lexeme, it) }
    }
}

private fun executeBlock(statements: List<Stmt?>, env: Environment) {
    environment
        .also { environment = env }
        .also { for (statement in statements) statement?.let(::execute) }
        .also { environment = it }
}

private fun evaluate(expr: Expr): Any? {
    return when (expr) {
        is Assign -> evaluate(expr.value)
            .apply { environment.assign(expr.name, this) }
            .run { return this }

        is Grouping -> evaluate(expr.expression)
        is Literal -> expr.value
        is Variable -> environment.get(expr.name)
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
                        expr.operator, "Operands must be two numbers, or two strings!"
                    )
                }

                else -> return null
            }
        }

        is Unary -> evaluate(expr.right).run {
            when (expr.operator.type) {
                TokenType.BANG -> !isTruthy(this)
                TokenType.MINUS -> {
                    checkNumberOperand(expr.operator, this)
                    return -(this as Double)
                }

                else -> {}
            }
        }
    }
}

private fun isTruthy(obj: Any?): Boolean = obj as? Boolean ?: (obj != null)

private fun checkNumberOperand(operator: Token, operand: Any?) {
    if (operand !is Double) throw RuntimeError(operator, "Operand must be a number!")
}

private fun checkNumberOperands(operator: Token, lhs: Any?, rhs: Any?) {
    if (lhs !is Double || rhs !is Double) throw RuntimeError(
        operator,
        "Operands must be numbers!"
    )
}

private fun stringify(obj: Any?): String = obj?.let {
    it.toString().run {
        if (endsWith(".0") && it is Double) substring(0, length - 2)
        else this
    }
} ?: "nil"