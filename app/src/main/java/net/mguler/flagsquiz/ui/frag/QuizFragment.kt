package net.mguler.flagsquiz.ui.frag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import net.mguler.flagsquiz.R
import net.mguler.flagsquiz.databinding.FragmentQuizBinding
import net.mguler.flagsquiz.ui.model.QuizViewModel
import net.mguler.flagsquiz.ui.model.Constants.NO_OF_QUESTIONS

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by activityViewModels()

    private var correctAnswerPos = -1
    private var questionNum = 0
    private var selectedOptPos = -1
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCheckChangeListeners()
        viewModel.getQuestions()
        placeQuestion()
        binding.chronometer.start()

        binding.btnSubmit.setOnClickListener { btnSubmit() }
        binding.btnNext.setOnClickListener { btnNext() }
    }

    private fun setCheckChangeListeners() {
        binding.radioGroup.children.forEach {
            it as RadioButton
            it.setOnCheckedChangeListener { button, b ->
                if (b) {
                    button.setBackgroundResource(R.drawable.bg_option_selected)
                    button.setTextColor(Color.WHITE)
                } else {
                    button.setBackgroundResource(R.drawable.bg_option)
                    button.setTextColor(Color.BLACK)
                }
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbOptOne -> selectedOptPos = 0
                R.id.rbOptTwo -> selectedOptPos = 1
                R.id.rbOptThree -> selectedOptPos = 2
                R.id.rbOptFour -> selectedOptPos = 3
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun btnSubmit() {
        //disabled
        binding.radioGroup.children.forEach { it.isEnabled = false }

        //visibility
        binding.btnSubmit.visibility = View.INVISIBLE
        binding.btnNext.visibility = View.VISIBLE

        highlightAnswer(selectedOptPos)
        if (selectedOptPos == correctAnswerPos) { viewModel.increaseCorrectAnswer() }

        //Stop chronometer
        if (questionNum == NO_OF_QUESTIONS-1) {
            binding.chronometer.stop()
            viewModel.setElapsedTime(binding.chronometer.text.toString())
        }
    }

    private fun btnNext() {
        binding.radioGroup.children.forEach { it.isEnabled = true }

        if (questionNum < NO_OF_QUESTIONS-1) {
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnNext.visibility = View.INVISIBLE

            questionNum++
            viewModel.getNextQuestion()

            clearSelection()
        }
        else {
            // TODO: clear backstack
            findNavController().navigate(R.id.action_quiz_to_results)
        }

        if (questionNum == NO_OF_QUESTIONS-1) {
            binding.btnNext.text = getString(R.string.results)
        }
    }

    private fun placeQuestion() {
        viewModel.question.observe(viewLifecycleOwner) { question->
            val progress = getString(R.string.question_of, questionNum+1, NO_OF_QUESTIONS)
            binding.textProgress.text = progress

            val imgName = question.image
            val imgId = resources.getIdentifier(imgName, "drawable", activity?.packageName)


            binding.imgFlag.setImageResource(imgId)

            binding.rbOptOne.text = question.options[0]
            binding.rbOptTwo.text = question.options[1]
            binding.rbOptThree.text = question.options[2]
            binding.rbOptFour.text = question.options[3]

            correctAnswerPos = question.correctAnswerPos

            println(question.toString())
            println(question.correctAnswerPos.toString())
        }
    }

    private fun clearSelection() {
        binding.radioGroup.clearCheck()
        binding.radioGroup.children.forEach {it as RadioButton
            it.setBackgroundResource(R.drawable.bg_radio)
            it.setTextColor(Color.BLACK)
        }
        selectedOptPos = -1
    }

    private fun highlightAnswer(pos: Int) {
        when(pos) {
            correctAnswerPos -> { highlightCorrect(pos) }
            -1 -> { highlightCorrect(correctAnswerPos) }
            else -> { highlightCorrect(correctAnswerPos); highlightWrong(pos) }
        }
    }

    private fun highlightCorrect(pos: Int) {
        binding.radioGroup[pos].apply {this as RadioButton
            setBackgroundResource(R.drawable.bg_option_correct)
            setTextColor(Color.WHITE)
        }
    }

    private fun highlightWrong(pos: Int) {
        binding.radioGroup[pos].apply {this as RadioButton
            setBackgroundResource(R.drawable.bg_option_wrong)
            setTextColor(Color.WHITE)
        }

    }

}