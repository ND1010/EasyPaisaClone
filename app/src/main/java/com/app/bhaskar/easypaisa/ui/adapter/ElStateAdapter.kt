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
import com.app.bhaskar.easypaisa.response_model.EelectricityStateResponse
import kotlinx.android.synthetic.main.raw_states.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class ElStateAdapter(
    var mContext: Context,
    var arrayList: ArrayList<EelectricityStateResponse.Message.State>,
    var itemClicked: (EelectricityStateResponse.Message.State) -> Unit
) : RecyclerView.Adapter<ElStateAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_states, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state: EelectricityStateResponse.Message.State = arrayList[position]
        holder.itemView.setOnClickListener {
            itemClicked(state)
        }
        holder.itemView.tvSelectState.text = state.state
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}