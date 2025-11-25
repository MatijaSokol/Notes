@file:Suppress("ktlint:standard:filename")

package com.matijasokol.notes.data.api

import io.ktor.resources.Resource

@Resource("/v1")
class V1 {

    @Resource("/user")
    class CreateUser(val v1: V1 = V1())

    @Resource("/note")
    class CreateNote(val v1: V1 = V1())

    @Resource("/note")
    class UpdateNote(val v1: V1 = V1())

    @Resource("/note")
    class DeleteNote(
        val v1: V1 = V1(),
        val noteId: String,
    )

    @Resource("/note")
    class GetNote(
        val v1: V1 = V1(),
        val noteId: String,
    )

    @Resource("/notes")
    class GetCurrentUserNotes(val v1: V1 = V1())
}
