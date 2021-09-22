package com.wealthfront.magellan.sample.advanced.cerealcollection

import android.content.Context
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.cerealcollection.CerealStatus.*
import com.wealthfront.magellan.sample.advanced.databinding.BrowseCollectionBinding
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.transitions.NoAnimationTransition

class BrowseCollectionJourney : Journey<BrowseCollectionBinding>(
  BrowseCollectionBinding::inflate,
  BrowseCollectionBinding::cerealDetailContainer
) {

  val monsterCerealDetails =
    CerealDetails(R.string.monster_cereals_title, R.string.monster_cereals_description, LIMITED)

  override fun onCreate(context: Context) {
    super.onCreate(context)
    navigator.goTo(CerealDetailStep(monsterCerealDetails), NoAnimationTransition())
  }

  override fun onShow(context: Context, binding: BrowseCollectionBinding) {
    binding.monsterCereals.isEnabled = false
    binding.monsterCereals.setOnClickListener {
      enableMenuChoices(binding)
      binding.monsterCereals.isEnabled = false

      navigator.replace(CerealDetailStep(monsterCerealDetails), NoAnimationTransition())
    }

    binding.cornflakes.setOnClickListener {
      enableMenuChoices(binding)
      binding.cornflakes.isEnabled = false

      val cerealDetails = CerealDetails(
        R.string.cornflakes_title,
        R.string.cornflakes_description,
        ACTIVE
      )
      binding.monsterCereals.isEnabled = false
      navigator.replace(CerealDetailStep(cerealDetails), NoAnimationTransition())
    }

    binding.dunkABalls.setOnClickListener {
      enableMenuChoices(binding)
      binding.dunkABalls.isEnabled = false

      val cerealDetails = CerealDetails(
        R.string.dunk_a_balls_title,
        R.string.dunk_a_balls_description,
        DISCONTINUED
      )
      navigator.replace(CerealDetailStep(cerealDetails), NoAnimationTransition())
    }

    binding.oreoOs.setOnClickListener {
      enableMenuChoices(binding)
      binding.oreoOs.isEnabled = false

      val cerealDetails = CerealDetails(
        R.string.oreo_os_title,
        R.string.oreo_os_description,
        ACTIVE
      )
      navigator.replace(CerealDetailStep(cerealDetails), NoAnimationTransition())
    }
  }

  private fun enableMenuChoices(binding: BrowseCollectionBinding) {
    setOf(
      binding.monsterCereals,
      binding.cornflakes,
      binding.dunkABalls,
      binding.oreoOs
    ).forEach { button ->
      button.isEnabled = true
    }
  }
}