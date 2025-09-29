package xyz.thetbw.bot.game


enum class Card(val face: String,val code:String,val level:Int) {

    C3("3","3",1),
    C4("4","4",2),
    C5("5","5",3),
    C6("6","6",4),
    C7("7","7",5),
    C8("8","8",6),
    C9("9","9",7),
    C10("10","10",8),
    CJ("J","J",9),
    CQ("Q","Q",10),
    CK("K","K",11),
    CA("A","A",12),
    C2("2","2",13),
    CSJoker("小鬼","S",14),
    CBJoker("大鬼","B",15),;

    override fun toString(): String {
        if (face == code){
            return face
        }else{
            return "$face($code)"
        }
    }


}