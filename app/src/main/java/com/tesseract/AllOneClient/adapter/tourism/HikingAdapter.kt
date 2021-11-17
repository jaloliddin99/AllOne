package com.tesseract.AllOneClient.adapter.tourism

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutHikingItemBinding
import com.tesseract.AllOneClient.model.tourism.HikingModel
import com.tesseract.AllOneClient.model.tourism.ImageModel

class HikingAdapter(
    private val context: Context,
    private val hikingList: List<HikingModel>,
    private val listener: OnItemClicked
)
    : RecyclerView.Adapter<HikingAdapter.HikingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HikingViewHolder {
        val binding=
            LayoutHikingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HikingViewHolder(binding)

    }

    override fun onBindViewHolder(holder: HikingViewHolder, position: Int) {
        val newsItem : HikingModel =hikingList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=hikingList.size

    inner class HikingViewHolder(private val itemBinding: LayoutHikingItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(newsItemBinding: HikingModel) {
            itemBinding.placeName.text=newsItemBinding.placeName
            itemBinding.destinationName.text=newsItemBinding.destinationName
            itemBinding.mony.text=newsItemBinding.money
            itemBinding.numberOfPeople.text=newsItemBinding.numberOfPeople
            itemBinding.transport.text=newsItemBinding.transport
            itemBinding.time.text=newsItemBinding.time



            itemBinding.viewPager.clipToPadding = false
            itemBinding.viewPager.adapter =
                HikingViewPagerAdapter(context, getImage())
            itemBinding.viewPager.pageMargin = 48

            itemBinding.indicator.setViewPager(itemBinding.viewPager)

            itemBinding.viewPager.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })


        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (position!= RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }
    fun getImage(): ArrayList<ImageModel> {
        return arrayListOf(
            ImageModel(R.drawable.mountain_stones),
            ImageModel(R.drawable.mountain_stones),
            ImageModel(R.drawable.mountain_stones),
        )
    }

    interface OnItemClicked{
        fun onItemClick(position: Int)
    }
}