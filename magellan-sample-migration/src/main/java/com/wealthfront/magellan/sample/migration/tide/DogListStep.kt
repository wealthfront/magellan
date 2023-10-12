package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.R
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.databinding.DashboardBinding
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class DogListStep(private val goToDogDetails: (name: String) -> Unit) :
  Step<DashboardBinding>(DashboardBinding::inflate) {

  @Inject lateinit var toolbarHelper: ToolbarHelper
  @Inject lateinit var api: DogApi

  override fun onCreate(context: Context) {
    (context.applicationContext as AppComponentContainer).injector().inject(this)
  }

  override fun onShow(context: Context, binding: DashboardBinding) {
    toolbarHelper.setTitle(context.getText(R.string.app_name))
    binding.dogItems.layoutManager = LinearLayoutManager(context, VERTICAL, false)
    binding.dogItems.adapter = DogListAdapter(goToDogDetails)
    val decoration = DividerItemDecoration(context, VERTICAL)
    binding.dogItems.addItemDecoration(decoration)

    binding.dogItemsLoading.visibility = View.VISIBLE
    shownScope.launch {
      val dogBreedsResponse = runCatching { api.getAllBreeds() }
      dogBreedsResponse.onSuccess { dogBreeds ->
        (binding.dogItems.adapter as DogListAdapter).dataSet = dogBreeds.message.keys.toTypedArray()
        (binding.dogItems.adapter as DogListAdapter).notifyDataSetChanged()
      }
      dogBreedsResponse.onFailure { throwable ->
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
      }
      binding.dogItemsLoading.visibility = View.GONE
    }
  }

  private class DogListAdapter(private val onDogSelected: (String) -> Unit) :
    RecyclerView.Adapter<DogListAdapter.ViewHolder>() {

    var dataSet: Array<String> = emptyArray()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val textView: TextView = view.findViewById(R.id.dogName)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
      val view = LayoutInflater.from(viewGroup.context)
        .inflate(R.layout.dog_item, viewGroup, false)

      return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
      viewHolder.textView.text = dataSet[position]
      viewHolder.itemView.setOnClickListener {
        onDogSelected(dataSet[position])
      }
    }

    override fun getItemCount() = dataSet.size
  }
}
