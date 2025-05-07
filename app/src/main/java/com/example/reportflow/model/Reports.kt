package com.example.reportflow.model

data class Reports(
    var id: Int,
    var userId: String,
    var userName: String,
    var userMobile: String,
    var userEmail: String,
    var description: String,
    var imageUri: String,
    var location: String,
    var timestamp: String,
    var workerId: String,
    var status: String,
    var workerName: String,
    var workerMobile: String,
    var type : String

    )
