package com.unity3d.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.unity3d.player.sa_TabFragment_Alarm.alarmListAdapter;

public class MainActivity extends AppCompatActivity {

    //private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainPagerAdapter mPageAdapter;
    private Context mContext;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent2= getIntent();
        try{
            if(intent2.getExtras().getString("AlarmStart").equals("Y")){
                Intent intent3 = new Intent(this, AlarmActivity.class);
                startActivity(intent3);
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }

        mContext=getApplicationContext();
        // DB 추가하기 (기존에 있는 DB활용)
        try {
            boolean bResult = isCheckDB(mContext);	// DB가 있는지?
            Log.d("MiniApp", "DB Check="+bResult);
            if(!bResult){	// DB가 없으면 복사
                copyDB(mContext);
            }else{

            }
        } catch (Exception e) {

        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //mTabLayout = findViewById(R.id.sa_main_tab);
        mViewPager = findViewById(R.id.sa_viewpager);


        // SA : tester ui added!
        // activate this paragraph & inactivate old one to check ui
        // + MainpagerAdapter.java에 있는 것도 활성화 시켜주세요!
        /*
        setContentView(R.layout.sa_activity_main);
        mTabLayout = findViewById(R.id.sa_main_tab);
        mViewPager = findViewById(R.id.sa_viewpager);
        */
        mPageAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_Information).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_Alarm).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_Statistic).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_Syssetting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_Information) {
                    mViewPager.setCurrentItem(0);
                }else if(item.getItemId() == R.id.action_Alarm){
                    mViewPager.setCurrentItem(1);
                }else if(item.getItemId() == R.id.action_Statistic){
                    mViewPager.setCurrentItem(2);
                }else if(item.getItemId() == R.id.action_Syssetting){
                    mViewPager.setCurrentItem(3);
                }
                return true;
            }
        });


        /*
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
         */
        //시작 프래그먼트 설정가능
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.viewpager, TabFragment_AlarmSet.newinstance()).commit();

        Intent intent3 = getIntent();
        try {
            String msg = intent3.getExtras().getString("alarm_addition");
            if (msg.equals("complete")) {
                Log.d("Added", "complete");
                mViewPager.setCurrentItem(1);
                bottomNavigationView.getMenu().findItem(R.id.action_Alarm).setChecked(true);
                alarmListAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){

        }

        try {
            String msg = intent3.getExtras().getString("alarm_delete");
            if (msg.equals("complete")) {
                Log.d("delete", "complete");
                mViewPager.setCurrentItem(1);
                bottomNavigationView.getMenu().findItem(R.id.action_Alarm).setChecked(true);
                alarmListAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){

        }


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragment로 사용할 레이아웃 공간.
        fragmentTransaction.replace(R.id.tabout, fragment);
        //fragmentTransaction.add(R.id.viewpager, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // DB가 있나 체크하기
    public boolean isCheckDB(Context mContext){
        String filePath = "/data/data/" + getPackageName() + "/databases/alarm.db";
        // mContext.getFilesDir().getPath();
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;

    }
    // DB를 복사하기
    // assets의
    // /db/xxxx.db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
    public void copyDB(Context mContext){
        Log.d("MiniApp", "copy start");
        AssetManager manager = mContext.getAssets();
        Log.d("MiniApp", "get Assets");

        String folderPath = "/data/data/" + getPackageName() + "/databases";
        String filePath = "/data/data/" + getPackageName() + "/databases/alarm.db";

        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            InputStream is = manager.open("alarm.db" );
            BufferedInputStream bis = new BufferedInputStream(is);
            if (folder.exists()) { }
            else{
                folder.mkdirs();
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.d("MiniApp", "alarm.b not ok");
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }


}
