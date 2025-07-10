import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AstPrinterTests {
    @Test
    fun notVeryPrettyPrinter() {
        val expression: Expr = Binary(
            Unary(
                Token(TokenType.MINUS, "-", null, 1),
                Literal(123)
            ),
            Token(TokenType.STAR, "*", null, 1),
            Grouping(Literal(45.67))
        )

        assertEquals(printAst(expression), "(* (- 123) (Grouping 45.67))")
    }
}