package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.CommentReply;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.HDateUtils;

/**
 * 评论详情-Adapter
 *
 * @author hunk
 */
public class CommentDetailAdapter extends BaseAdapter {

    private Context context;
    private List<CommentReply> datas;
    private GlideUtils glideUtils = GlideUtils.getInstance();

    public CommentDetailAdapter(Context context, List<CommentReply> datas) {
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
            convertView = View.inflate(context, R.layout.item_my_comment_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.iv_user_head = (ImageView) convertView.findViewById(R.id.iv_user_head);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        CommentReply data = datas.get(position);
        glideUtils.load(context, viewHolder.iv_user_head, data.getUsericon(), R.drawable.user_img_def);
        viewHolder.tv_name.setText(data.getNickname());
        if (!TextUtils.isEmpty(data.getAddtime())) {
            String strTime = HDateUtils.getDateTime(Long.parseLong(data.getAddtime()));
            viewHolder.tv_time.setText(strTime);
        }
        viewHolder.tv_content.setText(data.getContent());

        return convertView;
    }


    private class ViewHolder {
        TextView tv_content;
        ImageView iv_user_head;
        TextView tv_time;
        TextView tv_name;
    }

}
