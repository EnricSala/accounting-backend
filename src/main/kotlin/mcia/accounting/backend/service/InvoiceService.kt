package mcia.accounting.backend.service

import mcia.accounting.backend.config.AppSettings
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.PurchaseRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.core.io.FileSystemResource
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
    fun findInvoiceOf(purchaseId: Long): FileSystemResource {
        val purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow { RuntimeException("purchase id not found") }
        val invoicePath = purchase.invoicePath
                ?: throw RuntimeException("no invoice is attached")
        val baseDir = settings.invoiceBaseDir
        val invoiceFile = File(baseDir, invoicePath)
        if (!invoiceFile.isFile)
            throw RuntimeException("cannot find invoice: $invoicePath")
        return FileSystemResource(invoiceFile)
    }

    @Transactional
    fun saveInvoiceTo(purchaseId: Long, newInvoice: MultipartFile): Purchase {
        val purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow { RuntimeException("purchase id not found") }

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
                .orElseThrow { RuntimeException("purchase id not found") }

        val baseDir = settings.invoiceBaseDir
        val invoicePath = purchase.invoicePath
        val invoiceFile = File(baseDir, invoicePath)

        if (invoiceFile.isFile) {
            log.debug("Deleting invoice: {}", invoiceFile)
            try {
                Files.delete(invoiceFile.toPath())
            } catch (t: Throwable) {
                log.error("Could not delete $invoiceFile", t)
                throw IllegalStateException("could not delete invoice: $invoicePath")
            }
        }

        purchase.invoicePath = null
        return purchaseRepository.save(purchase)
                .also { log.info("Updated {}", it) }
    }

    companion object {
        private val log = loggerOf(InvoiceService::class)
        private const val EXTENSION = "pdf"
        private const val DIR_DATE_FORMAT = "yyyy-MM"
        private const val FILE_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss"
        private val DIR_FORMATTER = SimpleDateFormat(DIR_DATE_FORMAT, Locale.ENGLISH)
        private val FILE_FORMATTER = SimpleDateFormat(FILE_DATE_FORMAT, Locale.ENGLISH)
    }

}
