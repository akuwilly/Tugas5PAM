package com.example.tugas5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log; // Tambahkan import ini
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv1;
    EditText etNim, etNama;
    Button btnSimpan;
    ArrayList<Mahasiswa> data;
    MahasiswaAdapter adapter;
    DBHelper dbHelper;

    // Tambahkan TAG di sini
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        rv1 = findViewById(R.id.rv1);
        etNim = findViewById(R.id.editTextTextPersonName2);
        etNama = findViewById(R.id.etNim);
        btnSimpan = findViewById(R.id.bt1);

        data = getDataFromDB();
        adapter = new MahasiswaAdapter(this, data);
        rv1.setAdapter(adapter);
        rv1.setLayoutManager(new LinearLayoutManager(this));

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = etNim.getText().toString();
                String nama = etNama.getText().toString();

                if (!nim.isEmpty() && !nama.isEmpty()) {
                    boolean isInserted = dbHelper.addData(nim, nama);
                    if (isInserted) {
                        Mahasiswa mhs = new Mahasiswa();
                        mhs.nim = nim;
                        mhs.nama = nama;

                        // Tambahkan item baru di posisi 0
                        data.add(0, mhs);
                        adapter.notifyItemInserted(0); // Notifikasi adapter
                        rv1.scrollToPosition(0); // Scroll ke posisi paling atas
                        etNim.setText(""); // Clear input
                        etNama.setText(""); // Clear input
                        Log.d(TAG, "Data berhasil ditambahkan: " + nim + ", " + nama); // Log success
                    } else {
                        Toast.makeText(MainActivity.this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public ArrayList<Mahasiswa> getDataFromDB() {
        ArrayList<Mahasiswa> data = new ArrayList<>();
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.nim = cursor.getString(0); // Column nim
                mhs.nama = cursor.getString(1); // Column nama
                data.add(mhs);
                Log.d(TAG, "Data dari DB: NIM=" + mhs.nim + ", Nama=" + mhs.nama); // Log data dari DB
            }
        }
        cursor.close(); // Tutup cursor setelah digunakan
        return data;
    }
}
