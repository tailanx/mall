package com.example.topnewgrid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;

import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.bean.ItemChannel;
import com.example.topnewgrid.bean.MultiChannelItem;
import com.example.topnewgrid.tools.DataTools;
import com.example.topnewgrid.view.DragGrid;
import com.example.topnewgrid.view.MulitGridView;
import com.example.topnewgrid.view.OtherGridView;

/**
 * 频道管理
 * 
 * @Author RA
 * @Blog http://blog.csdn.net/vipzjyno1
 */
public class ChannelActivity extends PopupWindow implements OnItemClickListener,
		OnClickListener {
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	private MulitGridView mgv;

	private MultiChannelItem multiChannelItem;
	
	private Activity activity;
	private View view;
	
	public ChannelActivity(Activity activity){
		
	     this.activity = activity;
	        view = LayoutInflater.from(activity).inflate(R.layout.subscribe_activity, null);
	        this.setContentView(view);
	        init();
	        initView();
			initData();
	        this.setWidth(DataTools.getDisplayMetrics(activity).widthPixels);
	        this.setHeight(DataTools.getDisplayMetrics(activity).heightPixels);
	        this.setOutsideTouchable(true);
	        this.setFocusable(true);
	        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//	        this.setAnimationStyle(R.style.channel_style);
	        this.update();
	}
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.subscribe_activity);
//		init();
//		initView();
//		initData();
//	}

	private void init() {
		multiChannelItem = new MultiChannelItem();
		int k = 0;
		ArrayList<ItemChannel> listI = new ArrayList<ItemChannel>();
		for (int i = 0; i < 10; i++) {
			ItemChannel itemChannel = new ItemChannel();
			itemChannel.setTitle("模拟" + i);
			ArrayList<ChannelItem> listC = new ArrayList<ChannelItem>();
			for (int j = 4; j < 20; j++) {
				ChannelItem channelItem = new ChannelItem(k, "情" + "" + i + ""
						+ j, j, 0, false);
				listC.add(channelItem);
				k++;
			}
			itemChannel.setList(listC);
			listI.add(itemChannel);
		}
		if (null != listI && 0 != listI.size()) {
			multiChannelItem.setList(listI);
		}
	}

	/** 初始化数据 */
	private void initData() {
		// userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(
		// AppApplication.getApp().getSQLHelper()).getUserChannel());
		// otherChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(
		// AppApplication.getApp().getSQLHelper()).getOtherChannel());
		userChannelList = (ArrayList<ChannelItem>) ChannelManage.defaultUserChannels;
		otherChannelList = (ArrayList<ChannelItem>) ChannelManage.defaultOtherChannels;
		userAdapter = new DragAdapter(activity, userChannelList);
		userGridView.setAdapter(userAdapter);
		// otherAdapter = new OtherAdapter(this, otherChannelList);
		mgv.setDragAdapter(userAdapter);
		view.findViewById(R.id.my_category_tip_edit).setOnClickListener(this);
		// otherGridView.setAdapter(this.otherAdapter);
		// // 设置GRIDVIEW的ITEM的点击监听
		// otherGridView.setOnItemClickListener(this);
		// userGridView.setOnItemClickListener(this);
	}

	/** 初始化布局 */
	private void initView() {
		userGridView = (DragGrid) view.findViewById(R.id.userGridView);
		mgv = (MulitGridView) view.findViewById(R.id.mgv_view);
		mgv.setDragGrid(userGridView);

		mgv.setData(multiChannelItem);
		// otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
//		case R.id.userGridView:
//			// position为 0，1 的不可以进行任何操作
//			if (position != 0 && position != 1 && 2 != position) {
//				final ImageView moveImageView = getView(view);
//				if (moveImageView != null) {
//					TextView newTextView = (TextView) view
//							.findViewById(R.id.text_item);
//					final int[] startLocation = new int[2];
//					newTextView.getLocationInWindow(startLocation);
//					final ChannelItem channel = ((DragAdapter) parent
//							.getAdapter()).getItem(position);// 获取点击的频道内容
//					otherAdapter.setVisible(false);
//					// 添加到最后一个
//					// otherAdapter.addItem(channel);
//					new Handler().postDelayed(new Runnable() {
//						public void run() {
//							try {
//								int[] endLocation = new int[2];
//								/** 判断是不是有相同的频道在other中 */
//								boolean flag = false;
//								int p = -1;
//								List<ChannelItem> list = otherAdapter
//										.getChannnelLst();
//								for (int i = 0; i < list.size(); i++) {
//									if (channel.getId() == list.get(i).getId()) {
//										flag = true;
//										p = i;
//									}
//								}
//								if (!flag) {
//
//									// // 获取终点的坐标
//									// otherGridView.getChildAt(
//									// otherGridView
//									// .getLastVisiblePosition())
//									// .getLocationInWindow(endLocation);
//								} else {
//									if (-1 != p) {
//										// otherGridView.getChildAt(p)
//										// .getLocationInWindow(
//										// endLocation);
//									}
//								}
//
//								MoveAnim(moveImageView, startLocation,
//										endLocation, channel, userGridView);
//								userAdapter.setRemove(position);
//								// if (null != otherAdapter) {
//								otherAdapter.addDataStatue(channel);
//								// }
//							} catch (Exception localException) {
//							}
//						}
//					}, 50L);
//				}
//			}
//			break;
		// case R.id.otherGridView:
		// final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
		// .getItem(position);
		// if (!channel.isOtherSelecte()) {
		// final ImageView moveImageView = getView(view);
		//
		// if (moveImageView != null) {
		// TextView newTextView = (TextView) view
		// .findViewById(R.id.text_item);
		// final int[] startLocation = new int[2];
		// newTextView.getLocationInWindow(startLocation);
		//
		// userAdapter.setVisible(false);
		// // 添加到最后一个
		// userAdapter.addItem(channel);
		// new Handler().postDelayed(new Runnable() {
		// public void run() {
		// try {
		// int[] endLocation = new int[2];
		// // 获取终点的坐标
		// userGridView.getChildAt(
		// userGridView.getLastVisiblePosition())
		// .getLocationInWindow(endLocation);
		// MoveAnim(moveImageView, startLocation,
		// endLocation, channel, otherGridView);
		// // otherAdapter.setRemove(position);
		// otherAdapter.upDataStatue(position);
		//
		// } catch (Exception localException) {
		// }
		// }
		// }, 50L);
		// }
		// }
		// break;
		default:
			break;
		}
	}

//	/**
//	 * 点击ITEM移动动画
//	 * 
//	 * @param moveView
//	 * @param startLocation
//	 * @param endLocation
//	 * @param moveChannel
//	 * @param clickGridView
//	 */
//	private void MoveAnim(View moveView, int[] startLocation,
//			int[] endLocation, final ChannelItem moveChannel,
//			final GridView clickGridView) {
//		int[] initLocation = new int[2];
//		// 获取传递过来的VIEW的坐标
//		moveView.getLocationInWindow(initLocation);
//		// 得到要移动的VIEW,并放入对应的容器中
//		final ViewGroup moveViewGroup = getMoveViewGroup();
//		final View mMoveView = getMoveView(moveViewGroup, moveView,
//				initLocation);
//		// 创建移动动画
//
//		TranslateAnimation moveAnimation = new TranslateAnimation(
//				startLocation[0], endLocation[0], startLocation[1],
//				endLocation[1]);
//		moveAnimation.setDuration(100L);// 动画时间
//		// 动画配置
//		AnimationSet moveAnimationSet = new AnimationSet(true);
//		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
//		moveAnimationSet.addAnimation(moveAnimation);
//		mMoveView.startAnimation(moveAnimationSet);
//		moveAnimationSet.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				isMove = true;
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				moveViewGroup.removeView(mMoveView);
//				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
//				if (clickGridView instanceof DragGrid) {
//					otherAdapter.setVisible(true);
//					otherAdapter.notifyDataSetChanged();
//					userAdapter.remove();
//				} else {
//					userAdapter.setVisible(true);
//					userAdapter.notifyDataSetChanged();
//					otherAdapter.remove();
//				}
//				isMove = false;
//			}
//		});
//	}
//
//	/**
//	 * 获取移动的VIEW，放入对应ViewGroup布局容器
//	 * 
//	 * @param viewGroup
//	 * @param view
//	 * @param initLocation
//	 * @return
//	 */
//	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
//		int x = initLocation[0];
//		int y = initLocation[1];
//		viewGroup.addView(view);
//		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		mLayoutParams.leftMargin = x;
//		mLayoutParams.topMargin = y;
//		view.setLayoutParams(mLayoutParams);
//		return view;
//	}
//
//	/**
//	 * 创建移动的ITEM对应的ViewGroup布局容器
//	 */
//	private ViewGroup getMoveViewGroup() {
//		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
//		LinearLayout moveLinearLayout = new LinearLayout(this);
//		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		moveViewGroup.addView(moveLinearLayout);
//		return moveLinearLayout;
//	}
//
//	/**
//	 * 获取点击的Item的对应View，
//	 * 
//	 * @param view
//	 * @return
//	 */
//	private ImageView getView(View view) {
//		view.destroyDrawingCache();
//		view.setDrawingCacheEnabled(true);
//		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
//		view.setDrawingCacheEnabled(false);
//		ImageView iv = new ImageView(this);
//		iv.setImageBitmap(cache);
//		return iv;
//	}
//
//	// /** 退出时候保存选择后数据库的设置 */
//	// private void saveChannel() {
//	// ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//	// .deleteAllChannel();
//	// ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//	// .saveUserChannel(userAdapter.getChannnelLst());
//	// ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//	// .saveOtherChannel(otherAdapter.getChannnelLst());
//	// }
//
//	@Override
//	public void onBackPressed() {
//		// saveChannel();
//		super.onBackPressed();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_category_tip_edit:
//			this.startActivity(new Intent(this, cls))
			break;

		default:
			break;
		}
	}
}
