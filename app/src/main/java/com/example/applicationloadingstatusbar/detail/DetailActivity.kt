package com.example.applicationloadingstatusbar.detail

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.example.applicationloadingstatusbar.BuildConfig
import com.example.applicationloadingstatusbar.R
import com.example.applicationloadingstatusbar.databinding.DetailActivityBinding
import com.example.applicationloadingstatusbar.databinding.DetailContentBinding
import com.example.applicationloadingstatusbar.download.DownloadStatus
import com.google.android.material.R.color.design_default_color_error

class DetailActivity : AppCompatActivity() {
    private val fileName by lazy {
        intent?.extras?.getString(EXTRA_FILE_NAME, unknownText) ?: unknownText
    }
    private val downloadStatus by lazy {
        intent?.extras?.getString(EXTRA_DOWNLOAD_STATUS, unknownText) ?: unknownText
    }

    private val unknownText by lazy { getString(R.string.unknown) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<DetailActivityBinding>(this, R.layout.detail_activity )
            .apply {
                setSupportActionBar(toolbar)
                detailContent.initializeView()
            }
    }

    private fun DetailContentBinding.initializeView() {
        fileNameText.text = fileName
        downloadStatusText.text = downloadStatus
        okButton.setOnClickListener { finish() }
        changeViewForDownloadStatus()
    }

    private fun DetailContentBinding.changeViewForDownloadStatus() {
        when (downloadStatusText.text) {
            DownloadStatus.SUCCESSFUL.statusText -> {
                changeDownloadStatusImageTo(R.drawable.ic_check_circle_outline_24)
                changeDownloadStatusColorTo(R.color.colorPrimaryDark)
            }
            DownloadStatus.FAILED.statusText -> {
                changeDownloadStatusImageTo(R.drawable.ic_error_24)
                changeDownloadStatusColorTo(design_default_color_error)
            }
        }
    }

    private fun DetailContentBinding.changeDownloadStatusImageTo(@DrawableRes imageRes: Int) {
        downloadStatusImage.setImageResource(imageRes)
    }

    private fun DetailContentBinding.changeDownloadStatusColorTo(@ColorRes colorRes: Int) {
        ContextCompat.getColor(this@DetailActivity, colorRes)
            .also { color ->
                downloadStatusImage.imageTintList = ColorStateList.valueOf(color)
                downloadStatusText.setTextColor(color)
            }
    }

    companion object {
        private const val EXTRA_FILE_NAME = "${BuildConfig.APPLICATION_ID}.FILE_NAME"
        private const val EXTRA_DOWNLOAD_STATUS = "${BuildConfig.APPLICATION_ID}.DOWNLOAD_STATUS"


        fun bundleExtrasOf(
            fileName: String,
            downloadStatus: DownloadStatus
        ) = bundleOf(
            EXTRA_FILE_NAME to fileName,
            EXTRA_DOWNLOAD_STATUS to downloadStatus.statusText
        )
    }
}