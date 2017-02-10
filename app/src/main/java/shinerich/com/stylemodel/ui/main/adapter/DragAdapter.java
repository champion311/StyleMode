package shinerich.com.stylemodel.ui.main.adapter;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.constant.AppConstants;
import shinerich.com.stylemodel.widget.DraggerView.OnDragVHListener;
import shinerich.com.stylemodel.widget.DraggerView.OnItemMoveListener;

/**
 * Created by Administrator on 2016/9/7.
 */
public class DragAdapter extends RecyclerView.Adapter<DragAdapter.MineVieHolder> implements OnItemMoveListener {


    private boolean isEditMode = false;//是否为编辑模式

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    private List<ColumnItem> mItems;

    private Context mContext;

    private ItemTouchHelper helper;

    private float startDragTime;

    private MyColumnItemClick myColumnItemClick;

    public void setMyColumnItemClick(MyColumnItemClick myColumnItemClick) {
        this.myColumnItemClick = myColumnItemClick;
    }

    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    public DragAdapter(Context mContext, List<ColumnItem> mItems, ItemTouchHelper helper) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.helper = helper;
    }

    public List<ColumnItem> getmItems() {
        return mItems;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public MineVieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_my_column_layout, null);
        return new MineVieHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final MineVieHolder holder, final int position) {

        holder.mText.setText(mItems.get(position).getName());
        int res = 0;
        switch (mItems.get(position).getId()) {
            case AppConstants.TYPE_BLOGGER:
                res = R.drawable.blogger_icon;
                break;
            case AppConstants.TYPE_FASHION_DRESSS:
                res = R.drawable.fashion_address;
                break;
            case AppConstants.TYPE_HUAZHUANG:
                res = R.drawable.meizhuang_icon;
                break;
            case AppConstants.TYPE_LIFE_WORE:
                res = R.drawable.lovelive_icon;
                break;
            case AppConstants.TYPE_RECOMMEND:
                res = R.drawable.recommend_icon;
                break;
            case AppConstants.TYPE_HO:
                res = R.drawable.hi_icon;
                break;
            case AppConstants.TYPE_VIDEO:
                res = R.drawable.video_icon;
                break;
            default:
                res = R.drawable.recommend_icon;
                break;
        }
        holder.imageView.setImageResource(res);
        if (mItems.get(position).getId() == AppConstants.TYPE_RECOMMEND) {
            return;
        }
        holder.mWrapper.setOnLongClickListener(new View.OnLongClickListener() {
                                                   @Override
                                                   public boolean onLongClick(View v) {


                                                       if (!isEditMode) {
                                                           startEdit();
                                                       }
                                                       if (myColumnItemClick != null) {
                                                           myColumnItemClick.callAction();
                                                       }

                                                       //ToastUtils.show(mContext, "drag_able");
                                                       helper.startDrag(holder);
                                                       return true;

                                                   }
                                               }

        );
        holder.mWrapper.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   if (!isEditMode) {
                                                       return;
                                                   }
                                                   helper.startDrag(holder);

                                               }
                                           }

        );
        holder.mWrapper.setOnTouchListener(new View.OnTouchListener()

                                           {
                                               @Override
                                               public boolean onTouch(View v, MotionEvent event) {
                                                   if (isEditMode) {
                                                       switch (MotionEventCompat.getActionMasked(event)) {

                                                           case MotionEvent.ACTION_DOWN:
                                                               startDragTime = System.currentTimeMillis();
                                                               //if (System.currentTimeMillis() - startDragTime > SPACE_TIME) {
                                                               isEditMode = true;
                                                               helper.startDrag(holder);
                                                               //}
                                                               break;
                                                           case MotionEvent.ACTION_MOVE:
                                                               if (System.currentTimeMillis() - startDragTime > SPACE_TIME) {
                                                                   isEditMode = true;
                                                                   helper.startDrag(holder);
                                                               }
                                                               break;
                                                           case MotionEvent.ACTION_CANCEL:
                                                           case MotionEvent.ACTION_UP:
                                                               startDragTime = 0;
                                                               break;
                                                       }
                                                   }
                                                   return false;
                                               }
                                           }

        );
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (mItems.get(fromPosition).getId() == AppConstants.TYPE_RECOMMEND
                || mItems.get(toPosition).getId() == AppConstants.TYPE_RECOMMEND) {
            return;
        }
        ColumnItem item = mItems.get(fromPosition);
        mItems.remove(fromPosition);
        mItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        //ToastUtils.show(mContext, mItems.toString());

    }

    class MineVieHolder extends RecyclerView.ViewHolder implements OnDragVHListener {


        @BindView(R.id.mWrapper)
        FrameLayout mWrapper;
        @BindView(R.id.mText)
        TextView mText;
        @BindView(R.id.mImageView)
        ImageView imageView;

        private Context mContext;

        public MineVieHolder(View itemView, Context mContext) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = mContext;
        }

        @Override
        public void onItemSelected() {
            //ToastUtils.show(mContext, "selected");
        }

        @Override
        public void onItemFinish() {
            //ToastUtils.show(mContext, "finish");
        }
    }


    public List<String> getTabListData() {
        List<String> retTurnData = new ArrayList<>();
        for (ColumnItem item : mItems) {
            retTurnData.add(item.getName());
        }
        return retTurnData;
    }

    public boolean isDataEqual(List<ColumnItem> data1, List<ColumnItem> data2) {

        if (data1.size() != data2.size()) {
            return false;
        }
        for (int i = 0; i < data1.size(); i++) {
            ColumnItem item1 = data1.get(i);
            ColumnItem item2 = data2.get(i);
            if (item1.getId() == item2.getId()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public void setData(List<ColumnItem> mItems) {
        this.mItems.clear();
        this.mItems.addAll(mItems);
        //notifyDataSetChanged();
    }

    public void startEdit() {
        isEditMode = true;
        //TODO 添加全远抖动动画等

    }


    public interface MyColumnItemClick {
        void callAction();
    }

}
