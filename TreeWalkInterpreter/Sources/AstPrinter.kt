class AstPrinter : Expr.Visitor<String> {
    fun print(expr: Expr): String = expr.accept(this)

    override fun visitBinaryExpr(expr: Binary): String =
        parenthesise(expr.operator.lexeme, expr.left, expr.right)

    override fun visitGroupingExpr(expr: Grouping): String =
        parenthesise("Grouping", expr.expression)

    override fun visitLiteralExpr(expr: Literal): String =
        if (expr.value == null) "nil" else expr.value.toString()

    override fun visitUnaryExpr(expr: Unary): String =
        parenthesise(expr.operator.lexeme, expr.right)

    private fun parenthesise(name: String, vararg expressions: Expr): String {
        val builder = StringBuilder()

        builder.append("($name")
        for (expr in expressions) {
            builder.append(" ${expr.accept(this)}")
        }
        builder.append(")")

        return builder.toString()
    }
}
