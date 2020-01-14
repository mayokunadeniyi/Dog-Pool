package com.example.aboutdogs.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.aboutdogs.R
import com.example.aboutdogs.databinding.FragmentDetailBinding
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val args by navArgs<DetailFragmentArgs>()
        val viewModel by viewModels<DetailViewModel>{
            DetailViewModelFactory(args.dogUid, application)
        }
        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: DetailViewModel) {
        viewModel.dogBreed.observe(this, Observer {
            it?.let {
                binding.dog = it
            }
        })
    }


}
