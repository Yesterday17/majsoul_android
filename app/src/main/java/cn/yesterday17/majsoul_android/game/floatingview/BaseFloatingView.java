package cn.yesterday17.majsoul_android.game.floatingview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

/**
 * 修改自 https://github.com/leotyndale/EnFloatingView
 */
public abstract class BaseFloatingView extends FrameLayout {
    public static final int MARGIN_EDGE = 13;

    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;

    private static final int TOUCH_TIME_THRESHOLD = 150;
    private static final int TOUCH_TIME_THRESHOLD_LONG = 750;
    private long mLastTouchDownTime;

    protected MoveAnimator mMoveAnimator;

    protected int mScreenWidth;
    private int mScreenHeight;

    public BaseFloatingView(@NonNull Context context, int resource) {
        super(context);
        mMoveAnimator = new MoveAnimator();
        setClickable(true);
        inflate(context, resource, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeOriginalTouchParams(event);
                updateSize();
                mMoveAnimator.stop();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition(event);
                break;
            case MotionEvent.ACTION_UP:
                moveToEdge();
                if (!isDragEvent()) {
                    if (isOnClickEvent()) {
                        onClick();
                    } else if (isOnLongPressEvent()) {
                        onLongPress();
                    }
                }
                break;
        }
        return true;
    }

    protected abstract void onClick();

    protected abstract void onLongPress();

    private boolean isDragEvent() {
        return Math.abs(mOriginalX - getX()) > 20 || Math.abs(mOriginalY - getY()) > 20;
    }

    private boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    private boolean isOnLongPressEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime > TOUCH_TIME_THRESHOLD_LONG;
    }

    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        } else if (desY < 0) {
            desY = 0;
        }
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels - this.getWidth();
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public void moveToEdge() {
        float moveDistance = isNearestLeft() ? MARGIN_EDGE : mScreenWidth - MARGIN_EDGE;
        mMoveAnimator.start(moveDistance, getY());
    }

    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        return getX() < middle;
    }

    private void move(float deltaX, float deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - getY()) * progress;
            move(deltaX, deltaY);
            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }
}
