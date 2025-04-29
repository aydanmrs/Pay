package com.example.pay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.pay.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var onboardingItems: List<OnboardingItem>

    // Geri qayıdarkən tətbiqi bağlamaq üçün callback
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish() // Tətbiqi bağla
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Geri qayıdarkən tətbiqi bağlamaq üçün callback-i əlavə et
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        onboardingItems = listOf(
            OnboardingItem(
                title = "Təzə məhsullar birbaşa fermerlərdən",
                description = "Platformamızda yerli fermerlərin yetişdirdiyi təbii məhsulları kəşf edin.",
                imageResId = R.drawable.slide_one
            ),
            OnboardingItem(
                title = "Təbiətin dadı bir toxunuş uzaqda",
                description = "Mobil tətbiqimizlə təbii məhsulları asanlıqla sifariş edin, evinizdə rahatlıqla qarşılayın.",
                imageResId = R.drawable.slide_two
            ),
            OnboardingItem(
                title = "Təhlükəsiz və sürətli çatdırılma",
                description = "Seçdiyiniz məhsullar fermerlərdən toplanır və qapınıza qədər çatdırılır.",
                imageResId = R.drawable.slide_three
            )
        )

        val adapter = OnboardingItemsAdapter(onboardingItems)
        binding.onBoardingViewPager.adapter = adapter

        setupIndicators()
        binding.onBoardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        binding.btnGo.setOnClickListener {
            val currentItem = binding.onBoardingViewPager.currentItem
            if (currentItem + 1 < onboardingItems.size) {
                binding.onBoardingViewPager.setCurrentItem(currentItem + 1, true)
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
            }
        }

        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
    }

    private fun setupIndicators() {
        val indicators = Array(onboardingItems.size) { ImageView(requireContext()) }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }
        indicators.forEach { indicator ->
            indicator.layoutParams = layoutParams
            binding.indicatorsContainer.addView(indicator)
        }
        updateIndicators(0)
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until binding.indicatorsContainer.childCount) {
            val indicator = binding.indicatorsContainer.getChildAt(i) as ImageView
            val drawableRes = if (i == position) {
                R.drawable.indicator_active_background
            } else {
                R.drawable.indicator_inactive_background
            }
            indicator.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableRes))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}