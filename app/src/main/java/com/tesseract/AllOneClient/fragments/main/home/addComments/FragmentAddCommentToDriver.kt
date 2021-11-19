package com.tesseract.AllOneClient.fragments.main.home.addComments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.taxiCity.AddCommentAdapter
import com.tesseract.AllOneClient.databinding.FragmentAddCommentToDriwerBinding

class FragmentAddCommentToDriver : Fragment(), AddCommentAdapter.CancelOrderListener {

    private var _binding:FragmentAddCommentToDriwerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentAddCommentToDriwerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerComment.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerComment.adapter= AddCommentAdapter(this@FragmentAddCommentToDriver)

            binding.ready.setOnClickListener {
                setBackStackData("commentKey", binding.comment.text.toString(), true)
            }
        }
    }

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    override fun onItemClick(reason: String) {
        binding.comment.setText(reason)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}