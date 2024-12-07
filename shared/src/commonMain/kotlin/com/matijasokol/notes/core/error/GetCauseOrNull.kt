package com.matijasokol.notes.core.error

/**
 * Should retrieve generic T cause from throwable.
 *
 * @receiver the throwable to get the cause from.
 * @return the specified cause of this throwable (first if there are multiple) or null if not found.
 */
inline fun <reified T : Throwable> Throwable.getCauseOrNull(): T? {
    var curEx: Throwable? = this
    while (curEx?.cause != null && curEx !is T) {
        curEx = curEx.cause
    }
    return curEx as? T
}
