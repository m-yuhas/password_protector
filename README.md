# Password Protector
[中文指南](https://github.com/m-yuhas/password_protector/blob/master/doc/读我档案.md)

[Documentación en español](https://github.com/m-yuhas/password_protector/blob/master/doc/LÉAME.md)

[Documentation en français](https://github.com/m-yuhas/password_protector/blob/master/doc/LISEZ-MOI.md)

## Introduction
This program lets you store passwords, PINs, and answers to security
questions for various accounts.  To access the data, two unique passwords are
required so that if you are incapacitated, two trusted persons must be present
to unlock the accounts.  A utility to generate passwords is also provided, as
well as the ability to encrypt and decrypt files with a single password.

The underlying encryption mechanism is the Blowfish cypher with a 128 bit key.
Each time data is encrypted a new salt and initialization vector are randomly
generated.  The salt is the same length as the key and the initialization vector
is 64 bits.  The key is derived from the password using PBKDF2 with HMAC and the
SHA-1 hashing function.

## Setup the Build Environment
This program is written in Java and uses a Gradle build script.  It is highly
recommended to build and run with the JDK version 14 or greater.  Earlier
versions of Java have not been tested.  To build, Gradle 6 or greater is
required.

### Mac OS
* First install [Homebrew](https://brew.sh)
* Next install Java (you can also download and install directly from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)):

```
brew cask install java
```

* Install Gradle:

```
brew install gradle
```

### Windows
* First install the latest version of the JDK following the instructions on
    [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html)
* Next install the latest version of Gradle following the instructions on
    [Gradle's website](https://gradle.org/install/)

### Linux (Debian)
* First install the latest JDK:

```
sudo apt install default-jdk
```

* Install Gradle.  You will have to add a repository to apt to get the latest version:

```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt install gradle
```

### Linux (Red Hat)
When this document was written, Java 14 and Gradle 6 were not included in any of
the yum or dnf repositories.  This makes setting up the build environment a
little more complicated.

* First download the latest JDK (Check the URL before running on your own
    system, this command assumes you are running on an x64 system):

```
$ curl -O https://download.java.net/java/GA/jdk14/076bab302c7b4508975440c56f6cc26a/36/GPL/openjdk-14_linux-x64_bin.tar.gz
```

* Unzip the file:

```
$ tar xvf openjdk-14_linux-x64_bin.tar.gz
```

* Copy the folder containing the binaries to /opt/:

```
$ sudo mv jdk-14 /opt/
```

* Add a script in your profile.d folder to add Java to the path on every login:

```
$ sudo tee /etc/profile.d/jdk14.sh <<EOF
> export JAVA_HOME=/opt/jdk-14
> export PATH=\$PATH:\$JAVA_HOME/bin
> EOF
```

* Run the script we just created so that the Java path is added to the current
    Bash session:

```
$ source /etc/profile.d/jdk14.sh
```

* Get the the latest version of Gradle:

```
$ wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
```

* Unzip it:

```
$ unzip gradle-6.5-bin.zip
```

* Move the binaries to /usr/local/gradle:

```
$ sudo mv gradle-6.5 /usr/local/gradle
```

* Create a script in profile.d to add Gradle to the path on every login:

```
$ sudo echo "export PATH=/usr/local/gradle/bin:$PATH" >> /etc/profile.d/gradle.sh
```

* Run the script we just created to apply it this Bash session:

```
$ sudo source /etc/profile.d/gradle.sh
```

* Clean up the temporary files:

```
$ rm openjdk-14_linux-x64_bin.tar.gz
$ rm gradle-6.5-bin.zip
```

## Build
* Clone this repository:

```
git clone https://github.com/m-yuhas/password_protector.git
```

* Change working directory to the root of this repository:

```
cd password_protector
```

### Building the GUI
Run the following commands:

```
gradle clean
gradle jarGui
```

A file named *PasswordProtector.jar* should appear in the ```dist/``` directory

### Building the CLI
Run the following commands:

```
gradle clean
gradle jarCli
```

A file named *PasswordProtectorCli.jar* should appear in the ```dist/``` directory

## Run
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

## Future Tasks
- TODO: Include Gradle tasks to package the GUI jar file as a Bundle for
    MacOS and as a Windows Installer for Microsoft Windows.

