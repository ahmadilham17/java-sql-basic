package com.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringJoiner;

import com.config.Config;

public class Model extends Database {

    protected String table;

    public Model(String table) {
        this.table = table;
    }

    // select()
    public Boolean select(String field) {
        String sql = "SELECT " + field + "FROM " + this.table;
        Boolean next = false;

        try {
            PreparedStatement statement = connect().prepareStatement(sql);

            // jalankan query SQL
            Config.resultSet = statement.executeQuery(sql);

            next = Config.resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return next;
    }

    /**
     * Menampilkan data dari database dengan perintah SQL SELECT.
     *
     * @param select        String untuk memilih kolom yang ingin ditampilkan.
     *                      Gunakan "*" untuk memilih semua kolom atau Gunakan
     *                      "Namafield1, Namafield2, dan seterusnya" untuk memilih
     *                      field tertentu.
     * @param orderBy       Array yang berisi urutan pengurutan (ORDER BY) untuk
     *                      hasil data. Gunakan String[] namaArray = {"namaField",
     *                      "ASC/DESC"} untuk order BY.
     * @param kondisiField  Nama kolom yang digunakan sebagai kondisi WHERE.
     * @param kondisiValues Array yang berisi nilai-nilai yang digunakan sebagai
     *                      kondisi WHERE.
     * @return ResultSet yang berisi hasil data dari perintah SQL SELECT.
     */
    public ResultSet selectData(String select, String[] orderBy, String kondisiField, Object[] kondisiValues) {

        // Set seleksi field
        String setSelect = (select != null) ? select : "*";

        // Inisialisasi urutan
        String setOrder = "";

        // Jika orderBy ada
        if (orderBy != null && orderBy.length > 0) {
            // Set key dan value orderBy
            String keyOrder = orderBy[0];
            String valueOrder = orderBy[1];

            setOrder = "ORDER BY " + keyOrder + " " + valueOrder;
        }

        // Query awal
        String query = "SELECT " + setSelect + " FROM " + table + " " + setOrder;

        try {
            // Buat objek PreparedStatement
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            // Cek kondisi WHERE
            if (kondisiField != null && kondisiValues != null && kondisiValues.length > 0) {
                // Buat string untuk kondisi WHERE
                StringBuilder setKondisi = new StringBuilder();
                for (int i = 0; i < kondisiValues.length; i++) {
                    setKondisi.append(kondisiField).append(" = ? ");
                    if (i < kondisiValues.length - 1) {
                        setKondisi.append("AND ");
                    }
                }

                // Buat query dengan kondisi WHERE
                query = "SELECT " + setSelect + " FROM " + table + " WHERE (" + setKondisi + ") " + setOrder;

                // Siapkan PreparedStatement untuk query dengan kondisi WHERE
                preparedStatement = connect().prepareStatement(query);

                // Binding
                for (int i = 0; i < kondisiValues.length; i++) {
                    preparedStatement.setObject(i + 1, kondisiValues[i]);
                }
            }

            // Eksekusi perintah SQL SELECT
            Config.resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.resultSet;
    }

    // execute()
    public Boolean execute(String sql) {
        Boolean isExecute = false;
        try (PreparedStatement statement = connect().prepareStatement(sql)) {
            // menjalankan query SQL
            statement.execute(sql);

            isExecute = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isExecute;
    }

    /**
     * Menambahkan data ke dalam database.
     *
     * @param data Object yang berisi nilai-nilai yang akan disisipkan. Contoh
     *             parameter Object[] data = {"field1", "value1", "field2",
     *             "value2", ....}.
     * @return Jumlah baris yang berhasil ditambahkan ke dalam tabel.
     */
    public int insert(Object[] data) {
        StringJoiner joiner = new StringJoiner(", "); // Membuat StringJoiner dengan pemisah koma dan spasi

        for (int i = 0; i < data.length; i++) {
            joiner.add("?");
        }
        // Membuat placeholders ?
        String placeholders = joiner.toString();

        // Buat pernyataan SQL INSERT
        String insertQuery = "INSERT INTO " + this.table + " VALUES (" + placeholders + ")";

        try {
            // Membuat objek PreparedStatement
            PreparedStatement preparedStatement = connect().prepareStatement(insertQuery);

            // Mengatur parameter PreparedStatement dengan data
            for (int i = 0; i < data.length; i++) {
                preparedStatement.setObject(i + 1, data[i]);
            }

            // Menjalankan perintah SQL INSERT
            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Mengupdate data dalam database berdasarkan kondisi tertentu.
     *
     * @param updateValues Map yang berisi kolom dan nilai-nilai yang akan
     *                     diperbarui. Contoh parameter Map<String, Object>
     *                     namaVariabel = Map.of("field1", "value1", "field2",
     *                     "value2", ....)
     * @param kondisi      Map yang berisi kondisi WHERE (kolom dan nilainya).
     *                     Contoh paremeter Map<String, Object> namaVariabel =
     *                     Map.of("field", "value").
     * @return Jumlah baris yang berhasil diupdate dalam tabel.
     */
    public int update(Map<String, Object> updateValues, Map<String, Object> kondisi) {
        // Tangkap key dan value dari kondisi where
        String kondisiKey = kondisi.keySet().iterator().next();
        Object kondisiValue = kondisi.values().iterator().next();

        // Set placeholder menjadi "field1 = ?, field2 = ?, ..."
        StringBuilder placeholders = new StringBuilder();
        for (String key : updateValues.keySet()) {
            placeholders.append(key).append(" = ?, ");
        }
        placeholders.setLength(placeholders.length() - 2); // Menghapus dua karakter terakhir (", ") yang tidak
                                                           // diperlukan.

        // Inisialisasi query
        String sql = "UPDATE " + this.table + " SET " + placeholders + " WHERE " + kondisiKey + " = ?";

        try {
            // Membuat objek PreparedStatement
            PreparedStatement preparedStatement = connect().prepareStatement(sql);

            // Mengatur parameter PreparedStatement dengan data pembaruan
            int i = 1;
            for (Object value : updateValues.values()) {
                preparedStatement.setObject(i, value);
                i++;
            }

            // Mengatur parameter PreparedStatement untuk kondisi WHERE
            preparedStatement.setObject(i, kondisiValue);

            // Menjalankan perintah SQL UPDATE
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Menghapus data dari database berdasarkan kondisi tertentu.
     *
     * @param kondisiField Nama kolom yang digunakan sebagai kondisi WHERE.
     * @param kondisiValue Nilai yang digunakan sebagai kondisi WHERE.
     * @return Jumlah baris yang berhasil dihapus dari tabel.
     */
    public int delete(String kondisiField, Object kondisiValue) {
        // Inisialisasi query
        String deleteQuery = "DELETE FROM " + this.table + " WHERE " + kondisiField + " = ?";

        try {
            // Membuat objek PreparedStatement
            PreparedStatement preparedStatement = connect().prepareStatement(deleteQuery);

            // Mengatur parameter PreparedStatement untuk kondisi WHERE
            preparedStatement.setObject(1, kondisiValue);

            // Menjalankan perintah SQL DELETE
            int rowsDeleted = preparedStatement.executeUpdate();

            return rowsDeleted;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
