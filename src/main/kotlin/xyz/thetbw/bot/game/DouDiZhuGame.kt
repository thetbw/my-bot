package xyz.thetbw.bot.game

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Friend
import kotlin.random.Random

class DouDiZhuGame(val sender: Friend) {

    private var status: Status = Status.Init

    private var currentPlayerIndex:Int = 0 //当前玩家位置
    private var dizhuIndex:Int = 0 //地主位置
    private var activePlayerIndex = 0;//当前发牌处理

    private var cards = arrayOf(ArrayList<Card>(),  ArrayList<Card>(),ArrayList<Card>()) //三个玩家的牌


    private var lastSend = ArrayList<Card>()



    init {
        GlobalScope.launch {
            reset()
        }
    }

    fun gameHandle(msg: String): String {
        return ""
    }

    suspend fun over(){
        sender.sendMessage("游戏已结束")
    }

    suspend fun reset() {
        //重置
        status = Status.Ready
        currentPlayerIndex = Random.nextInt(0,3)
        dizhuIndex = Random.nextInt(0,3)
        activePlayerIndex = dizhuIndex
        sender.sendMessage(
            """
                游戏开始
                你当前为玩家  ${currentPlayerIndex + 1}
                地主为玩家   ${dizhuIndex + 1}
                发送 #结束 来结束游戏
                发送牌号来进行出牌，如果大鬼为B,小鬼为S
                发送 #过 来跳过出牌
            """.trimIndent()
        )

        val allCards = ArrayList<Card>()
        Card.values().forEach {
            if (it.level< 14){
                allCards.add(it)
                allCards.add(it)
                allCards.add(it)
                allCards.add(it)
            }
        }
        allCards.add(Card.CSJoker)
        allCards.add(Card.CBJoker)

        //初始化牌组
        for (i in 0..2){
            cards[i] = ArrayList()
            for (j in 0..17){
                cards[i].add(allCards.removeAt(Random.nextInt(0,allCards.size)))
            }
        }
        cards[dizhuIndex].addAll(allCards);
        info()
        system()
    }


    //获取当前状态
    private suspend fun info(){
        cards[currentPlayerIndex].sortBy { it.level }
        val playerCardsStr = cards[currentPlayerIndex].joinToString{it.toString()}
        sender.sendMessage("你的牌为：${playerCardsStr}")
    }

    private fun system(){

    }

    sealed class Status{
        object Init : Status()
        object Ready : Status()
        object Playing : Status()
        object Over : Status()
    }
}