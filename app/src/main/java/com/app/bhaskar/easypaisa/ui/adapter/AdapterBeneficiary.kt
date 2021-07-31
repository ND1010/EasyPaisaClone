/**
 *Created by dhruv on 06-06-2019.
 */
package aepsapp.easypay.com.aepsandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import kotlinx.android.synthetic.main.raw_beneficiary.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class AdapterBeneficiary(
    var mContext: Context,
    var arrayList: ArrayList<SearchRemitterResponse.Userdata.Benelist>,
    var itemClicked: (SearchRemitterResponse.Userdata.Benelist) -> Unit
) : RecyclerView.Adapter<AdapterBeneficiary.ViewHolder>(), Filterable {

    private var mFilterList: ArrayList<SearchRemitterResponse.Userdata.Benelist>

    init {
        mFilterList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_beneficiary, parent, false))
    }

    override fun getItemCount(): Int {
        return mFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bank: SearchRemitterResponse.Userdata.Benelist = mFilterList[position]
        holder.itemView.tv_bene_name.text = bank.benename
        holder.itemView.tv_bene_account.text = bank.beneaccount
        holder.itemView.tv_bene_ifsc.text = bank.beneifsc
        holder.itemView.tv_bank_name.text = bank.benebank

        holder.itemView.setOnClickListener {
            itemClicked(bank)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setBank(arrayList: ArrayList<SearchRemitterResponse.Userdata.Benelist>) {
        this.arrayList = arrayList
        this.mFilterList = arrayList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString().toLowerCase()
                if (charString.isEmpty()) {
                    mFilterList = arrayList
                } else {
                    /*val filteredList = arrayList.filter {
                        it.bankName.toLowerCase().contains(charString)
                    } as ArrayList<com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse.Aepsbank>

                    if (filteredList.isEmpty()) {
                        val bankDetail = com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse.Aepsbank()
                        bankDetail.bankName = "No bank available"
                        filteredList.add(bankDetail)
                        mFilterList = filteredList
                    } else {
                        mFilterList = filteredList
                    }*/
                }

                val filterResults = FilterResults()
                filterResults.values = mFilterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilterList = filterResults.values as ArrayList<SearchRemitterResponse.Userdata.Benelist>
                notifyDataSetChanged()
            }
        }
    }
}