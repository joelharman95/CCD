package com.nis.ccd.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nis.ccd.R
import kotlinx.android.synthetic.main.fragment_address_dialog.*

class AddressBottomSheetDialog(
    private val title: String,
    private val address: String,
    private val onSelection: onSelection
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_address_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = title
        etAddress.setText(address)
        btnProceed.setOnClickListener {
            if (etAddress.text.toString().isEmpty()) {
                etAddress.error = "Address needed"
                return@setOnClickListener
            }
            onSelection.invoke(etAddress.text.toString())
            dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }

    }

    companion object {
        private val TAG = AddressBottomSheetDialog.javaClass.simpleName
        fun showAddressBottomSheet(
            fragmentManager: FragmentManager,
            title: String,
            address: String,
            onSelection: onSelection
        ) {
            AddressBottomSheetDialog(title, address, onSelection).show(fragmentManager, TAG)
        }
    }
}