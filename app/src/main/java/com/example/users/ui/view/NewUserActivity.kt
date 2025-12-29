package com.example.users.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.users.ui.state.UserUiState
import com.example.users.R
import com.example.users.databinding.ActivityNewUserBinding
import com.example.users.domain.models.User
import com.example.users.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewUserBinding
    private val viewModel: UserViewModel by viewModels()

    private var userId: Long? = null
    private var isEditMode = false
    private var indexPhoto = 0

    private var isUserLoaded = false

    private val photos = arrayOf(
        R.drawable.photo_01,
        R.drawable.photo_02,
        R.drawable.photo_03,
        R.drawable.photo_04,
        R.drawable.photo_05,
        R.drawable.photo_06,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readArgs()
        setupToolbar()
        setupListeners()

        if (isEditMode) observeUser()

    }

    // -------------------------
    // Setup
    // -------------------------
    private fun readArgs() {
        userId = intent.extras?.getLong("USER_ID")
        isEditMode = userId != null
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.itoolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isEditMode)
                getString(R.string.update_user)
            else
                getString(R.string.add_user)
        }
    }

    private fun setupListeners() {
        binding.btnChangePhoto.setOnClickListener {
            selectPhoto()
        }
    }

    // -------------------------
    // Menu
    // -------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_new -> {
                saveUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    // -------------------------
    // Logic
    // -------------------------

    private fun saveUser() {
        val user = buildUser() ?: return
        if (isEditMode) viewModel.update(user) else viewModel.add(user)
        finish()
    }

    private fun buildUser(): User? {

        val name = binding.tvName.text.toString()
        val lastname = binding.tvLastname.text.toString()
        val email = binding.tvEmail.text.toString()

        val age = binding.tvAge.text.toString().toIntOrNull()
        val weight = binding.tvWeight.text.toString().toDoubleOrNull()

        if (
            name.isBlank() ||
            lastname.isBlank() ||
            email.isBlank() ||
            age == null ||
            weight == null
        ) {
            Toast.makeText(
                this,
                getString(R.string.invalid_data),
                Toast.LENGTH_SHORT
            ).show()

            return null
        }

        return User(
            id = userId ?: 0L,
            name = name,
            lastname = lastname,
            company = binding.tvCompany.text.toString(),
            age = age,
            email = email,
            phone = binding.tvPhone.text.toString(),
            weight = weight,
            address = binding.tvAddress.text.toString(),
            photoResId = photos[indexPhoto],
            website = "",
            isFavorite = false
        )
    }

    // -------------------------
    // Edit mode
    // -------------------------

    private fun observeUser() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state is UserUiState.Success && !isUserLoaded) {
                        state.users
                            .firstOrNull { it.id == userId }
                            ?.let {
                                fillUser(it)
                                isUserLoaded = true
                            }
                    }
                }
            }
        }
    }


    private fun fillUser(user: User) {
        binding.apply {
            tvName.setText(user.name)
            tvLastname.setText(user.lastname)
            tvCompany.setText(user.company)
            tvAge.setText(user.age.toString())
            tvWeight.setText(user.weight.toString())
            tvEmail.setText(user.email)
            tvPhone.setText(user.phone)
            tvAddress.setText(user.address)
            imgPhoto.setImageResource(user.photoResId)
            indexPhoto = photos.indexOf(user.photoResId).coerceAtLeast(0)

        }
    }


    // Photo selector
    private fun selectPhoto() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_image))
            .setItems(arrayOf(
                "Foto 1",
                "Foto 2",
                "Foto 3",
                "Foto 4",
                "Foto 5",
                "Foto 6",
            )
            ) { _, which ->
                indexPhoto = which
                binding.imgPhoto.setImageResource(photos[indexPhoto])
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

}


