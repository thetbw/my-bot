package xyz.thetbw.bot

import java.io.*

const val CHARSET = "utf-8"

fun readStringFromFile(file: File): String{
    val fileReader = InputStreamReader(FileInputStream(file), CHARSET)
    val builder  = StringBuilder()
    val buffer  = CharArray(1024)
    fileReader.use {
        var index = -1;
        while (fileReader.read(buffer).also { index = it } != -1){
            builder.append(buffer,0,index)
        }
    }
    return builder.toString()
}

fun writeStringToFile(file: File,data: String){
    val fileWriter = OutputStreamWriter(FileOutputStream(file), CHARSET)
    fileWriter.use {
        fileWriter.write(data)
        fileWriter.flush()
    }
}