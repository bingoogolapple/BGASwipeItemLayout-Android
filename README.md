:running:BGASwipeItemLayout-Android v1.0.0:running:
============
带弹簧效果的左右滑动控件、作为ListView和RecyclerView的Item左右滑动过程中不会触发长按和点击事件。
（作为AdapterView的item时的点击事件参考https://github.com/daimajia/AndroidSwipeLayout）

写这个控件的初衷：产品经理要求在列表中的item展开时的EditText中编辑并提交，最开始是使用代码家的AndroidSwipeLayout+ListView实现的，
但是在魅族3和魅族4上运行时，ListView中的EditText没法弹出键盘，所以就换成了代码家的AndroidSwipeLayout+RecyclerView实现，但是左右滑动
过程中会触发点击和长按item事件，然后单身:dog:从520那天就开始搞这个控件了:joy:

#### 效果图
![Image of 测试各种事件](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/1-event.gif)
![Image of ListViewDemo](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/2-listview.gif)
![Image of RecyclerViewDemo](https://raw.githubusercontent.com/bingoogolapple/BGASwipeItemLayout-Android/master/screenshots/3-recyclerview.gif)

>Gradle

```groovy
dependencies {
    compile 'cn.bingoogolapple:bga-swipeitemlayout:1.0.0@aar'
}
```

##### 详细用法请查看[Demo](https://github.com/bingoogolapple/BGASwipeItemLayout-Android/tree/master/demo):feet:

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
