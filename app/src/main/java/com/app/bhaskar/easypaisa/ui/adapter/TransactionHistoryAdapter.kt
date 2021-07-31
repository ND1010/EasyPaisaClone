/**
 *Created by dhruv on 06-06-2019.
 */
package aepsapp.easypay.com.aepsandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.TransactionResponse
import com.app.bhaskar.easypaisa.utils.Utils
import kotlinx.android.synthetic.main.raw_txn_history.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class TransactionHistoryAdapter(
    var mContext: Context,
    var arrayList: ArrayList<TransactionResponse.Data>,
    var itemClicked: (TransactionResponse.Data) -> Unit
) : RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.raw_txn_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = arrayList[position]
        holder.itemView.tvHistoryTitle.text = history.apiname
        holder.itemView.tvHistoryOrderId.text = history.txnid
        holder.itemView.tvHistoryStatus.text = history.status
        holder.itemView.tvHistoryDate.text = history.createdAt
        holder.itemView.tvHistoryAmount.text = Utils.formatAmount(history.amount)
        if (history.apiname == "AM-RECH" && history.number.isNotEmpty()){
            holder.itemView.grpMobile.visibility = View.VISIBLE
            holder.itemView.tvHistoryMobile.text = history.number
        }else{
            holder.itemView.grpMobile.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            itemClicked(history)
        }


        holder.itemView.tvHistoryStatus.text = history.status
        when (history.status) {
            "success" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
            }

            "failed" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
            }
            "pending" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorOrangeDark))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorOrangeDark))
            }
            "accept" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setHistiry(arrayList: ArrayList<TransactionResponse.Data>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }
}