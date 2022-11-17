package com.manriquetavi.routes

import com.manriquetavi.domain.model.ApiResponse
import com.manriquetavi.domain.model.Endpoint
import com.manriquetavi.domain.model.UserSession
import com.manriquetavi.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.deleteUserRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        delete(Endpoint.DeleteUser.path) {
            val userSession = call.principal<UserSession>()
            userSession?.let {
                try {
                    call.sessions.clear<UserSession>()
                    deleteUserFromDb(
                        app = app,
                        userId = userSession.id,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception) {
                    app.log.error("ERROR DELETING USER: ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            } ?: run {
                app.log.info("INVALID USER SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteUserFromDb(
    app: Application,
    userId: String,
    userDataSource: UserDataSource
) {
    val response = userDataSource.deleteUser(userId = userId)
    if(response) {
        app.log.info("USER SUCCESSFULLY DELETED")
        call.respond(
            message = ApiResponse(success = true),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("ERROR DELETING THE USER")
        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )
    }
}