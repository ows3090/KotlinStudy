package ows.kotlinstudy.useddeal.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
){
    constructor(): this("","")
}
