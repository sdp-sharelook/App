package com.github.sdpsharelook.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentDownloadLanguagesBinding
import com.github.sdpsharelook.databinding.LayoutLanguageDownloaderBinding
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.translate.MLKitTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DownloadLanguagesFragment : Fragment() {
    private val downloader = MLKitTranslatorDownloader
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentDownloadLanguagesBinding.inflate(inflater, container, false).apply {
        CoroutineScope(Dispatchers.IO).launch {
            val downloaded = downloader.downloadedLanguages()?.toSet() ?: setOf()
            MLKitTranslator.availableLanguages.forEach { language ->
                withContext(Dispatchers.Main) {
                    val languageDownloader =
                        createLanguageDownloader(inflater, language, language in downloaded)
                    linearLayoutLanguages.addView(languageDownloader)
                }
            }
        }
    }.root


    private fun createLanguageDownloader(
        inflater: LayoutInflater,
        language: Language,
        available: Boolean,
    ): View {
        val binding = LayoutLanguageDownloaderBinding.inflate(inflater)
        binding.apply {
            imageViewFlag.setImageResource(language.flagId(requireContext())
                ?: R.drawable.ic_no_image)
            textViewDisplayName.text = language.displayName
            textViewTagName.text = language.tag
            if (available) {
                imageButtonDownload.visibility = View.GONE
                imageButtonDelete.visibility = View.VISIBLE
                imageViewDownloaded.visibility = View.VISIBLE
            }
            imageButtonDownload.setOnClickListener {
                downloadLanguage(language, binding)
            }
            imageButtonDelete.setOnClickListener { deleteLanguage(language, binding) }
        }
        return binding.root
    }

    fun downloadLanguage(language: Language, binding: LayoutLanguageDownloaderBinding) =
        binding.apply {
            imageButtonDownload.visibility = View.GONE
            progressBarDownloading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val success = downloader.downloadLanguage(language)
                withContext(Dispatchers.Main) {
                    if (success) {
                        imageViewDownloaded.visibility = View.VISIBLE
                        imageButtonDelete.visibility = View.VISIBLE
                    } else {
                        imageButtonDownload.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),
                            "Couldn't download language ${language.tag}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                progressBarDownloading.visibility = View.GONE

            }
        }


    fun deleteLanguage(language: Language, binding: LayoutLanguageDownloaderBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            val success = downloader.deleteLanguage(language)
            withContext(Dispatchers.Main) {
                if (success) binding.apply {
                    imageViewDownloaded.visibility = View.GONE
                    imageButtonDelete.visibility = View.GONE
                    imageButtonDownload.visibility = View.VISIBLE
                }
                else Toast.makeText(requireContext(),
                    "Couldn't delete language ${language.tag}",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}