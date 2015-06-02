package cn.bingoogolapple.swipeitemlayout.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;
import android.widget.ListView;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.bingoogolapple.swipeitemlayout.demo.R;
import cn.bingoogolapple.swipeitemlayout.demo.model.NormalModel;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class AdapterViewAdapter extends BGAAdapterViewAdapter<NormalModel> {
    /**
     * 当前处于打开状态的item
     */
    private BGASwipeItemLayout mOpenedSil;

    public AdapterViewAdapter(Context context) {
        super(context, R.layout.item_bgaswipe);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_bgaswipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                ViewParent parent = swipeItemLayout.getParent();
                if (parent != null && parent instanceof ListView) {
                    ListView listView = (ListView) parent;
                    BGASwipeItemLayout item;
                    for (int i = 0; i < listView.getChildCount(); i++) {
                        View view = listView.getChildAt(i);
                        if (view instanceof BGASwipeItemLayout && view != swipeItemLayout) {
                            item = (BGASwipeItemLayout) view;
                            if (!item.isClosed()) {
                                item.closeWithAnim();
                            }
                        }
                    }
                }
                mOpenedSil = swipeItemLayout;
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
            }
        });
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_bgaswipe_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_bgaswipe_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, NormalModel model) {
        closeOpenedSwipeItemLayoutWithAnim();
        viewHolderHelper.setText(R.id.tv_item_bgaswipe_title, model.mTitle).setText(R.id.tv_item_bgaswipe_detail, model.mDetail).setText(R.id.et_item_bgaswipe_title, model.mTitle);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        if (mOpenedSil != null && mOpenedSil.isOpened()) {
            mOpenedSil.closeWithAnim();
        }
    }
}