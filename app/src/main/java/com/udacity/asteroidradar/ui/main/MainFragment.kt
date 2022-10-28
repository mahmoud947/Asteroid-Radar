package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.util.ConnectionState
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.ui.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(appContext = requireContext().applicationContext)
        )[MainViewModel::class.java]
    }
    private lateinit var navController: NavController
    private val asteroidsAdapter = AsteroidsAdapter(
        OnClickListener {
            viewModel.onNavigateToDetail(it)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        navController = findNavController()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
            asteroidsAdapter.submitList(asteroids)
        })

        binding.asteroidRecycler.apply {
            adapter = asteroidsAdapter
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                onNavigate(it)
                viewModel.onNavigateComplete()
            }
        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer { connectionState ->
            connectionState.let {
                when (it) {
                    is ConnectionState.Connected -> {
                        showSnackBar(it.message!!)
                        viewModel.onUpdateConnectionStateCompleted()
                    }
                    is ConnectionState.Disconnected -> {
                        showSnackBar(it.message!!)
                        viewModel.onUpdateConnectionStateCompleted()

                    }
                }
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.saved_menu -> AsteroidsFilter.SAVED
                R.id.week_menu -> AsteroidsFilter.WEEK
                else -> AsteroidsFilter.TODAY
            }
        )
        return true
    }

    private fun onNavigate(asteroid: Asteroid) {
        navController.navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    private fun showSnackBar(message:String){
        Snackbar.make(requireActivity().findViewById(R.id.nav_host_fragment),message,Snackbar.LENGTH_SHORT).show()
    }

}
