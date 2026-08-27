# jLox Interpreter — Technical Guide

This document gives a concise technical overview of the **jLox tree-walk interpreter** from Robert Nystrom's *Crafting Interpreters*.

Its purpose is to explain how the major components fit together and how Lox source code moves from text to execution.

---

## Architecture Overview

jLox processes a program through the following pipeline:

```text
Source Code
    ↓
Scanner
    ↓
Tokens
    ↓
Parser
    ↓
Abstract Syntax Tree
    ↓
Resolver
    ↓
Interpreter
    ↓
Program Output
```

Each stage has a focused responsibility:

| Component | Responsibility |
|---|---|
| `Lox` | Entry point and orchestration |
| `Scanner` | Converts source code into tokens |
| `Parser` | Converts tokens into an AST |
| `Expr` / `Stmt` | Represent expressions and statements |
| `Resolver` | Performs lexical scope analysis |
| `Interpreter` | Executes statements and evaluates expressions |
| `Environment` | Stores variables and models scope |
| `LoxFunction` | Runtime representation of functions |
| `LoxClass` | Runtime representation of classes |
| `LoxInstance` | Runtime representation of objects |

---

## Scanner

The scanner performs **lexical analysis**.

It reads the source code character by character and groups those characters into tokens.

For example:

```lox
var language = "Lox";
```

becomes roughly:

```text
VAR
IDENTIFIER(language)
EQUAL
STRING("Lox")
SEMICOLON
EOF
```

A token stores information such as its type, original text, literal value, and source line.

The scanner separates low-level character processing from parsing, so the parser can work with meaningful language elements instead of raw text.

---

## Parser and AST

The parser uses **recursive descent parsing** to turn tokens into an **Abstract Syntax Tree (AST)**.

For example:

```lox
1 + 2 * 3
```

is represented conceptually as:

```text
       +
      / \
     1   *
        / \
       2   3
```

The tree preserves operator precedence and expression structure.

jLox separates syntax into:

- `Expr` — expressions that produce values
- `Stmt` — statements that perform actions

Examples of expression nodes include `Binary`, `Literal`, `Variable`, and `Call`.

Examples of statement nodes include `Print`, `Var`, `Block`, `Function`, and `Class`.

---

## Visitor Pattern

The AST is traversed using the **Visitor pattern**.

Each AST node implements an `accept()` method, and visitors define what should happen for each node type.

A simplified evaluation flow is:

```text
Interpreter.evaluate(expr)
        ↓
expr.accept(interpreter)
        ↓
Binary.accept(interpreter)
        ↓
interpreter.visitBinaryExpr(...)
```

This allows multiple components to operate on the same AST without putting all behavior directly inside the AST classes.

For example:

- `Interpreter` executes the AST.
- `Resolver` analyzes the AST.

The generic visitor return type also allows different visitors to return different values.

---

## Interpreter

The `Interpreter` gives the AST runtime meaning.

A useful distinction is:

```text
Expression → evaluate
Statement  → execute
```

For a binary expression:

```lox
5 * 2
```

the interpreter:

1. Evaluates the left operand.
2. Evaluates the right operand.
3. Applies the operator.
4. Returns the result.

Lox is dynamically typed, so runtime values are represented using Java objects such as:

- `Double`
- `String`
- `Boolean`
- `null`
- `LoxFunction`
- `LoxClass`
- `LoxInstance`

---

## Environments and Scope

Variables are stored in `Environment` objects.

An environment contains mappings such as:

```text
name → "Lox"
count → 10
```

Nested scopes are represented by chaining environments together:

```text
Local Environment
        ↓
Enclosing Environment
        ↓
Global Environment
```

If a variable is not found in the current scope, lookup continues through the enclosing environments.

This structure models **lexical scope**.

---

## Functions and Closures

A function has two important representations:

- `Stmt.Function` — the parsed syntax
- `LoxFunction` — the runtime callable object

When a function is called, jLox creates a new environment, binds its parameters, and executes its body.

Functions also capture the environment where they were declared.

That captured environment creates a **closure**.

Example:

```lox
fun makeCounter() {
    var count = 0;

    fun counter() {
        count = count + 1;
        print count;
    }

    return counter;
}
```

Even after `makeCounter()` finishes, `counter()` still has access to `count` because the function retains its declaration environment.

---

## Classes and Instances

Classes are represented at runtime by `LoxClass`.

Objects created from classes are represented by `LoxInstance`.

A class stores methods, while an instance stores its own fields.

```text
LoxClass
   ↓
methods

LoxInstance
   ↓
fields
```

When accessing a property, jLox first checks instance fields and then searches for a method on the class.

Methods are bound to the current instance so that `this` refers to the correct object.

Inheritance works by allowing a class to reference a superclass and continue method lookup there when needed.

`super` provides explicit access to superclass methods.

---

## Resolver

Before execution, the `Resolver` performs a separate semantic-analysis pass.

Its main job is to determine which variable declaration each variable expression refers to.

It tracks nested scopes and records the lexical distance between a variable use and its declaration.

Conceptually:

```text
Current Scope
    ↓
Parent Scope
    ↓
Variable Declaration
```

The interpreter can then retrieve the correct variable directly instead of dynamically searching every environment.

The resolver also detects certain invalid uses of variables, `this`, `super`, and `return` before runtime.

---

## End-to-End Example

For:

```lox
var x = 10;

fun double(n) {
    return n * 2;
}

print double(x);
```

the interpreter processes the code as follows:

```text
Source Code
    ↓
Scanner creates tokens
    ↓
Parser builds AST
    ↓
Resolver determines lexical bindings
    ↓
Interpreter defines x
    ↓
Interpreter creates LoxFunction for double
    ↓
double(x) creates a call environment
    ↓
n is bound to 10
    ↓
n * 2 evaluates to 20
    ↓
print outputs 20
```

Output:

```text
20
```

---

## Design Summary

The key idea behind jLox is **separation of responsibilities**.

- The **scanner** understands characters.
- The **parser** understands grammar.
- The **AST** represents program structure.
- The **resolver** understands lexical relationships.
- The **environment** stores runtime bindings.
- The **interpreter** executes the program.
- Runtime classes represent functions, classes, and instances.

The interpreter becomes easier to understand when viewed as a sequence of transformations:

```text
Text
 ↓
Tokens
 ↓
Syntax Tree
 ↓
Resolved Program
 ↓
Runtime Execution
```

Each stage makes the program more structured and easier for the next stage to process.

---

## Reference

This implementation follows the **jLox interpreter** from Robert Nystrom's [*Crafting Interpreters*](https://craftinginterpreters.com/).

The Lox language, interpreter architecture, and implementation are based on the material presented in the book.