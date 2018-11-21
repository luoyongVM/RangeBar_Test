# RangeBar_Test
项目中主要用来做音频打标签和AB复读功能

![img](https://github.com/luoyongVM/RangeBar_Test/blob/master/raw/L5JjqSqS44.gif)

Examples
=======

## Layout XML

```xml
 <com.lygit.rangebar.RangeBar
            android:id="@+id/rangebar"
            android:layout_width="match_parent"
            android:layout_height="72dp"
            custom:tickStart="0"
            custom:tickInterval="1"
            custom:tickEnd="20"
            custom:temporaryPins="false"
            custom:pinMaxFont="10sp"
            android:layout_marginLeft="16dp"
            android:layout_marginRight="16dp"
            />
```

## Adding a rangebar listener
- Add a listener - rangeBar.setOnRangeBarChangeListener which returns left and right index as well as value.
```java
rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
            }

        });
```
## Adding a text formatter
Formats the text inside the pin.
- Add a formater - IRangeBarFormatter which will return the value of the current text inside of the pin
- Transform string s into any string you want and return the newly formated string. 
```java
rangebar.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String s) {
            // 格式化你想要展示的字符串，将会展示在滑块上
                return s;
            }
        });
```

How to Use
=======
## Method I
**In your app build.gradle. Add the following lines**

```
	dependencies {
	        implementation 'com.lzy.design:RangeBarLib:1.0.1'
	}
```
## Method II
**In your project build.gradle. Add the following lines**
```maven
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**In your app build.gradle. Add the following lines**

```
	dependencies {
	        implementation 'com.github.luoyongVM:RangeBar_Test:v1.1.0'
	}
```


**if you are already using android support library inside your project and run into multiple version issues related to android support library then modify the gradle path like this**
```
dependencies {
    compile ('com.github.luoyongVM:RangeBar_Test:Tag') {
            exclude module: 'support-compat'
    }
}
```

个人博客地址：[https://blog.csdn.net/luoyong_blog](https://blog.csdn.net/luoyong_blog)
