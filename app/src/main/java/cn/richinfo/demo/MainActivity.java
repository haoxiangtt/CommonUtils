package cn.richinfo.demo;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import cn.richinfo.utils.KeyboardUtil;

public class MainActivity extends AppCompatActivity {

    private KeyboardUtil mKeyBoard;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mEtInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEtInput.setFocusable(true);
                mEtInput.setFocusableInTouchMode(true);
                mEtInput.requestFocus();
                if (!mKeyBoard.isShowing()) {
                    mKeyBoard.showKeyboard();
                }

                return false;
            }
        });
        KeyboardView keyView = (KeyboardView) findViewById(R.id.keyboard);
        mKeyBoard = new KeyboardUtil(keyView, this, mEtInput);
        mKeyBoard.hideSoftInputMethod(this);
    }
}
