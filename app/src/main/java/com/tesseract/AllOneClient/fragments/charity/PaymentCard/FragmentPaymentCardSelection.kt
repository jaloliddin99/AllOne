package com.tesseract.AllOneClient.fragments.charity.PaymentCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentMakePaymentCardSelectionBinding
import com.tesseract.AllOneClient.model.charity.projectCards.Card
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentPaymentCardSelection:Fragment() {
    val args: FragmentPaymentCardSelectionArgs by navArgs()
    private lateinit var viewModel: SelectCardViewModel
    private lateinit var cards:List<Card>
    private lateinit var titleCard:String
    private lateinit var imageCard:String
    private var projectId by Delegates.notNull<Int>()
    private var _binding:FragmentMakePaymentCardSelectionBinding?=null
    private val binding get() = _binding!!
    private var upDownCounter=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentMakePaymentCardSelectionBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(SelectCardViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gerCreditCards(headerMapUniversal(requireContext()), args.id)
        binding.loader.loader.visibility=View.VISIBLE
        viewModel.errorM.observe(viewLifecycleOwner,{
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.creditCard.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            titleCard=it.title
            imageCard=it.image
            projectId=it.id
            binding.title.text=it.title
            cards=it.cards
            binding.cardNumber.text=it.cards[0].card_number
            binding.cardholderName.text=it.cards[0].cardholder_name
            if (it.cards[0].type=="humo"){
                binding.type.setImageResource(R.drawable.humo)
            }else if (it.cards[0].type=="uzcard"){
                binding.type.setImageResource(R.drawable.uzcard)
            }
            Picasso.get().load(it.image).into(binding.image)
        })

        binding.up.setOnClickListener {
            if (upDownCounter>0){
                upDownCounter--
                binding.cardNumber.text=cards[upDownCounter].card_number
                binding.cardholderName.text=cards[upDownCounter].cardholder_name
                if (cards[upDownCounter].type=="humo"){
                    binding.type.setImageResource(R.drawable.humo)
                }else if (cards[upDownCounter].type=="uzcard"){
                    binding.type.setImageResource(R.drawable.uzcard)
                }
            }
        }
        binding.down.setOnClickListener {
            if (upDownCounter<cards.size-1){
                upDownCounter++
                binding.cardNumber.text=cards[upDownCounter].card_number
                binding.cardholderName.text=cards[upDownCounter].cardholder_name
                if (cards[upDownCounter].type=="humo"){
                    binding.type.setImageResource(R.drawable.humo)
                }else if (cards[upDownCounter].type=="uzcard"){
                    binding.type.setImageResource(R.drawable.uzcard)
                }
            }
        }
        charityAmount()
    }

    private fun charityAmount(){
        binding.apply {
            signIn.setOnClickListener {
                val action=FragmentPaymentCardSelectionDirections.actionFragmentPaymentCardSelectionToFragmentChooseYourCars(
                    cards[upDownCounter].type,
                    cards[upDownCounter].card_number,
                    cards[upDownCounter].cardholder_name,
                    titleCard,
                    imageCard,
                    binding.charityAmount.text.toString(),
                    cards[upDownCounter].id,
                    projectId
                )
                findNavController().navigate(action)
            }
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}