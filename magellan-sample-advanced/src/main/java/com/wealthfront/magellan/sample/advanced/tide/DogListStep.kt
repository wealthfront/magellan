package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.DashboardBinding
import com.wealthfront.magellan.sample.advanced.toolbar.ToolbarHelper
import java.util.Locale

class DogListStep(private val goToDogDetails: (name: String) -> Unit) : Step<DashboardBinding>(DashboardBinding::inflate) {

  override fun onStart(context: Context, binding: DashboardBinding) {
    ToolbarHelper.setTitle(context.getText(R.string.app_name))
    binding.dogItems.adapter = DogListAdapter(context)
  }

  fun onDogSelected(name: String) {
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
        onDogSelected(dogDetail.getBreedName())
      }
      return view
    }
  }
}
