package cn.bingoogolapple.swipeitemlayout.demo.adapter;

import android.content.Context;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.bingoogolapple.swipeitemlayout.demo.R;
import cn.bingoogolapple.swipeitemlayout.demo.model.NormalModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 16:31
 * 描述:
 */
public class RecyclerViewAdapter extends BGARecyclerViewAdapter<NormalModel> {
    private BGASwipeItemLayout mOpenedSil;

    public RecyclerViewAdapter(Context context) {
        super(context, R.layout.item_bgaswipe);
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_bgaswipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                // 有新的item打开是，关闭之前处于打开状态的item
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil = swipeItemLayout;
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                // item关闭时，清空已打开的item
                mOpenedSil = null;
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
        if (mOpenedSil != null) {
            mOpenedSil.closeWithAnim();
            mOpenedSil = null;
        }
    }
}