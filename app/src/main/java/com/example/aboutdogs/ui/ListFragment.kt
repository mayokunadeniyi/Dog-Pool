package com.example.aboutdogs.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutdogs.R
import com.example.aboutdogs.ui.adapter.DogListAdapter
import com.example.aboutdogs.viewmodel.listfragment.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogListAdapter =
        DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.refresh()
        dogs_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogListAdapter
        }
        observeViewModel()
        setupRefreshLayout()
    }

    private fun setupRefreshLayout() {
        refresh_layout.setOnRefreshListener {
            dogs_recyclerview.visibility = View.GONE
            list_error.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refresh_layout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer {dogs ->
            dogs?.let {
                dogs_recyclerview.visibility = View.VISIBLE
                dogListAdapter.updateDogList(dogs)
            }
        })

        viewModel.dogError.observe(this, Observer {
            it?.let {
                list_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            if (it){
                dogs_recyclerview.visibility = View.GONE
                list_error.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_id -> {
                val action = ListFragmentDirections.actionListFragmentToSettingsFragment()
                this.findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
