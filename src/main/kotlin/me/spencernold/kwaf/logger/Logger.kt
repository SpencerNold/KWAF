package me.spencernold.kwaf.logger

abstract class Logger {

    companion object {

        private var systemLogger: Logger = SystemLogger()

        fun getSystemLogger(): Logger {
            return systemLogger
        }

        fun setSystemLogger(logger: Logger) {
            systemLogger = logger
        }
    }

    fun info(message: String) {
        log(Severity.INFO, message)
    }

    fun warn(message: String) {
        log(Severity.WARN, message)
    }

    fun error(message: String) {
        log(Severity.ERROR, message)
    }

    abstract fun log(severity: Severity, message: String)

    enum class Severity {
        INFO, WARN, ERROR
    }
}