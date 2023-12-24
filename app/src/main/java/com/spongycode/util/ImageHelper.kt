package com.spongycode.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision

object ImageHelper {
    suspend fun scaleDownBitmap(
        uri: Uri,
        imageRequestBuilder: ImageRequest.Builder,
        imageLoader: ImageLoader
    ): Bitmap? {
        val imageRequest = imageRequestBuilder
            .data(uri)
            .size(size = 768)
            .precision(Precision.EXACT)
            .build()
        try {
            val result = imageLoader.execute(imageRequest)
            if (result is SuccessResult) {
                return (result.drawable as BitmapDrawable).bitmap
            }
        } catch (_: Exception) {
        }
        return null
    }
}