package com.nis.ccd.ui.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nis.ccd.R
import com.nis.ccd.utils.loadPic
import kotlinx.android.synthetic.main.layout_offer_item.view.*

class OfferAdapter(private val offerList: List<String>) :
    RecyclerView.Adapter<OfferAdapter.OfferHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
        return OfferHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_offer_item, parent, false)
        );
    }

    override fun onBindViewHolder(holder: OfferHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount() = offerList.size

    inner class OfferHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                ivOffer.loadPic(offerList[position])
            }
        }
    }
}