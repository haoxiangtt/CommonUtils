package cn.richinfo.utils.parse;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.richinfo.utils.PackageUtil;

/**
 * @author ouyangjinfu
 * @data 2015年8月27日
 * @email ouyjf@giiso.com
 * @version 1.0
 * @description 将特殊字符串替换成相应图片，实现图文混排效果
 */
public class ImageParser {    
    public static int DEFAULT_IMAGE_RES_IDS = 0;

	public static int DEFAULT_IMAGE_TEXTS_IDS = 0;

	private static ImageParser sInstance;    
    
    private final Context mContext;    
    private final String[] mImageTexts;    
    private final Pattern mPattern;    
    private final HashMap<String, Integer> mImageToRes;
    private final HashMap<String, Drawable> mImageDrawable;
    
    private Paint mPaint;
    
    private ImageParser(Context context) {    
        mContext = context;
		DEFAULT_IMAGE_RES_IDS = PackageUtil.getIdentifier(context, "icon_array", "array");
		DEFAULT_IMAGE_TEXTS_IDS = PackageUtil.getIdentifier(context, "text_array", "array");
        mImageTexts = buildImageText();    
        mImageToRes = buildImageToRes();
        mImageDrawable = new HashMap<String, Drawable>();
        mPattern = buildPattern();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    
    private String[] buildImageText(){
    	return mContext.getResources().getStringArray(    
                DEFAULT_IMAGE_TEXTS_IDS);
    }
    
    private HashMap<String, Integer> buildImageToRes() {
		int[] imageIcon = mContext.getResources().getIntArray(
				DEFAULT_IMAGE_RES_IDS);
	    if (imageIcon.length != mImageTexts.length
				|| imageIcon.length == 0 || mImageTexts.length == 0) {
	        throw new IllegalStateException("Images resource ID/text mismatch");    
	    }    
	    HashMap<String, Integer> imageToRes = new HashMap<String, Integer>(    
	            mImageTexts.length);    
	    for (int i = 0; i < mImageTexts.length; i++) {    
	        imageToRes.put(mImageTexts[i], imageIcon[i]);
	    }    
	    return imageToRes;    
	}

	// 构建正则表达式    
	private Pattern buildPattern() {
//		String patternStr = "#\\d{2,}";
//		return patternStr;
	    StringBuilder patternString = new StringBuilder(mImageTexts.length * 3);    
	    patternString.append('(');    
	    for (String s : mImageTexts) {    
	        patternString.append(Pattern.quote(s));    
	        patternString.append('|');    
	    }    
	    patternString.replace(patternString.length() - 1, patternString    
	            .length(), ")");    
	    return Pattern.compile(patternString.toString());    
	}

	public static void init(Context context) {    
	    sInstance = new ImageParser(context);    
	}

	public static ImageParser getInstance() {
	    return sInstance;    
	}

	// 根据文本替换成图片    
	public CharSequence strToImage(CharSequence text,TextView textView) {
		float textSize = textView.getTextSize();
	    SpannableStringBuilder builder = new SpannableStringBuilder(text);    
	    Matcher matcher = mPattern.matcher(text);    
	    while (matcher.find()) {
	    	String str = matcher.group();
	        Drawable drawable = null;
	        if(mImageDrawable.get(str) != null){
	        	drawable = mImageDrawable.get(str);
	        }else{
	        	int resId = mImageToRes.get(str);   
	        	drawable = mContext.getResources().getDrawable(resId);
	        	mImageDrawable.put(str, drawable);
	        }
	        mPaint.setTextSize(textSize);
	        FontMetrics fm = mPaint.getFontMetrics();
	        float textHeight = (int) (Math.ceil(-fm.ascent) + 5);
	        float textWidth = textHeight * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
	        drawable.setBounds(0, 0, (int)textWidth, (int)textHeight);//这里设置图片的大小    
	        ImageSpan imageSpan = new VerticalImageSpan(drawable,VerticalImageSpan.ALIGN_MIDDLE,textView);    
	        builder.setSpan(imageSpan, matcher.start(),    
	                matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);    
	    }    
	    return builder;    
	}

}
