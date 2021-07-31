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
import com.app.bhaskar.easypaisa.response_model.MobileRechargePlanResponse
import com.app.bhaskar.easypaisa.utils.Utils
import kotlinx.android.synthetic.main.raw_recharge_plans.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class MobileRechargePlanAdapter(
        var mContext: Context,
        var arrayList: ArrayList<MobileRechargePlanResponse.Plan>,
        var itemClicked: (MobileRechargePlanResponse.Plan) -> Unit
) : RecyclerView.Adapter<MobileRechargePlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_recharge_plans, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan: MobileRechargePlanResponse.Plan = arrayList[position]
        holder.itemView.tvPlanAmount.text = Utils.formatAmount(plan.amount.toDouble())
        holder.itemView.tvPlanDetail.text = plan.detail
        holder.itemView.tvValidity.text = plan.validity
        holder.itemView.tvTalktime.text = if(plan.talktime.isNotEmpty() && plan.talktime != "NA") Utils.formatAmount(plan.talktime.toDouble()) else "NA"
        holder.itemView.btnSelectPlan.setOnClickListener {
            itemClicked(plan)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}