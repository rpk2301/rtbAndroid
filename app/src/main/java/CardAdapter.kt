package com.example.ridethebus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.ivCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        val resourceId = holder.itemView.context.resources.getIdentifier(card.getImageResourceName(), "drawable", holder.itemView.context.packageName)
        if (resourceId != 0) {
            holder.cardImage.setImageResource(resourceId)
        }
    }

    override fun getItemCount() = cards.size
}
