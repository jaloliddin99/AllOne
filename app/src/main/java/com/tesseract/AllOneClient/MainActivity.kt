package com.tesseract.AllOneClient

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.chuckerteam.chucker.api.Chucker
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.shreyaspatil.material.navigationview.MaterialNavigationView
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.ActivityMainBinding
import com.tesseract.AllOneClient.utils.changeCornerRadius
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Chucker.getLaunchIntent(this)
        startActivity(intent)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView=findViewById<NavigationView>(R.id.navView)
        val view:View=navView.getHeaderView(0)
        val profileName=view.findViewById<TextView>(R.id.profileName)
        val profileUserTel=view.findViewById<TextView>(R.id.textView_phoneNumber)

        val number = SaveData.getPhone1(this)
        profileName.text= SaveData.getName(this)
        profileUserTel.text= number


        binding.technicalSupport.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_fragmentTechnicalSupport)
            binding.drawerLayout.closeDrawers()
        }

        binding.logOut.setOnClickListener {
            SaveData.loginUser(this, false)
            finish()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNav.setupWithNavController(navController)



        binding.bottomNav.getOrCreateBadge(R.id.orderFragment).number = 3
        val radius = resources.getDimension(R.dimen.margin_padding_12dp)

        val bottomBarBackground = binding.bottomNav.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .build()

        languageConfig()

        binding.navView.setNavigationItemSelectedListener (object :NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
//                    R.id.orderHistory->{
//                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_orderFragment)
//                        binding.drawerLayout.closeDrawers()
//                    }
                    R.id.bonusCard->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_bonusFragment2)
                        binding.drawerLayout.closeDrawers()
                    }
                    R.id.paymentMethod->{

                    }
//                    R.id.charity->{
//                       // findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_charity)
//                        val navigationView=binding.bottomNav
//                        navigationView.menu.findItem(R.id.fragmentGoodMain).isChecked = true
//                        navigationView.menu.performIdentifierAction(R.id.fragmentGoodMain, 0)
//
//                        binding.drawerLayout.closeDrawers()
//                    }
                    R.id.language->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_changeLanguageFragment)
                        binding.drawerLayout.closeDrawers()
                    }
                    R.id.aboutProgram->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_aboutProgramFragment)
                        binding.drawerLayout.closeDrawers()
                    }


                }

                return true
            }

        })

        binding.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->

                when (destination.id) {
                    R.id.homeFragment -> {
                        menuItem?.setIcon(R.drawable.ic_bell_white)
                        showBottomNav()
                        menuItem?.isVisible = true
                    }
                    R.id.bonusFragment2 -> {
                        hideBottomNav()
                    }
                    R.id.changeLanguageFragment -> {
                        hideBottomNav()
                    }

                    R.id.profileFragment -> {
                        showBottomNav()
                        menuItem?.setIcon(R.drawable.ic_bell)
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                        menuItem?.isVisible = true
                    }
                    R.id.orderFragment -> {
                        menuItem?.setIcon(R.drawable.ic_bell)
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )

                        showBottomNav()
                        menuItem?.isVisible = true
                    }
                    R.id.fragmentTechnicalSupport -> {
                        hideBottomNav()
                    }
                    R.id.changeDataFragment -> {
                        hideBottomNav()
                    }
                    R.id.changePhoneFragment -> {
                        hideBottomNav()
                    }
                    R.id.aboutProgramFragment -> {
                        hideBottomNav()
                    }
                    R.id.fragmentActiveInterAreaOrder -> {
                        menuItem?.setIcon(R.drawable.ic_bell)
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )

                        hideBottomNav()
                    }
                    R.id.fragmentActiveParcelOrder -> {
                        menuItem?.setIcon(R.drawable.ic_bell)
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                        hideBottomNav()
                    }
                    R.id.fragmentOrderTaxi2 -> {
                        hideBottomNav()
                    }
                    R.id.fragmentTaxiRegions -> {
                        menuItem?.setIcon(R.drawable.ic_bell)
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )

                        hideBottomNav()
                    }

                    R.id.fragmentSearchDistrict -> {
                        hideBottomNav()
                    }


                    R.id.fragmentPayment -> {
                        hideBottomNav()
                    }
                    R.id.fragmentLocation -> {
                        hideBottomNav()

                    }
                    R.id.fragmentSearchCancelled -> {
                        hideBottomNav()
                    }
                    R.id.fragmentRegionDriverInfo -> {
                        hideBottomNav()
                    }
                    R.id.fragmentSearchTaxi -> {
                        hideBottomNav()
                    }
                    R.id.addCardFragment2 -> {
                        hideBottomNav()
                    }
                    R.id.renameCardsFragment -> {
                        hideBottomNav()
                    }
                    R.id.linkCardFragment -> {
                        hideBottomNav()
                    }
                    R.id.fragmentPostServiceSelection -> {
                        hideBottomNav()
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }

                    R.id.fragmentCityMap -> {
                        hideBottomNav()
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }

                    R.id.fragmentStations->{
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }
                    R.id.fragmentCityPaymentMethod->{
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }

                    R.id.fragmentContact->{
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }
                    R.id.fragmentMainClinic->{
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                        hideBottomNav()
                    }
                    R.id.fragmentTourismMain->{
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                        hideBottomNav()
                    }
                    R.id.fragmentOrderRegionAboutTrip->{
                        hideBottomNav()
                    }
                    R.id.fragmentOrderCityAboutTrip->{
                        hideBottomNav()
                    }
                    R.id.fragmentOrderParcelAboutTrip->{
                        hideBottomNav()
                    }
                    R.id.mapsFragment->{
                        hideBottomNav()
                    }
                    R.id.fragmentGoodMain->{
                        showBottomNav()
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }
                    R.id.fragmentDonationHistory->{
                        hideBottomNav()
                    }
                    R.id.fragmentMakeDonation->{
                        hideBottomNav()
                    }
                    R.id.fragmentNewsView->{
                        hideBottomNav()
                    }
                    R.id.fragmentOrderAboutDriver->{
                        hideBottomNav()
                    }
                    R.id.fragmentAllNews->{
                        hideBottomNav()
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }
                    R.id.chatFragment->{
                        hideBottomNav()
                        statusBarColor(
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            ResourcesCompat.getColor(resources, R.color.white, theme),
                            true
                        )
                    }
                }
            }
        }

        binding.bottomNav.setOnNavigationItemReselectedListener {}





    }

    private var menuItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.notification, menu)

        menuItem = menu?.findItem(R.id.notification)
        return true
    }

    private fun showBottomNav() {
        binding.run {
            bottomAppBar.performShow()
            bottomAppBar.visibility=View.VISIBLE

        }
    }

    private fun hideBottomNav() {
        binding.run {
            bottomAppBar.performHide()
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter(){
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    // Hide the BottomAppBar to avoid it showing above the keyboard
                    // when composing a new email.
                    bottomAppBar.visibility = View.GONE
                }
                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
        menuItem?.isVisible = false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun languageConfig(){
        when {
            SaveData.getEnglish(this) -> {
                setLocale(this, "en")
            }
            SaveData.getUzbek(this) -> {
                setLocale(this, "uz")
            }
            SaveData.getRussian(this) -> {
                setLocale(this, "ru")
            }
        }
    }
    private fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}