package com.example.dogpool.ui

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dogpool.R
import com.example.dogpool.databinding.FragmentDetailBinding
import com.example.dogpool.databinding.SendSmsLayoutBinding
import com.example.dogpool.model.DogBreed
import com.example.dogpool.model.DogPalette
import com.example.dogpool.model.SmsInfo
import com.example.dogpool.viewmodel.detailfragment.DetailViewModel
import com.example.dogpool.viewmodel.detailfragment.DetailViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: DetailViewModel
    lateinit var dog: DogBreed
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

        viewModel = ViewModelProviders.of(this, DetailViewModelFactory(args.dogUid, application))
                .get(DetailViewModel::class.java)

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: DetailViewModel) {
        viewModel.dogBreed.observe(this, Observer {
            it?.let {
                dog = it
                binding.dog = it
                setBackgroundColor(it.imageUrl)
            }
        })

        viewModel.smsStarted.observe(this, Observer {
            if (it) {
                (activity as MainActivity).checkSmsPermission()
            }
        })
    }

    private fun setBackgroundColor(imageUrl: String?) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
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
        inflater.inflate(R.menu.detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> {
                viewModel.startSms()
            }

            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this Dog breed")
                intent.putExtra(Intent.EXTRA_TEXT,"${dog.dogBreed} bred for ${dog.bredFor}")
                intent.putExtra(Intent.EXTRA_STREAM,dog.imageUrl)
                startActivity(Intent.createChooser(intent,"Share with"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if (permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo("", "${dog.dogBreed} bred for ${dog.bredFor}", dog.imageUrl)
                val dialogBinding = DataBindingUtil.inflate<SendSmsLayoutBinding>(
                        LayoutInflater.from(it),
                        R.layout.send_sms_layout,
                        null,
                        false
                )
                dialogBinding.smsInfo = smsInfo

                val alertDialog = AlertDialog.Builder(it)
                        .setView(dialogBinding. root)
                        .show()

                dialogBinding.sendSmsId.setOnClickListener {
                    if (!dialogBinding.smsDestination.text.isNullOrEmpty()){
                        smsInfo.to = dialogBinding.smsDestination.text.toString()
                        sendSMS(smsInfo)
                        viewModel.doneSendingSms()
                        Toast.makeText(context,"Message Sent!",Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                }

                dialogBinding.cancelId.setOnClickListener {
                    alertDialog.dismiss()
                }
            }
        }

    }

    private fun sendSMS(smsInfo: SmsInfo){
        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to,null,smsInfo.text,pendingIntent,null)
    }

}
