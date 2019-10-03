package cn.yesterday17.majsoul_android.game.floatingview;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 修改自 https://github.com/leotyndale/EnFloatingView
 */
public class FloatingViewManager {
    private BaseFloatingView floatingView;
    private FrameLayout mContainer;

    private static volatile FloatingViewManager instance;

    public static FloatingViewManager Instance() {
        if (instance == null) {
            synchronized (FloatingViewManager.class) {
                if (instance == null) {
                    instance = new FloatingViewManager();
                }
            }
        }
        return instance;
    }

    public FloatingViewManager remove() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (floatingView == null) {
                return;
            }
            if (floatingView.isAttachedToWindow() && mContainer != null) {
                mContainer.removeView(floatingView);
            }
            floatingView = null;
        });
        return this;
    }

    private void ensureMiniPlayer(BaseFloatingView view) {
        synchronized (this) {
            if (floatingView != null) {
                return;
            }
            floatingView = view;
            floatingView.setLayoutParams(getParams());
            addViewToWindow(floatingView);
        }
    }

    public FloatingViewManager add(BaseFloatingView view) {
        ensureMiniPlayer(view);
        return this;
    }

    public FloatingViewManager attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    public FloatingViewManager attach(FrameLayout container) {
        if (container == null || floatingView == null) {
            mContainer = container;
            return this;
        }
        if (floatingView.getParent() == container) {
            return this;
        }
        if (mContainer != null && floatingView.getParent() == mContainer) {
            mContainer.removeView(floatingView);
        }
        mContainer = container;
        container.addView(floatingView);
        return this;
    }

    public FloatingViewManager detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    private FloatingViewManager detach(FrameLayout container) {
        if (floatingView != null && container != null && floatingView.isAttachedToWindow()) {
            container.removeView(floatingView);
        }
        if (mContainer == container) {
            mContainer = null;
        }
        return this;
    }

    public BaseFloatingView getView() {
        return floatingView;
    }

    public FloatingViewManager layoutParams(ViewGroup.LayoutParams params) {
        if (floatingView != null) {
            floatingView.setLayoutParams(params);
        }
        return this;
    }

    private void addViewToWindow(final BaseFloatingView view) {
        if (mContainer == null) {
            return;
        }
        mContainer.addView(view);
    }

    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.END;
        params.setMargins(13, params.topMargin, params.rightMargin, 56);
        return params;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
