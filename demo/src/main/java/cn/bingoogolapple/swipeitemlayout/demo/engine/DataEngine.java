package cn.bingoogolapple.swipeitemlayout.demo.engine;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipeitemlayout.demo.model.NormalModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/26 上午1:03
 * 描述:
 */
public class DataEngine {

    public static List<NormalModel> loadNormalModelDatas() {
        List<NormalModel> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new NormalModel("title" + i, "detail" + i));
        }
        return datas;
    }

}