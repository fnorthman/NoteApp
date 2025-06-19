package com.ncorp.northnot

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

object DialogUtil {
    fun showDeleteConfirmation(
        context: Context,
        onDeleteConfirmed: () -> Unit
    ) {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.custom_delete_dialog, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnDelete.setOnClickListener {
            onDeleteConfirmed()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}