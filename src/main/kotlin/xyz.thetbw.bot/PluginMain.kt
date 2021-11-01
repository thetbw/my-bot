package xyz.thetbw.bot

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.info
import java.io.File

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.thetbw.bot",
        name = "thetbw-bot",
        version = "0.1.0"
    ) {
        author("thetbw(thetbw@outlook.com)")
        info(
            """
            个人机器人
        """.trimIndent()
        )
    }
) {
    private var config: Config? = null

    override fun PluginComponentStorage.onLoad() {
        logger.info("插件开始加载")
        logger.debug("开始记载配置文件")
        logger.debug("当前配置文件目录: ${configFolder.absolutePath}")

        val configFile = File(configFolder.absolutePath+File.separator+"config.yaml")
        if (!configFile.exists()){
            logger.warning("初次加载,请先修改配置文件,位于:${configFile.absolutePath}")
            configFile.createNewFile()
        }else{
            val configStr = readStringFromFile(configFile)
            config = Yaml.default.decodeFromString<Config>(configStr)
            logger.info("开始初始化思知机器人:${config!!.ownThinkConfig}")
            OwnThinkBot.init(config!!.ownThinkConfig)
        }
    }

    override fun onEnable() {
        logger.info { "插件加载成功" }

        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent>{
            var aboutMe = false
            var mainString  = StringBuilder()
            //分类示例
            message.forEach {
                if (it is At && it.target == message.bot.id){
                    aboutMe = true
                }else if (it is AtAll ){
                    aboutMe = true
                }else if (it is QuoteReply && it.source.fromId == message.bot.id){
                    aboutMe = true
                }
                if (it is PlainText) {
                   mainString.append(it.content)
                }
            }

            if (aboutMe){
                if (mainString.startsWith("#") || mainString.startsWith("/")){
                    return@subscribeAlways
                }
                val response = OwnThinkBot.sendMessage(mainString.toString(),sender.id)
                group.sendMessage(At(sender.id)+PlainText(response))
            }
        }
        eventChannel.subscribeAlways<FriendMessageEvent>{
            val content = message.content
            if (content.startsWith("#") || content.startsWith("/")){
                return@subscribeAlways
            }
            val response = OwnThinkBot.sendMessage(message.content,sender.id)
            //好友信息
            sender.sendMessage(response)
        }
        eventChannel.subscribeAlways<NewFriendRequestEvent>{
            if (config == null){
                return@subscribeAlways
            }
            val admin = this.bot.getFriend(config!!.adminId)
            if (admin == null){
                logger.warning("当前管理员不存在")
                return@subscribeAlways
            }
            admin.sendMessage("${fromId} 申请添加好友,已自动同意,申请消息为:${message}")
            accept()
        }
        eventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent>{
            if (config == null){
                return@subscribeAlways
            }
            val admin = this.bot.getFriend(config!!.adminId)
            if (admin == null){
                logger.warning("当前管理员不存在")
                return@subscribeAlways
            }
            admin.sendMessage("${invitorId} 邀请加入群 ${groupName}(${groupId}),已自动同意")
            //自动同意加群申请
            accept()
        }
        eventChannel.subscribeAlways<BotLeaveEvent>{
            if (config == null){
                return@subscribeAlways
            }
            val admin = this.bot.getFriend(config!!.adminId)
            if (admin == null){
                logger.warning("当前管理员不存在")
                return@subscribeAlways
            }
            admin.sendMessage("你退出了群 ${groupId}")
        }
        eventChannel.subscribeAlways<BotJoinGroupEvent> {
            if (config == null){
                return@subscribeAlways
            }
            val admin = this.bot.getFriend(config!!.adminId)
            if (admin == null){
                logger.warning("当前管理员不存在")
                return@subscribeAlways
            }
            admin.sendMessage("你被邀请加入群 ${it.groupId},已自动同意")
        }
        eventChannel.subscribeAlways<BotOnlineEvent> {
            if (config == null){
                return@subscribeAlways
            }
            val admin = this.bot.getFriend(config!!.adminId)
            if (admin == null){
                logger.warning("当前管理员不存在")
                return@subscribeAlways
            }
            admin.sendMessage("机器人启动成功")
        }
    }
}
