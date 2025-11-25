package com.matijasokol.notes.domain

import kotlin.uuid.Uuid

interface UUIDProvider {

    fun generate(): Uuid

    fun generateValue(): String
}
