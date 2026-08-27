# jLox Interpreter

A tree-walk interpreter for the **Lox programming language**, implemented in Java while working through Robert Nystrom's [*Crafting Interpreters*](https://craftinginterpreters.com/).

This project follows the **jLox implementation from the book** and was built as a hands-on project to strengthen my programming skills while developing a deeper understanding of interpreters and programming language implementation.

## Overview

Lox is a small, dynamically typed programming language created for *Crafting Interpreters*. Despite its simplicity, it includes many features found in real-world programming languages, including lexical scoping, functions, closures, classes, and inheritance.

jLox processes source code through several stages:

```text
Source Code
    ↓
Scanner
    ↓
Tokens
    ↓
Parser
    ↓
Abstract Syntax Tree (AST)
    ↓
Resolver
    ↓
Interpreter
    ↓
Program Output
```

## Features

The interpreter supports:

- Numbers, strings, Booleans, and `nil`
- Arithmetic, comparison, and logical operators
- Variables and assignment
- Lexical and block scope
- `if` / `else` statements
- `while` and `for` loops
- Functions and return values
- First-class functions
- Closures
- Native functions
- Classes and instances
- Fields and methods
- Constructors
- `this`
- Inheritance and method overriding
- `super`
- Static scope resolution
- Syntax and runtime error handling

## Example

```lox
class Animal {
  speak() {
    print "Animal sound";
  }
}

class Dog < Animal {
  speak() {
    super.speak();
    print "Woof!";
  }
}

var dog = Dog();
dog.speak();
```

Output:

```text
Animal sound
Woof!
```

## Running jLox

### Requirements

- Java Development Kit (JDK)

### Interactive Mode

Run the interpreter without a script to start the REPL:

```bash
java com.craftinginterpreters.lox.Lox
```

Then enter Lox code interactively:

```text
> print "Hello, Lox!";
Hello, Lox!
```

### Running a Lox Script

Pass a `.lox` file to the interpreter:

```bash
java com.craftinginterpreters.lox.Lox program.lox
```

For example:

```lox
fun fibonacci(n) {
  if (n <= 1) return n;
  return fibonacci(n - 2) + fibonacci(n - 1);
}

for (var i = 0; i < 10; i = i + 1) {
  print fibonacci(i);
}
```

## What I Learned

The primary goal of this project was to improve my programming skills by working through a complete, non-trivial software system.

Building jLox provided practical experience with:

- Recursive descent parsing
- Abstract Syntax Trees
- Recursive algorithms and tree traversal
- The Visitor design pattern
- Java interfaces, inheritance, and generics
- Lexical scope and environments
- Functions and closures
- Runtime representation of classes and objects
- Error handling
- Separating syntax analysis, semantic analysis, and execution

More importantly, the project gave me a much clearer mental model of what happens between writing source code and actually executing a program.

## Documentation

For a deeper technical walkthrough of the implementation, please refer to the following document:
**[jLox Technical Guide →](docs/JLOX_GUIDE.md)**

## Acknowledgments

This project follows the **jLox interpreter presented** in Robert Nystrom's [*Crafting Interpreters*](https://craftinginterpreters.com/).

The Lox language, interpreter architecture, and implementation are based on the material from the book. This repository is an educational implementation documenting my work through the jLox portion of *Crafting Interpreters*.
