package mcia.accounting.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppSettings {
    var invoiceBaseDir: String = "/home/test"
}
