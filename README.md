# Java2Many
Java to Many Transpiler is a transpiler tool designed to convert Java code into multiple target programming languages. Utilizing the Eclipse Abstract Syntax Tree (AST), this project offers a basic framework for developers aiming to translate Java code to other languages seamlessly. As of now, I am aiming to support the Go programming language, but the long-term idea is to support various popular programming languages, enabling cross-language compatibility.

## Supported Languages
- Go (Basic functionality, including the translation of Java classes to Go structs)


## Requirements
- Java 17
- Scala 3

## Running instructions
To run Java2Many, you may use the following command

```bash
java2scala [--file file] [--projectDir dir] [--language dir] [--target dir]
```

- `file`: The file you want to transpile
- `projectDir`: The directory you want to transpile
- `language`: The target language you want to transpile to.
- `target`: The target directory you want to transpile to

## Contributing
We welcome contributions from the community! Please read our [CONTRIBUTING.md](CONTRIBUTING.md) file for guidelines on how to contribute to this project.

