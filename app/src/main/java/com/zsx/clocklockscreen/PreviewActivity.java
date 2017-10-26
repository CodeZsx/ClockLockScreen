package com.zsx.clocklockscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zsx.clocklockscreen.widget.ClockView;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener{

    private ClockView mClockView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        setContentView(R.layout.activity_preview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById(R.id.iv_exit).setOnClickListener(this);

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
