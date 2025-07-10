fun printAst(expr: Expr): String {
    return when (expr) {
        is Binary -> parenthesise(expr.operator.lexeme, expr.left, expr.right)
        is Grouping -> parenthesise("Grouping", expr.expression)
        is Literal -> if (expr.value == null) "nil" else expr.value.toString()
        is Unary -> parenthesise(expr.operator.lexeme, expr.right)
    }
}

private fun parenthesise(name: String, vararg expressions: Expr): String {
    val builder = StringBuilder()

    builder.append("($name")
    for (expr in expressions) {
        builder.append(" ${printAst(expr)}")
    }
    builder.append(")")

    return builder.toString()
}
