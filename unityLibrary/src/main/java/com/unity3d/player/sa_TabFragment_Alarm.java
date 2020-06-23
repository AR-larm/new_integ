package com.unity3d.player;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.unity3d.player.IntroActivity.pdb;


public class sa_TabFragment_Alarm extends Fragment {
    int cnt=0;

    private RecyclerView alarm_recyclerView;

    public static AlarmListAdapter alarmListAdapter;
    private ArrayList<String> Alarm_id_List;
    private ArrayList<String> itemList;
    private HashMap<String, String> Alarm_TimeH_HM;
    private HashMap<String, String> Alarm_TimeM_HM;
    private HashMap<String, String> Alarm_weekofday_HM;
    private HashMap<String, String> Alarm_gameType_HM;
    private HashMap<String, String> Alarm_IsSuper_HM;
    private HashMap<String, String> Alarm_ActiveNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sa_tab_fragment_alarm, null);


        alarm_recyclerView = view.findViewById(R.id.alarm_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        alarm_recyclerView.setLayoutManager(layoutManager);

        Alarm_id_List = new ArrayList<>();
        itemList = new ArrayList<>();
        Alarm_TimeH_HM = new HashMap<>();
        Alarm_TimeM_HM = new HashMap<>();
        Alarm_weekofday_HM = new HashMap<>();
        Alarm_gameType_HM = new HashMap<>();
        Alarm_IsSuper_HM = new HashMap<>();
        Alarm_ActiveNum  = new HashMap<>();

        alarmListAdapter = new AlarmListAdapter(getContext(), itemList, Alarm_id_List, Alarm_TimeH_HM, Alarm_TimeM_HM, Alarm_weekofday_HM, Alarm_gameType_HM, Alarm_IsSuper_HM, Alarm_ActiveNum);
        alarmListAdapter.notifyDataSetChanged();
        alarm_recyclerView.setAdapter(alarmListAdapter);

        try {
            Cursor cursor = pdb.selectColumns();
            while (cursor.moveToNext()) {
                String Alarm_Pid = cursor.getString(cursor.getColumnIndexOrThrow("Pid"));
                String Alarm_Hour = cursor.getString(cursor.getColumnIndexOrThrow("Hour"));
                String Alarm_Minute = cursor.getString(cursor.getColumnIndexOrThrow("Minute"));
                String Alarm_weekofday = cursor.getString(cursor.getColumnIndexOrThrow("Week"));
                String Alarm_gameType = cursor.getString(cursor.getColumnIndexOrThrow("GameType"));
                String Alarm_IsSuper = cursor.getString(cursor.getColumnIndexOrThrow("IsSuper"));
                String Alarm_ActivateNum = cursor.getString(cursor.getColumnIndexOrThrow("ActivateNum"));
                String Alarm_Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));

                Alarm_id_List.add(Alarm_Pid);
                Alarm_TimeH_HM.put(Alarm_Pid, Alarm_Hour);
                Alarm_TimeM_HM.put(Alarm_Pid, Alarm_Minute);
                Alarm_weekofday_HM.put(Alarm_Pid, Alarm_weekofday);
                Alarm_gameType_HM.put(Alarm_Pid, Alarm_gameType);
                Alarm_IsSuper_HM.put(Alarm_Pid, Alarm_IsSuper);
                Alarm_ActiveNum.put(Alarm_Pid, Alarm_ActivateNum);

                itemList.add(Alarm_Content);
            }
        }catch (NullPointerException e){

        }
        alarmListAdapter.notifyDataSetChanged();


        Button button1 = (Button)view.findViewById(R.id.newalarm_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), AlarmSetActivity.class);
                startActivity(intent1);
            }
        });
        /*
        LinearLayout layout1 = view.findViewById(R.id.alarms);
        layout1.setBackgroundColor(Color.argb(255, 255, 255, 255));

        TextView tv_id;
        TextView tv_etc;

        tv_id = new TextView(this.getContext());
        tv_id.append("test");
        tv_id.setTextSize(20);

        layout1.addView(tv_id);
        tv_etc = new TextView(this.getContext());
        tv_etc.append("king\n");
        layout1.addView(tv_etc);
*/


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        alarmListAdapter.notifyDataSetChanged();
    }
}
