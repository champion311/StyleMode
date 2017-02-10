package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class EditorInfosBean {

    private EditorBean editor_data;

    private List<ListContentBean> list;

    public EditorBean getEditor_data() {
        return editor_data;
    }

    public void setEditor_data(EditorBean editor_data) {
        this.editor_data = editor_data;
    }

    public List<ListContentBean> getList() {
        return list;
    }

    public void setList(List<ListContentBean> list) {
        this.list = list;
    }
}
