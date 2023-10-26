package com.devhaus.fullstorypoc.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.barteksc.pdfviewer.PDFView
import com.devhaus.fullstorypoc.R

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainFragment : Fragment() {

    private lateinit var pdfView: PDFView

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.main_fragment, container, false)
        pdfView = rootView.findViewById(R.id.pdfView)

        val pdfUrl = "https://www.africau.edu/images/default/sample.pdf"

        GlobalScope.launch {
            val pdfData = downloadPdfFromUrl(pdfUrl)
            withContext(Dispatchers.Main) {
                if (pdfData != null) {
                    pdfView.fromBytes(pdfData.bytes())
                        .defaultPage(0)
                        .load()
                } else {
                    // Handle download failure
                }
            }
        }

        return rootView
    }

    private suspend fun downloadPdfFromUrl(url: String) = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body
        } else {
            null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
