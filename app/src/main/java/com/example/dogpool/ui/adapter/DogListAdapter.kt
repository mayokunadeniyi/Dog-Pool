package com.example.dogpool.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogpool.databinding.ItemDogBinding
import com.example.dogpool.model.DogBreed
import com.example.dogpool.ui.ListFragmentDirections
import kotlinx.android.synthetic.main.item_dog.view.*
import java.util.ArrayList

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class DogListAdapter(val dogList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogList: List<DogBreed>) {
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemDogBinding.inflate(layoutInflater)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog = dogList[position]
        holder.view.listener = this
    }

    override fun onDogClicked(v: View) {
        val dogUid = v.dogUid.text.toString().toInt()
        val action =
            ListFragmentDirections.actionListFragmentToDetailFragment().setDogUid(dogUid)
        Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}