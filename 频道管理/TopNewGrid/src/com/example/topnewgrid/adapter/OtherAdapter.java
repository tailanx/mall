package com.example.topnewgrid.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.bean.ChannelItem;

public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<ChannelItem> channelList;
	/** 是否可见 */
	boolean isVisible = true;
	/** 要删除的position */
	public int remove_position = -1;

	public OtherAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_subscribe_category, null);
			holder = new ViewHolder();
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.text_item);
			holder.ivDel = (ImageView) convertView.findViewById(R.id.icon_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChannelItem channel = getItem(position);
		if (channel.getName().toString().trim().length() <= 3) {
			holder.tvContent.setTextSize(15);
		} else {
			holder.tvContent.setTextSize(14);
		}
		holder.tvContent
				.setText(channel.getName().trim().length() > 4 ? channel
						.getName().substring(0, 4) + "..." : channel.getName());

		if (!isVisible && (position == -1 + channelList.size())) {
			holder.tvContent.setText("");
		}
		// if (remove_position == position) {
		// holder.tvContent.setText("");
		// }
		if (channel.isOtherSelecte()) {
			holder.tvContent.setSelected(true);
			holder.ivDel.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvContent;
		ImageView ivDel;
	}

	/** 获取频道列表 */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}

	/** 添加频道列表 */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 减少频道的时候更新数据 */
	public void upDataStatue(int postion) {
		if (postion < channelList.size()) {
			channelList.get(postion).setOtherSelecte(true);
			notifyDataSetChanged();
		}
	}

	/** 添加品到时候的更新数据 */
	public void addDataStatue(ChannelItem channelItem) {
		boolean flag = false;
		for (ChannelItem c : channelList) {
			if (c.getId() == channelItem.getId()) {
				c.setOtherSelecte(false);
				flag = true;
			}
		}
		if (!flag) {
			channelList.add(channelItem);
			notifyDataSetChanged();
		}
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
	}

	/** 删除频道列表 */
	public void remove() {
		// channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** 设置频道列表 */
	public void setListDate(List<ChannelItem> list) {
		channelList = list;
	}

	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
}