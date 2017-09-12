package cn.richinfo.utils.bitmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.richinfo.utils.BuildConfig;
import cn.richinfo.utils.LogUtil;
import cn.richinfo.utils.PermissionUtil;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/5/11 0011
 * @modifyDate : 2017/5/11 0011
 * @version    : 1.0
 * @desc       : 本地图片选择器帮助类.(此类是非线程安全的)
 * </pre>
 */
public class PhotoPicker {

	private static final String TAG = "PhotoPicker";
	public static final int PHOTO_REQUEST_TAKEPHOTO = 10003;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 10004;// 从相册中选择
	public static final int PHOTO_REQUEST_CROP = 10005;//图片裁剪
	public static final int PHOTO_REQUEST_PERSSION_CAMERA = 10006;//需要相机权限
	private Activity mActivity;
	private Fragment mFragment;
	private Uri mCurTempUri;
	private OnAddPhotoListener mListener;
	private PicSelectorDialog mDialog;
	private boolean mIsDestory = false;
	
	/*public PhotoPicker(Fragment fragment, OnAddPhotoListener listener) {
		this(fragment.getActivity(), listener);
		mFragment = fragment;

	}*/

	public PhotoPicker(Activity activity, OnAddPhotoListener listener) {
		mActivity = activity;
		if (listener == null) {
			throw new IllegalArgumentException(" listener must not null ");
		}
		mListener = listener;
		mIsDestory = false;
	}

	/**
	 * 显示选择添加图片方式界面
	 */
	public void showSelectView() {
		mDialog = new PicSelectorDialog(mActivity, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PermissionUtil.isNeedRequestPermission()
						&& PermissionUtil.checkSelfPermission(mActivity, Manifest.permission.CAMERA)) {
					PermissionUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA},
						PHOTO_REQUEST_PERSSION_CAMERA);
				} else {
					openCamera();
				}

			}
		}, new OnClickListener() {
			@Override
			public void onClick(View v) {
				openGallery();

			}
		});
		mDialog.show();
	}

	private void openGallery() {
		//				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//				getAlbum.setType("image/*");
		// 相册
		Intent getAlbum = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mActivity.startActivityForResult(getAlbum, PHOTO_REQUEST_GALLERY);
		mListener.onAddProcess();
	}

	private void openCamera() {
		// 指定调用相机拍照后照片的储存路径
		File mCurTempFile = new File(Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_PICTURES + File.separator + getPhotoFileName());
		Uri outputUri = null;
		if (Build.VERSION.SDK_INT < 24) {
			outputUri = Uri.fromFile(mCurTempFile);
		} else {
			outputUri = FileProvider.getUriForFile(mActivity,
					BuildConfig.APPLICATION_ID + ".provider", mCurTempFile);
		}
		mCurTempUri = outputUri;
		Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		mActivity.startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
		mListener.onAddProcess();
	}

	// 使用系统当前日期加以调整作为照片的名称
	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.CHINA);
		return dateFormat.format(date) + ".PNG";
	}

	public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == PHOTO_REQUEST_PERSSION_CAMERA) {
			if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				openCamera();
				return true;
			}
		}
		return false;
	}

	public boolean onActivityResult(final int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			//　取消操作
			return false;
		}
		switch (requestCode) {
			case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用

				/*Observable.just(mCurTempUri)
					.subscribeOn(SchedulerProvider.getInstance().ui())
					.subscribe(new Action1<Uri>() {
						@Override
						public void call(Uri uri) {

						}
					});*/
				if(!mIsDestory && null != mListener){
					try {
						mListener.onAddResult(requestCode, mCurTempUri);
					} catch (Exception e) {
						LogUtil.e(TAG, " process result error :"+e.toString());
					}
				}
				return true;
			case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
//				mListener.onAddProcess();
				// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
				if (data != null) {
					mListener.onAddResult(requestCode, data.getData());
				} else {
					mListener.onAddResult(requestCode, null);
					LogUtil.e(TAG, " data is null, request = " + requestCode);
				}
				return true;
			case PHOTO_REQUEST_CROP:
				if (data != null) {
					mListener.onAddResult(requestCode, mCurTempUri);
				} else {
					mListener.onAddResult(requestCode, null);
					LogUtil.e(TAG, "data is null, request = " + requestCode);
				}
				return true;
			default:
				mListener.onAddResult(requestCode, null);
				break;
		}
		return false;
	}

	private void rotatePhoto(String photoPath) {
		int angle = MBitmapUtil.readPictureDegree(photoPath);
		LogUtil.d(TAG, "photo degree is : " + angle);
		if (angle > 0) {
			Bitmap bmp = MBitmapUtil.getBmpToCache(photoPath);
			if (null == bmp) {
				LogUtil.e(TAG, " get photo to cache error ");
				return;
			}
			Bitmap rotatedBmp = MBitmapUtil.rotateImage(angle, bmp);
			if (null == rotatedBmp) {
				LogUtil.e(TAG, " rotate photo error ");
				if (null != bmp && !bmp.isRecycled()) {
					bmp.recycle();
				}
				return;
			}
			String tempPath = photoPath + ".temp";
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(tempPath);
				rotatedBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
				File tempFile = new File(tempPath);
				if (tempFile.exists() && tempFile.isFile()) {
					File photoFile = new File(photoPath);
					if (photoFile.exists()) {
						LogUtil.d(TAG, " delete photo file ");
						photoFile.delete();
					}
					boolean result = tempFile.renameTo(photoFile);
					LogUtil.d(TAG, " temp file rename " + result);
				}
			} catch (FileNotFoundException e) {
				LogUtil.e(TAG, " rotated file not found error ");
			} catch (Exception e) {
				LogUtil.e(TAG, " save rotated error ");
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException e) {
						LogUtil.e(TAG, " close stream error: "+e.toString());
					}
				}
				if (null != bmp && !bmp.isRecycled()) {
					bmp.recycle();
				}
				if (null != rotatedBmp && !rotatedBmp.isRecycled()) {
					rotatedBmp.recycle();
				}
			}
		}
	}

	/**
	 * activity 销毁时调用
	 */
	public void onDestory() {
		mIsDestory = true;
		if (mDialog != null) {
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			mDialog = null;
		}
		mActivity = null;
		mCurTempUri = null;
		mListener = null;
	}


	public interface OnAddPhotoListener {
		
		/**
		 * 处理图片中...
		 */
		void onAddProcess();

		/**
		 * 添加完成回调方法
		 * 
		 * @param uri
		 */
		void onAddResult(int requestCode, Uri uri);

	}
	
	public boolean startPhotoZoom(Activity activity, Uri input,Uri output, int width, int height) {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			mCurTempUri = output;
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(input, "image/*");
			// crop为true是设置在开启的intent中设置显示的view可以剪裁
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", width);
			intent.putExtra("aspectY", height);
			// outputX,outputY 是剪裁图片的宽高
			intent.putExtra("outputX", width);
			intent.putExtra("outputY", height);
			//图片输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, output);//输出位置
			intent.putExtra("return-data", false);//关闭返回数据
//			intent.putExtra("scale", false);
			intent.putExtra("outputFormat", "PNG");
			intent.putExtra("noFaceDetection", true);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			activity.startActivityForResult(intent, PHOTO_REQUEST_CROP);
			mListener.onAddProcess();
			return true;
		}
		return false;
	}
	
	/*public boolean startSyPhotoZoom(Activity activity, Uri uri, int width, int height, boolean returnData) {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//change by haoxiantt
//			File dir = new File(CommonPath.mImgCaChePath);
//			if(!dir.exists() && !dir.mkdirs()){
//				Logger.w(TAG, " mk dirs" + CommonPath.mImgCaChePath + " failed!");
//				return false;
//			}
//			String filePath = CommonPath.mImgCaChePath + File.separator + getPhotoFileName();//temp file
//			Uri imageUri = Uri.fromFile(new File(filePath));//The Uri to store the big bitmap
			
			File dir = activity.getExternalCacheDir();
			File imgFile = new File(dir,getPhotoFileName());
			Uri imageUri = Uri.fromFile(imgFile);
			/*//*************************
//			Intent intent = new Intent(activity, CropImageActivity.class);
//			intent.setDataAndType(uri, "image*//*");
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//			
//			// outputX,outputY 是剪裁图片的宽高
//			intent.putExtra("outputX", width);
//			intent.putExtra("outputY", height);
//			intent.putExtra("return-data", returnData);
//			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//			activity.startActivityForResult(intent, requestCode);
			return true;
		}
		return false;
	}*/

}
