package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.ImageGetterHelper
import kotlinx.android.synthetic.main.fragment_description_shannon_fano.*

class DescriptionShannonFanoFragment : Fragment() {

    companion object {
        fun newInstance(): DescriptionShannonFanoFragment {
            return DescriptionShannonFanoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_description_shannon_fano, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageGetterHelper = ImageGetterHelper(context?: throw NullPointerException("context is null"), 0.3f, 0.3f)
        val text = Html.fromHtml(resources.getString(R.string.html_description_shannon_fano), imageGetterHelper, null)

        text_description.text = text
    }
}
