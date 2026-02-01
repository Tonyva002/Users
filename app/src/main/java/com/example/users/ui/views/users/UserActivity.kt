package com.example.users.ui.view.users

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.users.R
import com.example.users.databinding.ActivityUserBinding
import com.example.users.ui.adapters.UserAdapter
import com.example.users.ui.states.UserUiState
import com.example.users.ui.view.add.NewUserActivity
import com.example.users.ui.view.details.UserDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityUserBinding
    private lateinit var listAdapter: UserAdapter
    private lateinit var gridAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupRecyclerViews()
        observeViewModel()

    }


    private fun setupToolbar() {
        setSupportActionBar(binding.itoolbar.toolbar)
    }

    private fun setupRecyclerViews() {
        listAdapter = UserAdapter(isGrid = false) { user ->
            goToDetail(user.id)
        }

        gridAdapter = UserAdapter(isGrid = true) { user ->
            goToDetail(user.id)
        }

        binding.recyclerList.apply {
            layoutManager = LinearLayoutManager(this@UserActivity)
            adapter = listAdapter
        }

        binding.recyclerGrid.apply {
            layoutManager = GridLayoutManager(this@UserActivity, 2)
            adapter = gridAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UserUiState.Loading -> {
                            //Loader optional
                        }

                        is UserUiState.Success -> {
                            listAdapter.submitList(state.users)
                            gridAdapter.submitList(state.users)
                        }

                        is UserUiState.Error -> {
                            Toast.makeText(
                                this@UserActivity,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
        }

    }


    private fun goToDetail(userId: Long) {
        startActivity(
            Intent(this, UserDetailsActivity::class.java)
                .putExtra("USER_ID", userId)

        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        setupSearch(menu)
        setupSwitch(menu)
        return true
    }

    private fun setupSearch(menu: Menu?) {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_user)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }

        })

    }

    private fun setupSwitch(menu: Menu?) {
        val itemSwitch = menu?.findItem(R.id.action_switch)
        itemSwitch?.setActionView(R.layout.switch_item)

        val switchView = itemSwitch?.actionView?.findViewById<SwitchCompat>(R.id.change_view)

        switchView?.setOnCheckedChangeListener { _, _ ->
            binding.viewSwitcher.showNext()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, NewUserActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}