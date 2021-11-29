package com.tesseract.AllOneClient.fragments.main.home.news.newsView

import android.R
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.FragmentNewsViewBinding
import com.tesseract.AllOneClient.utils.statusBarColor

class FragmentNewsView:Fragment() {

    private var _binding:FragmentNewsViewBinding?=null
    private val binding get() = _binding!!
    val args:FragmentNewsViewArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            com.google.android.material.transition.MaterialContainerTransform()
                .apply {
                    this.containerColor = Color.TRANSPARENT
                    this.startContainerColor = Color.TRANSPARENT
                    duration = 250.toLong()
                    scrimColor = Color.TRANSPARENT

                }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper: Context = ContextThemeWrapper(
            activity,
            R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(com.tesseract.AllOneClient.R.layout.fragment_news_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsViewBinding=FragmentNewsViewBinding.bind(view)
        _binding=newsViewBinding
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding.apply {
            mainView.transitionName="cardViewTransition${args.id}"
            Picasso.get().load(args.image).into(newsImageView)
            title.text=args.title
            description.text=args.description
            date.text=args.data

        }

    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        if (args.isFromHome){
            requireActivity().statusBarColor(
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.green, requireActivity().theme),
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.green, requireActivity().theme),
                false
            )
        }else{
            requireActivity().statusBarColor(
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
                true
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (args.isFromHome){
            requireActivity().statusBarColor(
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.green, requireActivity().theme),
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.green, requireActivity().theme),
                false
            )
        }else{
            requireActivity().statusBarColor(
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
                ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
                true
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}