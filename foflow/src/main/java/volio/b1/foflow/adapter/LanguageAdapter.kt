package volio.b1.foflow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.R
import volio.b1.foflow.model.LanguageItemModel

class LanguageAdapter(
    val selected: Int,
    private val items: List<LanguageItemModel>,
    private val onClick: (LanguageItemModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.VH>() {

    private var selectedPosition: Int = selected

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLanguageSelect: TextView? = itemView.findViewById(R.id.tvLanguageSelect)
        private val tvLanguage: TextView? = itemView.findViewById(R.id.tvLanguage)
        private val imvFlagLanguage: ImageView? = itemView.findViewById(R.id.imvFlagLanguage)
        private val imvSelect: ImageView? = itemView.findViewById(R.id.imvSelect)
        private val imvUnSelect: ImageView? = itemView.findViewById(R.id.imvUnSelect)
        private val viewUnSelect: View? = itemView.findViewById(R.id.viewUnSelect)
        private val viewSelect: View? = itemView.findViewById(R.id.viewSelect)
        private val tvDefault: TextView? = itemView.findViewById(R.id.tvDefault)

        fun bind(item: LanguageItemModel, isSelected: Boolean, isDefault: Boolean) {
            tvLanguageSelect?.text = item.nameLanguage
            tvLanguage?.text = item.nameLanguage
            tvLanguageSelect?.visibility = if (isSelected) View.VISIBLE else View.GONE
            imvSelect?.visibility = if (isSelected) View.VISIBLE else View.GONE
            imvUnSelect?.visibility = if (isSelected) View.GONE else View.VISIBLE
            if (tvLanguageSelect != null) tvLanguage?.visibility =
                if (isSelected) View.GONE else View.VISIBLE
            viewSelect?.visibility = if (isSelected) View.VISIBLE else View.GONE
            viewUnSelect?.visibility = if (isSelected) View.GONE else View.VISIBLE
            tvDefault?.visibility = if (isDefault) View.VISIBLE else View.GONE
            imvFlagLanguage?.setImageResource(item.resFlagLanguage)

            itemView.setOnClickListener {
                val oldPos = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(oldPos)
                notifyItemChanged(selectedPosition)
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutId =  FOFlowManager.config.language.itemLanguageLayoutRes
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position], position == selectedPosition, selected == position)

    override fun getItemCount() = items.size
}