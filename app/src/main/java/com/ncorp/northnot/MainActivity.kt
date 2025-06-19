package com.ncorp.northnot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncorp.northnot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private var notesList = mutableListOf<NotClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Toast.makeText(this, "NORTHMAN: Not Silmek İçin Uzun Tıklayın", Toast.LENGTH_LONG).show()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }







        // RecyclerView için layout manager ayarla
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // SharedPrefUtils ile notları yükle
        notesList = SharedPrefUtils.loadNotes(this)






        // Adapter'i oluştur ve RecyclerView'a bağla
        adapter = NoteAdapter(notesList)

        binding.recyclerView.adapter = adapter


    }
    override fun onResume() {
        super.onResume()
        // Notlar güncellendiğinde listeyi tekrar yükle ve adapter’a bildir
        notesList.clear()
        notesList.addAll(SharedPrefUtils.loadNotes(this))
        adapter.notifyDataSetChanged()
    }
    fun add(v:View){

        val intent = Intent(this, NotAdd::class.java)
        startActivity(intent)
    }



}