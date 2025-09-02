package io.github.confsched.profile.components

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import org.jetbrains.compose.resources.decodeToImageBitmap
import qrcode.QRCode

object CardPreviewImageBitmaps {
    val profileImage = ImageBitmap(160, 160).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Red
            },
        )
    }

    val frontCardImage = ImageBitmap(300, 380).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Green
            },
        )
    }

    val backCardImage = ImageBitmap(300, 380).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Blue
            },
        )
    }

    val qrImage = QRCode.ofSquares()
        .build("https://2025.droidkaigi.jp/")
        .renderToBytes()
        .decodeToImageBitmap()
}
