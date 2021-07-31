/**
 *Created by dhruv on 06-06-2019.
 */
package aepsapp.easypay.com.aepsandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.ElectricityBoardResponse
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse
import com.app.bhaskar.easypaisa.response_model.TransactionFilterDataResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.raw_board.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class TransactionFilterAdapter(
    var mContext: Context,
    var arrayList: ArrayList<TransactionFilterDataResponse.History>,
    var itemClicked: (TransactionFilterDataResponse.History) -> Unit
) : RecyclerView.Adapter<TransactionFilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.raw_history_filter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter: TransactionFilterDataResponse.History = arrayList[position]
        holder.itemView.setOnClickListener {
            itemClicked(filter)
        }
        holder.itemView.tvSelectBoard.text = filter.name
        holder.itemView.ivBoardImage.visibility  = View.GONE
        /*if (filter.icon!=0) {
            Glide.with(mContext)
                .load(filter.icon)
                .apply(RequestOptions().circleCrop().fitCenter())
                .into(holder.itemView.ivBoardImage)
        }*/
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}