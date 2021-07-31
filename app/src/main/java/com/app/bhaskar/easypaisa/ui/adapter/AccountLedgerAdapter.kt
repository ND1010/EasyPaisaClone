/**
 *Created by dhruv on 06-06-2019.
 */
package aepsapp.easypay.com.aepsandroid.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.AccountLedgerResponse
import com.app.bhaskar.easypaisa.response_model.TransactionResponse
import com.app.bhaskar.easypaisa.utils.Utils
import kotlinx.android.synthetic.main.layout_main_wallet.*
import kotlinx.android.synthetic.main.raw_txn_history.view.*
import java.text.DecimalFormat

/**
 *Created by dhruv on 06-06-2019.
 */
class AccountLedgerAdapter(
    var mContext: Context,
    var arrayList: ArrayList<AccountLedgerResponse.Data>,
    var itemClicked: (AccountLedgerResponse.Data) -> Unit
) : RecyclerView.Adapter<AccountLedgerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.raw_txn_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = arrayList[position]
        holder.itemView.tvHistoryTitle.text = "${history.name} - ${history.product}"
        holder.itemView.tvHistoryOrderId.text = if (history.number.isNotEmpty()) {
            holder.itemView.tvHistoryOrderId.visibility = View.VISIBLE
            history.number
        } else {
            holder.itemView.tvHistoryOrderId.visibility = View.GONE
            ""
        }
        holder.itemView.tvHistoryStatus.text = if (history.mode == "credit") "Cr" else "Dr"
        holder.itemView.tvHistoryDate.text = Utils.convertDateTimeAeps(history.updatedAt,"yyyy-MM-dd hh:mm:ss")//2020-11-25 21:44:51
        val decim = DecimalFormat("0.00")
        val amount = decim.format(history.amount)
        holder.itemView.tvHistoryAmount.text = Utils.formatAmount(amount.toDouble())

        holder.itemView.setOnClickListener {
            itemClicked(history)
        }

        when (history.mode) {
            "credit" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen))
            }
            "debit" -> {
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
            }
            else ->{
                holder.itemView.tvHistoryStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
                holder.itemView.tvHistoryAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorRed))
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setData(arrayList: ArrayList<AccountLedgerResponse.Data>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }
}