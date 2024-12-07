@file:Suppress("ktlint:standard:filename")

package com.matijasokol.notes.data.api

import io.ktor.resources.Resource

@Resource("/v1")
class V1 {

    @Resource("/create-user")
    class CreateUser(val v1: V1 = V1())
}
