package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.DashboardBinding
import java.util.Locale

class DashboardStep(private val goToDogDetails: (name: String) -> Unit) : Step<DashboardBinding>(DashboardBinding::inflate) {

  override fun onShow(context: Context, binding: DashboardBinding) {
    binding.catItems.adapter = DogListAdapter(context)
  }

  fun catDetailsSelected(name: String) {
    goToDogDetails(name)
  }

  enum class DogBreed {
    AKITA,
    BEAGLE,
    CHOW,
    MIX,
    LABRADOR,
    SHIBA,
    HUSKY,
    SHIHTZU;

    fun getName(): String {
      return name.replace("_", " ").toLowerCase(Locale.getDefault()).capitalize()
    }

    fun getBreedName(): String {
      return name.replace("_", " ").toLowerCase(Locale.getDefault())
    }
  }

  private inner class DogListAdapter(context: Context) : ArrayAdapter<DogBreed>(context, R.layout.dog_item, R.id.dogName, DogBreed.values()) {

    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      var view = convertView
      if (convertView == null) {
        view = View.inflate(context, R.layout.dog_item, null)
      }
      val dogDetail = getItem(position)!!
      val dogDetailTextView = view!!.findViewById<TextView>(R.id.dogName)
      dogDetailTextView.text = dogDetail.getName()
      view.setOnClickListener {
        catDetailsSelected(dogDetail.getBreedName())
      }
      return view
    }
  }
}
