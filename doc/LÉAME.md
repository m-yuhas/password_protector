# Protector de Contraseñas
[中文指南](https://github.com/m-yuhas/password_protector/blob/master/doc/读我档案.md)

[Documentación en español](https://github.com/m-yuhas/password_protector/blob/master/doc/LÉAME.md)

[Documentation en français](https://github.com/m-yuhas/password_protector/blob/master/doc/LISEZ-MOI.md)

## Introducción
Este programa le permite a usted de almacenar contraseñas, PINs, y respuestas a
las preguntas de seguridad para cuentas varias.  Para accesar los datos, dos
contraseñas únicas son requeridas así si usted esté incapacitado, dos personas
que confía son requeridas para desbloquear las cuentas.  Una utilidad para
generar contraseñas es también proporcionada, así como la capacidad de cifrar y
descifrar archivos con una contraseña sola.

El mecanismo de cifrada subyacente es el codificador de Blowfish con una clave
de 128 bits.  Cada vez que los datos son cifrados, una nueva sal y un nuevo
vector de inicialización se generan al azar.  La sal es el mismo largo de la
clave y el vector de inicialización es 64 bits.  La clave se deriva de la
contraseña usando PBKDF2 con HMAC y la función hash SHA-1.

## Preparar el Medio Ambiente de Compilación
Este programa estaba escrito en Java y usa un guión de compilación de Gradle.
Le recomiendo a usted de compilar y ejecutar con el JDK versión 14 o mayor.
Las versiones de Java más tempranas no han sido probadas.  Para compilar, Gradle
6 o mayor es requerido.

### Mac OS
* Primero, instale [Homebrew](https://brew.sh)
* A continuación instale Java (you can also download and install directly from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)):

```
brew cask install java
```

* Instale Gradle:

```
brew install gradle
```

### Windows
* Primero instale la versión más reciente del JDK siguiendo las instrucciones en
    [el sitio web de Oracle](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html)
* A continuación instale la versión más reciente de Gradle siguiendo las
    instrucciones en [el sitio web de Gradle](https://gradle.org/install/)

### Linux (Debian)
* Primero instale la versión más reciente del JDK:

```
sudo apt install default-jdk
```

* Instale Gradle.  Necesita añadir un repositorio a apt para cargar la versión
    más reciente:

```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt install gradle
```

### Linux (Red Hat)
Cuando este documenta estaba escrito, Java 14 y Gradle 6 no fueron incluidos en
ninguno de los repositorios de yum o dnf.  Este hace preparar el medio ambiente
de compilación un poco más complicado.


* Primero cargar el JDK más reciente (Verificar el URL antes de ejecutar en su 
    propia sistema, este comando asume que está ejecutando en una sistema de
    x64):

```
$ curl -O https://download.java.net/java/GA/jdk14/076bab302c7b4508975440c56f6cc26a/36/GPL/openjdk-14_linux-x64_bin.tar.gz
```

* Descomprima el archivo:

```
$ tar xvf openjdk-14_linux-x64_bin.tar.gz
```

* Copie la carpeta que contiene los acrchivos binarios a /opt/:

```
$ sudo mv jdk-14 /opt/
```

* Añada un guión en su carpeta de profile.d para añadir Java a la PATH en cada
    login:

```
$ sudo tee /etc/profile.d/jdk14.sh <<EOF
> export JAVA_HOME=/opt/jdk-14
> export PATH=\$PATH:\$JAVA_HOME/bin
> EOF
```

* Ejecute el guión que acabamos de crear para añadir Java a la PATH en la sesión
    de Bash actual:

```
$ source /etc/profile.d/jdk14.sh
```

* Cargue la versión más reciente de Gradle:

```
$ wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
```

* La descomprima:

```
$ unzip gradle-6.5-bin.zip
```

* Mueva los archivos binarios a /usr/local/gradle:

```
$ sudo mv gradle-6.5 /usr/local/gradle
```

* Crear un guión en profile.d para añadir Gradle a la PATH en cada login:

```
$ sudo echo "export PATH=/usr/local/gradle/bin:$PATH" >> /etc/profile.d/gradle.sh
```

* Ejecute el guión que acabamos de crear para aplicarlo a esta sesión de Bash:

```
$ sudo source /etc/profile.d/gradle.sh
```

* Limpie los archivos temporáneos:

```
$ rm openjdk-14_linux-x64_bin.tar.gz
$ rm gradle-6.5-bin.zip
```

## Compilar
* Cargue este repositorio:

```
git clone https://github.com/m-yuhas/password_protector.git
```

* Cambie el directorio corriente a la raíz de este repositorio:

```
cd password_protector
```

### Compilar la GUI
Ejecute los comandos siguientes:

```
gradle clean
gradle jarGui
```

Un archivo que se llama *PasswordProtector.jar* debe aparecer en el directorio de ```dist/```

### Compilar la CLI
Ejecute los comandos siguientes:

```
gradle clean
gradle jarCli
```

Un archivo que se llama *PasswordProtectorCli.jar* debe aparecer en el directorio de ```dist/```

## Ejecutar
#### GUI
Para lanzar, haga doble clic en el icono de PasswordProtector.jar o lo lance de
la interfaz de línea de comandos:

```
java -jar PasswordProtectorCli.jar
```

#### CLI
Para ver el menú de ayuda, ejecute:

```
$ java -jar PasswordProtectorCli.jar -h
usage: java -jar PasswordProtectorCli.jar
  -e,--encrypt                  Encrypt a file.
  -d,--decrypt                  Decrypt a file.
  -p,--password_protector <arg> Open a password storage file or create a new one.
  -i,--input_file <arg>         Input file for encrypt and decrypt modes.
  -o,--output_file <arg>        Output file for encrypt and decrypt modes.
```

Para abrir y editar un archivo que contiene contraseñas, ejecute:

```
$ java -jar PasswordProtectorCli.jar -p <file name>
```

Para cifrar un archivo:

```
$ java -jar PasswordProtectorCli.jar -e -i <unencrypted file> -o <output file>
```

Para descifrar un archivo:

```
$ java -jar PasswordProtectorCli.jar -d -i <encrypted file> -o <output file>
```

## Tareas Futuras
- Incluir tasks de Gradle para empaquetar la GUI como un Bundle por MacOS y un
    Windows Installer por Microsoft Windows.
