import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: kLox with script")
        exitProcess(64)
    } else if (args.size == 1) {
        runFile(args[0])
    } else {
        runPrompt()
    }
}

var hadError: Boolean = false
var hadRuntimeError: Boolean = false

private fun runFile(path: String) {
    run(File(path).readText(Charsets.UTF_8))

    if (hadError) exitProcess(65)
    if (hadRuntimeError) exitProcess(70)
}

private fun runPrompt() {
    while (true) {
        print("> ")
        val line: String = readln()
        if (line.isEmpty()) break
        run(line)
        hadError = false
    }
}

private fun run(source: String) {
    val tokens = Scanner(source).scanTokens()
    val statements: List<Stmt> = Parser(tokens).parse()

    if (hadError) return

    interpret(statements)
}

fun error(line: Int, message: String) = report(line = line, where = "", message = message)
fun error(token: Token, message: String) =
    if (token.type == TokenType.EOF) report(token.line, " at the end", message)
    else report(token.line, "at '${token.lexeme}'", message)

fun runtimeError(err: RuntimeError) {
    println(err.message)
    println("[line ${err.token.line}]")
    hadRuntimeError = true
}

private fun report(line: Int, where: String, message: String) {
    System.err.println("[line $line] Error: $where: $message")
    hadError = true
}