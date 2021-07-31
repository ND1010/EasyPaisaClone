/**
 *Created by dhruv on 12-08-2019.
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
import com.app.bhaskar.easypaisa.response_model.FingpayMiniStatementResponse
import com.app.bhaskar.easypaisa.utils.Utils
import kotlinx.android.synthetic.main.raw_mini_statement.view.*

/**
 *Created by dhruv on 12-08-2019.
 */
class MiniStatementAdapter(var mContext: Context, var arrayList: ArrayList<FingpayMiniStatementResponse.Statement>, var itemClicked: (FingpayMiniStatementResponse.Statement) -> Unit)
    : RecyclerView.Adapter<MiniStatementAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statement = arrayList[position]

        holder.itemView.tvLedgerName.text = statement.narration
        if (statement.txnType == "Cr"){
            holder.itemView.tvLedgerAmount.text = "+ ${Utils.formatAmount(statement.amount.toDouble())}"
            holder.itemView.tvLedgerAmount.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen))
        }else{
            holder.itemView.tvLedgerAmount.text = "- ${Utils.formatAmount(statement.amount.toDouble())}"
            holder.itemView.tvLedgerAmount.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed))
        }
        holder.itemView.tvLedgerDate.text = statement.date
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_mini_statement, parent, false))
    }

    fun setTransaction(arrayList: ArrayList<FingpayMiniStatementResponse.Statement>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}