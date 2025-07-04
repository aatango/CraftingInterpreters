class Token(val type: TokenType, val lexeme: String, val literal: Any?, val line: Int) {
    override fun toString(): String = "$type $lexeme $literal $line"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token) return false

        return type == other.type && lexeme == other.lexeme && literal == other.literal && line == other.line
    }

    override fun hashCode(): Int {
        var result = line
        result = 31 * result + type.hashCode()
        result = 31 * result + lexeme.hashCode()
        result = 31 * result + (literal?.hashCode() ?: 0)
        return result
    }
}