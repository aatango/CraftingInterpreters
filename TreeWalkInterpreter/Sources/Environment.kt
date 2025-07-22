class Environment(private val values: HashMap<String, Any?> = HashMap()) {
    fun assign(name: Token, value: Any?) {
        if (!values.containsKey(name.lexeme))
            throw RuntimeError(name, "Undefined variable '${name.lexeme}'")

        values.put(name.lexeme, value)
    }

    fun define(name: String, value: Any?) {
        values.put(name, value)
    }

    fun get(name: Token): Any? {
        if (values.contains(name.lexeme)) return values.get(name.lexeme)
        throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
    }

}