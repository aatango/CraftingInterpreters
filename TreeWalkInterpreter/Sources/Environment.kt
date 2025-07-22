class Environment(
    val enclosing: Environment? = null,
    private val values: HashMap<String, Any?> = HashMap()
) {
    fun assign(name: Token, value: Any?) {
        when {
            values.containsKey(name.lexeme) -> values.put(name.lexeme, value)
            enclosing != null -> enclosing.assign(name, value)
            else -> throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
        }
    }

    fun define(name: String, value: Any?) {
        values.put(name, value)
    }

    fun get(name: Token): Any? = when {
        values.contains(name.lexeme) -> values.get(name.lexeme)
        enclosing != null -> enclosing.get(name)
        else -> throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
    }
}