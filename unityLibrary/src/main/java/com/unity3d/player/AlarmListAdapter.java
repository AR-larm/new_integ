package com.unity3d.player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kyleduo.switchbutton.SwitchButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.unity3d.player.IntroActivity.pdb;
import static com.unity3d.player.sa_TabFragment_Alarm.alarmListAdapter;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> itemList;
    private ArrayList<String> Alarm_id_List;
    //private HashMap<String, String> Alarm_Time_HM;
    private HashMap<String, String> Alarm_weekofday_HM;
    private HashMap<String, String> Alarm_gameType_HM;
    private HashMap<String, String> Alarm_IsSuper_HM;
    private HashMap<String, String> Alarm_TimeH_HM;
    private HashMap<String, String> Alarm_TimeM_HM;
    private HashMap<String, String> Alarm_ActiveNum;


    public AlarmListAdapter(Context mContext, ArrayList<String> itemList, ArrayList<String> alarm_id_List, HashMap<String, String> alarm_TimeH_HM, HashMap<String, String> alarm_TimeM_HM, HashMap<String, String> alarm_weekofday_HM, HashMap<String, String> alarm_gameType_HM, HashMap<String, String> alarm_IsSuper_HM, HashMap<String, String> alarm_ActiveNum_HM) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.Alarm_id_List = alarm_id_List;
        this.Alarm_TimeH_HM = alarm_TimeH_HM;
        this.Alarm_TimeM_HM = alarm_TimeM_HM;
        this.Alarm_weekofday_HM = alarm_weekofday_HM;
        this.Alarm_gameType_HM = alarm_gameType_HM;
        this.Alarm_IsSuper_HM = alarm_IsSuper_HM;
        this.Alarm_ActiveNum = alarm_ActiveNum_HM;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_alarm, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = itemList.get(position);

        holder.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("알람 삭제");
                builder.setMessage("선택하신 알람을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    itemList.remove(position);
                                    int selected_P_id = Integer.parseInt(Alarm_id_List.get(position));
                                    pdb.deleteColumn(selected_P_id);
                                    //alarmListAdapter.notifyDataSetChanged();
                                    alarmListAdapter.notifyItemRemoved(position);
                                    //Toast.makeText(getApplicationContext(),"예를 선택습니다.",Toast.LENGTH_LONG).show();
                                }catch (IndexOutOfBoundsException e){

                                }
                            }
                        });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
                return false;
            }
        });

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, AlarmSetActivity.class);
                intent3.putExtra("UpdateAlarm", "update");

                String alarm_hour = Alarm_TimeH_HM.get(Alarm_id_List.get(position));
                String alarm_minute = Alarm_TimeM_HM.get(Alarm_id_List.get(position));
                String alarm_week= Alarm_weekofday_HM.get(Alarm_id_List.get(position));
                String alarm_isSuper = Alarm_IsSuper_HM.get(Alarm_id_List.get(position));

                intent3.putExtra("A_Position", position);
                intent3.putExtra("A_Hour", alarm_hour);
                intent3.putExtra("A_Minute", alarm_minute);
                intent3.putExtra("A_Week", alarm_week);
                intent3.putExtra("A_isSuper", alarm_isSuper);
                intent3.putExtra("A_pid", Alarm_id_List.get(position));

                mContext.startActivity(intent3);
            }
        });

        // 스위치 버튼입니다.
        //holder.switchButton.setTintColor(Color.parseColor("#FFBB71"));
        //holder.switchButton.setBackColor(ColorStateList.valueOf(R.color.ARlarm_white));

        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    //holder.switchButton.setBackColor(ColorStateList.valueOf(Color.parseColor("@color/ARlarm_blue")));
                    holder.switchButton.setBackColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.ARlarm_lightorange)));
                    pdb.Update_Active(position, 1);
                }else{
                    holder.switchButton.setBackColor(ColorStateList.valueOf(Color.GRAY));
                    pdb.Update_Active(position, 0);
                }
            }
        });


        //알람 제목
        holder.alarm_title1.setText(item);

        String alarm_hour = Alarm_TimeH_HM.get(Alarm_id_List.get(position));
        String alarm_minute = Alarm_TimeM_HM.get(Alarm_id_List.get(position));
        String alarm_time = alarm_hour+":"+alarm_minute;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try{
            Date temp_date = sdf.parse(alarm_time);
            SimpleDateFormat newsdf = new SimpleDateFormat("hh:mm aa", new Locale("en", "US"));
            holder.alarm_time_text.setText(newsdf.format(temp_date));
        }catch (Exception e){

        }

        Integer[] WD={0, 0, 0, 0, 0, 0, 0, 0};

        String week_str = holder.weekofDay_tv.getText().toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(week_str);
        String weekofDay = "";
        String temp_week= Alarm_weekofday_HM.get(Alarm_id_List.get(position));
        try {
            if ((Integer.parseInt(temp_week) & (1 << 7)) != 0) {
                weekofDay = "   토";
                WD[7] = 1;

                String sat = "토";
                int start = week_str.indexOf(sat);
                int end = start + sat.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if ((Integer.parseInt(temp_week) & (1 << 6)) != 0) {
                weekofDay = "   금" + weekofDay;
                WD[6] = 1;

                String fri = "금";
                int start = week_str.indexOf(fri);
                int end = start + fri.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            if ((Integer.parseInt(temp_week) & (1 << 5)) != 0) {
                weekofDay = "   목" + weekofDay;
                WD[5] = 1;

                String thu = "목";
                int start = week_str.indexOf(thu);
                int end = start + thu.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            if ((Integer.parseInt(temp_week) & (1 << 4)) != 0) {
                weekofDay = "   수" + weekofDay;
                WD[4] = 1;

                String wed = "수";
                int start = week_str.indexOf(wed);
                int end = start + wed.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            if ((Integer.parseInt(temp_week) & (1 << 3)) != 0) {
                weekofDay = "   화" + weekofDay;
                WD[3] = 1;

                String tue = "화";
                int start = week_str.indexOf(tue);
                int end = start + tue.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            if ((Integer.parseInt(temp_week) & (1 << 2)) != 0) {
                weekofDay = "월" + weekofDay;
                WD[2] = 1;

                String mon = "월";
                int start = week_str.indexOf(mon);
                int end = start + mon.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            if ((Integer.parseInt(temp_week) & (1 << 1)) != 0) {
                weekofDay = weekofDay + "   일";
                WD[1] = 1;

                String sun = "일";
                int start = week_str.indexOf(sun);
                int end = start + sun.length();
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            holder.weekofDay_tv.setText(ssb);
        }catch (Exception e){

        }


        boolean AlarmOn_today = false;
        Calendar temp_cal = Calendar.getInstance();
        int dayofweek_cal= temp_cal.get(Calendar.DAY_OF_WEEK);
        String dayLeft="";

        try {
            Date temp_date = sdf.parse(alarm_time);
            String date = sdf.format(new Date());
            Date currentTime = sdf.parse(date);
            long subt= temp_date.getTime()-currentTime.getTime();
            long diffSeconds = subt / 1000 % 60;
            long diffMinutes = subt / (60 * 1000) % 60;
            long diffHours = subt / (60 * 60 * 1000) % 24;
            long diffDays = subt / (24 * 60 * 60 * 1000);
            Log.d("TimeLL", "??"+diffHours+"시간 "+diffMinutes+"분 후");
            if(diffHours<=0 && diffMinutes<0){
                AlarmOn_today =false;
            }else{
                AlarmOn_today = true;
                if(diffHours == 0){
                    dayLeft = diffMinutes+"분 후";
                }else{
                    dayLeft = diffHours+"시간 "+diffMinutes+"분 후";
                }

            }
        }catch (Exception e){

        }

        if(WD[dayofweek_cal]==1 && AlarmOn_today){
            //dayLeft = "시간 "+"분 후";
        }else{
            for(int i =1;(dayofweek_cal+i)<WD.length;i++){
                if(WD[dayofweek_cal+i] == 1){
                    dayLeft = i+"일 후";
                    break;
                }
            }
            if(dayLeft.equals("")){
                for(int i =1;i<=dayofweek_cal;i++){
                    if(WD[i]==1){
                        dayLeft = WD.length-1 - dayofweek_cal+i+"일 후";
                        break;
                    }
                }
            }
        }
        holder.alarm_timeleft1.setText(dayLeft);


        String isSuper = Alarm_IsSuper_HM.get(Alarm_id_List.get(position));
        if(isSuper == null){
            isSuper = "0";
        }
        Log.d("SuperAlarm", "?? "+isSuper);
        String super_str = holder.alarm_sub11.getText().toString();
        SpannableStringBuilder ssb1 = new SpannableStringBuilder(super_str);
        try {
            if(isSuper.equals("1")){
                String check = "on";
                int start = super_str.indexOf(check);
                int end = start + check.length();
                ssb1.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB71")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.alarm_sub11.setText(ssb1);
            }else{
                super_str=super_str.replace("on", "off");
                holder.alarm_sub11.setText(super_str);

            }
        }catch (Exception e){

        }

        String GameType = Alarm_gameType_HM.get(Alarm_id_List.get(position));
        Log.d("Game", "?? "+GameType);
        //1이 먼먼이게임
        try{
            if(GameType.equals("1")){
                holder.game1.setBackground(mContext.getDrawable(R.drawable.header_rectangle2));
                holder.game2.setBackground(mContext.getDrawable(R.drawable.sa_header_rectangle));
            }else{
                holder.game1.setBackground(mContext.getDrawable(R.drawable.sa_header_rectangle));
                holder.game2.setBackground(mContext.getDrawable(R.drawable.header_rectangle2));
            }
        }catch (Exception e){

        }

        //holder.alarm_time_text.setText(Alarm_TimeH_HM.get(Alarm_id_List.get(position))+":"+Alarm_TimeM_HM.get(Alarm_id_List.get(position)));

        /*
        //알람 시간 && 알람 남은 시간
        String alarm_time= Alarm_Time_HM.get(Alarm_id_List.get(position));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
            Date temp_date = sdf.parse(alarm_time);
            SimpleDateFormat newsdf = new SimpleDateFormat("hh:mm aa", new Locale("en", "US"));
            holder.alarm_time_text.setText(newsdf.format(temp_date));
            Log.d(String.valueOf(this), "TIME: "+newsdf.format(temp_date));

            /*
            * 남은 시간 계산시 앞으로의 알람 일정에서 빼는 방식으로 수정할 필요가 있음*/
        /*
            Date currentTime = Calendar.getInstance().getTime();
            long subt= temp_date.getTime()-currentTime.getTime();
            long diffSeconds = subt / 1000 % 60;
            long diffMinutes = subt / (60 * 1000) % 60;
            long diffHours = subt / (60 * 60 * 1000) % 24;
            long diffDays = subt / (24 * 60 * 60 * 1000);
            Log.d(String.valueOf(this), "SubTime: "+diffDays+"일 "+diffHours+"시간 "+diffMinutes+"분 후");

        }catch (Exception e){

        }
         */

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView alarm_title1;
        public TextView alarm_time_text;
        public LinearLayout item_layout;
        public TextView weekofDay_tv;
        public TextView alarm_timeleft1;
        public SwitchButton switchButton;
        public TextView alarm_sub11;
        public View game1;
        public View game2;

        public ViewHolder(View itemView) {
            super(itemView);

            item_layout= itemView.findViewById(R.id.item_layout);
            alarm_title1= itemView.findViewById(R.id.alarm_title1);
            alarm_time_text = itemView.findViewById(R.id.alarm_time1);
            weekofDay_tv = itemView.findViewById(R.id.weekofDay_tv);
            alarm_timeleft1 =itemView.findViewById(R.id.alarm_timeleft1);
            switchButton = itemView.findViewById(R.id.sb_use_listener);
            alarm_sub11 = itemView.findViewById(R.id.alarm_sub11);
            game1 = itemView.findViewById(R.id.game1);
            game2 = itemView.findViewById(R.id.game2);
        }
    }

    private String getTimeWithoutDate(String alarm_time){
        String temp2="";
        String temp="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date setdate = sdf.parse(alarm_time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            temp2 = sdf2.format(setdate);
            Log.d(String.valueOf(this), "temp2?: "+temp2);
            temp = temp2.split(" ")[1];
        }catch (ParseException e){

        }catch (NullPointerException e){

        }

        return temp;
    }


    private TextView setColorInPartitial(String pre_string, String string, String color, TextView textView){
        SpannableStringBuilder builder = new SpannableStringBuilder(pre_string + string);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, pre_string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(builder);
        return textView;
    }

}
