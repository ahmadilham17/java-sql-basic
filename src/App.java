import java.sql.ResultSet;
import java.util.Map;

import com.config.Config;
import com.core.Model;

public class App {
    static Model mahasiswa = new Model("mahasiswa");

    public static void main(String[] args) throws Exception {
        try {

            tampilMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mahasiswa.disconnect();
    }

    static void tampilMenu() {
        System.out.println("\n========= MENU UTAMA =========");
        System.out.println("1. Tambah Data Siswa");
        System.out.println("2. Tampilkan Data Siswa");
        System.out.println("3. Ubah Data Siswa");
        System.out.println("4. Hapus Data Siswa");
        System.out.println("0. Keluar");
        System.out.println("");
        System.out.print("PILIHAN : ");

        try {
            int pilihan = Integer.parseInt(Config.input.readLine()); // mengambil Config.input user (pilihan
                                                                     // menu)

            switch (pilihan) {
                case 0: // jika pilihan 0
                    System.exit(0); // program dihentikan
                    break;
                case 1: // jika pilihan 1
                    tambahDataSiswa(); // memanggil method tambahDataSiswa
                    break;
                case 2: // jika pilihan 2
                    ambilDataSiswa(); // memanggil method ambilDataSiswa
                    break;
                case 3: // jika pilihan 3
                    updateDataSiswa(); // memanggil method updateDataSiswa
                    break;
                case 4: // jika pilihan 4
                    hapusDataSiswa(); // memanggil method hapusDataSiswa
                    break;
                default: // jika pilihan selain diatas
                    System.out.println("Pilihan tidak ada !");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void ambilDataSiswa() {

        try { // proses perintah selama tidak ada error

            System.out.println("            DATA SISWA                ");
            System.out.println("+------------------------------------+");

            String[] orderBy = { "id", "DESC" };

            ResultSet rsSelect = mahasiswa.selectData(null, orderBy, null, null);

            if (Boolean.FALSE.equals(rsSelect.next())) {
                System.out.println(" Belum ada data !");
            } else {
                do { // jika tidak kosong, lakukan looping
                    String nim = Config.resultSet.getString("nim"); // ambil data dari field nis
                    String nama = Config.resultSet.getString("nama"); // ambil data dari field nama
                    String email = Config.resultSet.getString("email"); // ambil data dari field alamatSiswa
                                                                        // noTelponSiswa
                    System.out.println(
                            String.format("%s, %s - %s", nim, nama, email)); // cetak
                                                                             // masing-masing
                                                                             // baris
                } while (Config.resultSet.next());
            }

            System.out.println("+------------------------------------+");
            System.out.print("Tekan sembarang tombol untuk melanjutkan !");
            Config.input.readLine(); // tunggu penekanan tombol
            Config.clearScreen(); // bersihkan layar

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void tambahDataSiswa() {
        Config.clearScreen();
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

    }

    static void updateDataSiswa() {
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
    }

    static void hapusDataSiswa() {
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
    }
}
