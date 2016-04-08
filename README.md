:running:BGASwipeItemLayout-Android:running:
============

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-swipeitemlayout/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-swipeitemlayout)

类似iOS带弹簧效果的左右滑动控件，可作为AbsListView和RecyclerView的item。支持给BGASwipeItemLayout和其子控件设置margin和padding属性

### 效果图
![Image of 测试各种事件](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/1-event.gif)
![Image of ListViewDemo](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/2-listview.gif)
![Image of RecyclerViewDemo](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/3-recyclerview.gif)

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

### 关于我

| 新浪微博 | 个人主页 | 邮箱 | BGA系列开源库QQ群 |
| ------------ | ------------- | ------------ | ------------ |
| <a href="http://weibo.com/bingoogol" target="_blank">bingoogolapple</a> | <a  href="http://www.bingoogolapple.cn" target="_blank">bingoogolapple.cn</a>  | <a href="mailto:bingoogolapple@gmail.com" target="_blank">bingoogolapple@gmail.com</a> | ![BGA_CODE_CLUB](http://7xk9dj.com1.z0.glb.clouddn.com/BGA_CODE_CLUB.png?imageView2/2/w/200) |

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
