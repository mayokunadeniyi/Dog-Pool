package com.example.aboutdogs.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.aboutdogs.R
import com.example.aboutdogs.databinding.FragmentDetailBinding
import com.example.aboutdogs.model.DogPalette
import com.example.aboutdogs.utils.getProgressDrawable
import com.example.aboutdogs.utils.loadImage
import com.example.aboutdogs.viewmodel.detailfragment.DetailViewModel
import com.example.aboutdogs.viewmodel.detailfragment.DetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentDetailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val args by navArgs<DetailFragmentArgs>()

        viewModel = ViewModelProviders.of(this,DetailViewModelFactory(args.dogUid,application))
            .get(DetailViewModel::class.java)

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: DetailViewModel) {
        viewModel.dogBreed.observe(this, Observer {
            it?.let {
                binding.dog = it
                setBackgroundColor(it.imageUrl)
            }
        })

        viewModel.smsStarted.observe(this, Observer {
            if (it){
                (activity as MainActivity).checkSmsPermission()
            }
        })
    }

    private fun setBackgroundColor(imageUrl: String?) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                  Palette.from(resource)
                      .generate { palette ->
                          val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                          val dogPalette = DogPalette(intColor)
                          binding.dogPalette = dogPalette
                      }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_send -> {
                viewModel.startSms()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean){

    }


}
