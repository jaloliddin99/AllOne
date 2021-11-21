package com.tesseract.AllOneClient.fragments.tourism.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.filter.CountryAdapter
import com.tesseract.AllOneClient.adapter.tourism.filter.CurrenciesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismFilterBinding
import com.tesseract.AllOneClient.fragments.main.home.routeTariffs.FragmentTaxiRegionsSelectionDirections
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTourismFIlter:Fragment(), CountryAdapter.OnItemClickListener , CurrenciesAdapter.OnChipClickListener{
    private var _binding:FragmentTourismFilterBinding?=null
    private val binding get() = _binding!!
    private val args:FragmentTourismFIlterArgs by navArgs()
    private lateinit var viewModel: TourismFilterViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentTourismFilterBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(TourismFilterViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setBackStackData("cancelledInfo", 1, true)
                }
            })

        binding.cancel.setOnClickListener {
            setBackStackData("cancelledInfo", 1, true)
        }


        if (args.purpose==1){

        }
        if (args.purpose==2){
            viewModel.getAllCountries(headerMapUniversal(requireContext()))
            binding.apply {
                loader.loader.visibility=View.GONE
                viewModel.getAllCountries.observe(viewLifecycleOwner, {
                    title.text=getString(R.string.country_selection)
                    recyclerView.apply {
                        layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter= CountryAdapter(it.content, this@FragmentTourismFIlter)
                    }
                })
            }
            viewModel.countriesError.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility=View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
        }
        if (args.purpose==3){
            viewModel.getAllCurrencies(headerMapUniversal(requireContext()))
            binding.apply {
                loader.loader.visibility=View.GONE
                viewModel.getAllCurrencies.observe(viewLifecycleOwner, {
                    title.text=getString(R.string.country_selection)
                    recyclerView.apply {
                        layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter= CurrenciesAdapter(this@FragmentTourismFIlter, it.content)
                    }
                })
            }
            viewModel.currencyError.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility=View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
        }


    }

    override fun onItemClick(searchItemBinding: Content) {
        setBackStackData("onCountrySelectKey", searchItemBinding, true)

    }

    override fun onChipClicked(position: Content) {
        setBackStackData("onCurrencySelectedKey", position, true)
    }



    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

}