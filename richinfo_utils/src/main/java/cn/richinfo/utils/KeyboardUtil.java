package cn.richinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ouyangjinfu
 * @version 1.0
 * @Date 2015年8月25日
 * @Email ouyjf@giiso.com
 * @Description -->自定义软键盘
 */
public class KeyboardUtil {

	private static final String TAG = "KeyboardUtil";
	private static final boolean DEBUG = BuildConfig.DEBUG;

	private KeyboardView keyboardView;
	private Keyboard upperLetterKeyboard;// 大写字母键盘
	private Keyboard lowerLetterKeyboard;//小写字母键盘
	private Keyboard numberKeyboard;// 数字键盘
	private boolean isNumberKeyBoard;// 是否数据键盘
	private boolean isUpperCase = false;// 是否大写
	
	private boolean isKeyboradShowing;

	private EditText ed;
	private Animation keyboardCloseAnim;
	private Animation keyboardOpenAnim;

	public KeyboardUtil(KeyboardView view, Context ctx, EditText edit) {

		this.ed = edit;
		upperLetterKeyboard = new Keyboard(ctx,
			PackageUtil.getIdentifier(ctx, "utils_keyboard_upper_letter", "xml"));
		lowerLetterKeyboard = new Keyboard(ctx,
			PackageUtil.getIdentifier(ctx, "utils_keyboard_lower_letter", "xml"));
		numberKeyboard = new Keyboard(ctx,
			PackageUtil.getIdentifier(ctx, "utils_keyboard_number", "xml"));

		isNumberKeyBoard = false;
		isUpperCase = false;

		keyboardView = view;
		keyboardView.setKeyboard(lowerLetterKeyboard);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
		keyboardCloseAnim = AnimationUtils.loadAnimation(ctx,
			PackageUtil.getIdentifier(ctx, "operate_controler_close", "anim"));
		keyboardOpenAnim = AnimationUtils.loadAnimation(ctx,
			PackageUtil.getIdentifier(ctx, "operate_controler_open", "anim"));
		keyboardOpenAnim.setFillAfter(true);
		keyboardCloseAnim.setFillAfter(true);
		keyboardOpenAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				keyboardView.setVisibility(View.VISIBLE);
				keyboardView.clearAnimation();
			}
		});
		keyboardCloseAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				keyboardView.setVisibility(View.GONE);
				keyboardView.clearAnimation();
			}
		});
		
		
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=swipeUp=");
			}
		}

		@Override
		public void swipeRight() {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=swipeRight=");
			}
		}

		@Override
		public void swipeLeft() {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=swipeLeft=");
			}
		}

		@Override
		public void swipeDown() {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=swipeDown=");
			}
		}

		@Override
		public void onText(CharSequence text) {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=onText=" + text);
			}
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			editable.insert(start, text);
		}

		@Override
		public void onRelease(int primaryCode) {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=onRelease=" + primaryCode);
			}
		}

		@Override
		public void onPress(int primaryCode) {
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=onPress=" + primaryCode);
			}
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=primaryCode=" + primaryCode
						+ "  " + keyCodes[0]);
			}
			int start = ed.getSelectionStart();
			if (DEBUG) {
				Log.d("  ", "==OnKeyboardActionListener=start=" + start + "  "
						+ editable);
			}
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 取消
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DONE) {//完成
				hideKeyboard();
				ed.onEditorAction(EditorInfo.IME_ACTION_DONE);
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isNumberKeyBoard) {
					isNumberKeyBoard = false;
					if (isUpperCase) {
						keyboardView.setKeyboard(upperLetterKeyboard);
					} else {
						keyboardView.setKeyboard(lowerLetterKeyboard);
					}
				} else {
					isNumberKeyBoard = true;
					keyboardView.setKeyboard(numberKeyboard);
				}
			} else if (primaryCode == -14) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == -13) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else if (primaryCode == -12) { //upper/lower case mode
				if (isUpperCase) {
					isUpperCase = false;
					keyboardView.setKeyboard(lowerLetterKeyboard);
				} else {
					isUpperCase = true;
					keyboardView.setKeyboard(upperLetterKeyboard);
				}
			} else if (primaryCode == -11) {//previout
				ed.onEditorAction(EditorInfo.IME_ACTION_PREVIOUS);
			} else if (primaryCode == -10) {//next
				ed.onEditorAction(EditorInfo.IME_ACTION_NEXT);
			} else if (primaryCode == -9) {//send
				ed.onEditorAction(EditorInfo.IME_ACTION_SEND);
			} else if (primaryCode == -8) {//go
				hideKeyboard();
				ed.onEditorAction(EditorInfo.IME_ACTION_GO);
			} else if (primaryCode == -7) {//search
				hideKeyboard();
				ed.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}

		}
	};

	/**
	 * 打开自定义键盘
	 */
	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
			keyboardView.startAnimation(keyboardOpenAnim);
			isKeyboradShowing=true;
			
		}
	}

	/**
	 * 关闭自定义键盘
	 */
	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.startAnimation(keyboardCloseAnim);
			isKeyboradShowing=false;
		}
	}
	
	public boolean isShowing(){
		return isKeyboradShowing;
	}

	public void setEditText(EditText edit, boolean focus) {
		ed = edit;
		setEditFocus(focus);
	}

	/**
	 * 隐藏默认键盘
	 * 在使用自定义键盘前必须调用此方法
	 * @param activity
	 */
	public void hideSoftInputMethod(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setEditFocus(false);
	}

	private void setEditFocus(boolean focus) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}

		if (methodName == null) {
			ed.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName,
						boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(ed, focus);
			} catch (NoSuchMethodException e) {
				ed.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}
