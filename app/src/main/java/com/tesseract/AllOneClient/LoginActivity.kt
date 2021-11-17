package com.tesseract.AllOneClient

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.ActivityLoginBinding
import com.tesseract.AllOneClient.fragments.login.SplashFragment
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SaveData.loginUser(this@LoginActivity, true)

        if (SaveData.getLoginUser(applicationContext)){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashFragment) {
                statusBarColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        theme
                    ), ResourcesCompat.getColor(
                        resources,
                        R.color.green,
                        theme
                    ), false
                )
            } else {


            }


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp() || super.onSupportNavigateUp()
    }

//    override fun languageSetter(lan: String) {
//        when (lan) {
//            "Uzbek" -> {
//                setLocate("uz")
//            }
//            "English" -> {
//                setLocate("en")
//            }
//            else -> {
//                setLocate("ru")
//            }
//        }
//    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources?.updateConfiguration(config, this.resources.displayMetrics)
    }
}