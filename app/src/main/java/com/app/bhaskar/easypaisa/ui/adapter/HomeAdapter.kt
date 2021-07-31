/**
 *Created by dhruv on 12-08-2019.
 */
package aepsapp.easypay.com.aepsandroid.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.model.HomeData
import kotlinx.android.synthetic.main.raw_item_home.view.*

/**
 *Created by dhruv on 12-08-2019.
 */


class HomeAdapter(var mContext: Context, var arrayList: ArrayList<HomeData>,
                  var itemClicked: (HomeData.Servi) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var servicesAdapter: ServiceHomeAdapter

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderPopulerBank(
            LayoutInflater.from(mContext).inflate(
                R.layout.raw_item_home,
                parent,
                false
            )
        )
    }

    inner class ViewHolderPopulerBank(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var arrayListService: ArrayList<HomeData.Servi> = ArrayList()
        private lateinit var layoutManager: GridLayoutManager
        private lateinit var serviceHomeAdapter: ServiceHomeAdapter

        fun bindView(arrayhomeData: ArrayList<HomeData.Servi>?) {
            arrayListService = arrayhomeData!!
            layoutManager = GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false)
            serviceHomeAdapter = ServiceHomeAdapter(mContext, arrayListService){
                itemClicked(it)
            }
            val viewPoolpopbank: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

            itemView.recyclerviewHomeItem.setRecycledViewPool(viewPoolpopbank)
            itemView.recyclerviewHomeItem.setHasFixedSize(false)
            itemView.recyclerviewHomeItem.layoutManager = layoutManager
            itemView.recyclerviewHomeItem.adapter = serviceHomeAdapter

            /*val recyclerviewPopBank = object : SearchBankActivity.RecyclerviewPopBank {
                override fun onPopularBankChange(state: Int) {
                    if (state == 0) {
                        itemView.tvPopulerBankLabel.visibility = View.GONE
                        itemView.recyclerviewPopulerBank.visibility = View.GONE
                    } else {
                        itemView.tvPopulerBankLabel.visibility = View.VISIBLE
                        itemView.recyclerviewPopulerBank.visibility = View.VISIBLE
                    }
                }
            }*/
            //(mContext as SearchBankActivity).setRecyclerviewPopBank(recyclerviewPopBank)

        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder: ViewHolderPopulerBank = holder as ViewHolderPopulerBank
        val arrayhomeData: ArrayList<HomeData.Servi>? = arrayList[position].servi
        holder.itemView.tvCategoryName.text = arrayList[position].categoryName
        mHolder.bindView(arrayhomeData)
    }

}