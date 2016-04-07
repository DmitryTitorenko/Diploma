package com.handstudio.android.hzgrapher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.CurveGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraph;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraphVO;

public class CurveGraphActivity extends Activity {

    private static final int REPORT = 100;
    private ViewGroup layoutGraphView;

    private TextView tv1;
    private TextView tvSet1;
    private TextView tvSet2;
    private TextView tvSet3;
    private TextView tv4;
    private TextView tvSet4;
    private TextView tv5;
    private TextView tvSet5;
    private TextView tv6;
    private TextView tvSet6;


    private int stepEvent = 0;//счетчик событий
    private int tm = 0;//Модельное время
    private final ArrayList<Integer> eventSupport = new ArrayList<>();//Count event in support mode
    private final ArrayList<Double> Q = new ArrayList<>();// количество теплоты/холода, которое необходимо для нагревания/охлаждения
    private final ArrayList<Double> time = new ArrayList<>();//время, которое необходимо для нагревания Q на один градус
    private final ArrayList<Double> Nr_oll = new ArrayList<>();//реальная производительность кондиционера
    private final ArrayList<Double> Q_heat_loss = new ArrayList<>();// теплопотери
    private final ArrayList<Integer> graphT = new ArrayList<>();       //Хранятся данные о температуре на заданом шаге 1й режим
    private final ArrayList<Integer> t_street_random_array = new ArrayList<>();//случайная темпераутура на улице для 1го режима и так же в доме для 2го
    private final ArrayList<Integer> tStreet = new ArrayList<>();// температура на улице для 1го режима
    private final ArrayList<Integer> HomeTemp = new ArrayList<>();//температура в доме для 1го режима
    private final ArrayList<Integer> RandomHome = new ArrayList<>();//изменение температуры в доме
    private ArrayList<Integer> eventMod = new ArrayList<>();//события - изменения температуры на улице или в доме
    private int tStreetReal = 0;//текущая температура на улице для 2го режима
    private int countModeFirst = 0;//количество событий в режиме 1
    private int randomEvent;//случайное событие


    private int callRandomEvent1() {
        return -1 + (int) (Math.random() * ((2) + 1));
    }

    private int callRandomEvent2() {//-1 или 1
        int a = (int) (Math.random() * 3) - 1;
        if (a == 0) {
            a = callRandomEvent2();
        }
        return a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);
        tv1 = (TextView) findViewById(R.id.tv1);
        tvSet1 = (TextView) findViewById(R.id.tvSet1);
        tvSet2 = (TextView) findViewById(R.id.tvSet2);
        tvSet3 = (TextView) findViewById(R.id.tvSet3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tvSet4 = (TextView) findViewById(R.id.tvSet4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tvSet5 = (TextView) findViewById(R.id.tvSet5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tvSet6 = (TextView) findViewById(R.id.tvSet6);
        Button btn_event = (Button) findViewById(R.id.btn_event);

        //отрисовка при запуске Activity
        randomEvent = callRandomEvent2();
        setCurveGraph(stepEvent, randomEvent);
        mathAll();
        stepEvent++;


        btn_event.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                try {
                    try {
                        if (Integer.valueOf(extras.getString(ModeFirst.t2)) != 0) {// для первого режима
                            int t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
                            int t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
                            if (t1 < t2) {
                                if (t1 + stepEvent < t2) {
                                    randomEvent = callRandomEvent1();
                                    mathAll();
                                    setCurveGraph(stepEvent, randomEvent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                                }
                            } else if (t1 > t2) {
                                if (t1 - stepEvent > t2) {
                                    randomEvent = callRandomEvent1();
                                    mathAll();
                                    setCurveGraph(stepEvent, randomEvent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        if (Integer.valueOf(extras.getString(ModeSecond.t_support)) != 0) {// для второго  режима
                            if (stepEvent < event_limit()) {
                                randomEvent = callRandomEvent2();
                                mathAll();
                                setCurveGraph(stepEvent, randomEvent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (NumberFormatException e) {//3й режим
                    int t2 = Integer.valueOf(extras.getString(ModeThird.t2_support));
                    int t1 = Integer.valueOf(extras.getString(ModeThird.t1));
                    int a;
                    if (t2 > t1) {
                        a = t2 - t1;
                    } else {
                        a = t1 - t2;
                    }
                    if (stepEvent < event_limit() + a) {
                        randomEvent = callRandomEvent2();
                        mathAll();
                        setCurveGraph(stepEvent, randomEvent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                    }
                }
                stepEvent++;
            }
        });
    }

    private int event_limit() {// метод для нахождения количества событий
        Bundle extras = getIntent().getExtras();
        ArrayList<Integer> event_mode = new ArrayList<>();
        int tm = 0;
        if (extras.getIntegerArrayList(ModeSecond.eventArray_) != null) {
            event_mode = extras.getIntegerArrayList(ModeSecond.eventArray_);
        } else if (extras.getIntegerArrayList(ModeThird.eventArray_) != null)
            event_mode = extras.getIntegerArrayList(ModeThird.eventArray_);

        Integer[] eventArray = new Integer[event_mode.size()];//convert ArrayList to Integer[]
        event_mode.toArray(eventArray);
        for (int ia = 0; ia < eventArray.length; ia++) {//находим кол-во элементов массива (события)
            tm++;
        }
        return tm;
    }

    private void writeFileSD() {
        Bundle extras = getIntent().getExtras();
        if (Integer.valueOf(extras.getString(ModeFirst.t1)) != 0) ;
        writeFileSDFirst();
    }

    private void writeFileSDFirst() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String LOG_TAG = "myLogs";
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        String DIR_SD = "Report of modeling";
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        String FILENAME_SD = "Report.txt";
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            // попытка записать в цикле данные с массива (тест)
            bw.write(" температура на заданном шаге : ");
            for (float element : HomeTemp) {
                bw.write(" " + element);
            }
            bw.write(" теплопотери ");
            for (double element5 : Q_heat_loss) {
                bw.write(" " + element5);
            }
            bw.write(" изменение температуры на улице ");
            for (double element6 : t_street_random_array) {
                bw.write(" " + element6);
            }
            bw.write(" температура на улице: ");
            for (float element : tStreet) {
                bw.write(" " + element);
            }
            // закрываем поток
            if (HomeTemp.get(0) > HomeTemp.get(1)) {
                bw.write(" количество теплоты, которое необходимо для остывания: ");
                for (double element2 : Q) {
                    bw.write(" " + element2);
                }
                bw.write(" время, которое необходимо для охлождения Q на один градус: ");
                for (double element3 : time) {
                    bw.write(" " + element3);
                }
                bw.write(" реальная производительность кондиционера на охлождение ");
                for (double element4 : Nr_oll) {
                    bw.write(" " + element4);
                }
            } else {
                bw.write(" количество теплоты, которое необходимо для обогрева: ");
                for (double element2 : Q) {
                    bw.write(" " + element2);
                }
                bw.write(" время, которое необходимо для обогрева Q на один градус: ");
                for (double element3 : time) {
                    bw.write(" " + element3);
                }
                bw.write(" реальная производительность кондиционера на обогрев ");
                for (double element4 : Nr_oll) {
                    bw.write(" " + element4);
                }
            }
            bw.write(" модельное время: " + tm);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, REPORT, Menu.NONE, "Report of modeling");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case REPORT:
                writeFileSD();
                Toast.makeText(getApplicationContext(), " Report of modeling created successfully", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void setCurveGraph(int i, int random_event) {
        //all setting
        CurveGraphVO vo = makeCurveGraphAllSetting(i, random_event);

        //default setting
//		CurveGraphVO vo = makeCurveGraphDefaultSetting();

        layoutGraphView.removeAllViews();//удаляем предыдущие View, что бы обновить график
        layoutGraphView.addView(new CurveGraphView(this, vo));
    }

    private CurveGraphVO makeCurveGraphAllSetting(int i, int random_event) {

        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom = CurveGraphVO.DEFAULT_PADDING;
        int paddingTop = CurveGraphVO.DEFAULT_PADDING;
        int paddingLeft = CurveGraphVO.DEFAULT_PADDING;
        int paddingRight = CurveGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop = CurveGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight = CurveGraphVO.DEFAULT_MARGIN_RIGHT;
        int maxValue = 30;//max температура

        //increment
        int increment = 1;// по оси y 1 т.к. отслеживаем изменение температуры на 1 градус

        //GRAPH SETTING

        // алгоритм для поднятия температуры
        String[] legendArr = {Arrays.toString(axisAll(i))};
        List<CurveGraph> arrGraph = new ArrayList<CurveGraph>();

        arrGraph.add(new CurveGraph("Графік", 0xaa000000, graphAll(random_event)));

        CurveGraphVO vo = new CurveGraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, maxValue, increment, legendArr, arrGraph);
        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        //set graph name box
        vo.setGraphNameBox(new GraphNameBox());
        //set draw graph region
        return vo;
    }


    private void mathAll() throws NumberFormatException {
        Bundle extras = getIntent().getExtras();
        try {
            try {
                if (Integer.valueOf(extras.getString(ModeFirst.t2)) != null) {// для первого режима
                    float BD = Float.valueOf(extras.getString(ModeFirst.p));
                    int t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
                    int t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
                    float c = Float.valueOf(extras.getString(ModeFirst.c));
                    float N_heat_productivity = Float.valueOf(extras.getString(ModeFirst.n));
                    int a = Integer.valueOf(extras.getString(ModeFirst.a));
                    int b = Integer.valueOf(extras.getString(ModeFirst.b));
                    int c_height = Integer.valueOf(extras.getString(ModeFirst.c_height));
                    int t_street = Integer.valueOf(extras.getString(ModeFirst.t_street));
                    float nn = Float.valueOf(extras.getString(ModeFirst.nn));
                    float R0 = Float.valueOf(extras.getString(ModeFirst.R0));
                    float B = Float.valueOf(extras.getString(ModeFirst.B));
                    if (t1 < t2) {
                        math(BD, t1, t2, c, N_heat_productivity,
                                a, b, c_height, t_street, nn, R0, B, 0, "First");
                    } else if (t1 > t2) {
                        math(BD, t1, t2, c, N_heat_productivity,
                                a, b, c_height, t_street, nn, R0, B, 0, "First");
                    }
                }
            } catch (NumberFormatException e) {
                if (Integer.valueOf(extras.getString(ModeSecond.t_support)) != null) {
                    float BD = Float.valueOf(extras.getString(ModeSecond.p));
                    float t_support = Float.valueOf(extras.getString(ModeSecond.t_support));
                    float c = Float.valueOf(extras.getString(ModeSecond.c));
                    float N_loss_productivity = Float.valueOf(extras.getString(ModeSecond.n_loss));
                    int a = Integer.valueOf(extras.getString(ModeSecond.a));
                    int b = Integer.valueOf(extras.getString(ModeSecond.b));
                    int c_height = Integer.valueOf(extras.getString(ModeSecond.c_height));
                    int t_street = Integer.valueOf(extras.getString(ModeSecond.t_street));
                    float nn = Float.valueOf(extras.getString(ModeSecond.nn));
                    float R0 = Float.valueOf(extras.getString(ModeSecond.R0));
                    float B = Float.valueOf(extras.getString(ModeSecond.B));
                    eventMod = extras.getIntegerArrayList(ModeSecond.eventArray_);
                    math(BD, 0, 0, c, N_loss_productivity, a, b, c_height, t_street, nn, R0, B, t_support, "Second");
                }
            }
        } catch (NumberFormatException e) {
            mathsThird();
        }
    }

    private void math(float BD, int t1, int t2, double c, double N_heat_productivity,
                      int a, int b, int c_height, int t_street, double nn, double R0, double B, double t_support, String modeType) {
        double tKelvin = t1 + 273.15;// температура в Кельвинах
        double p = 0.473 * (BD / tKelvin);// плотность
        Integer V = a * b * c_height;//обьем
        double m = p * V; //масса воздуха
        if (modeType.equals("First")) {
            t_street_random_array.add(-1 + (int) (Math.random() * ((2) + 1)));
            tStreetReal += t_street_random_array.get(tm);
            tStreet.add(tStreetReal + t_street);
            Q.add(m * c * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж
            Q_heat_loss.add(a * b * (t1 - tStreet.get(tm)) * (1 + B) * nn / R0);
            Nr_oll.add(N_heat_productivity - Q_heat_loss.get(tm));
            time.add(Q.get(tm) / Nr_oll.get(tm));
            if (t1 < t2) {
                t1++;
                HomeTemp.add(t1 + tm);
                tv4.setText("Час, котрий необхідний для обогріву на 1 градус, сек");
            } else if (t1 > t2) {
                t1--;
                HomeTemp.add(t1 - tm);
                tv4.setText("Час, котрий необхідний для охолодження на 1 градус, сек");
            }
            tv1.setText("Поточна температура");
            tvSet1.setText("" + HomeTemp.get(tm));
            tvSet2.setText("" + t_street_random_array.get(tm));
            tvSet3.setText("" + tStreet.get(tm));
            tvSet4.setText("" + time.get(tm));
            tv5.setText("Модельний час");
            tvSet5.setText("" + (tm + 1));
            tv6.setText("");
            tvSet6.setText("");
            RandomHome.add(null);
        } else if (modeType.equals("Second")) {
            if (eventMod.get(tm - countModeFirst) == 0) {//изменение на улице
                t_street_random_array.add(callRandomEvent2());
                tStreetReal += t_street_random_array.get(tm);
                tStreet.add(tStreetReal + t_street);
                Q_heat_loss.add(a * b * (t1 - tStreet.get(tm)) * (1 + B) * nn / R0);
                Q.add(null);//т.к. они не используются в этом режиме
                time.add(null);
                Nr_oll.add(null);
                RandomHome.add(null);
                tvSet1.setText("" + t_support);
                tvSet2.setText("" + t_street_random_array.get(tm));
                tvSet3.setText("" + tStreet.get(tm));
                tv4.setText("Необхідна потужність, Дж/сек");
                tvSet4.setText("" + Q_heat_loss.get(tm));
                tv5.setText("Модельний час");
                tvSet5.setText("" + (tm + 1));
                tv6.setText("");
                tvSet6.setText("");
            } else {
                t_street_random_array.add(-1 + (int) (Math.random() * ((2) + 1)));
                tStreetReal += t_street_random_array.get(tm);
                tStreet.add(tStreetReal + t_street);
                RandomHome.add(callRandomEvent2());
                Q.add(m * c * 1000);
                Q_heat_loss.add(a * b * (t1 - tStreet.get(tm)) * (1 + B) * nn / R0);
                Nr_oll.add(N_heat_productivity - Q_heat_loss.get(tm));
                time.add(Q.get(tm) / Nr_oll.get(tm));
                if (t1 < t2) {
                    t1++;
                    tv4.setText("Час, котрий необхідний для обогріву на 1 градус, сек");
                } else if (t1 > t2) {
                    t1--;
                    tv4.setText("Час, котрий необхідний для охолодження на 1 градус, сек");
                }
                tvSet1.setText("" + t_support);
                tvSet2.setText("" + t_street_random_array.get(tm));
                tvSet3.setText("" + tStreet.get(tm));
                tv4.setText("Зміна температури в домі, °C");
                tvSet4.setText("" + RandomHome.get(tm));
                tv5.setText("Час, необхідний для охолодження/обогріву на 1 градус, сек");
                tvSet5.setText("" + time.get(tm));
                tv6.setText("Модельний час");
                tvSet6.setText("" + (tm + 1));
            }
        }
        graphT.add(t1);
        tm++;
    }

    private void mathsThird() {
        Bundle extras = getIntent().getExtras();
        Float BD = Float.valueOf(extras.getString(ModeThird.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeThird.t1));
        Integer t2 = Integer.valueOf(extras.getString(ModeThird.t2_support));
        Float c = Float.valueOf(extras.getString(ModeThird.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeThird.n));
        Integer a = Integer.valueOf(extras.getString(ModeThird.a));
        Integer b = Integer.valueOf(extras.getString(ModeThird.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeThird.c_height));
        Integer t_street = Integer.valueOf(extras.getString(ModeThird.t_street));
        Float nn = Float.valueOf(extras.getString(ModeThird.nn));
        Float R0 = Float.valueOf(extras.getString(ModeThird.R0));
        Float B = Float.valueOf(extras.getString(ModeThird.B));
        float aq;
        if (t1 > t2) {
            aq = t1 - t2;
        } else if (t1 < t2) {
            aq = t2 - t1;
        } else aq = 0;
        if (stepEvent + 1 <= aq) {              //проверка на события, если события==температуры, которую нужно достич, то переходим к режиму 2
            if (t1 > t2) {
                math(BD, t1, t2, c, N_heat_productivity, a, b, c_height, t_street, nn, R0, B, 0, "First");
                countModeFirst++;
            } else {
                math(BD, t1, t2, c, N_heat_productivity, a, b, c_height, t_street, nn, R0, B, 0, "First");
                countModeFirst++;
            }
            eventMod = extras.getIntegerArrayList(ModeThird.eventArray_);
        } else {
            math(BD, 0, 0, c, N_heat_productivity, a, b, c_height, t_street, nn, R0, B, t2, "Second");
        }
    }

    private int[] axisAll(int eventX) {// отсчет для оси х// in start events = 0;
        int[] axis;
        axis = new int[eventX + 2];
        for (int i = 0; i < axis.length; i++) {
            axis[i] = i;
        }
        return axis;
    }

    private float[] graphAll(int randomEvent) throws NumberFormatException {
        Bundle extras = getIntent().getExtras();
        float[] graphT = {0};
        try {
            try {
                if (Integer.valueOf(extras.getString(ModeFirst.t2)) != null) {
                    Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
                    Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
                    if (t1 < t2) {
                        graphT = graphFirst(t1, t2, "up");
                    } else if (t1 > t2) {
                        graphT = graphFirst(t1, t2, "down");
                    }
                }
            } catch (NumberFormatException e) {
                if (Integer.valueOf(extras.getString(ModeSecond.t_support)) != null)
                    graphT = graphSupport(randomEvent, "SecondMode");
            }
        } catch (NumberFormatException e) {
            graphT = graphThird(randomEvent);
        }
        return graphT;
    }

    private float[] graphFirst(int t1, int t2, String type) {
        int tm = 0;//размерность массива
        int t1add = t1;
        float[] graphT = {};
        if (type.equals("up")) {
            for (int ia = 0; t1add <= t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
                tm++;
                t1add++;
                if (ia > stepEvent) break;
            }
            graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
            for (int ia = 0; t1 <= t2; ia++) {
                graphT[ia] = t1;
                t1++;
                if (ia > stepEvent) break;
            }
        } else if (type.equals("down")) {
            for (int ia = 0; t1add >= t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
                tm++;
                t1add--;
                if (ia > stepEvent) break;
            }
            graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
            for (int ia = 0; t1 >= t2; ia++) {
                graphT[ia] = t1;
                t1--;
                if (ia > stepEvent) break;
            }
        }
        return graphT;
    }

    private float[] graphSupport(int random_event, String type) {
        Bundle extras = getIntent().getExtras();
        int t_support = 0;
        if (type.equals("SecondMode")) {
            t_support = Integer.valueOf(extras.getString(ModeSecond.t_support));
            if (stepEvent == 0)
                eventSupport.add(t_support);// write graph start in t_support
        } else if (type.equals(("ThirdMode"))) {
            t_support = Integer.valueOf(extras.getString(ModeThird.t2_support));
        }
        eventSupport.add(t_support + random_event);
        eventSupport.add(t_support);
        float[] floatArray = new float[eventSupport.size()];
        int i = 0;
        for (Integer f : eventSupport) {
            floatArray[i++] = f;
        }
        return floatArray;
    }

    private float[] graphThird(int randomEvent) {
        Bundle extras = getIntent().getExtras();
        Integer t1 = Integer.valueOf(extras.getString(ModeThird.t1));
        Integer t2 = Integer.valueOf(extras.getString(ModeThird.t2_support));
        float graphT[] = {};
        float a;
        if (t1 > t2) {
            a = t1 - t2;
        } else if (t1 < t2) {
            a = t2 - t1;
        } else a = 0;
        if (a != 0) {
            if (stepEvent + 1 <= a) {
                if (stepEvent == 0) {
                    eventSupport.add(t1);// write graph start in t1 and end in t1+stepEvent+1
                    eventSupport.add(t1 + stepEvent + 1);
                } else eventSupport.add(t1 + stepEvent + 1);
                if (t1 > t2) {
                    graphT = graphFirst(t1, t2, "down");
                } else {
                    graphT = graphFirst(t1, t2, "up");
                }
            } else {
                graphT = graphSupport(randomEvent, "ThirdMode");
            }
        }
        return graphT;
    }
}