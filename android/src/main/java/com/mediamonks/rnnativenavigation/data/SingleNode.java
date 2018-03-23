package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode extends BaseNode {
	public static final String SHOW_MODAL = "showModal";
	public static final String UPDATE_STYLE = "updateStyle";

	public static String JS_NAME = "SingleView";

	public static Map<String, Object> getConstants() {
		Map<String, Object> map = new HashMap<>();
		map.put(SHOW_MODAL, SHOW_MODAL);
		map.put(UPDATE_STYLE, UPDATE_STYLE);
		return map;
	}

	private static final String PAGE = "page";
	private static final String MODAL = "modal";
	private static final String STYLE = "style";

	private String _page;
	private Node _modal;
	private ReadableNativeMap _style;

	@Override
	public BaseFragment<SingleNode> generateFragment() {
		SingleFragment fragment = new SingleFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setData(ReadableMap map) {
		super.setData(map);
		_page = map.getString(PAGE);
		_style = (ReadableNativeMap) map.getMap(STYLE);

		if (map.hasKey(MODAL)) {
			try {
				_modal = NodeHelper.getInstance().nodeFromMap(map.getMap(MODAL), getInstanceManager());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> data = super.getData();
		data.put(PAGE, _page);
		data.put(STYLE, _style.toHashMap());
		if (_modal != null) {
			data.put(MODAL, _modal.getData());
		}
		return data;
	}

	public void setModal(Node modal) {
		_modal = modal;
	}

	public Node getModal() {
		return _modal;
	}

	public ReadableNativeMap getStyle() {
		return _style;
	}

	public void setStyle(WritableNativeMap style) {
		_style = style;
	}
}
