package volio.b1.foflow.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.R
import volio.b1.foflow.adapter.OnboardingAdapter.AdsVH
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.utils.setPreventDoubleClick

class OnboardingAdapter(
    val items: List<OnboardingItemModel>,
    val onLoadAds: (ViewGroup, OnboardingItemModel) -> Unit,
    val onNextPage: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NormalVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitleOnboarding: TextView? = itemView.findViewById(R.id.tvTitleOnboarding)
        private val tvDescriptionOnboarding: TextView? =
            itemView.findViewById(R.id.tvDescriptionOnboarding)
        private val imgOnboarding: ImageView? = itemView.findViewById(R.id.imgOnboarding)
        private val lotteOnboarding: LottieAnimationView? =
            itemView.findViewById(R.id.lotteOnboarding)

        fun bind(item: OnboardingItemModel) {

            val isJsonFile = item.pathImage.lowercase().endsWith(".json")
            if (isJsonFile) {
                lotteOnboarding?.setAnimation(item.pathImage)
                lotteOnboarding?.visibility = View.VISIBLE
                imgOnboarding?.visibility = View.GONE
            } else {
                imgOnboarding?.let { Glide.with(itemView).load(item.pathImage).into(it) }
                imgOnboarding?.visibility = View.VISIBLE
                lotteOnboarding?.visibility = View.GONE
            }

            tvTitleOnboarding?.text = itemView.context.getString(item.title)

            if (item.content != null) {
                tvDescriptionOnboarding?.text = itemView.context.getString(item.content)
                tvDescriptionOnboarding?.visibility = View.VISIBLE
            } else tvDescriptionOnboarding?.visibility = View.GONE
        }
    }

    inner class AdsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutAds: FrameLayout = itemView.findViewById(R.id.layoutAds)
        private val btnCloseNative: ImageView? = itemView.findViewById(R.id.btnCloseNative)
        private val tvNext: TextView? = itemView.findViewById(R.id.tvNext)
        private val tvGetStarted: TextView? = itemView.findViewById(R.id.tvGetStarted)
        fun bind(item: OnboardingItemModel) {
            onLoadAds.invoke(layoutAds, item)
            if (tvGetStarted != null) {
                if (position == items.size - 1) {
                    tvGetStarted.visibility = View.VISIBLE
                    tvNext?.visibility = View.GONE
                } else {
                    tvGetStarted.visibility = View.GONE
                    tvNext?.visibility = View.VISIBLE
                }
            }

            btnCloseNative?.setPreventDoubleClick {
                onNextPage.invoke()
            }
            tvNext?.setPreventDoubleClick {
                onNextPage.invoke()
            }
            tvGetStarted?.setPreventDoubleClick {
                onNextPage.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return if (isAdsLayout(viewType)) {
            AdsVH(view)
        } else {
            NormalVH(view)
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
        return items[position].layoutItem
    }

    private fun isAdsLayout(layoutItem: Int): Boolean {
        return items.any { it.layoutItem == layoutItem && it.type == OnboardingItemModel.TYPE_ADS }
    }

    override fun getItemCount() = items.size
}