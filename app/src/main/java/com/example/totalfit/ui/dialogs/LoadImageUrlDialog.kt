package com.example.totalfit.ui.dialogs

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.totalfit.R
import com.example.totalfit.databinding.LoadImageFromWebDialogBinding

class LoadImageUrlDialog(
    var url: Uri? = null,
    var quandoImagemCarregada: (imagem: Uri?) -> Unit
) : DialogFragment() {

    lateinit var binding: LoadImageFromWebDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = LoadImageFromWebDialogBinding.inflate(layoutInflater)

        url?.let {
            binding.loadImageFromWebDialogImageUrl.editText?.setText(url.toString())
            carregaImagemNaView(url)
        }

        return activity?.let {

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.load_image_dialog_confirm_label)) { _, _ ->
                    quandoImagemCarregada(url)
                }

            setOnClickListener()

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun setOnClickListener() {
        binding.loadImageFromWebDialogButtonCarregar.setOnClickListener {
            val imageUrlText = binding.loadImageFromWebDialogImageUrl.editText?.text.toString()
            if (imageUrlText.isNotEmpty()) {
                carregaImagemNaView(Uri.parse(imageUrlText))
            }
        }
    }

    private fun carregaImagemNaView(url: Uri?) {
        val imageView = binding.loadImageFromWebDialogIv
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url)
        this.url = url
    }

    companion object {
        val TAG = "LoadImageUrlDialog"
    }
}