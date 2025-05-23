<p align="center">
    <img width="256" alt="Logo" src=".github/images/logo-v2.png" />
</p>

<p align="center">
  <a href="#">
    <img alt="badges" src="./.github/images/badges.svg" />
  </a>
</p>

#
<h1 align="center">⚝ U4J: ULID Java Library ⚝</h1>

**U4J** _(ULIDs for Java)_ is a lightweight utility library for generating and managing ULIDs (Universally Unique
Lexicographically Sortable Identifiers) in Java. It provides convenient static methods for creating ULIDs, either based
on the current timestamp, a custom timestamp, or existing ULID strings.

## 📑 Table of Contents
- [Features](#Features)
- [Getting Started](#Getting-Started)
  - [Installation](#Installation)
- [Usage](#Usage)
- [API Reference](#API-Reference)
- [License](#license)
- [Contributing](#contributing)

## 🛠️ Features

- Generate ULIDs using the current timestamp.
- Create ULIDs from specific timestamps (in milliseconds since the Unix epoch).
- Support for creating ULIDs from existing strings.
- Immutable, lightweight, and thread-safe design.

## 🏁 Getting Started

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

```Kotlin
implementation 'io.github.marvuchko:u4j:1.0.0'
```

## ⚙️ Usage

The main class for working with ULIDs is U4J, which provides simple static methods to create or manage ULIDs. Below are
examples of how to use the library.

```Java

import static io.github.marvuchko.u4j.U4J.ulid;

public class Main {
  public static void main(String[] args) {

    // ULID generated based on the current timestamp.
    var ulid = ulid();

    System.out.println(ulid);
    // 01J867H5SF1P2H7S846Q2K1R1M

  }
}

```

**Creating ULID from a Specific Timestamp**

You can create a ULID based on a specific timestamp:

```Java

// Any UTC timestamp in milliseconds
long timestamp = Instant.now().toEpochMilli();

ULID ulid = ulid(timestamp);

System.out.printf("ULID from timestamp: %s%n", ulid);
// ULID from timestamp: 01J8THQNJ9VHR3S2QHP2DRRTKS

```

**Creating ULID from an Existing String**

```Java

// Example ULID string
String ulidString = "01J8THQNJ9VHR3S2QHP2DRRTKS";

ULID ulid = ulid(ulidString);

System.out.printf("ULID from string: %s%n", ulid);
// ULID from string: 01J8THQNJ9VHR3S2QHP2DRRTKS

```

**Getting Timestamp from an Existing ULID**

```Java

// Example ULID string
String ulidString = "01J8THQNJ9VHR3S2QHP2DRRTKS";

ULID ulid = ulid(ulidString);
Instant timestamp = ulid.getTimestamp();

System.out.printf("Timestamp from existing ulid: %s%n", timestamp);
// Timestamp from existing ulid: 2024-09-27T13:11:09.769Z

```

## 🗎 API Reference

**U4J Class**

The `U4J` class provides the following static methods:

* `ULID ulid()` <br>
  Generates a new ULID based on the current system timestamp.

* `ULID ulid(long timestamp)` <br>
  Generates a new ULID from a specified timestamp (milliseconds since Unix epoch).

* `ULID ulid(String existingULID)` <br>
  Creates a ULID from an existing ULID string.

**ULID Class**

The `ULID` class offers the following methods:

* `String getValue()` <br>
  Returns the string representation of the ULID.

* `Instant getTimestamp()` <br>
  Extracts the timestamp from the ULID.

## 🗋 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🔨 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to enhance the functionality.

## 🗹 Sections Explained:

1. **Installation**: Provides instructions for adding the library to a project via Maven or Gradle.
2. **Usage**: Shows examples of generating ULIDs using the `U4J` class, including from a timestamp, or
   string.
3. **API Reference**: Documents the public methods available in `U4J` and `ULID` classes.
4. **Contributing**: Encourages contributions and provides contact info for support.