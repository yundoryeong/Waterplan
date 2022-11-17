package com.example.waterplan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.waterplan.R;
import com.example.waterplan.dto.WaterPlanDTO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
    그래프, pref 테스트 클래스
 */
public class StudyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private EditText eatingWater;
    private TextView textView;
    private ArrayList<WaterPlanDTO> waterPlanDTO;
    private SharedPreferences.Editor editor;
    private JSONArray jsonArray;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        button = (Button) findViewById(R.id.button);
        eatingWater = (EditText) findViewById(R.id.eatingWater);
        textView = (TextView) findViewById(R.id.textView);
        lineChart = (LineChart) findViewById(R.id.chart);

        waterPlanDTO = new ArrayList<>();
        jsonArray = new JSONArray();

        // Preferences 선언 (MODE_PRIVATE = 읽기 쓰기)
        SharedPreferences pref = getSharedPreferences("water", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // 앱 실행 시 Preferences(쿠키, 세션과 같은 개념)에 데이터가 있다면
        if(pref.getString("water", null) != null) {
            String response = pref.getString("water", null);
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    waterPlanDTO.add(new WaterPlanDTO(jsonObject.getString("waterTime"), jsonObject.getString("eatingWater")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("데이터 존재 ! : ", String.valueOf(waterPlanDTO));
        }
        if(!waterPlanDTO.isEmpty()){
            createChart();
        }
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                waterPlanDTO.add(new WaterPlanDTO(getTime(), String.valueOf(eatingWater.getText())));
                try {
                    for(int i = 0 ; i < waterPlanDTO.size() ; i++){
                        JSONObject tempJsonObject = new JSONObject();
                        tempJsonObject.put("eatingWater", waterPlanDTO.get(i).getEatingWater());
                        tempJsonObject.put("waterTime", waterPlanDTO.get(i).getWaterTime());
                        jsonArray.put(tempJsonObject);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                editor.putString("water", String.valueOf(jsonArray));
                editor.commit();
                break;
        }
    }

    // 현재 시간을 "MM-dd hh:mm"로 표시하는 메소드
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    void createChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i = 0; i < waterPlanDTO.size() ; i++){
            entries.add(new Entry(
                    (float) Integer.parseInt(waterPlanDTO.get(i).getEatingWater())/100, (float) i
            ));
        }

        // 차트 스타일
        lineChart.setBackgroundColor(Color.rgb(212, 244, 250));
        // 차트 설명 비활성화
        lineChart.getDescription().setEnabled(false);
        // 차트 터치 제스쳐 활성화
        lineChart.setTouchEnabled(true);
        // 리스너 비활성화
        lineChart.setDrawGridBackground(false);
        // 크기조정 및 드래그 활성화
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // 강제 핀치 줌 활성화
        lineChart.setPinchZoom(true);

        // x축 설정
        XAxis xAxis = lineChart.getXAxis();
        // x축의 위치 아래쪽으로
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 수직 격자선
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        // 수직 범례 색상
        xAxis.setTextColor(Color.BLACK);
        xAxis.setAxisLineColor(Color.BLACK);
        // 그래프 좌우 여백
        xAxis.setSpaceMax(0.3f);
        xAxis.setSpaceMin(0.3f);
//        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new TimeAxisValueFormat());
//        xAxis.enableGridDashedLine(8, 24, 0);

        // y축 설정
        YAxis yAxis = lineChart.getAxisLeft();
        // 이중 축 비활성화 (왼쪽만 사용)
        lineChart.getAxisRight().setEnabled(false);
        // 수평 격자선
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        // 수평 범례색상
        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);
        // axis range
        yAxis.setAxisMaximum(30f);
        yAxis.setAxisMinimum(0f);

        // 그래프 set Data
        LineDataSet lineDataSet;
        if(lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0){
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(entries);
            lineDataSet.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(entries, "먹은 물의 양");

            // 특정 그래프 스타일 (LineDataSet)
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawIcons(false);
            // 격자선 지정
            lineDataSet.enableDashedLine(10f, 5f, 0f);
            // 포인트 스타일
            lineDataSet.setColor(Color.WHITE);
            lineDataSet.setCircleColor(Color.WHITE);
            // 선 두께, 포인트 크기
            lineDataSet.setLineWidth(1f);
            lineDataSet.setCircleRadius(2f);
            // 점을 실선으로 표현
            lineDataSet.setDrawCircleHole(false);
            // 범례 사용자 정의
            lineDataSet.setLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(
                    new float[]{10f, 5f}, 0f
            ));
            lineDataSet.setFormSize(15.f);
            // values 텍스트 크기
            lineDataSet.setValueTextSize(9f);
            // 점선으로 선택 선 그리기
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

            // 채워진 영역 설정
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            lineDataSet.setFillColor(Color.WHITE);

            LineData data = new LineData(lineDataSet);
            lineChart.setData(data);
        }
        // 최대 X좌표 기준 몇 개까지 보여줄 것인지
        lineChart.setVisibleXRangeMaximum(7);
        // 가장 최근 데이터로 이동
        lineChart.moveViewToX(lineDataSet.getEntryCount());
        lineChart.animateX(1500);
        // 범례 지정 후 그리기
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
    }
}

class TimeAxisValueFormat extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = dateFormat.format(date);
        return getTime;
    }
}