package com.unity3d.player;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class IntroActivity extends AppCompatActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    public static int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;
    PermissionListener permissionListener;

    public static AlarmDB pdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setImageResource(R.drawable.intro);


        pdb = new AlarmDB(this);
        //pdb.deleteAllColumns();

        if(Build.VERSION.SDK_INT>=29){
            permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        public void run() {
                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            };
        }else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
        try{
            startLoading(permissionListener);
        } catch (Exception e){
            e.printStackTrace();
            startLoading(permissionListener);
        }

    }

    private void startLoading(PermissionListener permissionlistener) {
        try{
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                //.setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setDeniedMessage("권한을 허용하지 않으시면 앱을 정상적으로 사용할 수 없습니다.\n\n권한을 허용하고 싶으시면 [설정] > [권한] 을 통해 승인해주세요.")
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .check();
        } catch (Exception e){
            e.printStackTrace();
        }
//        //원래 이렇게 밖에 있었어용.
//        TedPermission.with(this)
//                .setPermissionListener(permissionlistener)
//                //.setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setDeniedMessage("권한을 허용하지 않으시면 앱을 정상적으로 사용할 수 없습니다.\n\n권한을 허용하고 싶으시면 [설정] > [권한] 을 통해 승인해주세요.")
//                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
//                .check();
    }
}