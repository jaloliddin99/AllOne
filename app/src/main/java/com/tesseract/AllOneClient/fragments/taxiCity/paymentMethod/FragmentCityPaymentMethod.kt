package com.tesseract.AllOneClient.fragments.taxiCity.paymentMethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.charity.SelectCardToDonate
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentCityPaymentMethodBinding
import com.tesseract.AllOneClient.dialogs.main.DialogBonusMoney
import com.tesseract.AllOneClient.fragments.profile.addcard.getCards.GetCardViewModel
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import com.tesseract.AllOneClient.model.taxiCity.CityShareCardBonusModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCityPaymentMethod : Fragment(), DialogBonusMoney.OnBonusSelected, SelectCardToDonate.OnItemClickListener{
    private var _binding: FragmentCityPaymentMethodBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel2: GetCardViewModel
    private lateinit var addCardAdapter: SelectCardToDonate
    private lateinit var getCardData: List<GetCardData>
    private  var paymentType="cash"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCityPaymentMethodBinding.inflate(inflater, container, false)
        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel2 = ViewModelProvider(this).get(GetCardViewModel::class.java)
        viewModel2.getCardDataList(headerMapUniversal(requireContext()))

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }


        radioController()
        setupCards()

    }

    private lateinit var getCardData1: GetCardData
    private fun setupCards(){
        viewModel2.cardDataList.observe(requireActivity(),  {
            getCardData=it
            addCardAdapter= SelectCardToDonate(it as ArrayList<GetCardData>, this, requireContext())

            binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter=addCardAdapter

            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = getCurrentItem()

                        getCardData1=getCardData[position]

                    }
                }
            })

            binding.recyclerView.onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(binding.recyclerView)

            binding.linearLayout.visibility = View.GONE

        })
    }

    private fun radioController() {
        binding.apply {
            withCardRadio.setOnClickListener {
                paymentType="card"
                withCardRadio.isChecked = true
                withCashRadio.isChecked = false
                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cashCard.strokeColor = context?.getColor(R.color.grey)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cardCard.strokeColor = context?.getColor(R.color.green)!!
                cardCard.invalidate()
            }
            withCash.setOnClickListener {
                paymentType="cash"
                withCardRadio.isChecked = false
                withCashRadio.isChecked = true

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cashCard.strokeColor = context?.getColor(R.color.green)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cardCard.strokeColor = context?.getColor(R.color.grey)!!
                cardCard.invalidate()

            }
            withCard.setOnClickListener {
                paymentType="card"
                withCardRadio.isChecked = true
                withCashRadio.isChecked = false

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cashCard.strokeColor = context?.getColor(R.color.grey)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cardCard.strokeColor = context?.getColor(R.color.green)!!
                cardCard.invalidate()
            }

            withCashRadio.setOnClickListener {
                paymentType="cash"
                withCardRadio.isChecked = false
                withCashRadio.isChecked = true

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cashCard.strokeColor = context?.getColor(R.color.green)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cardCard.strokeColor = context?.getColor(R.color.grey)!!
                cardCard.invalidate()
            }

            withCardRadio.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    linearLayout.visibility = View.VISIBLE
                } else {
                    linearLayout.visibility = View.GONE
                }
            }

            addNewCard.setOnClickListener {
                val action=FragmentCityPaymentMethodDirections.actionGlobalAddCardFragment()
                findNavController().navigate(action)

            }

            bonusAmount.setOnClickListener {
                if (bonusAmount.isChecked){
                    DialogBonusMoney(this@FragmentCityPaymentMethod, SaveData.getBalance(requireContext())!!).show(
                        parentFragmentManager,
                        tag
                    )
                }
            }

            confirm.setOnClickListener {
                val citySHareCardBonusModel=CityShareCardBonusModel(
                    getCardData1.type!!,
                    getCardData1.cardNumber!!,
                    getCardData1.id!!,
                    this@FragmentCityPaymentMethod.bonusAmount,
                    paymentType,

                )
                setBackStackData("FragmentCityPaymentMethod", citySHareCardBonusModel, true)
            }
        }

    }

    private var bonusAmount="0.0"
    override fun bonusAmount(bonus: String) {
        bonusAmount=bonus
    }

    private fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    private fun getCurrentItem(): Int {
        return (binding.recyclerView.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    override fun onItemClick(card: GetCardData) {

    }

}