package com.example.chatapp

/**
 * This class represent object message
 *  - has 2 attributes
 *  - first hold data of message
 *  - second hold sender Id
 *
 */
class Message {
    var message: String? = null
    var senderId: String? = null

    constructor(){}
    constructor(paMessage: String?, paSenderId: String?){
        this.message = paMessage
        this.senderId = paSenderId
    }
}