package geek.owl.com.ua.KNUSchedule.Util.Adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import geek.owl.com.ua.KNUSchedule.Repository.*

class SettingsAdapter(val settingsClickListener:OnItemClick, val data:List<SimpleAdapter.ItemModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val viewHolderCreator = ViewRenderer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolderCreator.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item  = data[position]
        when(item){
            is ListSetting ->{ (holder as ViewRenderer.ListSettingViewHolder).bind(item, settingsClickListener)}
            is GroupSetting ->{(holder as ViewRenderer.GroupSettingViewHolder).bind(item,settingsClickListener)}
            is SwitchSetting ->{(holder as ViewRenderer.SwitchSettingViewHolder).bind(item, settingsClickListener)}
            is LinkSetting ->{(holder as ViewRenderer.LinkSettingViewHolder).bind(item, settingsClickListener)}
            is Wallet ->{(holder as ViewRenderer.MoneyViewHolder).bind(item, settingsClickListener)}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }
}