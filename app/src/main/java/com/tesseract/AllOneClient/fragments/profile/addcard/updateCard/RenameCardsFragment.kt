package com.tesseract.AllOneClient.fragments.profile.addcard.updateCard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentRenameCardsBinding
import com.tesseract.AllOneClient.dialogs.profile.DialogDeleteCard
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RenameCardsFragment : Fragment(), DialogDeleteCard.DeleteListener {

    val args: RenameCardsFragmentArgs by navArgs()
    var _fragmentRenameCardsBinding:FragmentRenameCardsBinding?=null

    val fragmentRenameCardsBinding get() = _fragmentRenameCardsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentRenameCardsBinding= FragmentRenameCardsBinding.inflate(inflater, container, false)

        return fragmentRenameCardsBinding.root
    }

    private lateinit var viewModel: UpdateCardViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this).get(UpdateCardViewModel::class.java)


        fragmentRenameCardsBinding.apply {

            loader.loader.visibility=View.GONE

            save.setOnClickListener {
                if (notes.text.toString().isEmpty()){
                    return@setOnClickListener
                }else{
                    fragmentRenameCardsBinding.loader.loader.visibility=View.VISIBLE
                    viewModel.updateCard(headerMapUniversal(requireContext()), args.cardId, notes.text.toString())
                }
            }

            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }

            cardNum.text=SaveData.formatCard(args.cardNum)
            expDate.text=SaveData.formatExpDate(args.cardExp)

            notes.hint = args.cardName

            delete.setOnClickListener{

                Log.i("card id ", ""+args.cardId)
                DialogDeleteCard(
                    args.cardId,
                    args.cardName,
                    args.cardExp,
                    args.cardNum,
                    this@RenameCardsFragment
                ).show(
                    parentFragmentManager,
                    tag
                )
            }

        }

        viewModel.successM.observe(requireActivity(), Observer {
            fragmentRenameCardsBinding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.errorM.observe(requireActivity(), Observer {
            fragmentRenameCardsBinding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }



    override fun deleted() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentRenameCardsBinding=null
    }

}