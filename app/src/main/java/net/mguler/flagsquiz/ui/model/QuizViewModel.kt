package net.mguler.flagsquiz.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class QuizViewModel(app: Application) : AndroidViewModel(app) {
    private val jsonString: String
    private lateinit var questions: List<Question>

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> = _question
    private var count = 0

    private val _answer = MutableLiveData<Answer>()
    val answer: LiveData<Answer> = _answer

    init {
        app.assets.open("country.json").bufferedReader()
            .use { jsonString = it.readText() }

        _answer.value = Answer("", 0)
    }

    fun getNextQuestion() {
        if (count < Constants.NO_OF_QUESTIONS) {
            _question.value = questions[count]
        }

    }

    fun getQuestions(): List<Question> {
        count = 0
        questions = Constants.getQuestions(jsonString)
        _question.value = questions[0]
        return questions
    }

    fun increaseCorrectAnswer() {
        _answer.value = Answer(_answer.value?.elapsedTime, _answer.value?.correctAnswer?.plus(1))
    }

    fun setElapsedTime(timeStr: String) {
        _answer.value = Answer(timeStr, _answer.value?.correctAnswer)
    }


}