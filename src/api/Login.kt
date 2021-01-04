package com.askar.ulubayev.api

import com.askar.ulubayev.*
import com.askar.ulubayev.repository.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val LOGIN_ENDPOINT = "/login"

@Location(LOGIN_ENDPOINT)
class Login

fun Route.login(db: Repository, jwtService: JWTService) {
    post<Login> {
        val params = call.receive<Parameters>()
        val userId = params["userId"] ?: return@post call.redirect(it)
        val password = params["password"] ?: return@post call.redirect(it)

        val user = db.user(userId, hash(password))
        if (user != null) {
            val token = jwtService.generateToken(user)
            call.respondText(token)
        } else {
            call.respondText("Invalid user")
        }
    }
}