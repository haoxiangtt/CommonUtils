安卓应用工具包说明：
	此项目集成大部分通用的工具包，主要包括：
	多文件：
	1、cn.richinfo.utils.bitmap包：图片处理工具、图片选择器，目前只支持单选，封装了截屏、图片裁剪、从相册中获取图片等功能；
	2、cn.richinfo.utils.dualsim包：双卡手机sim卡信息获取工具包，用于获取双卡手机sim卡的imsi、imei等数据信息；
	3、cn.richinfo.utils.parse包：图文混排解析工具，可以用于制作表情包子的工具；
	4、cn.richinfo.utils.secret包：数据加密解密工具，对数据进行base64、MD5、AES、DES和RSA加密解密处理；
	5、cn.richinfo.utils.time包：时间处理工具，将时间转换为指定的格式输出；
	6、cn.richinfo.utils.customsharepreference：自定义SharedPreference存储组件，可以选定文件存储位置；

	单文件：
	1、cn.richinfo.utils.ActivityManager：处理Activity栈的工具；
	2、cn.richinfo.utils.AlarmTaskController：设置定时任务的工具；
	3、cn.richinfo.utils.DialogUtil：Dialog 简易对话框工具类；
	4、cn.richinfo.utils.FileUtil：文件处理工具；
	5、cn.richinfo.utils.FragmentUserVisibleController：Fragment的mUserVisibleHint属性控制器，用于准确的监听Fragment是否对用户可见；
	6、cn.richinfo.utils.GZIPCompressUtil：zip压缩解压缩工具类；
	7、cn.richinfo.utils.LogUtils：日志打印工具；
	8、cn.richinfo.utils.MessageUtil：消息提示工具；
	9、cn.richinfo.utils.MobileUtil：判断手机号码；
	10、cn.richinfo.utils.NetworkState：监测网络状态工具；
	11、cn.richinfo.utils.PackageUtil：包管理器工具；
	12、cn.richinfo.utils.PermissionUtil：授权管理工具；
	13、cn.richinfo.utils.Platform：简易线程池管理工具；
	14、cn.richinfo.utils.ReflectionUtil：反射类工具；
	15、cn.richinfo.utils.SharePersistent：存储封装类工具；
	16、cn.richinfo.utils.StrictModeUtil：严格模式管理工具；
	17、cn.richinfo.utils.SystemUtil：系统管理工具。
	18、cn.richinfo.utils.KeyboardUtil：自定义键盘生成工具；
	
目前工具包中的部分类不够完善，欢迎大家一起扩展完善。