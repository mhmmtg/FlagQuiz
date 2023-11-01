package net.mguler.flagsquiz.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import net.mguler.flagsquiz.R
import net.mguler.flagsquiz.databinding.FragmentResultsBinding
import net.mguler.flagsquiz.ui.model.QuizViewModel
import net.mguler.flagsquiz.ui.model.Constants

class ResultsFragment : Fragment() {
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRestart.setOnClickListener {
            findNavController().navigate(R.id.action_results_to_start)
        }

        viewModel.answer.observe(viewLifecycleOwner) {
            val correctAnswers = it.correctAnswer!!
            val elapsedTimeStr = it.elapsedTime!!

            val elapsedTime = elapsedTimeStr.split(":")
            val elapsedSeconds = (elapsedTime[0].toInt() * 60 ) + (elapsedTime[1].toInt())

            binding.textResultCorrectAnswers.text =
                getString(R.string.correct_answers, correctAnswers)
            binding.textResultPlusPts.text =
                getString(R.string.points, (correctAnswers * 50))
            binding.textResultElapsedTime.text =
                getString(R.string.elapsed_time, elapsedTimeStr)
            binding.textResultMinusPts.text =
                getString(R.string.elapsed_seconds, elapsedSeconds)

            val score = (correctAnswers * 50) - elapsedSeconds
            binding.textResultScore.text = score.toString()
            binding.textResultName.text = Constants.name
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}