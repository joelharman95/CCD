package com.nis.ccd.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nis.ccd.R
import kotlinx.android.synthetic.main.fragment_coupon_dialog.*

typealias onSelection = (String) -> Unit

class PromoBottomSheetDialog(
    private val title: String,
    private val onSelection: onSelection
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coupon_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = title
        btnProceed.setOnClickListener {
            onSelection.invoke(etAddress.text.toString())
            dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }

    }

    companion object {
        private val TAG = PromoBottomSheetDialog.javaClass.simpleName
        fun showPromoBottomSheet(
            fragmentManager: FragmentManager,
            title: String,
            onSelection: onSelection
        ) {
            PromoBottomSheetDialog(title, onSelection).show(fragmentManager, TAG)
        }
    }
}