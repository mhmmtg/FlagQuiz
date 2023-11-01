package net.mguler.flagsquiz.ui.model

import org.json.JSONArray
import kotlin.random.Random

object Constants {

    const val USER_NAME: String = "user_name"
    const val CORRECT_ANSWERS: String = "correctAnswers"
    const val ELAPSED_TIME: String = "elapsedTime"
    const val NO_OF_QUESTIONS = 15

    var name = "Guest"

    fun getQuestions(countriesJson: String): List<Question> {
        val list = mutableListOf<Question>()
        for (i in 0 until NO_OF_QUESTIONS) {
            val country = getQuestion(countriesJson)
            list.add(country)
        }
        return list
    }

    private fun getQuestion(countriesJson: String): Question {
        val countriesJA = JSONArray(countriesJson)

        //List of four random numbers
        val myList = List(4) { Random.nextInt(0, countriesJA.length()) }


        val countriesJO = countriesJA.getJSONObject(myList[0])
        val name1 = countriesJO.getString("name")
        val code1 = countriesJO.getString("code")

        val countriesJO2 = countriesJA.getJSONObject(myList[1])
        val name2 = countriesJO2.getString("name")

        val countriesJO3 = countriesJA.getJSONObject(myList[2])
        val name3 = countriesJO3.getString("name")

        val countriesJO4 = countriesJA.getJSONObject(myList[3])
        val name4 = countriesJO4.getString("name")

        val options = listOf<String>(name1, name2, name3, name4).shuffled()

        return Question(1, code1, options.indexOf(name1), options)
    }
}