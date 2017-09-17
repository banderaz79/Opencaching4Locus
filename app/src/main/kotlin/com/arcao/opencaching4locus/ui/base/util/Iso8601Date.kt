package com.arcao.opencaching4locus.ui.base.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for handling a most common subset of ISO 8601 strings
 * (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are
 * missing.
 */
object Iso8601Date {
    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
}

/** Transform Date to ISO 8601 string.  */
fun Date.toIso8901(): String {
    val formatted = Iso8601Date.DATE_FORMAT.format(this)
    return formatted.substring(0, 22) + ":" + formatted.substring(22)
}

/** Get current date and time formatted as ISO 8601 string.  */
fun String.iso8061Now(): String {
    return Date().toIso8901()
}

/** Transform ISO 8601 string to Date.  */
@Throws(ParseException::class)
fun String.fromIso8601(): Date {
    var s = this.replace("Z", "+00:00", true)
    try {
        s = s.substring(0, 22) + s.substring(23)  // to get rid of the ":"
    } catch (e: IndexOutOfBoundsException) {
        throw ParseException("Invalid length", 0)
    }

    return Iso8601Date.DATE_FORMAT.parse(s)
}