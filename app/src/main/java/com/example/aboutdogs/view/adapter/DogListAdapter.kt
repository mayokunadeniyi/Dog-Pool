package com.example.aboutdogs.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutdogs.R
import com.example.aboutdogs.model.DogBreed
import com.example.aboutdogs.utils.getProgressDrawable
import com.example.aboutdogs.utils.loadImage
import com.example.aboutdogs.view.ListFragmentDirections
import kotlinx.android.synthetic.main.item_dog.view.*
import java.util.ArrayList

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class DogListAdapter(val dogList: ArrayList<DogBreed>): RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    fun updateDogList(newDogList: List<DogBreed>){
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_dog,parent,false)
        return DogViewHolder(
            view
        )
    }

    override fun getItemCount() = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val currentDogBreed = dogList[position]
        holder.view.dog_name.text = currentDogBreed.dogBreed
        holder.view.dog_lifespan.text = currentDogBreed.lifeSpan

        holder.view.dog_image.loadImage(currentDogBreed.imageUrl,
            getProgressDrawable(holder.view.dog_image.context))

        holder.view.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToDetailFragment()
                    .setStuff(position)
            Navigation.findNavController(it).navigate(action)
        }
    }


    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view)
}