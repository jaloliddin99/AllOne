package com.tesseract.AllOneClient.fragments.tourism.carRent.carView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.databinding.FragmentCarViewBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCarView : Fragment(), MedPhoneAdapter.OnClickListener {
    private val args: FragmentCarViewArgs by navArgs()

    private var _binding: FragmentCarViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CarViewViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CarViewViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isFavourite = false
        viewModel.getCarView(headerMapUniversal(requireContext()), args.carId)
        viewModel.getCarView.observe(viewLifecycleOwner, {

            binding.apply {
                backToHome.setOnClickListener {
                    findNavController().popBackStack()
                }

                isFavourite = it.content.is_favorite
                if (it.content.is_favorite) {
                    save.setImageResource(R.drawable.ic_saved)
                } else {
                    save.setImageResource(R.drawable.ic_savee)
                }
                name.text = it.content.name
                Picasso.get().load(it.content.logo).into(logo)
                Picasso.get().load(it.content.poster).into(poster)
                engineVolume.text = it.content.engine_volume
                fuelType.text = it.content.fuel_type
                color.text = it.content.color
                hasConditioner.text = it.content.has_conditioner
                transmisson.text = it.content.transmisson
                insurance.text = it.content.insurance
                price.text = it.content.price
                mortgagePrice.text = it.content.mortgage_price
                companyName.text = it.content.company_name
                companyTelegram.text = it.content.company_telegram
                companyWebsite.text = it.content.company_website
                companyAddr.text = it.content.company_addr
                companyWorkTime.text = it.content.company_work_time
                Picasso.get().load(it.content.company_poster).into(companyPoster)

                loader.loader.visibility=View.GONE


                recyclerViewPhones.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter =
                    MedPhoneAdapter(it.content.company_phone_number, this@FragmentCarView)


                if (it.content.company_closed) {

                } else {
                    companyClosedTitle.visibility = View.GONE
                }
            }
        })

        binding.save.setOnClickListener { someId ->
            try {
                if (!isFavourite) {
                    viewModel.carRentAddToFavourites(
                        headerMapUniversal(requireContext()),
                        args.carId
                    )
                    binding.loader.loader.visibility = View.VISIBLE
                }
            } catch (e: Exception) {

            }
        }

        viewModel.carRentRate.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility = View.GONE
            isFavourite = true
        })

        viewModel.rateError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

    override fun onChipClicked(position: String) {

    }


}