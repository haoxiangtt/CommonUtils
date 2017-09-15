package cn.richinfo.utils.bitmap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/5/11 0011
 * @modifyDate : 2017/5/11 0011
 * @version    : 1.0
 * @desc       : 本地图片选择器对话框
 * </pre>
 */
public class PicSelectorDialog extends Dialog implements DialogInterface.OnShowListener
		, DialogInterface.OnDismissListener{
	
	private static final String TAG = PicSelectorDialog.class.getSimpleName();

	public PicSelectorDialog(Context context, int resId,
			final View.OnClickListener takePhotoListener,
			final View.OnClickListener selectPicListener){
		super(context, resId);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setBackgroundDrawable(new ColorDrawable());

		setContentView(getIdentifier(context, "utils_photo_picker_dialog", "layout"));
		TextView takePhotoTv = (TextView)findViewById(getIdentifier(context, "bt_camera", "id"));
		TextView selectPicTv = (TextView)findViewById(getIdentifier(context, "bt_photo", "id"));
		TextView cancelTv = (TextView)findViewById(getIdentifier(context, "bt_cancel", "id"));
		
		
			takePhotoTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(takePhotoListener != null){
						takePhotoListener.onClick(v);
					}
					dismiss();
				}
			});
			selectPicTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(selectPicListener != null){
						selectPicListener.onClick(v);
					}
					dismiss();
				}
			});
		
		cancelTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(true);

		setOnShowListener(this);

		setOnDismissListener(this);

	}

	private int getIdentifier(Context context, String name, String defType) {
		int id = context.getResources().getIdentifier(name, defType,
				context.getPackageName());
		return id;
	}

	@Override
	public void onShow(DialogInterface dialog) {
//		ScreenUtil.backgroundAlpha(getOwnerActivity(), 0.4f);
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		window.getDecorView().setPadding(0, 0 ,0 ,0);
		window.setAttributes(lp);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
//		ScreenUtil.backgroundAlpha(getOwnerActivity(), 1f);
	}
}
