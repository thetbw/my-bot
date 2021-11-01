package xyz.thetbw.bot

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

val client = HttpClient(OkHttp){
    install(JsonFeature){
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json{
            isLenient = false
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = false
        })
        acceptContentTypes = acceptContentTypes + ContentType.Application.Json + ContentType("text","json")
    }
}
