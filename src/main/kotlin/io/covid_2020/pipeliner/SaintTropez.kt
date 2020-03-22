package io.covid_2020.pipeliner

import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileWriter

fun main() {
    val stateMap: MutableMap<String, Info> = mutableMapOf()

    val address: String = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?source=csbs"
    val response: Response = khttp.get(url = address)
    val obj: JSONObject = response.jsonObject

    val locations: JSONArray = obj["locations"] as JSONArray
    locations.forEach() {
        val place = it as JSONObject
        val state = place.get("state") as String
        val latest: JSONObject = place.get("latest") as JSONObject

        var confirmed: Int = latest.getInt("confirmed")
        var deaths: Int = latest.getInt("deaths")
        var recovered: Int = latest.getInt("recovered")

        if (stateMap.containsKey(state)) {
            confirmed += stateMap[state]!!.confirmed
            deaths += stateMap[state]!!.deaths
            recovered += stateMap[state]!!.recovered
        }
        stateMap[state] = Info(confirmed, deaths, recovered)
    }

//    stateMap.forEach() {
//        println("${it.key}\n" +
//                "confirmed: ${it.value.confirmed}\n" +
//                "deaths: ${it.value.deaths}\n" +
//                "recovered: ${it.value.recovered}\n\n")
//    }

    val header: String = "state,confirmed,deaths,recovered"
    val fileWriter: FileWriter = FileWriter("csbs.csv")
    try {
        fileWriter.append("${header}\n")

        stateMap.forEach() {
            fileWriter.append("${it.key},${it.value.confirmed},${it.value.deaths},${it.value.recovered}\n")
        }
    } finally {
        try {
            fileWriter.flush()
            fileWriter.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}

data class Info(
    val confirmed: Int,
    val deaths: Int,
    val recovered: Int
)