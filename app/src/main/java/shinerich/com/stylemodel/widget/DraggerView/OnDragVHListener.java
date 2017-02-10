package shinerich.com.stylemodel.widget.DraggerView;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface OnDragVHListener {

    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
