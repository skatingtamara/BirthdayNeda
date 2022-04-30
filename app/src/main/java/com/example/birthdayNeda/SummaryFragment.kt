/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.birthdayNeda

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cupcake.R
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.birthdayNeda.model.OrderViewModel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Binding object instance corresponding to the fragment_summary.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSummaryBinding? = null

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root


    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            summaryFragment = this@SummaryFragment


            // ImageView from layout
            val ima : ImageView = view.findViewById(R.id.gif)
            // create AnimatedDrawable
            val decodedAnimation = ImageDecoder.decodeDrawable(
                // create ImageDecoder.Source object
                ImageDecoder.createSource(resources, R.drawable.lemon))
            // set the drawble as image source of ImageView
            ima.setImageDrawable(decodedAnimation)
            // play the animation
            (decodedAnimation as? AnimatedImageDrawable)?.start()


        }


/*          For a later solution...... a gif would be fun
        // trying to add a gif
        //setContentView(R.layout.fragment_summary)
        context?.let {
            val media = "https://giphy.com/embed/3ogwG1rvNPnl2DiWrK"

            Glide.with(this)
                .load(media)            //  Try with a png. @ tamara
                .into(view.findViewById(R.id.gif))
        }

*/

    }

    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    fun sendOrder() {
        // Construct the order summary text with information from the view model
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        val orderSummary = getString(
            R.string.gift_details,
            resources.getQuantityString(R.plurals.gifts, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        // Create an ACTION_SEND implicit intent with order details in the intent extras
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        // Check if there's an app that can handle this intent before launching it
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            // Start a new activity with the given intent (this may open the share dialog on a
            // device if multiple apps can handle this intent)
            startActivity(intent)
        }
    }



    fun sendOrdertoTamara() {
        // Construct the order summary text with information from the view model
        val numberOfGifts: Int = sharedViewModel.selectedGifts.count()


        val orderSummary = getString(
            R.string.gift_details,
            resources.getQuantityString(R.plurals.gifts, numberOfGifts, numberOfGifts),
            sharedViewModel.listOfGiftDescriptions,
            sharedViewModel.date.value.toString(),
            sharedViewModel.subtotal.value.toString()
        )

        // Create an ACTION_SEND implicit intent with order details in the intent extras
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        // Check if there's an app that can handle this intent before launching it
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            // Start a new activity with the given intent (this may open the share dialog on a
            // device if multiple apps can handle this intent)
            startActivity(intent)
        }
    }








    /**
     * Cancel the order and start over.
     */
    fun cancelOrder() {
        // Reset order in view model
        sharedViewModel.resetOrder()

        // Navigate back to the [StartFragment] to start over
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}