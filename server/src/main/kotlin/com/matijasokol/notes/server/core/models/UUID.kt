package com.matijasokol.notes.server.core.models

import java.util.UUID

val UUID.value: String get() = toString()
