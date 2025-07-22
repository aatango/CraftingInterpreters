class Token(val type: TokenType, val lexeme: String, val literal: Any?, val line: Int) {
    override fun toString(): String = "$type $lexeme $literal $line"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token) return false

        return type == other.type && lexeme == other.lexeme && literal == other.literal && line == other.line
    }

    override fun hashCode(): Int = line
        .apply { 31 * this + type.hashCode() }
        .apply { 31 * this + lexeme.hashCode() }
        .apply { 31 * this + (literal?.hashCode() ?: 0) }
}