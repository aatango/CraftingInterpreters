private class ParseError : RuntimeException()

class Parser(private val tokens: List<Token>, private var current: Int = 0) {
    fun parse(): Expr? = try {
        expression()
    } catch (_: ParseError) {
        null
    }

    private fun expression(): Expr = equality()

    private fun equality(): Expr =
        binaryOperation(::comparison, TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)

    private fun comparison(): Expr = binaryOperation(
        ::term,
        TokenType.GREATER,
        TokenType.GREATER_EQUAL,
        TokenType.LESS,
        TokenType.LESS_EQUAL
    )

    private fun term(): Expr = binaryOperation(::factor, TokenType.MINUS, TokenType.PLUS)
    private fun factor(): Expr = binaryOperation(::unary, TokenType.SLASH, TokenType.STAR)

    private fun unary(): Expr {
        if (matchTokens(TokenType.BANG, TokenType.MINUS)) {
            val operator: Token = previous()
            val right: Expr = unary()
            return Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        return when {
            matchTokens(TokenType.FALSE) -> Literal(false)
            matchTokens(TokenType.TRUE) -> Literal(true)
            matchTokens(TokenType.NIL) -> Literal(null)
            matchTokens(TokenType.NUMBER, TokenType.STRING) -> Literal(previous().literal)
            matchTokens(TokenType.LEFT_PAREN) -> {
                val expr: Expr = expression()
                consumeToken(TokenType.RIGHT_PAREN, "Expect ')' after expression")

                return Grouping(expr)
            }

            else -> throw tokenError(peek(), "Expect expression!")
        }
    }

    private fun binaryOperation(
        higherPrecedenceOperation: () -> Expr,
        vararg matchingTokens: TokenType
    ): Expr {
        var expr: Expr = higherPrecedenceOperation()

        while (matchTokens(*matchingTokens)) {
            val operator: Token = previous()
            val right: Expr = higherPrecedenceOperation()
            expr = Binary(expr, operator, right)
        }

        return expr
    }

    private fun matchTokens(vararg types: TokenType): Boolean {
        for (type in types) {
            if (isToken(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++

        return previous()
    }

    private fun isToken(type: TokenType): Boolean = if (isAtEnd()) false else peek().type == type
    private fun isAtEnd(): Boolean = peek().type == TokenType.EOF
    private fun peek(): Token = tokens[current]
    private fun previous(): Token = tokens[current - 1]

    private fun consumeToken(type: TokenType, message: String): Token {
        if (isToken(type)) return advance()

        throw tokenError(peek(), message)
    }

    private fun tokenError(token: Token, message: String): ParseError {
        error(token, message)
        return ParseError()
    }
}