package xyz.thetbw.bot

import io.ktor.client.request.*
import kotlinx.serialization.Serializable

/**
 * 思知机器人
 */
object OwnThinkBot {

    private const val SUCCESS_FLAG = "success"

    lateinit var url: String
    lateinit var appid: String


    /**
     * 初始化
     */
    fun init(config: Config.OwnThinkConfig){
        this.url = config.url
        this.appid = config.appid
    }

    /**
     * 发送消息
     * @return 收到的消息
     */
    suspend fun sendMessage(message: String,userId: Long): String{
        if (!this::url.isInitialized){
            return "连接失败,机器人还未初始化异常"
        }
        val resp: BotResp = client.post(url){
            header("Content-Type","application/json")
            body = BotReq(message, appid,userId)
        }
        if (resp.message != SUCCESS_FLAG){
//            println("请求异常")
        }
        return resp.data.info.text
    }


    /**
     * 机器人请求数据
     */
    @Serializable
    private data class BotReq(
        val spoken: String,
        val appid: String,
        val userid: Long
    )

    /**
     * 机器人相应数据
     */
    @Serializable
    private data class BotResp(
        val message: String,
        val data: RespData
    ){
        @Serializable
        data class RespData(
            val type: Int,
            val info: RespDataInfo
        )

        @Serializable
        data class RespDataInfo(
            val text: String
        )
    }


}