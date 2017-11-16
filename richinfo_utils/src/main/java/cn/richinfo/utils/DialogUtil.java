package cn.richinfo.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Dialog 工具类
 * 
 */
public class DialogUtil {
	
	/**
	 * 显示默认加载对话框（不带文字，可取消）
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public  static Dialog showProgressDialog(Context context) {
		return showProgressDialog(context,null,Gravity.CENTER,true);
	}

	public static Dialog showProgressDialogWidthNoFocus(Context context, String message){
		return showProgressDialog(context,message,Gravity.CENTER,false);
	}

	public static Dialog showProgressDialog(Context context,int gravity){
		return showProgressDialog(context,null,gravity,true);
	}

	/**
	 * 显示进度对话框（带文字，不可取消）
	 * 
	 * @param
	 * @param message
	 * @param
	 */
	public static Dialog showProgressDialog(Context context,String message) {
		return showProgressDialog(context,message,Gravity.CENTER,true);
	}

	public static Dialog showProgressDialog(Context context,String message, int gravity, boolean focusable){
		Dialog progressDialog = new Dialog(context,
			PackageUtils.getIdentifier(context, "utils_progress_dialog", "style"));
		progressDialog.setContentView(
			PackageUtils.getIdentifier(context, "utils_progress_dialog", "layout"));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);

		if(!focusable) {
			progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		}

		WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
		if(lp != null) {
			lp.gravity = gravity;
		}
		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView msg = (TextView) progressDialog
				.findViewById(PackageUtils.getIdentifierId(context, "id_tv_loadingmsg"));
		if(!TextUtils.isEmpty(message)) {
			msg.setVisibility(View.VISIBLE);
			msg.setText(message);
		}else{
			msg.setVisibility(View.GONE);
		}
		progressDialog.show();

		return progressDialog;
	}


	/**
	 * 显示图片对话框
	 * @param context
	 * @param message
	 */
	public static Dialog showImageDialog(Context context, String message, Drawable image){
		Dialog progressDialog = new Dialog(context,
			PackageUtils.getIdentifier(context, "utils_progress_dialog", "style"));
		progressDialog.setContentView(PackageUtils.getIdentifierLayout(context, "utils_showtip_dialog"));
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView msg = (TextView) progressDialog.findViewById(PackageUtils.getIdentifierId(context, "tv_message"));
		msg.setText(message);
		ImageView iv = (ImageView)progressDialog.findViewById(PackageUtils.getIdentifierId(context, "iv_tip"));
		iv.setImageDrawable(image);
		progressDialog.show();
		return progressDialog;
	}


	public static Dialog showPicture(Context context, Drawable drawable){
		Dialog progressDialog = new Dialog(context,
			PackageUtils.getIdentifier(context, "utils_progress_dialog", "style"));
		progressDialog.setContentView(PackageUtils.getIdentifierLayout(context, "utils_show_picture_dialog"));
		ImageView imageView = (ImageView) progressDialog.findViewById(
			PackageUtils.getIdentifierId(context, "iv_picture"));
		imageView.setImageDrawable(drawable);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		progressDialog.show();
		return progressDialog;
	}


}
