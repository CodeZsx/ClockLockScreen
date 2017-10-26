package com.zsx.clocklockscreen;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.skyfishjy.library.RippleBackground;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_start;
    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(" ");
        }

        mIntent = new Intent(MainActivity.this, LockService.class);
        findViewById(R.id.block_pull).setOnClickListener(this);
        btn_start = (Button) findViewById(R.id.btn_start);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        if (isServiceRunning(MainActivity.this,"com.zsx.clocklockscreen.LockService")){
            Log.e("zsx", "service running");
            btn_start.setText("关闭");
            btn_start.setTag(true);
            rippleBackground.startRippleAnimation();
        }else {
            Log.e("zsx", "service isn't running");
            btn_start.setText("开启");
            btn_start.setTag(false);
            rippleBackground.stopRippleAnimation();
        }
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((boolean)btn_start.getTag()==false){
                    startService(mIntent);
                    btn_start.setText("关闭");
                    btn_start.setTag(true);
                    rippleBackground.startRippleAnimation();
                }else {
                    stopService(mIntent);
                    btn_start.setText("开启");
                    btn_start.setTag(false);
                    rippleBackground.stopRippleAnimation();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.block_pull:
                startActivity(new Intent(MainActivity.this, PreviewActivity.class));
                overridePendingTransition(R.anim.bottom_in,R.anim.top_out);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_feedback) {
            return true;
        }else if (id == R.id.action_exit) {
            MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    /*
     * 判断服务是否启动,context上下文对象 ，className服务的name
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
