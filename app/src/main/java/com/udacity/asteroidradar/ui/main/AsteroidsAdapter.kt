package com.udacity.asteroidradar.ui.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.model.Asteroid

class AsteroidsAdapter(
    private val onClickListener: OnClickListener
) :
    ListAdapter<Asteroid, AsteroidsAdapter.AsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean =
            oldItem === newItem


        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean =
            oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder =
        AsteroidViewHolder(
            AsteroidItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid = asteroid)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(asteroid)
        }
    }


    class AsteroidViewHolder(private val binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }
}

class OnClickListener(val clickListener: ((asteroid: Asteroid) -> Unit)) {
    fun onClick(asteroid: Asteroid){
        clickListener(asteroid)
    }
}