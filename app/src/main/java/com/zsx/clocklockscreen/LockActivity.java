package com.zsx.clocklockscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.zsx.clocklockscreen.widget.ClockView;

public class LockActivity extends AppCompatActivity{

    private ClockView mClockView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_lock);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.main);
        layout.setOnTouchListener(new View.OnTouchListener() {
            float mDownY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取收按下时的y轴坐标
                        mDownY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                        // 获取当前滑动的y轴坐标
                        float curY = event.getY();
                        // 获取移动的y轴距离
                        float deltaY = mDownY-curY;
                        if (deltaY > 0) {

                        }
                        // 未超过制定距离，则返回原来位置
                        if (deltaY > 300) {
                            LockActivity.this.finish();
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        }


                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //屏蔽menu
    @Override
    public void onWindowFocusChanged(boolean pHasWindowFocus) {
        super.onWindowFocusChanged(pHasWindowFocus);
        if (!pHasWindowFocus) {
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }
}
