private class ParseError : RuntimeException()

class Parser(private val tokens: List<Token>, private var current: Int = 0) {
    fun parse(): List<Stmt> {
        val statements: ArrayList<Stmt> = ArrayList()

        while (!isAtEnd()) {
            declaration()?.let { statements.add(it) }
        }

        return statements
    }

    private fun expression(): Expr = assignment()

    private fun declaration(): Stmt? = try {
        if (matchTokens(TokenType.VAR)) varDeclaration() else statement()
    } catch (_: ParseError) {
        synchronise()
        null
    }

    private fun statement(): Stmt =
        if (matchTokens(TokenType.PRINT)) printStatement() else expressionStatement()

    private fun expressionStatement(): Stmt {
        val expr: Expr = expression()
        consumeToken(TokenType.SEMICOLON, "Expect ';' after expression")
        return Expression(expr)
    }

    private fun assignment(): Expr {
        val expr: Expr = equality()

        if (matchTokens(TokenType.EQUAL)) {
            val equals: Token = previous()
            val value: Expr = assignment()

            if (expr is Variable) return Assign(expr.name, value)

            error(equals, "Invalid assignment target")
        }

        return expr
    }

    private fun printStatement(): Stmt {
        val value: Expr = expression()
        consumeToken(TokenType.SEMICOLON, "Expect ';' after value")
        return Print(value)
    }

    private fun varDeclaration(): Stmt {
        val name: Token = consumeToken(TokenType.IDENTIFIER, "Expect variable name")
        val initializer: Expr? = if (matchTokens(TokenType.EQUAL)) expression() else null

        consumeToken(TokenType.SEMICOLON, "Expect ';' after variable declaration")

        return Var(name, initializer)
    }

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
            matchTokens(TokenType.IDENTIFIER) -> Variable(previous())
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

    private fun synchronise() {
        advance()

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return

            when (peek().type) {
                TokenType.CLASS,
                TokenType.FUN,
                TokenType.VAR,
                TokenType.FOR,
                TokenType.IF,
                TokenType.WHILE,
                TokenType.PRINT,
                TokenType.RETURN -> return

                else -> {}
            }

            advance()
        }
    }
}