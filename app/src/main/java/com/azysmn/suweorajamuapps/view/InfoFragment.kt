package com.azysmn.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azysmn.suweorajamuapps.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }
}
