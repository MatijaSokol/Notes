package com.matijasokol.notes.domain

import kotlin.uuid.Uuid

class UUIDProviderImpl : UUIDProvider {

    override fun generate(): Uuid = Uuid.random()

    override fun generateValue(): String = generate().toString()
}
