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

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        get(Endpoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()
            userSession?.let {
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    app.log.error("GETTING USER INFO ERROR: ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            } ?: run {
                app.log.info("getUserInfoRoute: INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }

        }
    }
}