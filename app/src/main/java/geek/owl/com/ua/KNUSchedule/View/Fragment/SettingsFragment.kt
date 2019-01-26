package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.*
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SettingsAdapter
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import kotlinx.android.synthetic.main.settings_fragment_layout.*

class SettingsFragment : BaseFragment(), SharedPreferences.OnSharedPreferenceChangeListener, OnItemClick {
    override fun onClick(item: SimpleAdapter.ItemModel) {
        if(item.getType() == ItemType.LINK_SETTING.ordinal){
           item as LinkSetting
                findNavController().navigate(R.id.action_settingsFragment_to_facultyFragment)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeForData()
    }

    private fun subscribeForData() {

    }

    private fun setupRecyclerView() {
        val settings = listOf(
                SwitchSetting(getString(R.string.notification_preference_title))
                , ListSetting(getString(R.string.selectCurrentWeek))
                , LinkSetting(getString(R.string.group_preference_title))
                , LinkSetting(getString(R.string.refresh_preference_title))
                , GroupSetting(getString(R.string.help_project))
                , Wallet("BTC", "19W2euNuTq9eVuNcZd769ptmdN1xaRF61g")
                , Wallet( "ETH", "0x8d5e5071247950e18b0b33c978e9e271995f991e"))
        val adapter = SettingsAdapter(this, settings)
        settings_recycler.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        settings_recycler.addItemDecoration(DividerItemDecoration(this.context,RecyclerView.VERTICAL))
        settings_recycler.adapter = adapter


    }
}