package io.covid_2020.pipeliner

class SaintTropez {

    private val address: String = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?source=csbs"

    fun activity(player_id: String, route: String) {
        try {
            khttp.post(
                url = "${this.address}write?db=tschess",
                data = "activity player=\"${player_id}\",route=\"${route}\""
            )
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }
}

fun main() {
    println("Hello Kotlin!")
}