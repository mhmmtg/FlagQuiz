package net.mguler.flagsquiz.ui.model

data class Question(
    val id: Int,
    val image: String,
    //val correctAnswer: Int,
    val correctAnswerPos: Int,
    //val optionOne: String,
    //val optionTwo: String,
    //val optionThree: String,
    //val optionFour: String,
    val options: List<String>
)