package com.tesseract.AllOneClient.fragments.charity.ChooseYourCard

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.charity.SelectCardToDonate
import com.tesseract.AllOneClient.constants.SaveData.formatCard
import com.tesseract.AllOneClient.constants.SaveData.formatPhone
import com.tesseract.AllOneClient.databinding.FragmentGoodChooseYourCardBinding
import com.tesseract.AllOneClient.fragments.profile.addcard.getCards.GetCardViewModel
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.RecyclerView




@AndroidEntryPoint
class FragmentChooseYourCars: Fragment(),SelectCardToDonate.OnItemClickListener {

    private lateinit var binding: FragmentGoodChooseYourCardBinding
    val args:FragmentChooseYourCarsArgs by navArgs()
    private lateinit var addCardAdapter: SelectCardToDonate
    private lateinit var getCardData: List<GetCardData>
    private var clientCardId :Int=0
    private lateinit var viewModel: GetCardViewModel
    private lateinit var viewModelDonate:DonateViewModel
    lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGoodChooseYourCardBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this).get(GetCardViewModel::class.java)
        viewModelDonate=ViewModelProvider(this).get(DonateViewModel::class.java)
        return binding.root

    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCardDataList(headerMapUniversal(requireContext()))

        loader()

        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }

            Picasso.get().load(args.image).into(image)
            title.text=args.title
            if (args.type=="humo"){
                type.setImageResource(R.drawable.humo)
            }else if (args.type=="uzcard"){
                type.setImageResource(R.drawable.uzcard)
            }
            cardNumber.text=formatCard(args.cardNumber)
            cardholderName.text=args.placeholderName
            amount.text= formatPhone(args.donationAmount)+" "+getString(R.string.summa1)



            addCard.setOnClickListener {
                findNavController().navigate(FragmentChooseYourCarsDirections.actionGlobalAddCardFragment())
            }

            recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            signIn.setOnClickListener {
                dialog.show()
               viewModelDonate.donate(headerMapUniversal(requireContext()),args.projectId,  clientCardId, args.charityProjectCardId, args.donationAmount.toDouble())
            }

            viewModelDonate.donateSuccess.observe(viewLifecycleOwner, {
                dialog.dismiss()
                successfullyPaid.animate().alpha(1f).duration=500
            })

            returnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            history.setOnClickListener {
                findNavController().navigate(FragmentChooseYourCarsDirections.actionFragmentChooseYourCarsToFragmentDonationHistory())
            }

        }

        viewModel.cardDataList.observe(requireActivity(),  {
            dialog.dismiss()
            getCardData=it
            addCardAdapter= SelectCardToDonate(it as ArrayList<GetCardData>, this, requireContext())
            binding.recyclerView.adapter=addCardAdapter

            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = getCurrentItem()
                        clientCardId= getCardData[position].id!!

                    }
                }
            })

            binding.recyclerView.onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        })

    }

    private fun loader(){
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onItemClick(card: GetCardData) {

    }

    fun hasPreview(): Boolean {
        return getCurrentItem() > 0
    }

    operator fun hasNext(): Boolean {
        return binding.recyclerView.adapter != null &&
                getCurrentItem() < binding.recyclerView.adapter!!.itemCount - 1
    }

    fun preview() {
        val position = getCurrentItem()
        if (position > 0) setCurrentItem(position - 1, true)
    }

    operator fun next() {
        val adapter: RecyclerView.Adapter<*> = binding.recyclerView.adapter ?: return
        val position = getCurrentItem()
        val count = adapter.itemCount
        if (position < count - 1) setCurrentItem(position + 1, true)
    }

    private fun getCurrentItem(): Int {
        return (binding.recyclerView.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    private fun setCurrentItem(position: Int, smooth: Boolean) {
        if (smooth) binding.recyclerView.smoothScrollToPosition(position) else binding.recyclerView.scrollToPosition(
            position
        )
    }

}