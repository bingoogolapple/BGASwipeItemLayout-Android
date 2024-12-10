:running:BGASwipeItemLayout-Android:running:
============

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-swipeitemlayout/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-swipeitemlayout)

类似iOS带弹簧效果的左右滑动控件，可作为AbsListView和RecyclerView的item。支持给BGASwipeItemLayout和其子控件设置margin和padding属性

### 效果图
![1-event](https://cloud.githubusercontent.com/assets/8949716/17476589/5a35c400-5d93-11e6-9de9-fbf6153dc5f3.gif)
![2-listview](https://cloud.githubusercontent.com/assets/8949716/17476591/5aad359e-5d93-11e6-9a1c-f96b3e72ce30.gif)
![3-recyclerview](https://cloud.githubusercontent.com/assets/8949716/17476592/5b5bcb54-5d93-11e6-8a81-34b114c32ca1.gif)

### Gradle依赖

```groovy
dependencies {
    compile 'com.android.support:support-v4:latestVersion'
    compile 'cn.bingoogolapple:bga-swipeitemlayout:latestVersion@aar'
}
```

### BGASwipeItemLayout方法说明

```java
/**
 * 以动画方式打开
 */
public void openWithAnim()

/**
 * 以动画方式关闭
 */
public void closeWithAnim()

/**
 * 直接打开
 */
public void open()

/**
 * 直接关闭。如果在AbsListView中删除已经打开的item时，请用该方法关闭item，否则重用item时有问题。RecyclerView中可以用该方法，也可以用closeWithAnim
 */
public void close()

/**
 * 当前是否为打开状态
 *
 * @return
 */
public boolean isOpened()

/**
 * 当前是否为关闭状态
 *
 * @return
 */
public boolean isClosed()

/**
 * 获取顶部视图
 *
 * @return
 */
public View getTopView()

/**
 * 获取底部视图
 *
 * @return
 */
public View getBottomView()

/**
 * 设置是否可滑动
 *
 * @return
 */
public void setSwipeAble(boolean swipeAble)
```

### BGASwipeItemLayoutDelegate接口说明

```java
/**
 * 变为打开状态
 *
 * @param swipeItemLayout
 */
void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout);

/**
 * 变为关闭状态
 *
 * @param swipeItemLayout
 */
void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout);

/**
 * 从关闭状态切换到正在打开状态
 *
 * @param swipeItemLayout
 */
void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout);
```

### 自定义属性说明

属性名 | 说明 | 默认值
:----------- | :----------- | :-----------
bga_sil_swipeDirection         | 往左滑还是往右滑为打开状态(left或right)        | left
bga_sil_bottomMode         | 底部视图展现方式(layDown或pullOut)        | pullOut
bga_sil_springDistance         | 弹簧距离        | 0dp
bga_sil_swipeAble         | 是否可左右滑动        | true

## 作者联系方式

| 个人主页 | 邮箱 |
| ------------- | ------------ |
| <a  href="https://www.bingoogolapple.cn" target="_blank">bingoogolapple.cn</a>  | <a href="mailto:bingoogolapple@gmail.com" target="_blank">bingoogolapple@gmail.com</a> |

| 个人微信号 | 微信群 | 公众号 |
| ------------ | ------------ | ------------ |
| <img width="180" alt="个人微信号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/BGAQrCode.png"> | <img width="180" alt="微信群" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/WeChatGroup1QrCode.jpg"> | <img width="180" alt="公众号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/GongZhongHao.png"> |

| 个人 QQ 号 | QQ 群 |
| ------------ | ------------ |
| <img width="180" alt="个人 QQ 号" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/BGAQQQrCode.jpg"> | <img width="180" alt="QQ 群" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/QQGroup1QrCode.jpg"> |

## 打赏支持作者

如果您觉得 BGA 系列开源库或工具软件帮您节省了大量的开发时间，可以扫描下方的二维码打赏支持。您的支持将鼓励我继续创作，打赏后还可以加我微信免费开通一年 [上帝小助手浏览器扩展/插件开发平台](https://github.com/bingoogolapple/bga-god-assistant-config) 的会员服务

| 微信 | QQ | 支付宝 |
| ------------- | ------------- | ------------- |
| <img width="180" alt="微信" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-wechat.jpg"> | <img width="180" alt="QQ" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-qq.jpg"> | <img width="180" alt="支付宝" src="https://github.com/bingoogolapple/bga-god-assistant-config/raw/main/images/donate-alipay.jpg"> |

## 作者项目推荐

* 欢迎您使用我开发的第一个独立开发软件产品 [上帝小助手浏览器扩展/插件开发平台](https://github.com/bingoogolapple/bga-god-assistant-config)

## License

    Copyright 2015 bingoogolapple

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
