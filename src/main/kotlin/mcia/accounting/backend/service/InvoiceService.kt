package mcia.accounting.backend.service

import mcia.accounting.backend.config.AppSettings
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.PurchaseRepository
import mcia.accounting.backend.service.exception.FileNotFoundException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

@Service
class InvoiceService(private val settings: AppSettings,
                     private val purchaseRepository: PurchaseRepository) {

    @Transactional(readOnly = true)
    fun findInvoiceOf(purchaseId: Long): Pair<Resource, String> {
        val purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow { FileNotFoundException("purchase id not found") }
        val invoicePath = purchase.invoicePath
                ?: throw FileNotFoundException("no invoice is attached")
        val baseDir = settings.invoiceBaseDir
        val invoiceFile = File(baseDir, invoicePath)
        if (!invoiceFile.isFile)
            throw FileNotFoundException("cannot find invoice: $invoicePath")
        return FileSystemResource(invoiceFile) to filenameOf(purchase)
    }

    @Transactional
    fun saveInvoiceTo(purchaseId: Long, newInvoice: MultipartFile): Purchase {
        val purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow { ResourceNotFoundException("purchase id not found") }

        val date = Date()
        val dirname = DIR_FORMATTER.format(date)
        val filename = "${FILE_FORMATTER.format(date)}_id_$purchaseId.$EXTENSION"

        val baseDir = settings.invoiceBaseDir
        val targetDir = File(baseDir, dirname).apply { mkdir() }
        val newInvoicePath = Paths.get(dirname, filename).toString()
        val newInvoiceFile = File(targetDir, filename)
        val oldInvoicePath = purchase.invoicePath ?: ""
        val oldInvoiceFile = File(baseDir, oldInvoicePath)

        log.debug("Saving invoice: {}", newInvoiceFile)
        newInvoice.transferTo(newInvoiceFile)

        purchase.invoicePath = newInvoicePath
        return purchaseRepository.save(purchase).also {
            log.info("Updated {}", it)
            if (oldInvoiceFile.isFile) {
                log.debug("Deleting old invoice: {}", oldInvoiceFile)
                try {
                    Files.delete(oldInvoiceFile.toPath())
                } catch (t: Throwable) {
                    log.warn("Could not delete $oldInvoiceFile", t)
                }
            }
        }
    }

    @Transactional
    fun deleteInvoiceOf(purchaseId: Long): Purchase {
        val purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow { ResourceNotFoundException("purchase id not found") }

        val invoicePath = purchase.invoicePath
        if (invoicePath.isNullOrBlank()) return purchase

        val baseDir = settings.invoiceBaseDir
        val invoiceFile = File(baseDir, invoicePath)

        if (invoiceFile.isFile) {
            log.debug("Deleting invoice: {}", invoiceFile)
            try {
                Files.delete(invoiceFile.toPath())
            } catch (t: Throwable) {
                throw RuntimeException("could not delete invoice: $invoicePath", t)
            }
        }

        purchase.invoicePath = null
        return purchaseRepository.save(purchase)
                .also { log.info("Updated {}", it) }
    }

    private fun filenameOf(purchase: Purchase): String {
        val name = purchase.code
                .replace(DISALLOWED_NAME_CHARS, "_")
                .trim('_', '-', '.')
        return "invoice_$name.$EXTENSION"
    }

    companion object {
        private val log = loggerOf(InvoiceService::class)
        private const val EXTENSION = "pdf"
        private const val DIR_DATE_FORMAT = "yyyy-MM"
        private const val FILE_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss"
        private val DIR_FORMATTER = SimpleDateFormat(DIR_DATE_FORMAT, Locale.ENGLISH)
        private val FILE_FORMATTER = SimpleDateFormat(FILE_DATE_FORMAT, Locale.ENGLISH)
        private val DISALLOWED_NAME_CHARS = Regex("[^\\w]+")
    }

}
