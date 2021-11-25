package com.tesseract.AllOneClient.fragments.tourism.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.tourism.filter.CountryAdapter
import com.tesseract.AllOneClient.adapter.tourism.filter.CurrenciesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismFilterBinding
import com.tesseract.AllOneClient.fragments.main.home.routeTariffs.FragmentTaxiRegionsSelectionDirections
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.model.tourism.countries.TourismCountries
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTourismFIlter : Fragment(), CountryAdapter.OnItemClickListener,
    CurrenciesAdapter.OnChipClickListener{
    private var _binding: FragmentTourismFilterBinding? = null
    private val binding get() = _binding!!
    private val args: FragmentTourismFIlterArgs by navArgs()
    private lateinit var viewModel: TourismFilterViewModel
    private lateinit var adapter: CountryAdapter

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTourismFilterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TourismFilterViewModel::class.java)
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


        if (args.purpose == 1) {
            binding.apply {
                loader.loader.visibility = View.GONE
                val array: Array<String> = resources.getStringArray(R.array.sort_array)
                val list: List<String> = array.toList()

                val list2=ArrayList<Content>()
                for (i in list.indices){
                    list2.add(Content(1, list[i]))
                }

                recyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    this@FragmentTourismFIlter.adapter =
                        CountryAdapter(list2, this@FragmentTourismFIlter)
                    adapter = this@FragmentTourismFIlter.adapter

                }
            }
        }
        if (args.purpose == 2) {
            viewModel.getAllCountries(headerMapUniversal(requireContext()))
            binding.searchView.visibility = View.VISIBLE
            binding.apply {
                loader.loader.visibility = View.GONE
                title.text = getString(R.string.country_selection)
                viewModel.getAllCountries.observe(viewLifecycleOwner, {
                    recyclerView.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        this@FragmentTourismFIlter.adapter =
                            CountryAdapter(it.content, this@FragmentTourismFIlter)
                        adapter = this@FragmentTourismFIlter.adapter

                    }
                })
            }
            viewModel.countriesError.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility = View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
        }
        if (args.purpose == 3) {
            viewModel.getAllCurrencies(headerMapUniversal(requireContext()))
            binding.apply {
                loader.loader.visibility = View.GONE
                viewModel.getAllCurrencies.observe(viewLifecycleOwner, {
                    title.text = getString(R.string.country_selection)
                    recyclerView.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = CurrenciesAdapter(this@FragmentTourismFIlter, it.content)
                    }
                })
            }
            viewModel.currencyError.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility = View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
        }

        if (args.purpose == 10) {
            binding.apply {
                headerText.visibility = View.VISIBLE
                line.visibility = View.VISIBLE
                viewModel.getCarCompanies(headerMapUniversal(requireContext()))
                title.text = getString(R.string.car_companies)
                viewModel.getCarCompanies.observe(viewLifecycleOwner, {
                    loader.loader.visibility = View.GONE
                    recyclerView.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        this@FragmentTourismFIlter.adapter =
                            CountryAdapter(it.content, this@FragmentTourismFIlter)
                        adapter = this@FragmentTourismFIlter.adapter
                    }
                })
                viewModel.errorCarCompanies.observe(viewLifecycleOwner, {
                    binding.loader.loader.visibility = View.GONE
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })


            }
        }

        if (args.purpose == 11) {
            binding.apply {
                headerText.visibility = View.VISIBLE
                line.visibility = View.VISIBLE

                viewModel.getCarModels(headerMapUniversal(requireContext()))
                title.text = getString(R.string.car_companies)
                viewModel.getCarModels.observe(viewLifecycleOwner, {
                    loader.loader.visibility = View.GONE
                    recyclerView.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        this@FragmentTourismFIlter.adapter =
                            CountryAdapter(it.content, this@FragmentTourismFIlter)
                        adapter = this@FragmentTourismFIlter.adapter
                    }
                })
                viewModel.errorCarModels.observe(viewLifecycleOwner, {
                    binding.loader.loader.visibility = View.GONE
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
            }
        }

        if (args.purpose == 12) {
            binding.apply {
                headerText.visibility = View.VISIBLE
                line.visibility = View.VISIBLE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

    }

    override fun onItemClick(searchItemBinding: Content) {
        if (args.purpose == 1) {
            setBackStackData("onSortClickKey", searchItemBinding.name, true)
        }
        if (args.purpose == 2) {
            setBackStackData("onCountrySelectKey", searchItemBinding, true)
        }

        if (args.purpose == 10) {
            setBackStackData("onCarCompaniesSelected", searchItemBinding, true)
        }

        if (args.purpose == 11) {
            setBackStackData("onCarModelsSelected", searchItemBinding, true)
        }

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