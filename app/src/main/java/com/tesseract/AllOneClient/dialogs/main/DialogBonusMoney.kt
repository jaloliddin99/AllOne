package com.tesseract.AllOneClient.dialogs.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogBonusForPostBinding

class DialogBonusMoney(
    private val bonusListener: OnBonusSelected,
    private val bonusAmountSent: String
) : DialogFragment(R.layout.dialog_bonus_for_post) {

    private var binding: DialogBonusForPostBinding? = null
    private var chosenBonusAmount: String = ""


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bonusForPostBinding = DialogBonusForPostBinding.bind(view)
        binding = bonusForPostBinding

        binding?.apply {
            maximumBOnus.text = bonusAmountSent + getString(R.string.summa1)
            cancelImage.setOnClickListener {
                bonusListener.bonusAmount("0.0")
                dialog?.dismiss()
            }
            cancelButton.setOnClickListener {
                if (bonusAmountSent.toFloat()>=1000f){
                    bonusListener.bonusAmount(chosenBonusAmount)
                }else{
                    bonusListener.bonusAmount("0.0")
                }
                dialog?.dismiss()
            }
            if (bonusAmountSent.toFloat()<1000f){
                textTitle.text=getString(R.string.your_bonus_is_not_enough)
                textTitle.setTextColor(requireContext().getColor(R.color.red))

            }else{
                chosenBonusAmount = (40f * (bonusAmountSent.toFloat() - 500f) / 100f + 500f).toString()
                bonusAmount.text = chosenBonusAmount
                seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        chosenBonusAmount = (progress.toFloat() * (bonusAmountSent.toFloat() - 500f) / 100f + 500f).toString()
                        bonusAmount.text = chosenBonusAmount
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                })
            }


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_bonus_for_post, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface OnBonusSelected {
        fun bonusAmount(bonus: String)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

}