package com.app.bhaskar.easypaisa.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.bhaskar.easypaisa.ui.fragment.AccountFragment
import com.app.bhaskar.easypaisa.ui.fragment.HomeFragment
import com.app.bhaskar.easypaisa.ui.fragment.TransactionFragment
import com.app.bhaskar.easypaisa.ui.fragment.WalletFragment

class BottomNavigationAdapter(
    val mContext: Context,
    var fragmentManager: FragmentManager,
    var arrFragment: ArrayList<Fragment>
) : FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getItem(position: Int): Fragment {
        return arrFragment[position]
    }

    override fun getCount(): Int {
        return arrFragment.size
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is HomeFragment) {
            return super.getItemPosition(`object`)
        }else if (`object` is AccountFragment) {
            return super.getItemPosition(`object`)
        }
        return POSITION_NONE
    }
}