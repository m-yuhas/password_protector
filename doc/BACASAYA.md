# Pelindung Kata Laluan
## Mukadimah
Atur cara ini membenarkan anda simpan kata-kata laluan, nombor-nombor
pengenalan peribadi, dan jawapan-jawapan kepada soalan-soalan keselamatan
untuk akaun-akaun yang berbeza. Untuk mengakses data ini, dua kata laluan unik
diperlukan supaya jika anda tidak berupaya, dua orang yang dipercayai mesti
hadir untuk membuka kunci akaun-akaun anda. Alat untuk menjana kata laluan
juga disediakan, serta kebolehan untuk menyulitkan dan menyahsulit fail-fail
menggunakan kata laluan tunggal.

Mekanisme penyulitan yang mendasari adalah sifir Blowfish dengan kunci 128
bit.  Setiap masa yang data disulitkan garam baru dan vektor permulaan dijana
secara rawak.  Garam dan kunci sama panjang sambil vektor permulaan 64 bit.
Kunci diperolehi daripada kata laluan menggunakan PBKDF2 dengan HMAC dan
fungsi hash SHA-1.

## Sediakan Persekitaran Membina
Atur cara ini ditulis dengan Java dan menggunakan skrip membina Gradle.
Adalah amat disyorkan membina dan menjalankan dengan JDK versi 14 dan ke atas.
Versi Java lebih awal tidak belum diuji.  Untuk membina, Gradle 6 atau ke atas
diperlukan.

### Mac OS
* Pertama, memasang [Homebrew](https://brew.sh)
* Seterusnya, memasang Java (juga boleh muat turun dan memasang secara langsung daripada [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)):

```
brew cask install java
```

* Memasang Gradle:

```
brew install gradle
```

### Windows
* Pertama, memasang versi terkini JDK oleh ikut arahan di
    [laman web Oracle](https://www.oracle.com/java/technologies/javase-jdk14-downloads.html)
* Seterusnya, memasang versi terkini Gradle oleh ikut arahan di
    [laman web Gradle](https://gradle.org/install/)

### Linux (Debian)
* Pertama, memasang JDK terkini:

```
sudo apt install default-jdk
```

* Memasang Gradle.  Mesti tambah repositori ke apt untuk mendapat versi
    terkini:

```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt install gradle
```

### Linux (Red Hat)
Apabila dokumen ini ditulis, Java 14 dan Gradle 6 tidak termasuk di repositori
sebarang yum atau dnf.  Situasi ini menjadikannya yang sediakan persekitaranm
embina sedikit lebih rumit.

* Pertama, muat turun JDK terkini (menyemak URL ini sebelum menjalankan di
    sistem anda, perintah ini mengandaikan yang sistem anda x64):

```
$ curl -O https://download.java.net/java/GA/jdk14/076bab302c7b4508975440c56f6cc26a/36/GPL/openjdk-14_linux-x64_bin.tar.gz
```

* Unzip fail:

```
$ tar xvf openjdk-14_linux-x64_bin.tar.gz
```

* Berpindah direktori yang mengandungi fail-fail binari ke /opt/:

```
$ sudo mv jdk-14 /opt/
```

* Tambah skrip ini ke direktori profile.d anda untuk tambah Java ke pemboleh
    ubah PATH anda setiap kali log masuk.

```
$ sudo tee /etc/profile.d/jdk14.sh <<EOF
> export JAVA_HOME=/opt/jdk-14
> export PATH=\$PATH:\$JAVA_HOME/bin
> EOF
```

* Menjalankan skrip yang baru dicipta untuk tambah laluan ke Java di sesi Bash
    semasa:

```
$ source /etc/profile.d/jdk14.sh
```

* Dapatkan versi Gradle terkini:

```
$ wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
```

* Unzipnya:

```
$ unzip gradle-6.5-bin.zip
```

* Berpindah fail-fail binari ke /usr/local/gradle:

```
$ sudo mv gradle-6.5 /usr/local/gradle
```

* Mencipta skrip di profile.d untuk tambah Gradle ke pemboleh ubah PATH setiap
    kali log masuk:

```
$ sudo echo "export PATH=/usr/local/gradle/bin:$PATH" >> /etc/profile.d/gradle.sh
```

* Menjalankan skrip yang baru dicipta untuk memohonnya ke sesi Bash ini:

```
$ sudo source /etc/profile.d/gradle.sh
```

* Bersihkan fail-fail sementara:

```
$ rm openjdk-14_linux-x64_bin.tar.gz
$ rm gradle-6.5-bin.zip
```

## Membina
* Klon repositori ini:

```
git clone https://github.com/m-yuhas/password_protector.git
```

* Berubah direktori kerja ke akar repositori ini:

```
cd password_protector
```

### Membina GUI
Menjalankan perintah-perintah yang berikut:

```
gradle clean
gradle jarGui
```

Fail yang bernama *PasswordProtector.jar* sepatutnya muncul di direktori
```dist/```.

### Membina CLI
Menjalankan perintah-perintah yang berikut:

```
gradle clean
gradle jarCli
```

Fail yang bernama *PasswordProtectorCli.jar* sepatutnya muncul di direktori
```dist/```.

## Menjalankan
#### GUI
Untuk melancarkan, klik dua kali di ikon PasswordProtector.jar atau
melancarkan daripada baris arahan:

```
java -jar PasswordProtectorCli.jar
```

#### CLI
Untuk melihat menu bantuan, menjalankan:

```
$ java -jar PasswordProtectorCli.jar -h
usage: java -jar PasswordProtectorCli.jar
  -e,--encrypt                  Encrypt a file.
  -d,--decrypt                  Decrypt a file.
  -p,--password_protector <arg> Open a password storage file or create a new one.
  -i,--input_file <arg>         Input file for encrypt and decrypt modes.
  -o,--output_file <arg>        Output file for encrypt and decrypt modes.
```

Untuk membuka dan menyunting fail kata laluan, menjalankan:

```
$ java -jar PasswordProtectorCli.jar -p <file name>
```

Untuk menyulitkan fail:

```
$ java -jar PasswordProtectorCli.jar -e -i <unencrypted file> -o <output file>
```

Untuk menyahsulit fail:

```
$ java -jar PasswordProtectorCli.jar -d -i <encrypted file> -o <output file>
```

## Membina dan Menjalankan dengan Docker
Tambahan pula, boleh membina fail jar menggunakan fail Docker disediakan:

```
docker build -t password_protector:latest .
```

Sementara membina imej, *PasswordProtectorCli.jar* dan *PasswordProtector.jar*
akan ditulis ke jilid supaya boleh diakses walaupan bekas tidak diaktifkan.
Di Linux, jilid boleh dijumpai di bawah */var/lib/docker/volumes*, sementara
di Windows boleh dijumpai di bawah
*\\\\wsl$\docker-desktop-data\data\docker\volumes*. Jika pemasangan Java
serasi tidak tersedia di sistem hos, juga boleh menjalankan
*PasswordProtectorCli.jar* di dalam bekas dan pelekap jilid yang mengandungi
fail kata laluan:

```
$ docker run -it -v <path to passwords file on host>:/data password_protector:latest
# java -jar PasswordProtectorCli.jar -p /data/<passwords file>
```

## Tugas-Tugas Masa Hadapan
- Termasuk tugas Gradle untuk membungkus fail GUI jar sebagai Bundle untuk
    MacOS dan sebagai Windows Installer untuk Microsoft Windows.

