package volio.b1.foflow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import volio.b1.foflow.R
import volio.b1.foflow.model.OnboardingItemModel

class OnboardingAdapter(
    private val items: List<OnboardingItemModel>,
) : RecyclerView.Adapter<OnboardingAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutId = R.layout.item_onboarding
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}