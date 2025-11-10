package volio.b1.foflow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import volio.b1.foflow.R
import volio.b1.foflow.model.LanguageItemModel

class LanguageAdapter(
    val selected: Int,
    private val items: List<LanguageItemModel>,
    private val onClick: (LanguageItemModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.VH>() {

    private var selectedPosition: Int = selected

    inner class VH(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageItemModel, isSelected: Boolean, isDefault: Boolean) {
            // Truy cập trực tiếp view con
            val tvLanguage = binding.root.findViewById<TextView>(R.id.tvLanguage)
            val imvFlagLanguage = binding.root.findViewById<ImageView>(R.id.imvFlagLanguage)
            val tvDefault = binding.root.findViewById<TextView>(R.id.tvDefault)

            tvLanguage.text = item.nameLanguage
            imvFlagLanguage.setImageResource(item.resFlagLanguage)
            tvDefault?.visibility = if (isDefault) View.VISIBLE else View.GONE

            val isSelectedId = binding.root.context.resources.getIdentifier(
                "isSelected", "id", binding.root.context.packageName
            )

            binding.setVariable(isSelectedId, isSelected)

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
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.item_language, parent, false)
        val binding = androidx.databinding.DataBindingUtil.bind<ViewDataBinding>(root)!!
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position], position == selectedPosition, selected == position)

    override fun getItemCount() = items.size
}
