# YearPicker
年份选择器

![image](https://github.com/huweijian5/YearPicker/blob/master/screenshots/device-2016-09-29-221318.png)


###示例（参考demo）
 ```
  <com.hwj.juneng.yp.YearPicker
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/yp_year"
        android:layout_centerHorizontal="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:yp_selectedColor="#1273f0"
        app:yp_pointerColor="#00ffff"
        />
 ```

###为了更好地自定义，开放了如下的自定义属性，可根据实际要求设置
 ```
 <declare-styleable name="YearPicker">
        <!--选中年份的颜色-->
        <attr name="yp_selectedColor" format="color"></attr>
        <!--选中年份文字的大小-->
        <attr name="yp_selectedSize" format="dimension"></attr>
        <!--未选中年份的透明度 0`255-->
        <attr name="yp_unselectedAlpha" format="integer"></attr>
        <!--未选中年份文字的大小-->
        <attr name="yp_unselectedSize" format="dimension"></attr>
        <!--背景颜色-->
        <attr name="yp_bgColor" format="color"></attr>
        <!--指针颜色-->
        <attr name="yp_pointerColor" format="color"></attr>
        <!--指针可见性-->
        <attr name="yp_pointerVisibility" format="enum">
            <enum name="visible" value="1" />
            <enum name="gone" value="0" />
        </attr>
        <!--指针占控件的高度比例-->
        <attr name="yp_pointerHeightScale" format="float"></attr>
        <!--指针占控件的宽度比例-->
        <attr name="yp_pointerwidthScale" format="float"></attr>
        <!--年份的上下边距-->
        <attr name="yp_padding" format="dimension"></attr>
        <!--默认选中的年份-->
        <attr name="yp_defaultYear" format="integer"></attr>
        <!--两个年份之间的间距-->
        <attr name="yp_yearGap" format="dimension"></attr>
        <!--能选中的最大的年份-->
        <attr name="yp_maxYear" format="integer"></attr>
        <!--能选中的最小的年份-->
        <attr name="yp_minYear" format="integer"></attr>
        <!--年份的对齐方式-->
        <attr name="yp_alignMode" format="enum">
            <enum name="center" value="0" />
            <enum name="bottom" value="1" />
        </attr>
    </declare-styleable>
 ``` 


###直接引用此控件的步骤

Step 1.Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.huweijian5:YearPicker:latest_version'
	}
* 其中latest_version请到[releases](https://github.com/huweijian5/YearPicker/releases)中查看
