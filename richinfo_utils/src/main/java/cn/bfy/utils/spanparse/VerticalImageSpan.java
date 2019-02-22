package cn.bfy.utils.spanparse;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * @author OuyangJinfu;change by ouyangjinfu
 * @email ouyjf@giiso.com
 * @date 2015/8/25
 * @version 1.0
 * @description 扩展垂直居中的ImageSpan
 */
public class VerticalImageSpan extends ImageSpan {
	
	public static final int ALIGN_MIDDLE = 2;
	
	TextView mTextView;
 
    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }
    
    
 
    public VerticalImageSpan(Drawable d, int verticalAlignment,TextView textView) {
		super(d, verticalAlignment);
		mTextView = textView;
	}



	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
 
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
 
            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }
 
    @SuppressLint("NewApi")
	@Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
//        int transY = 0;
//        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        int transY = bottom - drawable.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }else if(mVerticalAlignment == ALIGN_MIDDLE){
            //change by haoxiangtt
        	int offset = 0;//精准计算图片的偏差
        	if(android.os.Build.VERSION.SDK_INT >= 16){
	        	offset = (int)((mTextView.getLineSpacingMultiplier() - 1) *
	        			(bottom - top) / 4);
        	}
        	transY = ((bottom - top) - drawable.getBounds().height()) / 2 + top - offset;
        }
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
