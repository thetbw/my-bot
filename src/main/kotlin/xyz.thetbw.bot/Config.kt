package xyz.thetbw.bot

import kotlinx.serialization.Serializable

@Serializable
class Config (
    val ownThinkConfig: OwnThinkConfig,
    val adminId: Long
    ){

    @Serializable
    data class OwnThinkConfig(
        val url: String,
        val appid: String
    )


}