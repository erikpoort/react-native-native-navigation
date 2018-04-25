package com.mediamonks.rnnativenavigation.factory;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.mediamonks.rnnativenavigation.R;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.data.StackNode;

import java.util.Stack;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackFragment extends BaseFragment<StackNode> implements Navigatable {
	private FrameLayout _holder;
	private Toolbar _toolbar;
	private int _toolbarHeight;
	private Drawable _upIcon;
	private DeviceEventManagerModule.RCTDeviceEventEmitter _eventEmitter;

	@Override public void setNode(StackNode node) {
		super.setNode(node);

		ReactContext context = node.getInstanceManager().getCurrentReactContext();
		if (context != null) {
			_eventEmitter = context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// I'm calling generateViewId() twice, calling it once doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		relativeLayout.setBackgroundColor(Color.WHITE);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		_holder = new FrameLayout(getActivity());
		_holder.setId(View.generateViewId());

		_toolbar = (Toolbar) inflater.inflate(R.layout.toolbar, relativeLayout, false);
		_toolbar.setId(View.generateViewId());
		_upIcon = _toolbar.getNavigationIcon();
		TypedValue typedValue = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
			_toolbarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
			_toolbar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, _toolbarHeight));
		}

		RelativeLayout.LayoutParams holderParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		relativeLayout.addView(_holder, holderParams);
		relativeLayout.addView(_toolbar);

		return relativeLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();

				SingleNode singleNode = getCurrentFragment().getNode();
				if (singleNode.getStyle().hasKey("rightBarButtons") && singleNode.getStyle().getType("rightBarButtons") == ReadableType.Array) {
					ReadableArray buttons = singleNode.getStyle().getArray("rightBarButtons");
					assert buttons != null;

					if (id >= 0 && id < buttons.size()) {
						ReadableMap data = buttons.getMap(id);
						_eventEmitter.emit(singleNode.getScreenID(), data.getString("id"));

						return true;
					}
				}

				return false;
			}
		});
	}

	@Override public void onDestroyView() {
		super.onDestroyView();

		int leni = getNode().getStack().size();
		for (int i = leni - 1; i > 0; --i) {
			Node node = getNode().getStack().get(i);
			removeNode(node, FragmentTransaction.TRANSIT_NONE);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		showPeek(FragmentTransaction.TRANSIT_NONE);
	}

	@Override public void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		switch (name) {
			case StackNode.PUSH: {
				this.handlePushCall(arguments, rootFragment, callback);
				break;
			}
			case StackNode.POP: {
				this.handlePopCall();
				break;
			}
			case StackNode.POP_TO: {
				this.handlePopToCall(arguments);
				break;
			}
			case StackNode.POP_TO_ROOT: {
				this.handlePopToRootCall();
				break;
			}
			case SingleNode.UPDATE_STYLE: {
				this.handleUpdateStyleCall(arguments);
				break;
			}
		}
	}

	private void handlePushCall(final ReadableMap arguments, final RNNNFragment rootFragment, final Callback callback) {
		final Node node;
		try {
			node = NodeHelper.getInstance().nodeFromMap(arguments.getMap("screen"), getNode().getInstanceManager());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		final boolean reset = arguments.getMap("arguments").getBoolean("reset");

		Stack<Node> stack = getNode().getStack();
		if (reset) {
			stack.removeAllElements();
		}
		stack.add(node);

		callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData()));

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (reset) {
					while (getNode().getStack().size() > 1) {
						removeNode(getNode().getStack().pop(), FragmentTransaction.TRANSIT_NONE);
					}
				}

				StackFragment.this.pushNode(node, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}
		});
	}

	private void handlePopCall() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (getNode().getStack().size() > 1) {
					popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				}
			}
		});
	}

	private void handlePopToCall(ReadableMap arguments) {
		try {
			Node foundNode = null;
			for (Node node : getNode().getStack()) {
				if (node.getScreenID().equals(arguments.getString("path"))) {
					foundNode = node;
				}
			}
			if (foundNode == null) {
				return;
			}

			final int index = getNode().getStack().indexOf(foundNode);
			if (index < 0) {
				return;
			}

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					while (getNode().getStack().size() - 1 > index + 1) {
						popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_NONE);
					}

					popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePopToRootCall() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				while (getNode().getStack().size() > 1) {
					popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_NONE);
				}
			}
		});
	}

	private void handleUpdateStyleCall(final ReadableMap arguments) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (arguments.hasKey("title")) {
					_toolbar.setTitle(arguments.getString("title"));
				}
				if (arguments.hasKey("barTint")) {
					int color = arguments.getInt("barTint");
					_toolbar.setTitleTextColor(color);
					_upIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				}
				if (arguments.hasKey("barBackground")) {
					int color = arguments.getInt("barBackground");
					_toolbar.setBackgroundColor(color);
				}
			}
		});
	}

	private void showPeek(int transition) {
		Node node = getNode().getStack().peek();
		pushNode(node, transition);
	}

	public void pushNode(Node node, int transition) {
		BaseFragment fragment = node.generateFragment();
		fragment.setStackFragment(this);
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(_holder.getId(), fragment, node.getScreenID());
		transaction.setTransition(transition);
		transaction.commitNowAllowingStateLoss();
		node.setShown(true);

		handleCurrentStack(transition);
	}

	private void removeNode(Node node, int transition) {
		if (node.isShown()) {
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			String topID = node.getScreenID();
			transaction.remove(getChildFragmentManager().findFragmentByTag(topID));
			transaction.setTransition(transition);
			transaction.commitNowAllowingStateLoss();
			node.setShown(false);
		}
	}

	public void popNode(Node node, int transition) {
		removeNode(node, transition);
		getNode().getStack().remove(getNode().getStack().peek());

		if (!getNode().getStack().peek().isShown()) {
			showPeek(FragmentTransaction.TRANSIT_NONE);
		}

		handleCurrentStack(transition);
	}

	private void handleCurrentStack(int transition) {
		int size = getNode().getStack().size();

		Node showNode = getNode().getStack().peek();
		if (showNode instanceof SingleNode) {
			SingleNode singleNode = (SingleNode) showNode;

			// Current values
			ColorDrawable background = (ColorDrawable) _toolbar.getBackground();
			int fromColor = background.getColor();
			int fromHeight = _toolbar.getMeasuredHeight();

			// Requested values
			boolean barTransparent = false;
			if (singleNode.getStyle().hasKey("barTransparent")) {
				barTransparent = singleNode.getStyle().getBoolean("barTransparent");
			}

			boolean barHidden = false;
			if (singleNode.getStyle().hasKey("barHidden")) {
				barHidden = singleNode.getStyle().getBoolean("barHidden");
			}
			int toHeight = barHidden ? 0 : _toolbarHeight;

			if (singleNode.getStyle().hasKey("title")) {
				_toolbar.setTitle(singleNode.getStyle().getString("title"));
			} else {
				_toolbar.setTitle("");
			}

			if (singleNode.getStyle().hasKey("barFont") && singleNode.getStyle().hasKey("barFontSize")) {
				String font = singleNode.getStyle().getString("barFont");
				int fontSize = singleNode.getStyle().getInt("barFontSize");
				SpannableString span = new SpannableString(_toolbar.getTitle());
				span.setSpan(new TypefaceSpan(font), 0, _toolbar.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				span.setSpan(new AbsoluteSizeSpan(fontSize, true), 0, _toolbar.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				_toolbar.setTitle(span);
			}

			if (singleNode.getStyle().hasKey("barTint")) {
				Integer tintColor = (int) singleNode.getStyle().getDouble("barTint");
				_upIcon.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
				_toolbar.setTitleTextColor(tintColor);
				_toolbar.setSubtitleTextColor(tintColor);
			}

			Integer barBackgroundColor = Color.TRANSPARENT;
			if (singleNode.getStyle().hasKey("barBackground") && !barTransparent) {
				barBackgroundColor = (int) singleNode.getStyle().getDouble("barBackground");
			}

			_toolbar.getMenu().clear();

			BitmapDrawable customLeftIcon = null;

			if (singleNode.getStyle().hasKey("leftBarButton") && singleNode.getStyle().getType("leftBarButton") == ReadableType.Map) {
				ReadableMap button = singleNode.getStyle().getMap("leftBarButton");
				assert button != null;

				String title = button.getString("title");

				if (title != null) {
					TextPaint paint = new TextPaint();

					if (button.hasKey("font")) {
						Typeface typeface = Typeface.create(button.getString("font"), Typeface.NORMAL);
						paint.setTypeface(typeface);
					}
					if (button.hasKey("color")) {
						paint.setColor(button.getInt("color"));
					} else if (singleNode.getStyle().hasKey("barTint")) {
						paint.setColor(singleNode.getStyle().getInt("barTint"));
					}
					if (button.hasKey("fontSize")) {
						paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, button.getInt("fontSize"), getResources().getDisplayMetrics()));
					}

					paint.setStyle(Paint.Style.FILL);
					paint.setAntiAlias(true);

					Rect bounds = new Rect();
					paint.getTextBounds(title, 0, title.length(), bounds);

					int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
					Bitmap bitmap = Bitmap.createBitmap(bounds.width() + padding * 2, bounds.height(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					canvas.drawText(title, -bounds.left + padding, bitmap.getHeight(), paint);
					customLeftIcon = new BitmapDrawable(getResources(), bitmap);
				}
			}

			if (singleNode.getStyle().hasKey("rightBarButtons") && singleNode.getStyle().getType("rightBarButtons") == ReadableType.Array) {
				ReadableArray buttons = singleNode.getStyle().getArray("rightBarButtons");
				assert buttons != null;

				int leni = buttons.size();
				for (int i = 0; i < leni; ++i) {
					ReadableMap map = buttons.getMap(i);

					if (map.hasKey("title")) {
						String title = map.getString("title");

						_toolbar.getMenu().add(title);
						_toolbar.getMenu().getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

						View view = _toolbar.findViewById(i);
						if (view instanceof TextView) {
							TextView textView = (TextView) view;

							textView.setAllCaps(false);
							if (map.hasKey("fontSize")) {
								textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, map.getInt("fontSize"));
							}
							if (map.hasKey("color")) {
								textView.setTextColor(map.getInt("color"));
							} else if (singleNode.getStyle().hasKey("barTint")) {
								textView.setTextColor(singleNode.getStyle().getInt("barTint"));
							}
							if (map.hasKey("font")) {
								Typeface typeface = Typeface.create(map.getString("font"), Typeface.NORMAL);
								textView.setTypeface(typeface);
							}
						}
					}
				}
			}

			// Config
			int duration = transition == FragmentTransaction.TRANSIT_NONE
					? 0
					: getResources().getInteger(android.R.integer.config_shortAnimTime);

			// Execution
			ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
			anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					int val = (Integer) valueAnimator.getAnimatedValue();
					ViewGroup.LayoutParams layoutParams = _toolbar.getLayoutParams();
					layoutParams.height = val;
					_toolbar.setLayoutParams(layoutParams);
				}
			});
			anim.setDuration(duration);
			anim.start();

			if (!barHidden) {
				_toolbar.setNavigationIcon(size > 1 ? _upIcon : customLeftIcon);

				ObjectAnimator animator = ObjectAnimator.ofObject(_toolbar, "backgroundColor", new ArgbEvaluator(), fromColor, barBackgroundColor);
				animator.setDuration(duration);
				animator.setInterpolator(new DecelerateInterpolator());
				animator.start();

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _holder.getLayoutParams();
				if (barTransparent) {
					params.removeRule(RelativeLayout.BELOW);
				} else {
					params.addRule(RelativeLayout.BELOW, _toolbar.getId());
				}
				_holder.setLayoutParams(params);
			}
		} else {
			_toolbar.setNavigationIcon(size > 1 ? _upIcon : null);
		}
	}

	@Override
	public boolean onBackPressed() {
		if (getNode().getStack().size() > 1) {
			popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			return true;
		}
		return false;
	}

	@Override
	public BaseFragment fragmentForPath(String path) {
		if (path.equals(getNode().getScreenID())) {
			return this;
		}
		if (path.indexOf(getNode().getScreenID()) == 0) {

			BaseFragment foundFragment = null;
			Node checkNode;

			int i = 0;
			do {
				if (i < getNode().getStack().size()) {
					checkNode = getNode().getStack().get(i++);

					if (path.indexOf(checkNode.getScreenID()) == 0) {
						foundFragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(checkNode.getScreenID());
					}
				} else {
					checkNode = null;
				}
			}
			while (checkNode != null);

			if (foundFragment != null) {
				if (!foundFragment.getNode().getScreenID().equals(path)) {
					foundFragment = foundFragment.fragmentForPath(path);
				}

				return foundFragment;
			}
		}
		return null;
	}

	@Override public void invalidate() {
		for (Node node : getNode().getStack()) {
			BaseFragment fragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(node.getScreenID());
			if (fragment != null) {
				fragment.invalidate();
			}
		}
	}

	@Override
	public SingleFragment getCurrentFragment() {
		String topID = getNode().getStack().peek().getScreenID();
		BaseFragment topFragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(topID);
		if (topFragment instanceof SingleFragment) {
			return (SingleFragment) topFragment;
		}
		return topFragment.getCurrentFragment();
	}
}
