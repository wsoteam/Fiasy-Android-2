package com.wsoteam.diet.presentation.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiaryFragment : Fragment() {
  protected val container: RecyclerView by lazy { RecyclerView(requireContext()) }

  override fun onCreateView(inflater: LayoutInflater,
    parent: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    container.layoutManager = LinearLayoutManager(context)
    container.adapter = WidgetsAdapter()
    return container
  }


}
