package net.khirr.android.privacypolicy

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.net_khirr_dialog_privacy_policy_item.view.*

class PrivacyPoliciesAdapter(private val textColor: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PrivacyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(textColor: Int, number: Int, body: String) = with(itemView) {
            this.numberTextView.text = ("$number.")
            this.bodyTextView.text = body
            this.numberTextView.setTextColor(textColor)
            this.bodyTextView.setTextColor(textColor)
        }
    }

    private val items = ArrayList<String>()
    override fun getItemCount(): Int = items.size

    fun updateDataSet(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.net_khirr_dialog_privacy_policy_item, parent, false)
        return PrivacyItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PrivacyItemViewHolder -> holder.bind(textColor,position + 1, items[position])
        }
    }

}