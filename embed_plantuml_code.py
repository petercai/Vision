import argparse
import base64
from pathlib import Path
import zlib


PLANTUML_ALPHABET = b"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
BASE64_ALPHABET = b"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
B64_TO_PLANTUML = bytes.maketrans(BASE64_ALPHABET, PLANTUML_ALPHABET)
DEFAULT_LINE_COUNT = 30
MARKETING_NOTE = (
    '<div align="center">\n\n'
    '<sub><em>Optimized for viewing with '
    '<a href="https://github.com/petercai/vscode-umlmark">vscode-umlmark</a>'
    "</em></sub>\n\n"
    "</div>"
)


def plantuml_encode(puml_text: str) -> str:
    zlibbed = zlib.compress(puml_text.encode("utf-8"))[2:-4]
    return base64.b64encode(zlibbed).translate(B64_TO_PLANTUML).decode("ascii")


def build_diagram_url(puml_text: str) -> str:
    encoded = plantuml_encode(puml_text)
    return f"https://www.plantuml.com/plantuml/svg/{encoded}"


def build_source_block(lines: list[str], show_all: bool, max_lines: int) -> str:
    if show_all or len(lines) <= max_lines:
        visible_lines = lines
        heading = "## PlantUML Source"
        note = ""
    else:
        visible_lines = lines[:max_lines]
        heading = f"## PlantUML Source (First {max_lines} Lines)"
        note = (
            f"\n_Trimmed from {len(lines)} total lines. "
            "Re-run with `--full` to embed the complete file._\n"
        )

    source = "\n".join(visible_lines)
    return f"{heading}{note}\n```plantuml\n{source}\n```\n"


def build_markdown(puml_path: Path, puml_text: str, show_all: bool, max_lines: int) -> str:
    lines = puml_text.splitlines()
    diagram_url = build_diagram_url(puml_text)
    source_block = build_source_block(lines, show_all=show_all, max_lines=max_lines)

    return (
        f"# {puml_path.stem}\n\n"
        "## Diagram\n\n"
        f"![{puml_path.stem}]({diagram_url})\n\n"
        f"{MARKETING_NOTE}\n\n"
        f"{source_block}"
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate a Markdown file with a PlantUML diagram and source snippet."
    )
    parser.add_argument("path", type=Path, help="Path to the .puml file to embed.")
    parser.add_argument(
        "--lines",
        type=int,
        default=DEFAULT_LINE_COUNT,
        help=f"Number of source lines to include when not using --full. Default: {DEFAULT_LINE_COUNT}.",
    )
    parser.add_argument(
        "--full",
        action="store_true",
        help="Embed the full PlantUML source instead of only the first N lines.",
    )
    return parser.parse_args()


def validate_args(args: argparse.Namespace) -> None:
    if not args.full and args.lines <= 0:
        raise ValueError("--lines must be greater than 0 unless --full is used.")


def main() -> None:
    args = parse_args()
    validate_args(args)

    puml_path = args.path.expanduser().resolve()
    puml_text = puml_path.read_text(encoding="utf-8")
    markdown_path = puml_path.with_suffix(".md")
    markdown = build_markdown(
        puml_path,
        puml_text,
        show_all=args.full,
        max_lines=args.lines,
    )
    markdown_path.write_text(markdown, encoding="utf-8")
    print(markdown_path)


if __name__ == "__main__":
    main()
