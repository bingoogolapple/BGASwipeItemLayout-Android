package cn.bingoogolapple.swipeitemlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/26 上午2:07
 * 描述:适用于AdapterView和RecyclerView的水平方向滑动item。【AdapterView的item单击和长按参考代码家https://github.com/daimajia/AndroidSwipeLayout】
 */
public class BGASwipeItemLayout extends RelativeLayout {
    private static final String TAG = BGASwipeItemLayout.class.getSimpleName();
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_OPEN_CLOSE = "status_open_close";
    private static final int VEL_THRESHOLD = 400;
    private ViewDragHelper mDragHelper;
    // 顶部视图
    private View mTopView;
    // 底部视图
    private View mBottomView;
    // 拖动的弹簧距离
    private int mSpringDistance = 0;
    // 允许拖动的距离【注意：最终允许拖动的距离是 (mDragRange + mSpringDistance)】
    private int mDragRange;
    // 控件滑动方向（向左，向右），默认向左滑动
    private SwipeDirection mSwipeDirection = SwipeDirection.Left;
    // 移动过程中，底部视图的移动方式（拉出，被顶部视图遮住），默认是被顶部视图遮住
    private BottomModel mBottomModel = BottomModel.PullOut;
    // 滑动控件当前的状态（打开，关闭，正在移动），默认是关闭状态
    private Status mCurrentStatus = Status.Closed;
    // 滑动控件滑动前的状态
    private Status mPreStatus = mCurrentStatus;
    // 顶部视图下一次layout时的left
    private int mTopLeft;
    // 顶部视图外边距
    private MarginLayoutParams mTopLp;
    // 底部视图外边距
    private MarginLayoutParams mBottomLp;
    // 滑动比例，【关闭->展开  =>  0->1】
    private float mDragRatio;
    // 手动拖动打开和关闭代理
    private BGASwipeItemLayoutDelegate mDelegate;

    private GestureDetectorCompat mGestureDetectorCompat;
    private OnLongClickListener mOnLongClickListener;
    private OnClickListener mOnClickListener;

    public BGASwipeItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGASwipeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initProperty();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGASwipeItemLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGASwipeItemLayout_bga_sil_swipeDirection) {
            // 默认向左滑动
            int leftSwipeDirection = typedArray.getInt(attr, mSwipeDirection.ordinal());

            if (leftSwipeDirection == SwipeDirection.Right.ordinal()) {
                mSwipeDirection = SwipeDirection.Right;
            }
        } else if (attr == R.styleable.BGASwipeItemLayout_bga_sil_bottomMode) {
            // 默认是拉出
            int pullOutBottomMode = typedArray.getInt(attr, mBottomModel.ordinal());

            if (pullOutBottomMode == BottomModel.LayDown.ordinal()) {
                mBottomModel = BottomModel.LayDown;
            }
        } else if (attr == R.styleable.BGASwipeItemLayout_bga_sil_springDistance) {
            // 弹簧距离，不能小于0，默认值为0
            mSpringDistance = typedArray.getDimensionPixelSize(attr, mSpringDistance);
            if (mSpringDistance < 0) {
                throw new RuntimeException("bga_sil_springDistance不能小于0");
            }
        }
    }

    private void initProperty() {
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), mSimpleOnGestureListener);
    }

    public void setDelegate(BGASwipeItemLayoutDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException(BGASwipeItemLayout.class.getSimpleName() + "必须有且只有两个子控件");
        }
        mTopView = getChildAt(1);
        mBottomView = getChildAt(0);
        // 避免底部视图被隐藏时还能获取焦点被点击
        mBottomView.setVisibility(INVISIBLE);

        mTopLp = (MarginLayoutParams) mTopView.getLayoutParams();
        mBottomLp = (MarginLayoutParams) mBottomView.getLayoutParams();
        mTopLeft = getPaddingLeft() + mTopLp.leftMargin;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetectorCompat.onTouchEvent(ev);
    }

    // -----------------------------------参考代码家AndroidSwipeLayout开始---------------------------------------------
    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        mOnLongClickListener = l;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (insideAdapterView()) {
            if (mOnClickListener == null) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performAdapterViewItemClick();
                    }
                });
            }
            if (mOnLongClickListener == null) {
                setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        performAdapterViewItemLongClick();
                        return true;
                    }
                });
            }
        }
    }

    private void performAdapterViewItemClick() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            AdapterView view = (AdapterView) t;
            int p = view.getPositionForView(BGASwipeItemLayout.this);
            if (p != AdapterView.INVALID_POSITION) {
                view.performItemClick(view.getChildAt(p - view.getFirstVisiblePosition()), p, view.getAdapter().getItemId(p));
            }
        }
    }

    private boolean performAdapterViewItemLongClick() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            AdapterView view = (AdapterView) t;
            int p = view.getPositionForView(BGASwipeItemLayout.this);
            if (p == AdapterView.INVALID_POSITION) return false;
            long vId = view.getItemIdAtPosition(p);
            boolean handled = false;
            try {
                Method m = AbsListView.class.getDeclaredMethod("performLongPress", View.class, int.class, long.class);
                m.setAccessible(true);
                handled = (boolean) m.invoke(view, BGASwipeItemLayout.this, p, vId);

            } catch (Exception e) {
                e.printStackTrace();

                if (view.getOnItemLongClickListener() != null) {
                    handled = view.getOnItemLongClickListener().onItemLongClick(view, BGASwipeItemLayout.this, p, vId);
                }
                if (handled) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }
            }
            return handled;
        }
        return false;
    }

    private boolean insideAdapterView() {
        return getAdapterView() != null;
    }

    private AdapterView getAdapterView() {
        ViewParent t = getParent();
        if (t instanceof AdapterView) {
            return (AdapterView) t;
        }
        return null;
    }
    // -----------------------------------参考代码家AndroidSwipeLayout结束---------------------------------------------

    private void requestParentDisallowInterceptTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || Math.abs(distanceY) > 15) {
                requestParentDisallowInterceptTouchEvent();
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                requestParentDisallowInterceptTouchEvent();
                return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // 2
            setPressed(false);
            if (isClosed()) {
                return performClick();
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 1
            if (isClosed()) {
                setPressed(true);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isClosed()) {
                setPressed(true);
                postDelayed(mCancelPressedTask, 300);
                performLongClick();
            }
        }

        // 作为ListView或者RecyclerView的item，双击事件很少，这里就不处理双击事件了╮(╯_╰)╭
        public boolean onDoubleTap(MotionEvent e) {
            if (isClosed()) {
                setPressed(true);
            }
            return false;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            setPressed(false);
            return false;
        }
    };

    private Runnable mCancelPressedTask = new Runnable() {
        @Override
        public void run() {
            setPressed(false);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        mGestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = mBottomView.getMeasuredWidth() + mBottomLp.leftMargin + mBottomLp.rightMargin;

        int topTop = getPaddingTop() + mTopLp.topMargin;
        int topBottom = topTop + mTopView.getMeasuredHeight();
        int topRight = mTopLeft + mTopView.getMeasuredWidth();

        int bottomTop = getPaddingTop() + mBottomLp.topMargin;
        int bottomBottom = bottomTop + mBottomView.getMeasuredHeight();
        int bottomLeft;
        int bottomRight;

        if (mSwipeDirection == SwipeDirection.Left) {
            // 向左滑动

            if (mBottomModel == BottomModel.LayDown) {
                // 遮罩，位置固定不变（先计算right，然后根据right计算left）

                bottomRight = r - getPaddingRight() - mBottomLp.rightMargin;
                bottomLeft = bottomRight - mBottomView.getMeasuredWidth();
            } else {
                // 拉出，位置随顶部视图的位置改变

                // 根据顶部视图的left计算底部视图的left
                bottomLeft = mTopLeft + mTopView.getMeasuredWidth() + mTopLp.rightMargin + mBottomLp.leftMargin;

                // 底部视图的left被允许的最小值
                int minBottomLeft = r - getPaddingRight() - mBottomView.getMeasuredWidth() - mBottomLp.rightMargin;
                // 获取最终的left
                bottomLeft = Math.max(bottomLeft, minBottomLeft);
                // 根据left计算right
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        } else {
            // 向右滑动

            if (mBottomModel == BottomModel.LayDown) {
                // 遮罩，位置固定不变（先计算left，然后根据left计算right）

                bottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            } else {
                // 拉出，位置随顶部视图的位置改变

                // 根据顶部视图的left计算底部视图的left
                bottomLeft = mTopLeft - mDragRange;
                // 底部视图的left被允许的最大值
                int maxBottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                // 获取最终的left
                bottomLeft = Math.min(maxBottomLeft, bottomLeft);
                // 根据left计算right
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        }

        mBottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);
        mTopView.layout(mTopLeft, topTop, topRight, topBottom);
    }

    public void openWithAnim() {
        smoothSlideTo(1);
    }

    public void closeWithAnim() {
        smoothSlideTo(0);
    }

    public void open() {
        slideTo(1);
    }

    public void close() {
        slideTo(0);
    }

    public View getTopView() {
        return mTopView;
    }

    public View getBottomView() {
        return mBottomView;
    }

    /**
     * 打开或关闭滑动控件
     *
     * @param isOpen 1表示打开，0表示关闭
     */
    private void smoothSlideTo(int isOpen) {
        if (mDragHelper.smoothSlideViewTo(mTopView, getCloseOrOpenTopViewFinalLeft(isOpen), getPaddingTop() + mTopLp.topMargin)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开或关闭滑动控件
     *
     * @param isOpen 1表示打开，0表示关闭
     */
    private void slideTo(int isOpen) {
        if (isOpen == 1) {
            mBottomView.setVisibility(VISIBLE);
            ViewCompat.setAlpha(mBottomView, 1.0f);
            mCurrentStatus = Status.Opened;
            if (mDelegate != null) {
                mDelegate.onBGASwipeItemLayoutOpened(this);
            }
        } else {
            mBottomView.setVisibility(INVISIBLE);
            mCurrentStatus = Status.Closed;
            if (mDelegate != null) {
                mDelegate.onBGASwipeItemLayoutClosed(this);
            }
        }
        mPreStatus = mCurrentStatus;
        mTopLeft = getCloseOrOpenTopViewFinalLeft(isOpen);
        requestLayout();
    }

    private int getCloseOrOpenTopViewFinalLeft(int isOpen) {
        int left = getPaddingLeft() + mTopLp.leftMargin;
        if (mSwipeDirection == SwipeDirection.Left) {
            left = left - isOpen * mDragRange;
        } else {
            left = left + isOpen * mDragRange;
        }
        return left;
    }

    public boolean isOpened() {
        return (mCurrentStatus == Status.Opened) || (mCurrentStatus == Status.Moving && mPreStatus == Status.Opened);
    }

    public boolean isClosed() {
        return mCurrentStatus == Status.Closed || (mCurrentStatus == Status.Moving && mPreStatus == Status.Closed);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putInt(STATUS_OPEN_CLOSE, mCurrentStatus.ordinal());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.getInt(STATUS_OPEN_CLOSE) == Status.Opened.ordinal()) {
                open();
            } else {
                close();
            }
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTopView;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 这里要返回控件的getPaddingTop() + mTopLp.topMargin，否则有margin和padding快速滑动松手时会上下跳动
            return getPaddingTop() + mTopLp.topMargin;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange + mSpringDistance;
        }

        /**
         *
         * @param child
         * @param left ViewDragHelper帮我们计算的当前所捕获的控件的left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int minTopLeft;
            int maxTopLeft;

            if (mSwipeDirection == SwipeDirection.Left) {
                // 向左滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin - (mDragRange + mSpringDistance);
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin;
            } else {
                // 向右滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin;
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin + (mDragRange + mSpringDistance);
            }

            return Math.min(Math.max(minTopLeft, left), maxTopLeft);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTopLeft = left;

            // 此时顶部视图水平方向偏移量的绝对值
            int topViewHorizontalOffset = Math.abs(mTopLeft - (getPaddingLeft() + mTopLp.leftMargin));
            if (topViewHorizontalOffset > mDragRange) {
                mDragRatio = 1.0f;
            } else {
                mDragRatio = 1.0f * topViewHorizontalOffset / mDragRange;
            }

            // 处理底部视图的透明度
            float alpha = 0.1f + 0.9f * mDragRatio;
            ViewCompat.setAlpha(mBottomView, alpha);

            dispatchSwipeEvent();

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 默认关闭，接下来再判断为打开时的条件
            int finalLeft = getPaddingLeft() + mTopLp.leftMargin;
            if (mSwipeDirection == SwipeDirection.Left) {
                // 向左滑动为打开，向右滑动为关闭

                if (xvel < -VEL_THRESHOLD || (mPreStatus == Status.Closed && xvel < VEL_THRESHOLD && mDragRatio >= 0.3f) || (mPreStatus == Status.Opened && xvel < VEL_THRESHOLD && mDragRatio >= 0.7f)) {
                    // 向左的速度达到条件
                    finalLeft -= mDragRange;
                }

            } else {
                // 向左滑动为关闭，向右滑动为打开

                if (xvel > VEL_THRESHOLD || (mPreStatus == Status.Closed && xvel > -VEL_THRESHOLD && mDragRatio >= 0.3f) || (mPreStatus == Status.Opened && xvel > -VEL_THRESHOLD && mDragRatio >= 0.7f)) {
                    finalLeft += mDragRange;
                }
            }
            mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop() + mTopLp.topMargin);

            // 要执行下面的代码，不然不会自动收缩完毕或展开完毕
            ViewCompat.postInvalidateOnAnimation(BGASwipeItemLayout.this);
        }

    };

    private void dispatchSwipeEvent() {
        Status preStatus = mCurrentStatus;
        updateCurrentStatus();
        if (mCurrentStatus != preStatus) {
            if (mCurrentStatus == Status.Closed) {
                mBottomView.setVisibility(INVISIBLE);
                if (mDelegate != null && mPreStatus != mCurrentStatus) {
                    mDelegate.onBGASwipeItemLayoutClosed(this);
                }
                mPreStatus = Status.Closed;
            }
            if (mCurrentStatus == Status.Opened) {
                if (mDelegate != null && mPreStatus != mCurrentStatus) {
                    mDelegate.onBGASwipeItemLayoutOpened(this);
                }
                mPreStatus = Status.Opened;
            } else if (preStatus == Status.Closed) {
                mBottomView.setVisibility(VISIBLE);
                if (mDelegate != null) {
                    mDelegate.onBGASwipeItemLayoutStartOpen(this);
                }
            }
        }
    }

    private void updateCurrentStatus() {
        if (mSwipeDirection == SwipeDirection.Left) {
            // 向左滑动

            if (mTopLeft == getPaddingLeft() + mTopLp.leftMargin - mDragRange) {
                mCurrentStatus = Status.Opened;
            } else if (mTopLeft == getPaddingLeft() + mTopLp.leftMargin) {
                mCurrentStatus = Status.Closed;
            } else {
                mCurrentStatus = Status.Moving;
            }
        } else {
            // 向右滑动

            if (mTopLeft == getPaddingLeft() + mTopLp.leftMargin + mDragRange) {
                mCurrentStatus = Status.Opened;
            } else if (mTopLeft == getPaddingLeft() + mTopLp.leftMargin) {
                mCurrentStatus = Status.Closed;
            } else {
                mCurrentStatus = Status.Moving;
            }
        }
    }


    public enum SwipeDirection {
        Left, Right
    }

    public enum BottomModel {
        PullOut, LayDown
    }

    public enum Status {
        Opened, Closed, Moving
    }

    public interface BGASwipeItemLayoutDelegate {
        void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout);

        void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout);

        void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout);
    }

}