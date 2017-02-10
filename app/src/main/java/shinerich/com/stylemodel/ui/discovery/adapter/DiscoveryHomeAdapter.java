package shinerich.com.stylemodel.ui.discovery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.DiscoveryItem;

/**
 * 发现首页-Adapter
 *
 * @author hunk
 */
public class DiscoveryHomeAdapter extends BaseAdapter {

    private Context context;
    private List<DiscoveryItem> datas;

    public DiscoveryHomeAdapter(Context context, List<DiscoveryItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_discovery_home, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        DiscoveryItem data = datas.get(position);
        String content = "· " + data.getTitle();
        viewHolder.tv_content.setText(content);
        return convertView;
    }


    private class ViewHolder {
        TextView tv_content;
    }


}
