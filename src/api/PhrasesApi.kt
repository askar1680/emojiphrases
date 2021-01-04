package com.askar.ulubayev.api

import com.askar.ulubayev.*
import com.askar.ulubayev.api.requests.*
import com.askar.ulubayev.model.*
import com.askar.ulubayev.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val PHRASES_API_ENDPOINT = "$API_VERSION/phrases"

@Location(PHRASES_API_ENDPOINT)
class PhrasesApi

fun Route.phrasesApi(db: Repository) {
    authenticate("jwt") {
        get<PhrasesApi> {
            call.respond(db.phrases())
        }

        post<PhrasesApi> {
            val user = call.apiUser!!
            try {
                val request = call.receive<PhrasesApiRequest>()
                val phrase = db.add(user.userId, request.emoji, request.phrase)
                if (phrase != null) {
                    call.respond(phrase)
                } else {
                    call.respondText("Invalid data received", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Throwable) {
                call.respondText("Invalid data received", status = HttpStatusCode.BadRequest)
            }
        }
    }
}