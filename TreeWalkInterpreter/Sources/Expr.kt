interface Expr {
    interface Visitor<T> {
        fun visitBinaryExpr(expr: Binary): T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitLiteralExpr(expr: Literal): T
        fun visitUnaryExpr(expr: Unary): T
    }

    fun <T> accept(visitor: Visitor<T>): T

}

class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr {
    override fun <T> accept(visitor: Expr.Visitor<T>): T = visitor.visitBinaryExpr(this)
}

class Grouping(val expression: Expr) : Expr {
    override fun <T> accept(visitor: Expr.Visitor<T>): T = visitor.visitGroupingExpr(this)
}

class Literal(val value: Any?) : Expr {
    override fun <T> accept(visitor: Expr.Visitor<T>): T = visitor.visitLiteralExpr(this)
}

class Unary(val operator: Token, val right: Expr) : Expr {
    override fun <T> accept(visitor: Expr.Visitor<T>): T = visitor.visitUnaryExpr(this)
}

