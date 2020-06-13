# Password Protector
[中文指南](https://github.com/m-yuhas/password_protector/blob/master/doc/读我档案.md)

[Documentación en español](https://github.com/m-yuhas/password_protector/blob/master/doc/LÉAME.md)

[Documentation en français](https://github.com/m-yuhas/password_protector/blob/master/doc/LISEZ-MOI.md)

## Introduction
This program lets you store passwords, pin numbers, and answers to security
questions for various accounts.  To access the data, two unique passwords are
required so that if you are incapacitated, two trusted persons must be present
to unlock the accounts.  A utility to generate passwords is also provided, as
well as the ability to encrypt and decrypt files with a single password.

## Build

This program is written in Java and uses a Gradle build script.  It is highly
recommended to build and run with the latest version of the JRE.  Only Java 8
and above are supported.

### Mac OS

1. First install [Homebrew](https://brew.sh)
2. Next install Java (you can also download and install directly from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)):

```
brew cask install java
```

3. Install Gradle:

```
brew install gradle
```

4. Clone this repository:

```
git clone https://github.com/m-yuhas/password_protector.git
```

5. Change working directory to the root of this repository:

```
cd password_protector
```

#### Building the GUI

Run the following commands:

```
gradle clean
gradle jarGui
```

A file named *PasswordProtector.jar* should appear in the ```dist/``` directory

#### Building the CLI

Run the following commands:

```
gradle clean
gradle jarCli
```

A file named *PasswordProtectorCli.jar* should appear in the ```dist/``` directory

### Windows

### Linux (Debian)

### Linux (Red Hat)

## Usage

### POSIX

#### GUI

To launch, double click the PasswordProtector.jar icon or launch from the
command line:

```
java -jar PasswordProtectorCli.jar
```

#### CLI

To view the help menu, run:

```
$ java -jar PasswordProtectorCli.jar -h
usage: java -jar PasswordProtectorCli.jar
  -e,--encrypt                  Encrypt a file.
  -d,--decrypt                  Decrypt a file.
  -p,--password_protector <arg> Open a password storage file or create a new one.
  -i,--input_file <arg>         Input file for encrypt and decrypt modes.
  -o,--output_file <arg>        Output file for encrypt and decrypt modes.
```

To open and edit a file containing passwords run:

```
$ java -jar PasswordProtectorCli.jar -p <file name>
```

To encrypt a file:

```
$ java -jar PasswordProtectorCli.jar -e -i <unencrypted file> -o <output file>
```

To decrypt a file:

```
$ java -jar PasswordProtectorCli.jar -d -i <encrypted file> -o <output file>
```

### Windows

#### GUI

#### CLI

## Future Tasks

- TODO: Include Gradle tasks to package the GUI jar file as a Bundle for
    MacOS and as a Windows Installer for Microsoft Windows.
