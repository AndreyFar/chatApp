package com.example.chatapp

/**
 * This class represent object user
 *  - has 3 attributes
 *  - first hold name of user
 *  - second hold users email
 *  - third hold users id
 *
 */
class User {
    var name: String? = null
    var email: String? = null
    lateinit var uniqueId: String

    constructor(){}
    constructor(paName: String?, paEmail: String?, paUniqueId: String){
        this.name = paName
        this.email = paEmail
        this.uniqueId = paUniqueId
    }
}