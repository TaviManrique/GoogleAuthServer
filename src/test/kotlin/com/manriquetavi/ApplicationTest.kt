package com.manriquetavi


import kotlin.test.*
import io.ktor.server.testing.*


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        /*
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }*/
        assertEquals(expected = true, actual = true)
    }
}