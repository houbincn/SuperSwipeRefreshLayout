package com.bin.superswiperefreshlayout.library;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by lihoubin on 15/8/16.
 */
public class SuperSwipeRefreshLayout extends SwipeRefreshLayout {
    private static final String TAG="SuperSwipeRefreshLayoutTag";
    private OnLoadListener mOnLoadListener;
    private int mTouchSlop;
    private boolean mLoading;
    private View footerView;

    private Context context;

    private boolean canLoad;
    private boolean canRefresh;

    public SuperSwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public SuperSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    private void init(Context context) {
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        this.context=context;
    }
    public void setMode(Mode mode){
        switch (mode){
            case LOAD:
                canLoad=true;
                canRefresh=false;
                break;
            case REFRESH:
                canRefresh=true;
                canLoad=false;
                break;
            case BOTH:
                canLoad=true;
                canRefresh=true;
                break;
        }
        if(canLoad){
            addFooterView();
            getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if(dY>=mTouchSlop){
                        if(scrollState==SCROLL_STATE_IDLE){
                            if(canLoadMore()){
                                setLoading(true);
                            }
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }
    public void addFooterView(){
        if(getListView()!=null){
            footerView= View.inflate(context,R.layout.footer_view,null);
            getListView().addFooterView(footerView);
        }
    }
    public void setOnLoadListener(OnLoadListener mOnLoadListener) {
        this.mOnLoadListener = mOnLoadListener;
    }
    private float startY,endY;
    private float dY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action= MotionEventCompat.getActionMasked(ev);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                endY=ev.getRawY();
                dY=startY-endY;
                break;
        }
        if(!canRefresh)setRefreshing(false);
        return super.dispatchTouchEvent(ev);
    }

    private ListView getListView(){
        if(getChildCount()==0)return null;
        for(int i=0;i<getChildCount();i++){
           if(getChildAt(i) instanceof  ListView){
               return (ListView)getChildAt(i);
           }
        }
        return null;
    }
    public void setLoading(boolean loading) {
        if (getListView() == null) return;
        this.mLoading=loading;
        if (loading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
            footerView.setVisibility(VISIBLE);
            getListView().setSelection(getListView().getAdapter().getCount() - 1);
            if(mOnLoadListener!=null){
                mOnLoadListener.onLoad();
            }
        } else {
            startY = 0;
            endY = 0;
            footerView.setVisibility(GONE);
        }
    }
    private boolean canLoadMore() {
        return isBottom()&&canLoad&&!mLoading&&!isRefreshing();
    }
    private boolean isBottom() {
        if (getListView().getCount() > 0) {
            if (getListView().getLastVisiblePosition() == getListView().getAdapter().getCount() - 1 &&
                    getListView().getChildAt(getListView().getChildCount() - 1).getBottom() <= getListView().getHeight()) {
                return true;
            }
        }
        return false;
    }
    public static enum Mode {
        LOAD(0x1),
        REFRESH(0x2),
        BOTH(0x3);
        private int mIntValue;
        Mode(int modeInt) {
            mIntValue = modeInt;
        }
        int getIntValue() {
            return mIntValue;
        }

    }
    public interface OnLoadListener{
        void onLoad();
    }
}
