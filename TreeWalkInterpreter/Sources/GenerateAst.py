import argparse
import pathlib


def parse_output_directory() -> pathlib.Path:
    parser = argparse.ArgumentParser(prog="GenerateAST",
                                     description="Generate Abstract Syntax Tree (AST) for kLox")

    parser.add_argument("outputDirectory", type=str, help="Output directory for the generated AST")

    return parser.parse_args().outputDirectory


def define_type(name: str, class_name: str, fields: str) -> str:
    content: str = f"class {class_name}("

    for field in fields.split(", "):
        field_type, field_name = field.split()
        content += f"val {field_name}: {field_type}, "

    content = content[:-2]  # Remove unnecessary trailing comma

    content += f") : {name} {{\n"

    content += (f"    override fun <T> accept(visitor: Expr.Visitor<T>): T"
                f" = visitor.visit{class_name}{name}(this)\n")

    content += "}\n\n"

    return content


def define_ast(name: str, types: list[str]) -> str:
    content: str = f"interface {name} {{\n"

    content += define_visitor(name, types)

    content += "    fun <T> accept(visitor: Visitor<T>): T\n\n"

    content += "}\n\n"

    for t in types:
        type_name, fields = t.split(":")
        content += define_type(name, type_name.rstrip(), fields)

    return content


def define_visitor(base: str, types: list[str]) -> str:
    content = "    interface Visitor<T> {\n"

    for t in types:
        type_name, _ = t.split(":")
        content += f"        fun visit{type_name.rstrip()}{base}({base.lower()}: {type_name.rstrip()}): T\n"

    content += "    }\n\n"

    return content


if __name__ == "__main__":
    expression_types: list[str] = [
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Any? value",
        "Unary    : Token operator, Expr right"]

    base_name: str = "Expr"
    file_content: str = define_ast(base_name, expression_types)

    pathlib.Path(parse_output_directory(), f"{base_name}.kt").write_text(file_content)
