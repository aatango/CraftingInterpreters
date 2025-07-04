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

private fun runFile(path: String) {
    run(File(path).readText(Charsets.UTF_8))

    if (hadError) exitProcess(65)
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
    val scanner = Scanner(source)
    val tokens: List<Token> = scanner.scanTokens()

    for (token in tokens) {
        println(token)
    }
}

fun error(line: Int, message: String) {
    report(line = line, where = "", message = message)
}

private fun report(line: Int, where: String, message: String) {
    System.err.println("[line $line] Error: $where: $message")
    hadError = true
}