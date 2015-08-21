package com.example.topnewgrid.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.bean.ItemChannel;
import com.example.topnewgrid.bean.MultiChannelItem;

public class MulitGridView extends LinearLayout implements OnItemClickListener {
	private Context context;
	public DragGrid dragGrid;
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	private MultiChannelItem multiChannelItem;
	private DragAdapter dragAdapter;

	public void setDragAdapter(DragAdapter dragAdapter) {
		this.dragAdapter = dragAdapter;
	}

	public void setDragGrid(DragGrid dragGrid) {
		this.dragGrid = dragGrid;
	}

	public MulitGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public MulitGridView(Context context) {
		super(context);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
	}

	@SuppressLint("InflateParams")
	public void setData(MultiChannelItem multiple) {
		if (null != dragGrid) {
			dragGrid.setOnItemClickListener(this);
		}
		this.multiChannelItem = multiple;
		this.removeAllViews();
		if (null != multiple && 0 != multiple.getList().size()) {
			for (int i = 0; i < multiple.getList().size(); i++) {
				final ItemChannel item = multiple.getList().get(i);

				View view = LayoutInflater.from(context).inflate(
						R.layout.item_channel_view, null);
				Log.e("info", "--->>>"+1);


				TextView tvtitle = (TextView) view.findViewById(R.id.tv_title);
				LinearLayout.LayoutParams lp = null;
				final OtherGridView otherGridView = (OtherGridView) view
						.findViewById(R.id.og_view);
				if (0 != i) {
					lp = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					lp.setMargins(dptopx(14), dptopx(20), 0, 0);
					tvtitle.setLayoutParams(lp);
				}
				tvtitle.setText("" + item.getTitle());

				final OtherAdapter otherAdapter = new OtherAdapter(context,
						item.getList());
				otherGridView.setAdapter(otherAdapter);
				this.addView(view);
				
				otherGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						if (isMove) {
							return;
						}
						Log.e("info", "--->>>");

						final ChannelItem channelItem = (ChannelItem) arg0
								.getAdapter().getItem(arg2);
						if (!channelItem.isOtherSelecte()) {
							final ImageView ivImageView = getView(arg1);
							// 获取移动的位置
							if (null != ivImageView) {
								TextView otherView = (TextView) arg1
										.findViewById(R.id.text_item);
								final int[] startData = new int[2];
								otherView.getLocationInWindow(startData);
								dragAdapter.addItem(channelItem);

								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										try {
											int[] endposition = new int[2];
											dragGrid.getChildAt(
													dragGrid.getLastVisiblePosition())
													.getLocationInWindow(
															endposition);
											animationView(ivImageView,
													startData, endposition,
													channelItem, otherGridView);
											otherAdapter.upDataStatue(arg2);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}, 50L);
							}

						}

					}
				});
			}
		}
	}

	/** 添加动画效果 */
	private void animationView(final ImageView iv, int[] start, int[] end,
			ChannelItem item, GridView gridView) {
		int[] initposition = new int[2];
		iv.getLocationInWindow(initposition);
		// 得到要移动的view 并把它放到对应的容器
		final ViewGroup viewGroup = getViewGrop();
		final View moveView = getMoveView(viewGroup, iv, initposition);

		/** 创建移动动画 */
		final TranslateAnimation animation = new TranslateAnimation(start[0],
				end[0], start[1], end[1]);
		animation.setDuration(400L);
		AnimationSet set = new AnimationSet(true);
		set.setFillAfter(false);
		set.addAnimation(animation);
		moveView.clearAnimation();
		moveView.startAnimation(set);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				viewGroup.removeView(moveView);

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				viewGroup.removeView(moveView);
				dragAdapter.setVisible(true);
				isMove = false;

			}
		});
	}

	private View getMoveView(ViewGroup viewGroup, View view, int[] init) {
		int x = init[0];
		int y = init[1];
		viewGroup.addView(view);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;

	}

	private ViewGroup getViewGrop() {
		ViewGroup viewGroup = (ViewGroup) ((Activity) context).getWindow()
				.getDecorView();
		LinearLayout layout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		viewGroup.addView(layout);
		return viewGroup;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		if (isMove) {
			return;
		}
		switch (arg0.getId()) {
		case R.id.userGridView:
			if (arg2 != 0 && arg2 != 1) {
				final ImageView moveImageView = getView(arg1);
				if (moveImageView != null) {
					TextView newTextView = (TextView) arg1.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final ChannelItem channel = ((DragAdapter) arg0.getAdapter()).getItem(arg2);//��ȡ�����Ƶ������
//					otherAdapter.setVisible(false);
//					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								if (isContain(channel)) {
									setData(multiChannelItem);
								}
								if (null != dragAdapter) {
									dragAdapter.setRemove(arg2);
								}
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}
			
			
//			if (0 != arg2 && 1 != arg2 && 2 != arg2) {
//				final ImageView imageView = getView(arg1);
//				if (imageView != null) {
//					TextView tvContent = (TextView) arg1
//							.findViewById(R.id.text_item);
//					int[] startp = new int[2];
//					tvContent.getLocationInWindow(startp);
//					final ChannelItem channelItem = ((DragAdapter) arg0
//							.getAdapter()).getItem(arg2);
//					// 将这个channeltem添加到下面的布局中去
//					new Handler().postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							// 判断下面的不居中中是不是含有相同的item
//							if (isContain(channelItem)) {
//								setData(multiChannelItem);
//							}
//							if (null != dragAdapter) {
//								dragAdapter.setRemove(arg2);
//							}
//
//						}
//					}, 50l);
//
//				}
//			}

			break;

		default:
			break;
		}
	}

	/** 判断一个数是不是在集合中 */
	private boolean isContain(ChannelItem c) {
		boolean flag = false;
		if (null != multiChannelItem) {
			List<ItemChannel> itemChannels = multiChannelItem.getList();
			if (null != itemChannels && 0 != itemChannels.size()) {
				for (ItemChannel t : itemChannels) {
					List<ChannelItem> list = t.getList();
					if (null != list && 0 != list.size()) {
						for (ChannelItem channelItem : list) {
							if (channelItem.getId() == c.getId()) {
								flag = true;
								channelItem.setOtherSelecte(false);
								channelItem.setName(c.getName());
								this.invalidate();
							}
						}
					}
				}
			}

		}
		return flag;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(context);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** dp转换为px */

	private int dptopx(int value) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (value * scale + 0.5f);
	}
}
