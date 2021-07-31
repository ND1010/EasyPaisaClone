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
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import kotlinx.android.synthetic.main.raw_bank.view.*

/**
 *Created by dhruv on 06-06-2019.
 */
class AadharBankAdapter(
    var mContext: Context,
    var arrayList: ArrayList<UserRequiredDataResponse.Aadharbank>,
    var itemClicked: (UserRequiredDataResponse.Aadharbank) -> Unit
) : RecyclerView.Adapter<AadharBankAdapter.ViewHolder>(), Filterable {

    private var mFilterList: ArrayList<UserRequiredDataResponse.Aadharbank>

    init {
        mFilterList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.raw_bank, parent, false))
    }

    override fun getItemCount(): Int {
        return mFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bank: UserRequiredDataResponse.Aadharbank = mFilterList[position]
        holder.itemView.tvBankName.text = bank.bankName
        holder.itemView.setOnClickListener {
            itemClicked(bank)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setBank(arrayList: ArrayList<UserRequiredDataResponse.Aadharbank>) {
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
                    val filteredList = arrayList.filter {
                        it.bankName.toLowerCase().contains(charString)
                    } as ArrayList<UserRequiredDataResponse.Aadharbank>

                    if (filteredList.isEmpty()) {
                        val bankDetail = UserRequiredDataResponse.Aadharbank()
                        bankDetail.bankName = "No bank available"
                        filteredList.add(bankDetail)
                        mFilterList = filteredList
                    } else {
                        mFilterList = filteredList
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = mFilterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilterList = filterResults.values as ArrayList<UserRequiredDataResponse.Aadharbank>
                notifyDataSetChanged()
            }
        }
    }
}