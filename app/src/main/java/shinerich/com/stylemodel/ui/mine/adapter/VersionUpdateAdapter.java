package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;

/**
 * 版本更新-Adapter
 *
 * @author hunk
 */
public class VersionUpdateAdapter extends BaseAdapter {

    private Context context;
    private List<String> datas;

    public VersionUpdateAdapter(Context context, List<String> datas) {
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
            convertView = View.inflate(context, R.layout.item_version_update, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_content.setText(datas.get(position));

        return convertView;
    }


    private class ViewHolder {
        TextView tv_content;
    }

}
