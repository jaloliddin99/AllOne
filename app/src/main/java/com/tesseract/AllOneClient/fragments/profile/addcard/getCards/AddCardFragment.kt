package com.tesseract.AllOneClient.fragments.profile.addcard.getCards

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.addcard.AddCardAdapter
import com.tesseract.AllOneClient.databinding.FragmentAddCardBinding
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import com.tesseract.AllOneClient.utils.firstCardFragmentEntrance
import com.tesseract.AllOneClient.utils.getNavOptions
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.newCardAdded
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddCardFragment : Fragment(R.layout.fragment_add_card), AddCardAdapter.OnItemClickListener {

    private var fragmentAddCardBinding: FragmentAddCardBinding? = null
    private lateinit var addCardAdapter: AddCardAdapter
    private lateinit var viewModel: GetCardViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddCardBinding.bind(view)
        fragmentAddCardBinding = binding
        viewModel=ViewModelProvider(this).get(GetCardViewModel::class.java)




        binding.rvAddCard.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        addCardAdapter=AddCardAdapter(ArrayList(), this, requireContext())
        binding.rvAddCard.adapter=addCardAdapter
        val resId: Int = R.anim.layout_animation
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        binding.rvAddCard.layoutAnimation = animation
        binding.rvAddCard.setHasFixedSize(true)
        if (newCardAdded||firstCardFragmentEntrance){
            viewModel.getCardDataList(headerMapUniversal(requireContext()))
            viewModel.cardDataList.observe(requireActivity(), {
                binding.loader.loader.visibility=View.GONE

                addCardAdapter=AddCardAdapter(it as ArrayList<GetCardData>, this, requireContext())
                binding.rvAddCard.adapter=addCardAdapter

            })

        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    firstCardFragmentEntrance=true
                    findNavController().popBackStack()
                }
            })


        binding.backToHome.setOnClickListener {
            firstCardFragmentEntrance=true
            findNavController().popBackStack()
        }

        binding.ivAddCardPlus.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_add_card_fragment,
                null,
                getNavOptions()
            )
        }
    }

    override fun onItemClick(card: GetCardData) {
       Log.i("card id ", ""+card.id+" "+card.cardNumber)

        val action=AddCardFragmentDirections.actionAddCardFragment2ToRenameCardsFragment(
            card.id!!,
            card.cardValidity!!,
            card.cardNumber!!,
            card.cardName!!,
            card.type!!
        )
        findNavController().navigate(action)
    }


}