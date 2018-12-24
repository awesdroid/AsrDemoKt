package io.awesdroid.asrdemokt.ui

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter


class FragmentPageAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        when (position) {
            HOME_PAGE -> return HomePage()
            CHAT_PAGE -> return ChatPage()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return PAGES_NUMBER
    }

    companion object {
        internal val HOME_PAGE = 0
        internal val CHAT_PAGE = 1
        internal val PAGES_NUMBER = 2
    }
}
