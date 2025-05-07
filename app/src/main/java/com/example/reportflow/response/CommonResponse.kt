package com.example.reportflow.response

import com.example.reportflow.model.Entries
import com.example.reportflow.model.Reports


data class CommonResponse(
    var error: Boolean,
    var message: String,
    var data: ArrayList<Entries>,
    var data2: ArrayList<Reports>
)
