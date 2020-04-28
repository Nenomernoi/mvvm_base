package by.nrstudio.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.nrstudio.mvvm.databinding.ItemBreedBinding
import by.nrstudio.mvvm.net.response.Breed

class BreedAdapter(private val listener: BreedListener) : ListAdapter<Breed, BreedAdapter.BreedViewHolder>(DiffUserCallback) {

	companion object DiffUserCallback : DiffUtil.ItemCallback<Breed>() {
		override fun areItemsTheSame(oldItem: Breed, newItem: Breed): Boolean {
			return oldItem === newItem
		}

		override fun areContentsTheSame(oldItem: Breed, newItem: Breed): Boolean {
			return oldItem.name == newItem.name
		}
	}

	class BreedViewHolder(private var binding: ItemBreedBinding) : RecyclerView.ViewHolder(binding.root) {

		companion object {
			fun from(parent: ViewGroup): BreedViewHolder {
				val layoutInflater = LayoutInflater.from(parent.context)
				val binding = ItemBreedBinding.inflate(layoutInflater, parent, false)
				return BreedViewHolder(binding)
			}
		}

		fun bind(
			breed: Breed,
			listener: BreedListener
		) {
			binding.breed = breed
			binding.executePendingBindings()
			binding.listener = listener
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
		return BreedViewHolder.from(parent)
	}

	override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
		val country = getItem(position)
		holder.bind(country, listener)
	}

}

class BreedListener(val clickListener: (model: Breed) -> Unit) {
	fun onClick(model: Breed) = clickListener(model)
}