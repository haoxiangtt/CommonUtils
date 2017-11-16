安卓应用工具包说明：
	此项目集成大部分通用的工具包，主要包括：
	多文件：
	1、cn.richinfo.utils.bitmap包：图片处理工具、图片选择器，目前只支持单选，封装了截屏、图片裁剪、从相册中获取图片等功能；
	2、cn.richinfo.utils.dualsim包：双卡手机sim卡信息获取工具包，用于获取双卡手机sim卡的imsi、imei等数据信息；
	3、cn.richinfo.utils.spanparse包：图文混排解析工具，可以用于制作表情包子的工具；
	4、cn.richinfo.utils.secret包：数据加密解密工具，对数据进行base64、MD5、AES、DES和RSA加密解密处理；
	5、cn.richinfo.utils.time包：时间处理工具，将时间转换为指定的格式输出；
	6、cn.richinfo.utils.customsharepreference：自定义SharedPreference存储组件，可以选定文件存储位置；

	单文件：
	1、cn.richinfo.utils.ActivityManager：处理Activity栈的工具；
	2、cn.richinfo.utils.AlarmTaskController：设置定时任务的工具；
	3、cn.richinfo.utils.DialogUtil：Dialog 简易对话框工具类；
	4、cn.richinfo.utils.FileUtils：文件操作相关工具类；
	5、cn.richinfo.utils.FragmentUserVisibleController：Fragment的mUserVisibleHint属性控制器，用于准确的监听Fragment是否对用户可见；
	6、cn.richinfo.utils.GZIPCompressUtil：zip压缩解压缩工具类；
	7、cn.richinfo.utils.LogUtils：日志打印工具；
	8、cn.richinfo.utils.MessageUtil：消息提示工具；
	9、cn.richinfo.utils.MobileUtil：判断手机号码；
	10、cn.richinfo.utils.NetworkState/cn.richinfo.utils.NetworkUtils：网络相关工具类；
	11、cn.richinfo.utils.PackageUtils：包管理器工具；
	12、cn.richinfo.utils.PermissionUtil：授权管理工具；
	13、cn.richinfo.utils.Platform：简易线程池管理工具；
	14、cn.richinfo.utils.ReflectUtil：反射类相关工具；
	15、cn.richinfo.utils.SharePersistent：存储封装类工具；
	16、cn.richinfo.utils.StrictModeUtil：严格模式管理工具；
	17、cn.richinfo.utils.ProcessUtil：进程相关工具类。
	18、cn.richinfo.utils.KeyboardUtil：自定义键盘生成工具；
    19、cn.richinfo.utils.AppUtils：App相关工具类，获取签名、安装应用、启动应用等；
    20、cn.richinfo.utils.CleanUtils：文件、数据库缓存清除相关工具类；
	21、cn.richinfo.utils.FileIOUtils：文件读写相关工具类；
	22、cn.richinfo.utils.IntentUtils：意图操作相关工具类；
    23、cn.richinfo.utils.ShellUtils：Shell相关工具类；
    24、cn.richinfo.utils.CrashUtils：崩溃相关工具类；
    25、cn.richinfo.utils.SizeUtils：尺寸相关工具类；

目前工具包中的部分类不够完善，欢迎大家一起扩展完善。