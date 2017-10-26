package com.codezzz.clocklockscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.codezzz.clocklockscreen.widget.ClockView;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener{

    private ClockView mClockView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        setContentView(R.layout.activity_preview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu();
        findViewById(R.id.iv_exit).setOnClickListener(this);

    }
    /**
     * 隐藏虚拟按键
     */
    protected void hideBottomUIMenu(){
        //low api
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //new api versions
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exit:
                PreviewActivity.this.finish();
                overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
                break;
        }
    }
}
