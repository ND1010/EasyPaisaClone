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
import com.app.bhaskar.easypaisa.model.HomeData
import kotlinx.android.synthetic.main.raw_home_service.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class ServiceHomeAdapter(
    var mContext: Context,
    var arrayList: ArrayList<HomeData.Servi>,
    var itemClicked: (HomeData.Servi) -> Unit
) :
    RecyclerView.Adapter<ServiceHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.raw_home_service,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setvice: HomeData.Servi = arrayList[position]
        holder.itemView.tvServiceName.text = setvice.serviceName
        holder.itemView.ivServiceIcon.setImageResource(setvice.serviceIcon)

        holder.itemView.setOnClickListener {
            itemClicked(setvice)
        }


    }

    fun setBank(arrListOtherBank: java.util.ArrayList<HomeData.Servi>) {
        arrayList = arrListOtherBank
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}