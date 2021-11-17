package com.tesseract.AllOneClient.dialogs.commonDialogs.driverRating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.dialogs.DriverRatingAdapter
import com.tesseract.AllOneClient.databinding.DialogCityRateDriverBinding
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPost
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDriverRating(private val orderType:String,private val  orderId:Int, private val driverId:Int, private val goToMainHome: GoToHomeListener): BottomSheetDialogFragment(),DriverRatingAdapter.MakeComplaint {
    private var binding: DialogCityRateDriverBinding?=null

    private lateinit var viewModel: DriverRatingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCityRateDriverBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(DriverRatingViewModel::class.java)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val lyout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet.height
            lyout.parent.requestLayout()
        }

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestDriverRating(headerMapUniversal(requireContext()), orderType)

        viewModel.driverRatingObserver.observe(viewLifecycleOwner, Observer {
            dialog!!.setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog
                val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                val lyout = bottomSheet!!.parent as CoordinatorLayout
                val behavior: BottomSheetBehavior<*> =
                    BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = bottomSheet.height
                lyout.parent.requestLayout()
            }
            binding?.apply {
                recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerView.adapter=DriverRatingAdapter(it.content, this@DialogDriverRating)
            }
        })

        binding?.apply {
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ -> ratingFromUser=rating.toInt() }
            send.setOnClickListener {
                review1= binding?.review?.text.toString()

                if (review1.isEmpty()){
                    return@setOnClickListener
                }else if (ratingFromUser==0){
                    return@setOnClickListener
                }
                val body=DriverRatingPost(
                    orderId,
                    ratingFromUser,
                    reasonFor,
                    review1
                )

                viewModel.driverRatingPost(headerMapUniversal(requireContext()), driverId, body)

            }

            viewModel.driverRatingPostObserver.observe(viewLifecycleOwner, {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
                goToMainHome.gotoHome()
            })
        }

    }

//    override fun getTheme(): Int {
//       // return R.style.AppBottomSheetDialogTheme
//    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

    interface GoToHomeListener{
        fun gotoHome()
    }
    private var ratingFromUser:Int=0
    private var reasonFor=""
    private var review1:String=""
    override fun onItemClick(reason: String) {
        reasonFor=reason
    }

}