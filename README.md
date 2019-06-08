# navigation
Fragment导航框架
1.支持滑动返回
2.Fragment退出自动销毁
3.支持物理返回键事件

## 1.依赖

`implementation 'com.xqy.fragment.navigation:navigation:1.2.2'`

## 2.使用
* 创建navigation资源文件

 <img src="https://github.com/xqy666666/navigation/blob/master/navigation.png" width="200" height="400" />


* AppCompatActivity

```
class MainActivity : AppCompatActivity(), NavigatorCallback{//实现NavigatorCallback接口

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNavigator(R.navigation.nav)//创建navigator

    }
}
```
* Fragment

```
class LoanFragment:Fragment() {
    private lateinit var callback: NavigatorCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as NavigatorCallback
    }
}
```
> 在需要切换页面的逻辑处调用callback.navigate(R.id.login_fragment)

 <img src="https://github.com/xqy666666/navigation/blob/master/navigation.gif" width="200" height="400" />

