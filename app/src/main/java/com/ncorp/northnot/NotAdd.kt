package com.ncorp.northnot

import SharedPrefUtils
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ncorp.northnot.databinding.ActivityNotBinding

class NotAdd : AppCompatActivity() {
    private lateinit var binding: ActivityNotBinding
    private var selectedPriority: String = "Düşük"  // Default değer
    private var existingNote: NotClass? = null  // Sınıf seviyesi değişken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnBold.setOnClickListener {
            TextFormatter.toggleBold(binding.Not)
        }

        binding.btnItalic.setOnClickListener {
            TextFormatter.toggleItalic(binding.Not)
        }

        binding.btnUnderline.setOnClickListener {
            TextFormatter.toggleUnderline(binding.Not)
        }

        binding.btnBullet.setOnClickListener {
            TextFormatter.toggleBullet(binding.Not)
        }

        binding.btnTextSize.setOnClickListener {
            showTextSizeBottomSheet(binding.Not)
        }

        // Burada API seviyesine göre getSerializableExtra kullanımı:
        existingNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("note_data", NotClass::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("note_data") as? NotClass
        }

        existingNote?.let {
            binding.NotSubtitle.setText(it.title)
            binding.Not.setText(it.note)
            selectPriority(it.priority)
        } ?: selectPriority("Düşük")

        binding.btnLowPriority.setOnClickListener {
            selectPriority("Düşük")
        }
        binding.btnMediumPriority.setOnClickListener {
            selectPriority("Orta")
        }
        binding.btnHighPriority.setOnClickListener {
            selectPriority("Yüksek")
        }
        binding.btnUrgentPriority.setOnClickListener {
            selectPriority("Acil")
        }
        selectPriority("Düşük")
    }

    fun add(view: View) {
        val title = binding.NotSubtitle.text.toString().trim()
        val noteText = binding.Not.text.toString().trim()

        if (title.isEmpty()) {
            binding.NotSubtitle.error = "Başlık boş olamaz"
            binding.NotSubtitle.requestFocus()
            return
        }

        if (noteText.isEmpty()) {
            binding.Not.error = "Not boş olamaz"
            binding.Not.requestFocus()
            return
        }

        val notesList = SharedPrefUtils.loadNotes(this)

        if (existingNote != null) {
            val index = notesList.indexOfFirst {
                it.title == existingNote!!.title &&
                        it.note == existingNote!!.note &&
                        it.priority == existingNote!!.priority
            }
            if (index != -1) {
                notesList[index] = NotClass(title, noteText, selectedPriority)
            } else {
                notesList.add(NotClass(title, noteText, selectedPriority))
            }
        } else {
            notesList.add(NotClass(title, noteText, selectedPriority))
        }

        SharedPrefUtils.saveNotes(this, notesList)

        Toast.makeText(this, "Not kaydedildi", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun cancel(view: View) {
        finish()
    }

    private fun selectPriority(priority: String): String {
        selectedPriority = priority

        val strokeWidthSelected = resources.getDimensionPixelSize(R.dimen.stroke_width_selected)
        val strokeWidthDefault = 0

        binding.btnLowPriority.strokeWidth = strokeWidthDefault
        binding.btnMediumPriority.strokeWidth = strokeWidthDefault
        binding.btnHighPriority.strokeWidth = strokeWidthDefault
        binding.btnUrgentPriority.strokeWidth = strokeWidthDefault

        when(priority) {
            "Düşük" -> binding.btnLowPriority.strokeWidth = strokeWidthSelected
            "Orta" -> binding.btnMediumPriority.strokeWidth = strokeWidthSelected
            "Yüksek" -> binding.btnHighPriority.strokeWidth = strokeWidthSelected
            "Acil" -> binding.btnUrgentPriority.strokeWidth = strokeWidthSelected
        }
        return priority
    }

    fun showTextSizeBottomSheet(editText: EditText) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_text_size, null)
        dialog.setContentView(view)

        val btnSmall = view.findViewById<Button>(R.id.btnSmall)
        val btnMedium = view.findViewById<Button>(R.id.btnMedium)
        val btnLarge = view.findViewById<Button>(R.id.btnLarge)
        val btnXLarge = view.findViewById<Button>(R.id.btnXLarge)

        val changeTextSize = { sizeSp: Float ->
            val start = editText.selectionStart
            val end = editText.selectionEnd
            if (start == end) {
                editText.text.setSpan(RelativeSizeSpan(sizeSp / 16f), start, start, Spanned.SPAN_MARK_MARK)
            } else {
                editText.text.setSpan(RelativeSizeSpan(sizeSp / 16f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        btnSmall.setOnClickListener {
            changeTextSize(12f)
            dialog.dismiss()
        }
        btnMedium.setOnClickListener {
            changeTextSize(16f)
            dialog.dismiss()
        }
        btnLarge.setOnClickListener {
            changeTextSize(20f)
            dialog.dismiss()
        }
        btnXLarge.setOnClickListener {
            changeTextSize(24f)
            dialog.dismiss()
        }

        dialog.show()
    }
}
