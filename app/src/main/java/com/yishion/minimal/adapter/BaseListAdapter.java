package com.yishion.minimal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yishion.minimal.R;
import com.yishion.minimal.model.TodoItem;
import com.yishion.minimal.utils.DateUtils;
import com.yishion.minimal.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private final List<TodoItem> mDataList;

    private ItemClickListener<TodoItem> mListener;

    public BaseListAdapter(Context mContext, List<TodoItem> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    public BaseListAdapter(Context mContext, ItemClickListener<TodoItem> mListener) {
        this.mContext = mContext;
        this.mDataList = new ArrayList<>();
        this.mListener = mListener;
    }

    public BaseListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mDataList = new ArrayList<>();
    }

    public void setItemClickListener(ItemClickListener<TodoItem> mListener) {
        this.mListener = mListener;
    }

    public void addDataList(List<TodoItem> list) {
        if (list != null && list.size() > 0) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.cell_todo, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(R.id.id_tag_recycler, holder);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        TodoItem item = mDataList.get(pos);

        //设置背景和内容字体颜色
        int todoColor;
        int todoBg;
        if (SharedPreferencesUtils.isThemeLight(mContext)) {
            todoBg = Color.WHITE;
            todoColor = mContext.getResources().getColor(R.color.secondary_text);
        }
        else {
            todoBg = Color.DKGRAY;
            todoColor = Color.WHITE;
        }
        viewHolder.ll.setBackgroundColor(todoBg);

        //内容
        viewHolder.content.setText(item.content);
        viewHolder.content.setTextColor(todoColor);
        //日期
        if (item.date != null && item.hasReminder) {
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.date.setText(DateUtils.format(mContext, item.date));
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.primary_dark));

            viewHolder.content.setMaxLines(1);
        }
        else {
            viewHolder.date.setVisibility(View.GONE);
            //日期不可见的时候
            viewHolder.content.setMaxLines(2);
        }

        //头像
        TextDrawable drawable =
                TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(item.content.substring(0, 1), item.color);
        viewHolder.img.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.id_tag_recycler);
        ItemClickListener<TodoItem> listener = this.mListener;
        if (listener != null) {
            int position = holder.getAdapterPosition();
            listener.onItemClick(position, mDataList.get(position));
        }
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container)
        ViewGroup ll;
        @BindView(R.id.image)
        ImageView img;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.date)
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
