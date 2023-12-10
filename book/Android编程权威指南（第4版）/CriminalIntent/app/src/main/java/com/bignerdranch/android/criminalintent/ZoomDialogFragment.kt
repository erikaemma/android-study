package com.bignerdranch.android.criminalintent

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File

/*
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment

class ZoomDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.zoom_layout, container, false)
        val imageView = view.findViewById(R.id.zoom_image_view) as ImageView

        val photoFileName = arguments?.getSerializable("PHOTO_URI") as String
        imageView.setImageBitmap(BitmapFactory.decodeFile(requireContext().filesDir.path + "/" + photoFileName))

        return view
    }

    companion object {
        fun newInstance(photoFileName: String): ZoomDialogFragment {
            val frag = ZoomDialogFragment()
            val args = Bundle()
            args.putSerializable("PHOTO_URI", photoFileName)
            frag.arguments = args
            return frag
        }
    }

}
*/

private const val ARG_IMAGE = "image"

class ZoomDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            //get the layout inflater
            val inflater = requireActivity().layoutInflater

            //get a dialog picture view reference
            // Pass null as the parent view because its going in the dialog layout
            val view = inflater.inflate(R.layout.zoom_layout, null)

            // Inflate and set the layout for the dialog
            builder.setView(view)

            //get reference to crimePicture image view
            val crimePicture = view.findViewById(R.id.crimePicture) as ImageView

            //get the image file path argument
            val photoFile = arguments?.getSerializable(ARG_IMAGE) as File

            //get the scaled image
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())

            //set the picture in the crimePicture view
            crimePicture.setImageBitmap(bitmap)

            //set the dialog characteristics
            builder.setTitle(R.string.crime_photo)
                .setNegativeButton(
                    R.string.Dismiss,
                    DialogInterface.OnClickListener { _, _ -> dialog?.cancel() })

            // Create the AlertDialog object and return it
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }


    companion object {
        fun newInstance(photoFile: File): ZoomDialogFragment {
            val args = Bundle().apply { putSerializable(ARG_IMAGE, photoFile) }

            return ZoomDialogFragment().apply { arguments = args }

        }
    }
}