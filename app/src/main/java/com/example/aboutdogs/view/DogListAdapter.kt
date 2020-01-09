package com.example.aboutdogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutdogs.R
import com.example.aboutdogs.model.DogBreed
import kotlinx.android.synthetic.main.fragment_detail.view.*
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
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog_name.text = dogList[position].dogBreed
        holder.view.dog_lifespan.text = dogList[position].lifeSpan
        holder.view.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToDetailFragment().setStuff(position)
            Navigation.findNavController(it).navigate(action)
        }
    }


    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view)
}