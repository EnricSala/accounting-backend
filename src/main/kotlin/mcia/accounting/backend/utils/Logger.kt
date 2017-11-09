package mcia.accounting.backend.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun loggerOf(clazz: KClass<*>): Logger = LoggerFactory.getLogger(clazz.java)
