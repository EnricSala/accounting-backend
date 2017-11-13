package mcia.accounting.backend.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
@EnableConfigurationProperties(AppSettings::class)
class WebConfig(private val settings: AppSettings) : InitializingBean {

    override fun afterPropertiesSet() {
        val invoiceBaseDir = File(settings.invoiceBaseDir)
        if (!invoiceBaseDir.isDirectory)
            throw IllegalStateException("invoice base dir does not exist: $invoiceBaseDir")
    }

    companion object {
        const val BASE_API_PATH = "/api"
    }

}
