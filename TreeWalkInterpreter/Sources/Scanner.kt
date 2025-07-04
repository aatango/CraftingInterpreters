class Scanner(private val source: String, private val tokens: ArrayList<Token> = ArrayList()) {
    private var start: Int = 0
    private var current: Int = 0
    private var line: Int = 1

    private val keywords: Map<String, TokenType> = mapOf(
        "and" to TokenType.AND,
        "class" to TokenType.CLASS,
        "else" to TokenType.ELSE,
        "false" to TokenType.FALSE,
        "for" to TokenType.FOR,
        "fun" to TokenType.FUN,
        "if" to TokenType.IF,
        "nil" to TokenType.NIL,
        "or" to TokenType.OR,
        "print" to TokenType.PRINT,
        "return" to TokenType.RETURN,
        "super" to TokenType.SUPER,
        "this" to TokenType.THIS,
        "true" to TokenType.TRUE,
        "var" to TokenType.VAR,
        "while" to TokenType.WHILE
    )

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun isAtEnd(): Boolean = current >= source.length

    private fun advance() = source[current++]

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text: String = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun scanToken() {
        when (val c: Char = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '/' -> if (match('/')) {
                while (lookAhead() != '\n' && !isAtEnd()) advance()
            } else {
                addToken(
                    TokenType.SLASH
                )
            }
            ' ', '\r', '\t' -> {}
            '\n' -> line++
            '"' -> string()
            else -> if (isDigit(c)) {
                number()
            } else if (isAlpha(c)) {
                identifier()
            } else {
                error(line, "Unexpected character!")
            }
        }
    }

    private fun identifier() {
        while (isAlphaNumeric(lookAhead())) advance()

        var type: TokenType? = keywords[source.substring(start, current)]
        if (type == null) type = TokenType.IDENTIFIER
        addToken(type)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd() || source[current] != expected) return false

        current++
        return true
    }

    private fun lookAhead(n: Int = 1): Char =
        if (current + (n - 1) >= source.length) '\u0000' else source[current + (n - 1)]

    private fun isAlpha(c: Char): Boolean =
        (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'

    private fun isAlphaNumeric(c: Char): Boolean = isAlpha(c) || isDigit(c)

    private fun isDigit(c: Char): Boolean = c >= '0' && c <= '9'

    private fun number() {
        while (isDigit(lookAhead())) advance()

        if (lookAhead() == '.' && isDigit(lookAhead(n = 2))) {
            advance()

            while (isDigit(lookAhead())) advance()
        }

        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun string() {
        while (lookAhead() != '"' && !isAtEnd()) {
            if (lookAhead() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            error(line, "Unterminated string!")
            return
        }

        advance()

        val value: String = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }
}