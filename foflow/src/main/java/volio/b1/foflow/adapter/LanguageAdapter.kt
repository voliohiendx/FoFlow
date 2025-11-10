package volio.b1.foflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import volio.b1.foflow.databinding.ItemLanguageBinding
import volio.b1.foflow.model.LanguageItemModel

class LanguageAdapter(
    val selected: Int,
    private val items: List<LanguageItemModel>,
    private val onClick: (LanguageItemModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.VH>() {

    private var selectedPosition: Int = selected

    inner class VH(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageItemModel, isSelected: Boolean, isDefault: Boolean) {
            binding.tvLanguage.text = item.nameLanguage
            binding.imvFlagLanguage.setImageResource(item.resFlagLanguage)

            // Gán biến cho DataBinding
            binding.isSelected = isSelected
            binding.tvDefault.visibility =
                if (isDefault) android.view.View.VISIBLE else android.view.View.GONE

            binding.root.setOnClickListener {
                val oldPos = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(oldPos)
                notifyItemChanged(selectedPosition)
                onClick(item)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position], position == selectedPosition, selected == position)

    override fun getItemCount() = items.size
}
