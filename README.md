# ZuoMvp  

 **一个懒人模块化 mvp 框架,可以拿来即用,代码规范标准,已内嵌了大量开发中常用的功能**  
 
 ## 初衷: Android 开发 模块化与基础框架纷繁,实现方式众多,力求打造一个极简,功能齐全,拿来即用的框架,适用绝大部分功能,拓展性好,封装度高;
 ## 原则: 力求开发使用中极简,拒绝花里胡哨
  
## lib_common包含的模块: 
1.mosby/mvp框架的延伸与拓展;  
2.刷新与加载列表基类 mvp;  
3.glide 简单封装; 
4.model 基础类;  
5.rxjava3+okhttp3+retrofit2 的网络请求封装; 
6.utils工具包;  
7.arouter:阿里路由配置;  

## 使用:继承 BaseActivity举例 (BaseFragmnet同理):  
1,继承BaseActivity会通用一个根布局文件,包含了头布局,空布局,网络异常,数据异常(下拉刷新和加载更多默认关闭,只有在列表界面才默认开启),默认框架自行处理显示方式,这样的好处便是便于统一维护,通过显隐拓展性很强;  
2 只要设置setEventTag(this) 方法,默认注册eventbus,解绑已经默认处理了;  
3,权限处理调用,rxPermissions(String tip, String... permissions),重写rxPermissionsSuccess()便可以得到成功结果,失败默认跳转权限设置;  
4,扫一扫,选图扫码默认放在扫描界面,获取权限后只需调用startScan(),重写onScanSuccess(String result) onScanFailed() 便可以得到结果;  
5,多图选择,或者视频:获取权限后调用matisseSelect(et<MimeType> mimeTypes, int maxSelect),拍照默认在选择页面,重写onMatisseSelectResult(List<String> urls) 得到结果即可;  
 
BaseRecycleViewActivity 继承于 BaseActivity,用的是强大的BaseRecyclerViewAdapterHelper结合SmartRefreshLayout,强强联合,所以列表处理界面极为简单,可以实现各种复杂的界面;  
1, 布局无需重写,目前布局适用任何布局界面;  
2, createPresenter() 定义Presenter;  
3, createAdapter(), 创建适配器;  
4, loadData(boolean isLoadMore)  请求网络数据;  
就这么简单,结合headview,footview,极为复杂的界面都可完成,因为力求开发简单,基类做了很多通用的处理,如果不需要,自行去掉;例如阿里路由绑定,ButterKnife绑定等;  

## 网络: rxjava3+okhttp3+retrofit2 的网络请求封装 使用:
forNet(NetService.getInstance().getCommonService().getNews(getNetJson().set_token().set_object("status","0").set_object("pageIndex", page).builder()), new MineObserver<NewsDetail>(mMvpView) ());	
### 一行完成就这么简单(MineObserver 默认处理 onSubscribe(),onError(),onComplete())

### 嵌入的三方:在config.gradle 可查看全部;
### 模块化处理: 根据isModule来处理模块之间的关系,具体请尝试,新lib 参照homelib,只需定义applicationId,新建module包下清单即可;

## 细节处理:
1,当新加的依赖冲突,不建议个例化处理,建议放在module.build.gradle处理 ,在此运用了一个例子:glide-transformations与dialog 高斯模糊.so冲突;解决参照  
2fileprovider,前面已applicationId前缀,防止多包不能共存的问题  
3,依赖里butterknife 问题,建议用GenerateFindViewById   
4,一些values包下文件文件位置存放,路由的配置等等  

## tips*:
### 在此感谢我依赖库所有的大神!因为时间比较紧,示例代码没有展示全面并且很比较粗糙,起名随意,但是不影响使用的便捷



 

