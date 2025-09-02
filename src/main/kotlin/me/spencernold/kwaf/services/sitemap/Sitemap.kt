package me.spencernold.kwaf.services.sitemap

import java.util.*

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sitemap(val lastModified: String = "", val priority: Double = -1.0, val changeFrequency: ChangeFrequency = ChangeFrequency.NONE) {

    companion object {
        // TODO Put this somewhere better...
        fun getLastModifiedString(year: Int, month: Int, day: Int): String {
            val yearV = MapGeneratorTool.clamp(year.toDouble(), Double.MIN_VALUE, Double.MAX_VALUE).toInt()
            val monthV = MapGeneratorTool.clamp(month.toDouble(), 1.0, 12.0).toInt()
            val dayV = MapGeneratorTool.clamp(day.toDouble(), 1.0, 31.0).toInt()
            return String.format("%04d-%02d-%02d", yearV, monthV, dayV)
        }
    }

    enum class ChangeFrequency {

        NONE, NEVER, YEARLY, MONTHLY, WEEKLY, DAILY, HOURLY, ALWAYS;

        fun stringify(): String {
            return name.lowercase()
        }
    }

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Hidden
}
