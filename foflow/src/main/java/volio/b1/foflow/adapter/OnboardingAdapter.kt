package volio.b1.foflow.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import volio.b1.foflow.R
import volio.b1.foflow.adapter.OnboardingAdapter.AdsVH
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.utils.setPreventDoubleClick

class OnboardingAdapter(
    private val items: List<OnboardingItemModel>,
    private val onLoadAds: (ViewGroup) -> Unit,
    private val onNextPage: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NormalVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitleOnboarding: TextView = itemView.findViewById(R.id.tvTitleOnboarding)
        private val tvDescriptionOnboarding: TextView =
            itemView.findViewById(R.id.tvDescriptionOnboarding)
        private val imgOnboarding: ImageView = itemView.findViewById(R.id.imgOnboarding)
        fun bind(item: OnboardingItemModel) {

            Glide.with(itemView).load(item.pathImage).into(imgOnboarding)
            tvTitleOnboarding.text = itemView.context.getString(item.title)

            if (item.content != null) {
                tvDescriptionOnboarding.text = itemView.context.getString(item.content)
                tvDescriptionOnboarding.visibility = View.VISIBLE
            } else tvDescriptionOnboarding.visibility = View.GONE
        }
    }

    inner class AdsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutAds: FrameLayout = itemView.findViewById(R.id.layoutAds)
        private val btnCloseNative: ImageView? = itemView.findViewById(R.id.btnCloseNative)
        fun bind(item: OnboardingItemModel) {
            onLoadAds.invoke(layoutAds)
            btnCloseNative?.setPreventDoubleClick {
                onNextPage.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            OnboardingItemModel.TYPE_ADS -> {
                val view = inflater.inflate(R.layout.item_ads_full_onboarding, parent, false)
                AdsVH(view)
            }

            else -> {
                val view = inflater.inflate(R.layout.item_onboarding, parent, false)
                NormalVH(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]
        when (holder) {
            is NormalVH -> holder.bind(item)
            is AdsVH -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount() = items.size
}