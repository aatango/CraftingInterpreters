import argparse
import pathlib


def parse_output_directory() -> pathlib.Path:
    parser = argparse.ArgumentParser(prog="GenerateAST",
                                     description="Generate Abstract Syntax Tree (AST) for kLox")

    parser.add_argument("outputDirectory", type=str, help="Output directory for the generated AST")

    return parser.parse_args().outputDirectory


def define_type(class_name: str, fields: str) -> str:
    content: str = f"data class {class_name}("

    for field in fields.split(", "):
        field_type, field_name = field.split()
        content += f"val {field_name}: {field_type}, "

    content = content[:-2]  # Remove unnecessary trailing comma

    content += ") : Expr\n"

    return content


def define_ast(name: str, types: list[str]) -> str:
    content: str = f"sealed interface {name}\n\n"

    for t in types:
        type_name, fields = t.split(":")
        content += define_type(type_name.rstrip(), fields)

    return content


if __name__ == "__main__":
    expr_content: str = define_ast("Expr", [
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Any? value",
        "Unary    : Token operator, Expr right"])
    pathlib.Path("Expr.kt").write_text(expr_content)
