import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScannerTest {
    @Test
    fun booleans() {
        assertEquals(
            Scanner("true").scanTokens(),
            listOf(
                Token(TokenType.TRUE, "true", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("false").scanTokens(),
            listOf(
                Token(TokenType.FALSE, "false", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun numbers() {
        assertEquals(
            Scanner("1234").scanTokens(),
            listOf(
                Token(TokenType.NUMBER, "1234", 1234.0, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("12.34").scanTokens(),
            listOf(
                Token(TokenType.NUMBER, "12.34", 12.34, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun strings() {
        assertEquals(
            Scanner("\"I am a string\"").scanTokens(),
            listOf(
                Token(TokenType.STRING, "\"I am a string\"", "I am a string", 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("\"\"").scanTokens(),
            listOf(
                Token(TokenType.STRING, "\"\"", "", 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("\"123\"").scanTokens(),
            listOf(
                Token(TokenType.STRING, "\"123\"", "123", 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun arithmetic() {
        assertEquals(
            Scanner("+").scanTokens(),
            listOf(
                Token(TokenType.PLUS, "+", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("-").scanTokens(),
            listOf(
                Token(TokenType.MINUS, "-", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("*").scanTokens(),
            listOf(
                Token(TokenType.STAR, "*", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("/").scanTokens(),
            listOf(
                Token(TokenType.SLASH, "/", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun comparison() {
        assertEquals(
            Scanner("<").scanTokens(),
            listOf(
                Token(TokenType.LESS, "<", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("<=").scanTokens(),
            listOf(
                Token(TokenType.LESS_EQUAL, "<=", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner(">").scanTokens(),
            listOf(
                Token(TokenType.GREATER, ">", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner(">=").scanTokens(),
            listOf(
                Token(TokenType.GREATER_EQUAL, ">=", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun equality() {
        assertEquals(
            Scanner("==").scanTokens(),
            listOf(
                Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("!=").scanTokens(),
            listOf(
                Token(TokenType.BANG_EQUAL, "!=", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun logicalOperations() {
        assertEquals(
            Scanner("!").scanTokens(),
            listOf(
                Token(TokenType.BANG, "!", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("and").scanTokens(),
            listOf(
                Token(TokenType.AND, "and", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("or").scanTokens(),
            listOf(
                Token(TokenType.OR, "or", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun variables() {
        assertEquals(
            Scanner("var").scanTokens(),
            listOf(
                Token(TokenType.VAR, "var", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun controlFlow() {
        assertEquals(
            Scanner("if").scanTokens(),
            listOf(
                Token(TokenType.IF, "if", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("while").scanTokens(),
            listOf(
                Token(TokenType.WHILE, "while", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("for").scanTokens(),
            listOf(
                Token(TokenType.FOR, "for", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun functions() {
        assertEquals(
            Scanner("fun").scanTokens(),
            listOf(
                Token(TokenType.FUN, "fun", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("return").scanTokens(),
            listOf(
                Token(TokenType.RETURN, "return", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun classes() {
        assertEquals(
            Scanner("class").scanTokens(),
            listOf(
                Token(TokenType.CLASS, "class", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun inheritance() {
        assertEquals(
            Scanner("this").scanTokens(),
            listOf(
                Token(TokenType.THIS, "this", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner("super").scanTokens(),
            listOf(
                Token(TokenType.SUPER, "super", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )
    }

    @Test
    fun statements() {
        assertEquals(
            Scanner("var average = (min + max) / 2;").scanTokens(),
            listOf(
                Token(TokenType.VAR, "var", null, 1),
                Token(TokenType.IDENTIFIER, "average", null, 1),
                Token(TokenType.EQUAL, "=", null, 1),
                Token(TokenType.LEFT_PAREN, "(", null, 1),
                Token(TokenType.IDENTIFIER, "min", null, 1),
                Token(TokenType.PLUS, "+", null, 1),
                Token(TokenType.IDENTIFIER, "max", null, 1),
                Token(TokenType.RIGHT_PAREN, ")", null, 1),
                Token(TokenType.SLASH, "/", null, 1),
                Token(TokenType.NUMBER, "2", 2.0, 1),
                Token(TokenType.SEMICOLON, ";", null, 1),
                Token(TokenType.EOF, "", null, 1)
            )
        )

        assertEquals(
            Scanner(
                "fun greet() {\nprint \"Hello, world!\";\n}"
            ).scanTokens(),
            listOf(
                Token(TokenType.FUN, "fun", null, 1),
                Token(TokenType.IDENTIFIER, "greet", null, 1),
                Token(TokenType.LEFT_PAREN, "(", null, 1),
                Token(TokenType.RIGHT_PAREN, ")", null, 1),
                Token(TokenType.LEFT_BRACE, "{", null, 1),
                Token(TokenType.PRINT, "print", null, 2),
                Token(TokenType.STRING, "\"Hello, world!\"", "Hello, world!", 2),
                Token(TokenType.SEMICOLON, ";", null, 2),
                Token(TokenType.RIGHT_BRACE, "}", null, 3),
                Token(TokenType.EOF, "", null, 3)
            )
        )
    }
}