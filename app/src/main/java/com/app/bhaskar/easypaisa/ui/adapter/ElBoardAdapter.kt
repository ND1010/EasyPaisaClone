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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.raw_board.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class ElBoardAdapter(
    var mContext: Context,
    var arrayList: ArrayList<ElectricityBoardResponse.Message.Provider>,
    var itemClicked: (ElectricityBoardResponse.Message.Provider) -> Unit
) : RecyclerView.Adapter<ElBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_board, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board: ElectricityBoardResponse.Message.Provider = arrayList[position]
        holder.itemView.setOnClickListener {
            itemClicked(board)
        }
        holder.itemView.tvSelectBoard.text = board.name
        if (!board.logo.isNullOrEmpty()) {
            Glide.with(mContext)
                .load(board.logo)
                .apply(RequestOptions().fitCenter())
                .into(holder.itemView.ivBoardImage)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


}