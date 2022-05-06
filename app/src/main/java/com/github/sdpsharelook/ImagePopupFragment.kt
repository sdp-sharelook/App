package com.github.sdpsharelook

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class ImagePopupFragment : DialogFragment() {

    companion object {

        const val TAG = "ImageDialog"

        private const val KEY_SOURCE = "KEY_SOURCE"
        private const val KEY_TARGET = "KEY_TARGET"
        private const val KEY_IMAGE = "KEY_IMAGE"
        private const val KEY_DATE = "KEY_DATE"

        fun newInstance(source: String, target: String, date: Date, image: Bitmap): ImagePopupFragment {
            val args = Bundle()
            args.putString(KEY_SOURCE, source)
            args.putString(KEY_TARGET, target)
            args.putLong(KEY_DATE, date.time)
            args.putParcelable(KEY_IMAGE, image)
            val fragment = ImagePopupFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.image_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {
        view.findViewById<ImageView>(R.id.popUpImage).setImageBitmap(arguments?.getParcelable(
            KEY_IMAGE))
        view.findViewById<TextView>(R.id.popUpSource).text = arguments?.getString(KEY_SOURCE)
        view.findViewById<TextView>(R.id.popUpTarget).text = arguments?.getString(KEY_TARGET)
        view.findViewById<TextView>(R.id.popUpDate).text = Date(arguments?.getLong(KEY_DATE)!!).toString()
        view.findViewById<Button>(R.id.button).setOnClickListener { dismiss() }
    }
}