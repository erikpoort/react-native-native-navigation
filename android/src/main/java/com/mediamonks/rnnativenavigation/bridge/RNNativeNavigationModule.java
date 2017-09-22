package com.mediamonks.rnnativenavigation.bridge;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactFragmentActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.StackNode;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.RNNNFragment;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

class RNNativeNavigationModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String kRNNN = "RNNN";

    private Set<RNNNFragment> _fragments;
    private final FragmentManager.FragmentLifecycleCallbacks _lifecycleCallbacks;

    RNNativeNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);

        _fragments = new HashSet<>();
        _lifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                super.onFragmentAttached(fm, f, context);

                if (f instanceof RNNNFragment) {
                    RNNNFragment baseFragment = (RNNNFragment) f;
                    _fragments.add(baseFragment);
                }
            }

            @Override
            public void onFragmentDetached(FragmentManager fm, Fragment f) {
                super.onFragmentDetached(fm, f);
                RNNNFragment baseFragment = (RNNNFragment) f;
                _fragments.remove(baseFragment);
            }
        };

        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        /*
		 * Save the state of the application before reload
		 */
        RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
        RNNNFragment rootFragment = getRootFragment(anyFragment.getNode());
        WritableMap currentState = rootFragment.getNode().data();
        RNNNState.INSTANCE.state = currentState.toHashMap();

		/*
		 * Clear all existing fragments before Facebook reloads them. The onStart method will
		 * rebuild the fragments.
		 */
        FragmentActivity fragmentActivity = (FragmentActivity) getCurrentActivity();
        assert fragmentActivity != null;
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        for (RNNNFragment fragment : _fragments) {
            if (fragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove((Fragment) fragment);
                transaction.commit();
            }
        }

        super.onCatalystInstanceDestroy();
    }

    @Override
    public String getName() {
        return "ReactNativeNativeNavigation";
    }

    @ReactMethod
    public void onStart(Callback callback) {
        HashMap<String, Object> state = RNNNState.INSTANCE.state;
        if (state == null) {
            Log.i(kRNNN, "First load");
            callback.invoke();
        } else {
            Log.i(kRNNN, "Reload");
            callback.invoke(Arguments.makeNativeMap(state));
        }
    }

    @ReactMethod
    public void setSiteMap(ReadableMap map, final Promise promise) {
        RNNNState.INSTANCE.state = map.toHashMap();

        try {
            assert getCurrentActivity() != null;
            ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
            final Node node = NodeHelper.nodeFromMap(map, getReactInstanceManager());
            final FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment = node.generateFragment();
                    transaction.replace(android.R.id.content, fragment);
                    transaction.commit();
                    promise.resolve(true);
                }
            });
        } catch (Exception e) {
            promise.reject(kRNNN, "Could not start app", e);
        }
    }

    @ReactMethod
    public void handleBackButton(final Callback callback) {
        assert getCurrentActivity() != null;
        ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
        RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
        final BaseFragment rootFragment = (BaseFragment) getRootFragment(anyFragment.getNode());
        SingleFragment currentFragment = rootFragment.getCurrentFragment();
        final StackFragment stackFragment = currentFragment.getStackFragment();

        if (stackFragment != null) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.invoke(stackFragment.onBackPressed());
                }
            });
            return;
        }

        callback.invoke(false);
    }

    @ReactMethod
    public void push(final ReadableMap screen, Callback callback) {
        try {
            final Node node = NodeHelper.nodeFromMap(screen, getReactInstanceManager());

            assert getCurrentActivity() != null;
            ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
            RNNNFragment rootFragment = getRootFragment(node);
            assert rootFragment != null;

            int lastSlash = node.getScreenID().lastIndexOf("/");
            BaseFragment findFragment = rootFragment.fragmentForPath(node.getScreenID().substring(0, lastSlash));
            if (findFragment == null) {
                return;
            }

            final StackFragment stackFragment = findFragment.getStackFragment();
            StackNode stackNode = stackFragment.getNode();
            Stack<Node> stack = stackNode.getStack();
            stack.add(node);

            callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().data().toHashMap()));

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stackFragment.pushNode(node, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void showModal(final ReadableMap screen, Callback callback) {
        try {
            final Node node = NodeHelper.nodeFromMap(screen, getReactInstanceManager());

            assert getCurrentActivity() != null;
            ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
            RNNNFragment rootFragment = getRootFragment(node);
            assert rootFragment != null;

            int lastSlash = node.getScreenID().lastIndexOf("/modal");
            final SingleFragment findFragment = (SingleFragment) rootFragment.fragmentForPath(node.getScreenID().substring(0, lastSlash));
            if (findFragment == null) {
                return;
            }

            findFragment.getNode().setModal(node);

            callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().data().toHashMap()));

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findFragment.showModal(node);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ReactInstanceManager getReactInstanceManager() {
        assert getCurrentActivity() != null;
        ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
        ReactApplication mainApplication = (ReactApplication) mainActivity.getApplication();
        ReactNativeHost reactNativeHost = mainApplication.getReactNativeHost();
        return reactNativeHost.getReactInstanceManager();
    }

    private RNNNFragment getRootFragment(Node node) {
        RNNNFragment rootFragment = null;
        String rootPath = node.getRootPath();
        for (RNNNFragment findFragment : _fragments) {
            if (findFragment != null) {
                if (findFragment.getNode().getScreenID().equals(rootPath)) {
                    rootFragment = findFragment;
                    break;
                }
            }
        }
        return rootFragment;
    }

    @Override
    public void onHostResume() {
        ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
        assert mainActivity != null;
        mainActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(_lifecycleCallbacks, true);
    }

    @Override
    public void onHostPause() {
        ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
        assert mainActivity != null;
        mainActivity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(_lifecycleCallbacks);
    }

    @Override
    public void onHostDestroy() {
        _fragments.clear();
    }
}
