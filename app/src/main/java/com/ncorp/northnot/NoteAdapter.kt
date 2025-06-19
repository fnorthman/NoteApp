package com.ncorp.northnot

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncorp.northnot.databinding.ItemBinding

class NoteAdapter(val notClassList: MutableList<NotClass>): RecyclerView.Adapter<NoteAdapter.NotHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotHolder,
        position: Int
    ) {
        val note = notClassList[position]
        holder.binding.textViewTitle.text = note.title
        holder.binding.textViewSubtitle.text = note.note


        // Önceliğe göre renk atama
        val colorRes = when(note.priority) {
            "Düşük" -> android.graphics.Color.parseColor("#4CAF50")    // Yeşil
            "Orta" -> android.graphics.Color.parseColor("#FFEB3B")     // Sarı
            "Yüksek" -> android.graphics.Color.parseColor("#FF9800")   // Turuncu
            "Acil" -> android.graphics.Color.parseColor("#F44336")     // Kırmızı
            else -> android.graphics.Color.GRAY                         // Default
        }

        holder.binding.viewPriorityIndicator.setBackgroundColor(colorRes)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NotAdd::class.java)
            intent.putExtra("note_data", note)
            holder.itemView.context.startActivity(intent)


        }

        holder.itemView.setOnLongClickListener {
            DialogUtil.showDeleteConfirmation(holder.itemView.context) {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    notClassList.removeAt(pos)
                    notifyItemRemoved(pos)
                    SharedPrefUtils.saveNotes(holder.itemView.context, notClassList)

                    if (notClassList.isEmpty()) {
                        notifyDataSetChanged()
                    }
                }
            }
            true
        }
    }

    override fun getItemCount(): Int = notClassList.size

    class NotHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}
