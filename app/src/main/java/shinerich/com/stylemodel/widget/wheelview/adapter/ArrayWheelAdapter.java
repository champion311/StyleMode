/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package shinerich.com.stylemodel.widget.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import shinerich.com.stylemodel.R;

/**
 * The simple Array wheel adapter
 *
 * @author hunk
 */
public class ArrayWheelAdapter<T> extends BaseAdapter {

    // items
    private T items[];
    private Context context;


    public ArrayWheelAdapter(Context context, T items[]) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items == null ? 0 : items.length;
    }

    @Override
    public Object getItem(int position) {
        return items.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.wheelview_item, null);
        }
        TextView textView = (TextView) convertView;
        textView.setText(items[position].toString());
        return convertView;
    }

}
