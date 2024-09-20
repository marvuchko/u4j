<p align="center">
    <img width="128" alt="Logo" src=".github/images/logo.png" />
</p>

***
![Licence](https://camo.githubusercontent.com/074d841936b392ebeca682f2069fd1ec1eca8a6a375cf541c6549ee11688cb51/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d6d69742d626c75652e737667)

# U4J: ULID Java Library

**U4J** _(ULIDs for Java)_ is a lightweight utility library for generating and managing ULIDs (Universally Unique
Lexicographically Sortable Identifiers) in Java. It provides convenient static methods for creating ULIDs, either based
on the current timestamp, a custom timestamp, or existing ULID strings/byte arrays.

## Table of Contents
- [Features](#Features)
- [Getting Started](#Getting-Started)
  - [Installation](#Installation)
- [Usage](#Usage)
- [API Reference](#API-Reference)
- [License](#license)
- [Contributing](#contributing)

## Features

- Generate ULIDs using the current timestamp.
- Create ULIDs from specific timestamps (in milliseconds since the Unix epoch).
- Support for creating ULIDs from existing byte arrays or strings.
- Immutable, lightweight, and thread-safe design.
- Full support for serialization and comparison.

## Getting Started

### Installation

Add the following Maven dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>io.github.marvuchko</groupId>
    <artifactId>u4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

For Gradle:

```Groovy
implementation 'io.github.marvuchko:u4j:1.0.0'
```

## Usage

The main class for working with ULIDs is U4J, which provides simple static methods to create or manage ULIDs. Below are
examples of how to use the library.

```Java
import static io.github.marvuchko.U4J.ulid;

public class Main {
    public static void main(String[] args) {
        var ulid = ulid(); // ULID generated based on the current timestamp.
        System.out.println(ulid); // 01J867H5SF1P2H7S846Q2K1R1M
    }
}

```

**Creating a ULID from a Specific Timestamp**

You can create a ULID based on a specific timestamp:

```Java
long timestamp = System.currentTimeMillis(); // or any other timestamp in milliseconds
ULID ulid = ulid(timestamp);
System.out.printf("ULID from timestamp: %s%n",ulid);
```

**Creating a ULID from an Existing String**

```Java
String ulidString = "01F8MECHZX3TBDSZ7XY9GHZ4QJ"; // Example ULID string
ULID ulid = ulid(ulidString);
System.out.printf("ULID from string: %s%n",ulid);
```

**Creating a ULID from an Existing Byte Array**

```Java
byte[] ulidBytes = new byte[]{ ...}; // ULID byte array
ULID ulid = ulid(ulidBytes);
System.out.printf("ULID from bytes: %s%n",ulid);
```

## API Reference

**U4J Class**

The `U4J` class provides the following static methods:

* `ULID ulid()` <br>
  Generates a new ULID based on the current system timestamp.

* `ULID ulid(long timestamp)` <br>
  Generates a new ULID from a specified timestamp (milliseconds since Unix epoch).

* `ULID ulid(String existingULID)` <br>
  Creates a ULID from an existing ULID string.

* `ULID ulid(byte[] existingULID)` <br>
  Creates a ULID from an existing byte array.

**ULID Class**

The `ULID` class offers the following methods:

* `String value()` <br>
  Returns the string representation of the ULID.

* `byte[] getValue()` <br>
  Returns the byte array representation of the ULID.

* `Instant getTimestamp()` <br>
  Extracts the timestamp from the ULID.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to enhance the functionality.

## Sections Explained:

1. **Installation**: Provides instructions for adding the library to a project via Maven or Gradle.
2. **Usage**: Shows examples of generating ULIDs using the `U4J` class, including from a timestamp, byte array, or
   string.
3. **API Reference**: Documents the public methods available in `U4J` and `ULID` classes.
4. **Unit Tests**: Briefly mentions the existence of unit tests and includes a sample test case.
5. **Contributing**: Encourages contributions and provides contact info for support.