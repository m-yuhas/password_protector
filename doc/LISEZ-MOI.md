# Protecteur des Mots de Passe
## Introduction
Ce programme vous permet d'entreposer des mots de passe, PINs, et réponses à
questions de sécurité pour comptes variés.  Pour accéder les données, deux mots
de passe sont requis alors si vous êtes rendu incapable, deux personnes à qui
vous faites confiance doivent être présent pour ouvrir les comptes. Une
utilitaire pour générer mots de passes est aussi inclus, aussi bien que la
capacité de crypter et décrypter fichiers avec un seul mot de passe.

Le mécanisme de chiffrement sous-jacent est le chiffre de Blowfish avec une clé
de 128 bits.  Chaque fois que les données sont cryptées un nouveau sel et
nouveau vecteur d'initialisation sont généré au hasard.  Le sel est la même
longueur de la clé et le vecteur d'initialisation est 64 bits.  La clé est
dérivée du mot de passe en utilisant PBKDF2 avec HMAC et la fonction de hachage
SHA-1.

## Configurer L'Environnement de Compilation
Ce programme était écrit en Java et utilise un script de compilation de Gradle.
Il est fortement recommandé à compiler et exécuter avec JDK version 14 ou plus
grand.  Versions plus tôt de Java n'ont pas été testées.  Pour compiler, Gradle
6 ou plus grand est requis.

### Mac OS
* D'abord installez [Homebrew](https://brew.sh)
* Prochain installez Java (vous pouvez aussi télécharger et installer directement de [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)):

```
brew cask install java
```

* Installez Gradle:

```
brew install gradle
```

### Windows
* D'abord installez la version plus dernière du JDK en suivant les instructions
    dans [le site Web d'Oracle](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html)
* Prochain installez la version plus dernière de Gradle en suivant les
    insctructions dans [le site Web de Gradle](https://gradle.org/install/)

### Linux (Debian)
* D'abord installez la version plus dernière du JDK:

```
sudo apt install default-jdk
```

* Installez Gradle.  Vous devez ajouter un dépôt à apt pour obtenir la version
    plus dernière:

```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt install gradle
```

### Linux (Red Hat)
Quand ce document était écrit, Java 14 et Gradle 6 n'étaient pas inclus dans
aucun dépôt de yum ou dnf.  Cela fait configurant l'environnement de compilation
un petit peu plus compliqué.

* D'abord télécharger la version plus dernière du JDK (Vérifiez l'URL avant de
    l'exécutez en votre système, cette commande suppose votre système est x64):

```
$ curl -O https://download.java.net/java/GA/jdk14/076bab302c7b4508975440c56f6cc26a/36/GPL/openjdk-14_linux-x64_bin.tar.gz
```

* Décompressez le fichier:

```
$ tar xvf openjdk-14_linux-x64_bin.tar.gz
```

* Copiez le répertoire que contient les fichiers binaires à /opt/:

```
$ sudo mv jdk-14 /opt/
```

* Ajoutez un script à votre répertoire de profile.d pour ajouter Java au PATH en
    chaque login:

```
$ sudo tee /etc/profile.d/jdk14.sh <<EOF
> export JAVA_HOME=/opt/jdk-14
> export PATH=\$PATH:\$JAVA_HOME/bin
> EOF
```

* Exécutez le script que nous venons de créer afin que le chemin d'accès de Java
    est ajouté à la session de Bash actuelle:

```
$ source /etc/profile.d/jdk14.sh
```

* Obtenez la version plus dernière de Gradle:

```
$ wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
```

* Décompressez-le:

```
$ unzip gradle-6.5-bin.zip
```

* Déplacez les fichiers binaires à /usr/local/gradle:

```
$ sudo mv gradle-6.5 /usr/local/gradle
```

* Créez un script dans profile.d pour ajouter Gradle au PATH en chanque login:

```
$ sudo echo "export PATH=/usr/local/gradle/bin:$PATH" >> /etc/profile.d/gradle.sh
```

* Exécutez le script que nous venons de créer pour l'appliquer à cette session
    de Bash:

```
$ sudo source /etc/profile.d/gradle.sh
```

* Nettoyez les fichiers temporaires:

```
$ rm openjdk-14_linux-x64_bin.tar.gz
$ rm gradle-6.5-bin.zip
```

## Compiler
* Clonez ce dépôt:

```
git clone https://github.com/m-yuhas/password_protector.git
```

* Changez le répertoire à la racine de ce dépôt:

```
cd password_protector
```

### Compiler la GUI
Exécutez les commandes suivantes:

```
gradle clean
gradle jarGui
```

Un fichier que s'appelle *PasswordProtector.jar* devrait apparaître dans le
répertoire de ```dist/```.

### Compiler la CLI
Exécutez les commandes suivantes:

```
gradle clean
gradle jarCli
```

Un fichier que s'appelle *PasswordProtectorCli.jar* devrait apparaître dans le
répertoire de ```dist/```.

## Exécuter
#### GUI
Pour lancer, double-cliquez sur l'icône de PasswordProtector.jar ou le lancez de
la interface en ligne de commande:

```
java -jar PasswordProtectorCli.jar
```

#### CLI
Pour voir le menu d'aide, exécutez:

```
$ java -jar PasswordProtectorCli.jar -h
usage: java -jar PasswordProtectorCli.jar
  -e,--encrypt                  Encrypt a file.
  -d,--decrypt                  Decrypt a file.
  -p,--password_protector <arg> Open a password storage file or create a new one.
  -i,--input_file <arg>         Input file for encrypt and decrypt modes.
  -o,--output_file <arg>        Output file for encrypt and decrypt modes.
```

Pour ouvrir et éditer un fichier que contient mots de passe, exécutez:

```
$ java -jar PasswordProtectorCli.jar -p <file name>
```

Pour crypter un fichier:

```
$ java -jar PasswordProtectorCli.jar -e -i <unencrypted file> -o <output file>
```

Pour décrypter un fichier:

```
$ java -jar PasswordProtectorCli.jar -d -i <encrypted file> -o <output file>
```

## Tâches Futures
- Inclure les tâches de Gradle pour emballer la GUI comme un Bundle pour MacOS
    et comme un Windows Installer pour Microsoft Windows.

