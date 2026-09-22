package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.domain.model.ShortsScript
import java.io.File
import java.io.FileOutputStream

object ExportHelper {

    fun copyToClipboard(context: Context, script: ShortsScript) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Roteiro Shorts: ${script.title}", script.toFormattedPlainText())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Roteiro copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
    }

    fun exportAsTxt(context: Context, script: ShortsScript) {
        try {
            val fileName = "Roteiro_${sanitizeFileName(script.title)}.txt"
            val file = File(context.cacheDir, fileName)
            file.writeText(script.toFormattedPlainText())

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Roteiro YouTube Shorts: ${script.title}")
                putExtra(Intent.EXTRA_TEXT, script.toFormattedPlainText())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Exportar Roteiro (TXT)")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao exportar TXT: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportAsPdf(context: Context, script: ShortsScript) {
        try {
            // A4 page: 595 x 842 points at 72 dpi
            val pageWidth = 595
            val pageHeight = 842
            val margin = 48f
            val printableWidth = pageWidth - (margin * 2)

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Background
            val bgPaint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

            // Accent bar at top
            val accentPaint = Paint().apply {
                color = android.graphics.Color.rgb(225, 29, 72) // Crimson
                style = Paint.Style.FILL
            }
            canvas.drawRect(margin, margin, pageWidth - margin, margin + 4f, accentPaint)

            var yOffset = margin + 28f

            // Title
            val titlePaint = Paint().apply {
                color = android.graphics.Color.rgb(17, 24, 39)
                textSize = 18f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.drawText(script.title.ifBlank { "Roteiro sem título" }, margin, yOffset, titlePaint)
            yOffset += 20f

            // Metadata row
            val metaPaint = Paint().apply {
                color = android.graphics.Color.rgb(107, 114, 128)
                textSize = 10f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val metaText = "Categoria: ${script.category}  |  Status: ${script.status.label}  |  Alvo: ${script.targetDurationSeconds}s (~${script.estimatedSeconds}s / ${script.totalWords} palavras)"
            canvas.drawText(metaText, margin, yOffset, metaPaint)
            yOffset += 16f

            // Divider line
            val linePaint = Paint().apply {
                color = android.graphics.Color.rgb(229, 231, 235)
                strokeWidth = 1f
                isAntiAlias = true
            }
            canvas.drawLine(margin, yOffset, pageWidth - margin, yOffset, linePaint)
            yOffset += 20f

            // Helper to draw section with label and body text
            fun drawSection(sectionTitle: String, content: String, isHook: Boolean = false) {
                if (content.isBlank() || yOffset > pageHeight - 60f) return

                val sectionTitlePaint = Paint().apply {
                    color = if (isHook) android.graphics.Color.rgb(225, 29, 72) else android.graphics.Color.rgb(31, 41, 55)
                    textSize = 11f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText(sectionTitle, margin, yOffset, sectionTitlePaint)
                yOffset += 16f

                val bodyPaint = Paint().apply {
                    color = android.graphics.Color.rgb(55, 65, 81)
                    textSize = 10.5f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                }

                // Word wrap
                val words = content.split(" ")
                val lineBuilder = StringBuilder()
                for (word in words) {
                    val testLine = if (lineBuilder.isEmpty()) word else "$lineBuilder $word"
                    val measure = bodyPaint.measureText(testLine)
                    if (measure > printableWidth) {
                        canvas.drawText(lineBuilder.toString(), margin, yOffset, bodyPaint)
                        yOffset += 14f
                        lineBuilder.setLength(0)
                        lineBuilder.append(word)
                        if (yOffset > pageHeight - 50f) break
                    } else {
                        lineBuilder.setLength(0)
                        lineBuilder.append(testLine)
                    }
                }
                if (lineBuilder.isNotEmpty() && yOffset <= pageHeight - 50f) {
                    canvas.drawText(lineBuilder.toString(), margin, yOffset, bodyPaint)
                    yOffset += 14f
                }
                yOffset += 12f
            }

            if (script.hook.isNotBlank()) {
                drawSection("1. GANCHO INICIAL (0 - 3 segundos)", script.hook, isHook = true)
            }
            if (script.bodyContent.isNotBlank()) {
                drawSection("2. CORPO DO ROTEIRO / TRANSCRIÇÃO (3 - 45 segundos)", script.bodyContent)
            }
            if (script.callToAction.isNotBlank()) {
                drawSection("3. CHAMADA PARA AÇÃO - CTA (45 - 60 segundos)", script.callToAction)
            }
            if (script.visualNotes.isNotBlank()) {
                drawSection("4. NOTAS DE EDIÇÃO & CORTES VISUAIS", script.visualNotes)
            }
            if (script.aiSummary.isNotBlank()) {
                drawSection("5. RESUMO INTELIGENTE & DESTAQUES", script.aiSummary)
            }

            // Footer
            val footerPaint = Paint().apply {
                color = android.graphics.Color.rgb(156, 163, 175)
                textSize = 9f
                isAntiAlias = true
            }
            canvas.drawText("Gerado pelo aplicativo Roteiros Shorts", margin, pageHeight - margin + 10f, footerPaint)

            pdfDocument.finishPage(page)

            // Save PDF to cache
            val fileName = "Roteiro_${sanitizeFileName(script.title)}.pdf"
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Roteiro YouTube Shorts: ${script.title}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Exportar Roteiro (PDF)")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao gerar PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun sanitizeFileName(name: String): String {
        val clean = name.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        return clean.take(30).ifBlank { "script" }
    }
}
