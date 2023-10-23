## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Table of Contents

- [Configuration](#configuration)
- [Query Builder](#query-builder) 
    - [Select](#select)
    - [Insert](#insert)
    - [Update](#update)
    - [Delete](#delete)

## Configuration

### .Vscode Folder

Buat folder `.vscode` kemudian buat file `settings.json` didalamnya

tambahkan kode berikut ini di `settings.json`: 
```java
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

### Config folder

Copy file `.Config` lalu rename menjadi `Config.java`
Kemudian atur isi dari nama database, host, dan lain-lain.

Ubah Nama Database sesuai database kita:
```java
public static final String DATABASE_NAME = "database";
```

Ubah `username` dan `password` sesuai punya kita defaultnya `root` dan passwordnya kosong:
```java
public static final String USERNAME = "root";
public static final String PASSWORD = "";
```

Contoh kode pada `Config.java`:
```java
package com.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;

public class Config {

    // init database constants
    // com.mysql.jdbc.Driver
    public static final String DATABASE_NAME = "java_mysql";
    public static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static final String MAX_POOL = "250"; // set your own limit

    public static Statement statement;
    public static ResultSet resultSet;
    public static final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    public static final BufferedReader input = new BufferedReader(inputStreamReader);

    // method untuk membersihkan layar
    public static void clearScreen() {
        System.out.print("\033[H\033[2J"); // menempatkan kursor pada awal dan akhir layar
        System.out.flush(); // membersihkan layar
    }

}
```

# Query Builder

Sebuah query builder adalah sebuah alat atau perpustakaan perangkat lunak yang digunakan dalam pemrograman untuk membangun pernyataan SQL atau kueri basis data dengan cara yang lebih mudah, aman, dan terstruktur daripada menulis kueri secara langsung dengan string SQL biasa. 

Untuk menggunakan query builder Anda harus membuat object dari Model.

Contoh pembuatan misalnya Anda membuat object model dengan nama `mahasiswa`:

```java
public static Model mahasiswa = new Model("mahasiswa");
```

Setelah itu Anda dapat menggunakan method Query Builder.

## Select
Dalam konteks SQL (Structured Query Language), pernyataan `SELECT` digunakan untuk mengambil atau meng-query data dari tabel database.

Gunakan method select:

```java
mahasiswa.selectData()
```

Contoh kode untuk menampilkan data:
```java
String[] orderBy = { "id", "DESC" };

ResultSet rsSelect = mahasiswa.selectData(null, orderBy, null, null);

// jika data kosong
if (Boolean.FALSE.equals(rsSelect.next())) {
    // tampilkan data belum ada
    System.out.println(" Belum ada data !");
} else {
    do { 
        // jika tidak kosong, lakukan looping
        String nim = Config.resultSet.getString("nim");
        String nama = Config.resultSet.getString("nama"); 
        String email = Config.resultSet.getString("email"); 
        System.out.println(
                String.format("%s, %s - %s", nim, nama, email)); // cetak
    } while (Config.resultSet.next());
}
```

## Insert

Dalam konteks SQL, pernyataan `INSERT` digunakan untuk menambahkan data baru ke dalam sebuah tabel dalam database.

Gunakan method insert
```java
mahasiswa.insert()
```

Contoh kode untuk menambahkan data:
```java
try {
    // ambil Config.input dari user
    System.out.print("NIM: ");
    String nim = Config.input.readLine().trim(); // mengambil Config.input NIS dari user

    System.out.print("Nama Mahasiswa: ");
    String nama = Config.input.readLine().trim(); // mengambil Config.input nama siswa

    System.out.print("Email: ");
    String email = Config.input.readLine().trim(); // mengambil Config.input alamat siswa

    Object[] data = { null, nim, nama, email };

    if (mahasiswa.insert(data) > 0) {
        System.out.println("Data berhasil ditambahkan!");
    }

    System.out.print("Tekan sembarang tombol untuk melanjutkan !");
    Config.input.readLine();
    Config.clearScreen();

} catch (Exception e) {
    e.printStackTrace();
}
```

## Update

Dalam konteks SQL, pernyataan `UPDATE` digunakan untuk mengubah data pada tabel dalam database.

Gunakan method update
```java
mahasiswa.update()
```

Contoh kode untuk mengubahkan data:
```java
try {
    // ambil Config.input dari user
    System.out.print("NIM yang mau diedit: ");
    String nimCari = (Config.input.readLine());

    System.out.print("NIM: ");
    String nim = Config.input.readLine().trim();

    System.out.print("Nama Mahasiswa: ");
    String nama = Config.input.readLine().trim();

    System.out.print("Email: ");
    String email = Config.input.readLine().trim();

    Map<String, Object> updateValues = Map.of("nim", nim, "nama", nama, "email", email);
    Map<String, Object> kondisi = Map.of("nim", nimCari);

    if (mahasiswa.update(updateValues, kondisi) > 0) {
        System.out.println("Data berhasil di update");
    }

    System.out.print("Tekan sembarang tombol untuk melanjutkan !");
    Config.input.readLine();
    Config.clearScreen();

} catch (Exception e) {
    e.printStackTrace();
}
```

## Delete

Dalam konteks SQL, pernyataan `DELETE` digunakan untuk menghapus data pada tabel dalam database.

Gunakan method delete
```java
mahasiswa.delete()
```

Contoh kode untuk menghapus data:
```java
try {
    // ambil Config.input dari user
    System.out.print("NIM yang mau dihapus: ");
    String nim = (Config.input.readLine());

    // jalankan query SQL
    if (mahasiswa.delete("nim", nim) > 0) {
        System.out.println("Data berhasil di hapus!");
    }

    System.out.println("Tekan sembarang tombol untuk melanjutkan !");
    Config.input.readLine();
    Config.clearScreen();
} catch (Exception e) {
    e.printStackTrace();
}
```

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

> Created By [Haris](https://github.com/haris2303)