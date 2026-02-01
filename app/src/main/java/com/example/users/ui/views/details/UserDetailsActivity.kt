package com.example.users.ui.view.details

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.users.R
import com.example.users.databinding.ActivityUserDetailsBinding
import com.example.users.domain.models.User
import com.example.users.ui.states.UserDetailUiState
import com.example.users.ui.view.add.NewUserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels()

    private var userId: Long = -1
    private var currentUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        userId = intent.getLongExtra("USER_ID", -1)
        observeViewModel()

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.itoolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.user_details)
        }
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UserDetailUiState.Loading -> {
                            //Loader)
                        }

                        is UserDetailUiState.Success -> {
                            currentUser = state.user
                            bindUser(state.user)
                        }

                        is UserDetailUiState.Error -> {
                            Toast.makeText(
                                this@UserDetailsActivity,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }
    }

    private fun bindUser(user: User) = with(binding) {
        Glide.with(this@UserDetailsActivity)
            .load(user.photoResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(imgPhoto)

        tvName.text = "${user.name} ${user.lastname}"
        tvAge.text = getString(R.string.years, user.age)
        tvCompany.text = user.company
        tvWeight.text = "${user.weight} kg"
        tvEmail.text = user.email
        tvPhone.text = user.phone
        tvAddress.text = user.address


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_delete_user -> {
                currentUser?.let {
                    viewModel.delete(it)
                    finish()
                }
                true
            }

            R.id.action_edit_user -> {
                val intent = Intent(this, NewUserActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUser(userId)
    }


}