/**
 *Created by dhruv on 27-03-2020.
 */
package aepsapp.easypay.com.aepsandroid.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*

/**
 *Created by dhruv on 27-03-2020.
 */
class SliderImageAdapter(val mContext: Context, var arrayBannerImageList: ArrayList<UserRequiredDataResponse.Slides>, var clickBanner: (UserRequiredDataResponse.Slides) -> Unit) : SliderViewAdapter<SliderImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val inflate: View = LayoutInflater.from(mContext).inflate(R.layout.image_slider_layout_item, null)
        return ViewHolder(inflate)
    }

    override fun getCount(): Int {
        return arrayBannerImageList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {

        val bannersData = arrayBannerImageList[position]
        Glide.with(viewHolder!!.itemView)
                .load("https://retail.easypaisa.in/public/${bannersData.value}")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        viewHolder.itemView.progressLoadImageBanner.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        viewHolder.itemView.progressLoadImageBanner.visibility = View.GONE
                        return false
                    }
                })
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(viewHolder.itemView.iv_auto_image_slider)

        viewHolder.itemView.setOnClickListener {
            //clickBanner(arrayBannerImageList[position])
        }
    }

    class ViewHolder(var itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

    }

}