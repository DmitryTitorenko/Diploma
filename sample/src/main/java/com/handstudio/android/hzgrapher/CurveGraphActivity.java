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

    static final int REPORT = 100;
    private ViewGroup layoutGraphView;
    final String LOG_TAG = "myLogs";

    final String DIR_SD = "Report of modeling";
    final String FILENAME_SD = "Report.txt";

    TextView tv_1;
    TextView tv_2;
    TextView tv_21;
    TextView tv_31;
    TextView tv_3;
    TextView tv_4;
    TextView tv_5;
    TextView tv_6;
    Button btn_event;
    TextView pover_conditionin;
    TextView tvsupport;
    TextView tv_heat_loss;
    TextView tv_need_time;
    TextView tv_heat_quantity;//количество телоты
    TextView tv_41;
    TextView model_time;
    TextView model_time1;

    int ii_minusofplus = 0;//количество событий в режиме 1
    int stepEvent = 0;//счетчик событий
    ArrayList<Float> eventSupport = new ArrayList<>();//подсчет событий при поддержки температуры
    ArrayList<Float> event_math = new ArrayList<>();
    int count_eventArray = 0;
    int real_temp_street;//измененние температуры на улице
    int model_time_Mode_third = 0;//модельное время для третьего режима
    float graphFirstMod[] = {0};//массив с значениями температуры первого режима для ее поддержки в 3м режиме

    public int callRandomEvent1() {
        return -1 + (int) (Math.random() * ((2) + 1));
    }

    public int callRandomEvent2() {//-1 или 1
        int a = (int) (Math.random() * 3) - 1;
        if (a == 0) {
            a = callRandomEvent2();
        }
        return a;
    }

    int randomEvent;//случайное событие

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_21 = (TextView) findViewById(R.id.tv_21);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_31 = (TextView) findViewById(R.id.tv_31);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_heat_loss = (TextView) findViewById(R.id.textView39);
        tvsupport = (TextView) findViewById(R.id.textView12);
        btn_event = (Button) findViewById(R.id.btn_event);
        pover_conditionin = (TextView) findViewById(R.id.textView37);
        tv_need_time = (TextView) findViewById(R.id.tv_31);
        tv_heat_quantity = (TextView) findViewById(R.id.tv_21);
        tv_41 = (TextView) findViewById(R.id.textView41);
        model_time = (TextView) findViewById(R.id.tv_model_time);
        model_time1 = (TextView) findViewById(R.id.tv_model_teme1);

        //отрисовка при запуске Activity
        randomEvent = callRandomEvent2();
        setCurveGraph(stepEvent, randomEvent);
        maths_oll(stepEvent, randomEvent);
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
                                    maths_oll(stepEvent, randomEvent);
                                    setCurveGraph(stepEvent, randomEvent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                                }
                            } else if (t1 > t2) {
                                if (t1 - stepEvent > t2) {
                                    randomEvent = callRandomEvent1();
                                    maths_oll(stepEvent, randomEvent);
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
                                maths_oll(stepEvent, randomEvent);
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
                        maths_oll(stepEvent, randomEvent);
                        setCurveGraph(stepEvent, randomEvent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Моделювання закінченно", Toast.LENGTH_SHORT).show();
                    }
                }
                stepEvent++;
            }
        });
    }

    public int event_limit() {// метод для нахождения количества событий
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

    void writeFileSD() {
        Bundle extras = getIntent().getExtras();
        Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
        Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
        if (t1 < t2) {
            writeFileSD_plus();
        } else if (t1 > t2) {
            writeFileSD_minus();
        }
    }

    void writeFileSD_plus() {

        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeFirst.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeFirst.t1));//дополнительная переменная для цикла
        Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
        Float c = Float.valueOf(extras.getString(ModeFirst.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeFirst.n));
        Integer a = Integer.valueOf(extras.getString(ModeFirst.a));
        Integer b = Integer.valueOf(extras.getString(ModeFirst.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeFirst.c_height));

        Integer t_street = Integer.valueOf(extras.getString(ModeFirst.t_street));
        Float nn = Float.valueOf(extras.getString(ModeFirst.nn));
        Float R0 = Float.valueOf(extras.getString(ModeFirst.R0));
        Float B = Float.valueOf(extras.getString(ModeFirst.B));


        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

        Integer v = a * b * c_height;//обьем

        int maxValue = 20;//max температура
        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь

        //	if (t1<t2){// для поднятия температуры
        for (int i = 0; t1add < t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add++;
        }
        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице

        for (int i = 0; t1 < t2; i++) {
            graphT[i] = t1;
            axis[i] = i;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж

            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[i] = t_street_random;
            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Q_heat_loss[i] = q;
            Nr = N_heat_productivity - q;
            Nr_oll[i] = Nr;
            time[i] = Q[i] / Nr;
            t1++;
        }
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            // попытка записать в цикле данные с массива (тест)
            bw.write(" температура на заданном шаге : ");
            for (float element : graphT) {
                bw.write(" " + element);
            }

            bw.write(" количество холода, которое необходимо для нагревания: ");
            for (double element2 : Q) {
                bw.write(" " + element2);
            }
            bw.write(" время, которое необходимо для нагревания Q на один градус: ");
            for (double element3 : time) {
                bw.write(" " + element3);
            }
            bw.write(" модельное время: " + tm);
            bw.write(" реальная производительность кондиционера на обогрев ");
            for (double element4 : Nr_oll) {
                bw.write(" " + element4);
            }
            bw.write(" теплопотери ");
            for (double element5 : Q_heat_loss) {
                bw.write(" " + element5);
            }
            bw.write(" изменение температуры на улице ");
            for (double element6 : t_street_random_array) {
                bw.write(" " + element6);
            }

            // закрываем поток
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFileSD_minus() {

        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeFirst.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeFirst.t1));//дополнительная переменная для цикла
        Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
        Float c = Float.valueOf(extras.getString(ModeFirst.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeFirst.n));
        Float N_loss_productivity = Float.valueOf(extras.getString(ModeFirst.n_loss));

        Integer a = Integer.valueOf(extras.getString(ModeFirst.a));
        Integer b = Integer.valueOf(extras.getString(ModeFirst.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeFirst.c_height));

        Integer t_street = Integer.valueOf(extras.getString(ModeFirst.t_street));
        Float nn = Float.valueOf(extras.getString(ModeFirst.nn));
        Float R0 = Float.valueOf(extras.getString(ModeFirst.R0));
        Float B = Float.valueOf(extras.getString(ModeFirst.B));


        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

        Integer v = a * b * c_height;//обьем

        int maxValue = 20;//max температура
        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь

        for (int i = 0; t1add > t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add--;
        }
        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице


        for (int i = 0; t1 > t2; i++) {
            graphT[i] = t1;
            axis[i] = i;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[i] = t_street_random;

            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Q_heat_loss[i] = q;

            Nr = N_loss_productivity - q;
            Nr_oll[i] = Nr;
            time[i] = Q[i] / Nr;
            t1--;
        }


        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            // попытка записать в цикле данные с массива (тест)
            bw.write(" температура на заданном шаге : ");
            for (float element : graphT) {
                bw.write(" " + element);
            }

            bw.write(" количество теплоты, которое необходимо для остывания: ");
            for (double element2 : Q) {
                bw.write(" " + element2);
            }
            bw.write(" время, которое необходимо для охлождения Q на один градус: ");
            for (double element3 : time) {
                bw.write(" " + element3);
            }
            bw.write(" модельное время: " + tm);
            bw.write(" реальная производительность кондиционера на охлождение ");
            for (double element4 : Nr_oll) {
                bw.write(" " + element4);
            }
            bw.write(" теплопотери ");
            for (double element5 : Q_heat_loss) {
                bw.write(" " + element5);
            }
            bw.write(" изменение температуры на улице ");
            for (double element6 : t_street_random_array) {
                bw.write(" " + element6);
            }
            // закрываем поток
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


    public void maths_oll(int iiii, int random_event) throws NumberFormatException {
        Bundle extras = getIntent().getExtras();
        try {
            try {
                if (Integer.valueOf(extras.getString(ModeFirst.t2)) != null) {// для первого режима
                    Float BD = Float.valueOf(extras.getString(ModeFirst.p));
                    Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
                    Integer t1add = Integer.valueOf(extras.getString(ModeFirst.t1));//дополнительная переменная для цикла
                    Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
                    Float c = Float.valueOf(extras.getString(ModeFirst.c));
                    Float N_heat_productivity = Float.valueOf(extras.getString(ModeFirst.n));
                    Integer a = Integer.valueOf(extras.getString(ModeFirst.a));
                    Integer b = Integer.valueOf(extras.getString(ModeFirst.b));
                    Integer c_height = Integer.valueOf(extras.getString(ModeFirst.c_height));
                    Integer t_street = Integer.valueOf(extras.getString(ModeFirst.t_street));
                    Float nn = Float.valueOf(extras.getString(ModeFirst.nn));
                    Float R0 = Float.valueOf(extras.getString(ModeFirst.R0));
                    Float B = Float.valueOf(extras.getString(ModeFirst.B));

                    if (t1 < t2) {
                        mathFirstMode(iiii, BD, t1, t1add, t2, c, N_heat_productivity,
                                a, b, c_height, t_street, nn, R0, B);
                    } else if (t1 > t2) {
                        mathFirstMode(iiii, BD, t1, t1add, t2, c, N_heat_productivity,
                                a, b, c_height, t_street, nn, R0, B);
                    }
                }
            } catch (NumberFormatException e) {
                if (Integer.valueOf(extras.getString(ModeSecond.t_support)) != null)
                    maths_support(iiii, random_event);
            }
        } catch (NumberFormatException e) {
            maths_third(iiii, random_event);
        }
    }


    public void mathFirstMode(int ii, float BD, int t1, int t1add, int t2, float c, float N_heat_productivity,
                              int a, int b, int c_height, int t_street, float nn, float R0, float B) {
        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0
        Integer V = a * b * c_height;//обьем
        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь
        for (int ia = 0; t1add != t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add++;
            if (ia == ii) break;
        }
        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице
        for (int ia = 0; t1 != t2; ia++) {
            graphT[ia] = t1;
            axis[ia] = ia;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * V; //масса воздуха
            Q[ia] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[ia] = t_street_random;
            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Q_heat_loss[ia] = q;
            Nr = N_heat_productivity - q;
            Nr_oll[ia] = Nr;
            time[ia] = Q[ia] / Nr;
            if (t1 < t2) {
                t1++;
                tv_21.setText("Кількість теплоти, необхідної для нагріву, Дж");
                tv_31.setText("Час, котрий необхідний для обогріву на 1 градус, сек");
            } else if (t1 > t2) {
                t1--;
                tv_21.setText("Кількість теплоти, необхідної для охолодження, Дж");
                tv_31.setText("Час, котрий необхідний для охолодження на 1 градус, сек");
            }
            tv_1.setText("" + t1);
            tv_2.setText("" + Q[ia]);
            tv_6.setText("" + t_street_random);
            tv_5.setText("" + q);
            tv_4.setText("" + Nr);
            tv_3.setText("" + time[ia]);
            model_time.setText("" + tm);
            if (ia == ii)
                break;
        }
    }

    public void maths_minus(int ii) {

        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeFirst.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeFirst.t1));//дополнительная переменная для цикла
        Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
        Float c = Float.valueOf(extras.getString(ModeFirst.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeFirst.n));
        Float N_loss_productivity = Float.valueOf(extras.getString(ModeFirst.n_loss));
        Integer a = Integer.valueOf(extras.getString(ModeFirst.a));
        Integer b = Integer.valueOf(extras.getString(ModeFirst.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeFirst.c_height));

        Integer t_street = Integer.valueOf(extras.getString(ModeFirst.t_street));
        Float nn = Float.valueOf(extras.getString(ModeFirst.nn));
        Float R0 = Float.valueOf(extras.getString(ModeFirst.R0));
        Float B = Float.valueOf(extras.getString(ModeFirst.B));

        tv_21.setText("Кількість теплоти необхідне для охолодження, Дж");
        tv_31.setText("Час, котрий необхідний для охолодження на 1 градус, сек");

        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

        Integer v = a * b * c_height;//обьем

        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь

        for (int ia = 0; t1add > t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add--;
            if (ia == ii) break;
        }
        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице

        for (int ia = 0; t1 > t2; ia++) {

            graphT[ia] = t1;
            axis[ia] = ia;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[ia] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[ia] = t_street_random;

            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Q_heat_loss[ia] = q;
            Nr = N_loss_productivity - q;
            Nr_oll[ia] = Nr;
            time[ia] = Q[ia] / Nr;
            t1--;

            tv_1.setText("" + t1);
            tv_2.setText("" + Q[ia]);
            tv_6.setText("" + t_street_random);
            tv_5.setText("" + q);
            tv_4.setText("" + Nr);
            tv_3.setText("" + time[ia]);
            model_time.setText("" + tm);

            if (ia == ii)
                break;
        }
    }


    public void maths_support(int ii, int random_event) {
        Log.e(LOG_TAG, "maths__support ii " + ii);

        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeSecond.p));
        Float t_support = Float.valueOf(extras.getString(ModeSecond.t_support));
        Integer t1add = Integer.valueOf(extras.getString(ModeSecond.t_support));//дополнительная переменная для цикла
        Float c = Float.valueOf(extras.getString(ModeSecond.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeSecond.n));
        Float N_loss_productivity = Float.valueOf(extras.getString(ModeSecond.n_loss));
        Integer a = Integer.valueOf(extras.getString(ModeSecond.a));
        Integer b = Integer.valueOf(extras.getString(ModeSecond.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeSecond.c_height));
        Integer t_street = Integer.valueOf(extras.getString(ModeSecond.t_street));
        Float nn = Float.valueOf(extras.getString(ModeSecond.nn));
        Float R0 = Float.valueOf(extras.getString(ModeSecond.R0));
        Float B = Float.valueOf(extras.getString(ModeSecond.B));

        Integer v = a * b * c_height;//обьем
        float F = a * b;//площадь


        ArrayList<Integer> event_mode = new ArrayList<>();
        event_mode = extras.getIntegerArrayList(ModeSecond.eventArray_);

        Integer[] eventArray = new Integer[event_mode.size()];//convert ArrayList to Integer[]
        event_mode.toArray(eventArray);

//////////////////////////////////////////////заполняем изменение температуры
        int tm = 0;
        int ia;
        for (ia = 0; ia <= eventArray.length; ia++) {
            if (ia > ii) break;
            if (ii == 0)
                event_math.add(t_support);// добавляем значения что бы он рисовал при запуске
        }
        event_math.add(t_support + random_event);//ArrayList с значениями температуры
        float[] floatArray = new float[event_math.size()];//на соколько изменилась температура
        int i = 0;
        for (Float f : event_math) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        /////////////////////////////////////////// заполнинили


        Float q;
        real_temp_street += random_event;
        int real_temp_street_oll = t_street + real_temp_street;//реальное изменение температуры на данном шаге
        for (int iaa = 0; iaa < eventArray.length; iaa++) {//проход по списку событий
            tm++;
            if (iaa == ii) break;
        }
        if (eventArray[count_eventArray] == 0) {

            double[] Nr_oll = new double[tm];//реальная производительность кондиционера
            q = Float.valueOf(F * (t_support - real_temp_street_oll) * (1 + B) * nn / R0);//все теплопотери
            Nr_oll[ii] = q;

            tvsupport.setText("Температура, яка підтримується, °C");
            pover_conditionin.setText("Необхідна потужність кондиціонера Дж/сек");
            tv_heat_loss.setText("Температура на вулиці, °C");
            tv_1.setText("" + t_support);
            tv_2.setText("");
            tv_6.setText("" + random_event);
            tv_5.setText("" + real_temp_street_oll);
            tv_4.setText("" + Nr_oll[ii]);
            tv_3.setText("" + tm);
            tv_need_time.setText("Модельний час");
            tv_heat_quantity.setText("");
            model_time.setText("");
            model_time1.setText("");

        } else {

            double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
            double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
            double[] Nr_oll = new double[tm];//реальная производительность кондиционера
            double[] Q_heat_loss = new double[tm];//массив с теплопотерями
            double tKelvin = t_support + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[ii] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            q = Float.valueOf(F * (t_support - t_street) * (1 + B) * nn / R0);//все теплопотери
            Nr_oll[ii] = N_loss_productivity - q;
            Q_heat_loss[ii] = q;
            time[ii] = Q[ii] / Nr_oll[ii];

            tvsupport.setText("Температура, яка підтримується, °C");
            pover_conditionin.setText("Реальна потужність кондиціонера, Дж/сек");
            tv_heat_loss.setText("Температура на вулиці, °C");
            tv_1.setText("" + t_support);
            tv_2.setText("" + Q[ii]);
            tv_6.setText("" + random_event);
            tv_5.setText("" + t_street);
            tv_4.setText("" + Nr_oll[ii]);
            tv_3.setText("" + time[ii]);
            tv_41.setText("Змінення температури у домі, °C");
            model_time.setText("" + tm);
            model_time1.setText("Модельний час");
            if (random_event == -1) {
                tv_need_time.setText("Час, необхідний для нагрівання, сек");
                tv_heat_quantity.setText("Кількість теплоти, необхідної для нагрівання, Дж");
            } else {
                tv_need_time.setText("Час, необхідний для охолодження, сек");
                tv_heat_quantity.setText("Кількість теплоти, необхідної для охолодження, Дж");
            }
        }
        count_eventArray++;
    }

    public void maths_third(int ii, int random_event) {
        Log.e(LOG_TAG, "maths_third_ 1 ");

        Bundle extras = getIntent().getExtras();


        Float BD = Float.valueOf(extras.getString(ModeThird.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeThird.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeThird.t1));//дополнительная переменная для цикла
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

        float aq = 0;
        if (t1 > t2) {
            aq = t1 - t2;
        } else if (t1 < t2) {
            aq = t2 - t1;
        } else aq = 0;
        if (aq != 0) {
            if (ii + 1 <= aq) {            //режим 1  //проверка на события, если события==температуры, которую нужно достич, то переходим к режиму 2
                if (t1 > t2) {    //понижение
                    mathFirstMode(ii, BD, t1, t1add, t2, c, N_heat_productivity,
                            a, b, c_height, t_street, nn, R0, B);
                    ii_minusofplus++;
                } else {            //повышение

                    mathFirstMode(ii, BD, t1, t1add, t2, c, N_heat_productivity,
                            a, b, c_height, t_street, nn, R0, B);
                    ii_minusofplus++;
                }
            } else {                //режим 2
                maths_third_support(ii - ii_minusofplus, random_event);
            }
        } else {
            maths_third_support(ii, random_event);
        }
        model_time_Mode_third++;
        model_time.setText("" + model_time_Mode_third);
    }

    public void maths_third_minus(int ii) {
        Log.e(LOG_TAG, "maths_third_minus");

        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeThird.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeThird.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeThird.t1));//дополнительная переменная для цикла
        Integer t2 = Integer.valueOf(extras.getString(ModeThird.t2_support));
        Float c = Float.valueOf(extras.getString(ModeThird.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeThird.n));
        Float N_loss_productivity = Float.valueOf(extras.getString(ModeThird.n_loss));
        Integer a = Integer.valueOf(extras.getString(ModeThird.a));
        Integer b = Integer.valueOf(extras.getString(ModeThird.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeThird.c_height));

        Integer t_street = Integer.valueOf(extras.getString(ModeThird.t_street));
        Float nn = Float.valueOf(extras.getString(ModeThird.nn));
        Float R0 = Float.valueOf(extras.getString(ModeThird.R0));
        Float B = Float.valueOf(extras.getString(ModeThird.B));

        tv_21.setText("Кількість теплоти, що необхідно охолодити, Дж");
        tv_31.setText("Час, котрий необхідний для охолодження на 1 градус, сек");

        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

        Integer v = a * b * c_height;//обьем

        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь

        for (int ia = 0; t1add > t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add--;
            if (ia == ii) break;
        }
        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице

        for (int ia = 0; t1 > t2; ia++) {

            graphT[ia] = t1;
            axis[ia] = ia;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[ia] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[ia] = t_street_random;

            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Q_heat_loss[ia] = q;
            Nr = N_loss_productivity - q;
            Nr_oll[ia] = Nr;
            time[ia] = Q[ia] / Nr;
            t1--;

            tv_1.setText("" + t1);
            tv_2.setText("" + Q[ia]);
            tv_6.setText("" + t_street_random);
            tv_5.setText("" + q);
            tv_4.setText("" + Nr);
            tv_3.setText("" + time[ia]);

            if (ia == ii)
                break;
        }
    }

    public void maths_third_plus(int ii) {
        Log.e(LOG_TAG, "maths_third_plus");

        Bundle extras = getIntent().getExtras();
/////////////////////////////////
        Float BD = Float.valueOf(extras.getString(ModeThird.p));
        Integer t1 = Integer.valueOf(extras.getString(ModeThird.t1));
        Integer t1add = Integer.valueOf(extras.getString(ModeThird.t1));//дополнительная переменная для цикла
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


        ///////////////////////////////////////
        tv_21.setText("Кількість теплоти, необхідне для нагрівання, Дж");
        tv_31.setText("Час, котрий необхіден для нагрівання на 1 градус, сек");

        Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

        Integer V = a * b * c_height;//обьем

        int tm = 0;//Модельное время, так же используется для создания массивов
        float Nr;//реальноя производительность кондиционера
        float F = a * b;//площадь

        for (int ia = 0; t1add < t2; ia++) {//находим кол-во элементов массива который необходимо нарисовать
            tm++;
            t1add++;
            if (ia == ii) break;
        }

        float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
        int[] axis = new int[tm];// отсчет для оси х
        double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
        double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
        double[] Nr_oll = new double[tm];//реальная производительность кондиционера
        double[] Q_heat_loss = new double[tm];//массив с теплопотерями
        double[] t_street_random_array = new double[tm];//массив с случайной температурой на улице

        for (int ia = 0; t1 < t2; ia++) {
            graphT[ia] = t1;
            axis[ia] = ia;
            double tKelvin = t1 + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            Log.e(LOG_TAG, "test p=" + p);

            double m = p * V; //масса воздуха
            Log.e(LOG_TAG, "test m=" + m);

            Q[ia] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            Log.e(LOG_TAG, "test Q[ia]=" + Q[ia]);

            int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
            t_street += t_street_random;
            t_street_random_array[ia] = t_street_random;
            q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
            Log.e(LOG_TAG, "q" + q);

            Q_heat_loss[ia] = q;
            Nr = N_heat_productivity - q;
            Nr_oll[ia] = Nr;
            time[ia] = Q[ia] / Nr;

            t1++;

            tv_1.setText("" + t1);
            tv_2.setText("" + Q[ia]);
            tv_6.setText("" + t_street_random);
            tv_5.setText("" + q);
            tv_4.setText("" + Nr);
            tv_3.setText("" + time[ia]);

            if (ia == ii)
                break;
        }
    }

    public void maths_third_support(int ii, int random_event) {
        Log.e(LOG_TAG, "maths_third_support ii " + ii);
        Bundle extras = getIntent().getExtras();

        Float BD = Float.valueOf(extras.getString(ModeThird.p));
        Float t_support = Float.valueOf(extras.getString(ModeThird.t2_support));
        Integer t1add = Integer.valueOf(extras.getString(ModeThird.t2_support));//дополнительная переменная для цикла
        Float c = Float.valueOf(extras.getString(ModeThird.c));
        Float N_heat_productivity = Float.valueOf(extras.getString(ModeThird.n));
        Float N_loss_productivity = Float.valueOf(extras.getString(ModeThird.n_loss));
        Integer a = Integer.valueOf(extras.getString(ModeThird.a));
        Integer b = Integer.valueOf(extras.getString(ModeThird.b));
        Integer c_height = Integer.valueOf(extras.getString(ModeThird.c_height));
        Integer t_street = Integer.valueOf(extras.getString(ModeThird.t_street));
        Float nn = Float.valueOf(extras.getString(ModeThird.nn));
        Float R0 = Float.valueOf(extras.getString(ModeThird.R0));
        Float B = Float.valueOf(extras.getString(ModeThird.B));

        Integer v = a * b * c_height;//обьем
        float F = a * b;//площадь


        ArrayList<Integer> event_mode = new ArrayList<>();
        event_mode = extras.getIntegerArrayList(ModeSecond.eventArray_);

        Integer[] eventArray = new Integer[event_mode.size()];//convert ArrayList to Integer[]
        event_mode.toArray(eventArray);

//////////////////////////////////////////////заполняем изменение температуры
        int tm = 0;
        int ia;
        for (ia = 0; ia <= eventArray.length; ia++) {
            if (ia > ii) break;
            if (ii == 0)
                event_math.add(t_support);// добавляем значения что бы он рисовал при запуске
        }
        event_math.add(t_support + random_event);//ArrayList с значениями температуры
        float[] floatArray = new float[event_math.size()];//на соколько изменилась температура
        int i = 0;
        for (Float f : event_math) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        /////////////////////////////////////////// заполнинили


        Float q;
        real_temp_street += random_event;
        int real_temp_street_oll = t_street + real_temp_street;//реальное изменение температуры на данном шаге
        for (int iaa = 0; iaa < eventArray.length; iaa++) {//проход по списку событий
            tm++;
            if (iaa == ii) break;
        }
        if (eventArray[count_eventArray] == 0) {

            double[] Nr_oll = new double[tm];//реальная производительность кондиционера
            q = Float.valueOf(F * (t_support - real_temp_street_oll) * (1 + B) * nn / R0);//все теплопотери
            Nr_oll[ii] = q;
            for (double qaqa : Nr_oll) {
                Log.e(LOG_TAG, "qaqa" + qaqa);
            }
            tvsupport.setText("Температура, яка підтримується, °C");
            pover_conditionin.setText("Необхідна потужність кондиціонера, Дж ");
            tv_heat_loss.setText("Температура на вулиці, °C");
            tv_1.setText("" + t_support);
            tv_2.setText("");
            tv_6.setText("" + random_event);
            tv_5.setText("" + real_temp_street_oll);
            tv_4.setText("" + Nr_oll[ii]);
            tv_3.setText("");
            //tv_3.setText("" + tm);
            tv_need_time.setText("Модельний час");
            tv_heat_quantity.setText("");
            model_time.setText("");
            model_time1.setText("");

        } else {

            double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
            double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
            double[] Nr_oll = new double[tm];//реальная производительность кондиционера
            double[] Q_heat_loss = new double[tm];//массив с теплопотерями
            double tKelvin = t_support + 273.15;// температура в Кельвинах
            double p = 0.473 * (BD / tKelvin);// плотность
            double m = p * v; //масса воздуха

            Q[ii] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
            q = Float.valueOf(F * (t_support - t_street) * (1 + B) * nn / R0);//все теплопотери
            Nr_oll[ii] = N_loss_productivity - q;
            Q_heat_loss[ii] = q;
            time[ii] = Q[ii] / Nr_oll[ii];

            tvsupport.setText("Температура, яка підтримується, °C");
            pover_conditionin.setText("Реальна потужність кондиціонера, Дж/сек ");
            tv_heat_loss.setText("Температура на вулиці, °C");
            tv_1.setText("" + t_support);
            tv_2.setText("" + Q[ii]);
            tv_6.setText("" + random_event);
            tv_5.setText("" + t_street);
            tv_4.setText("" + Nr_oll[ii]);
            tv_3.setText("" + time[ii]);
            tv_41.setText("Змінення температури у домі, °C");
            //model_time.setText("" + tm);
            model_time1.setText("Модельний час");
            if (random_event == -1) {
                tv_need_time.setText("Час, необхідний для нагрівання, сек");
                tv_heat_quantity.setText("Кількість теплоти, необхідної для нагрівання, Дж");
            } else {
                tv_need_time.setText("Час, необхідний для охолодження, сек ");
                tv_heat_quantity.setText("Кількість теплоти, необхідної для охолодження, Дж");
            }
        }
        count_eventArray++;
    }


    public int[] axisAll(int eventX) {// отсчет для оси х// in start events = 0;
        int[] axis;
        axis = new int[eventX + 2];
        for (int i = 0; i < axis.length; i++) {
            axis[i] = i;
        }
        return axis;
    }

    public float[] graphAll(int randomEvent) throws NumberFormatException {
        Bundle extras = getIntent().getExtras();
        float[] graphT = {0};
        try {
            try {
                if (Integer.valueOf(extras.getString(ModeFirst.t2)) != null) {
                    Integer t1 = Integer.valueOf(extras.getString(ModeFirst.t1));
                    Integer t2 = Integer.valueOf(extras.getString(ModeFirst.t2));
                    if (t1 < t2) {
                        graphT = countTemp(stepEvent, t1, t2, "up");
                    } else if (t1 > t2) {
                        graphT = countTemp(stepEvent, t1, t2, "down");
                    }
                }
            } catch (NumberFormatException e) {
                if (Integer.valueOf(extras.getString(ModeSecond.t_support)) != null)
                    graphT = graphSupport(stepEvent, randomEvent);
            }
        } catch (NumberFormatException e) {
            graphT = graphThird(stepEvent, randomEvent);
        }
        return graphT;
    }

    public float[] countTemp(int stepEvent, int t1, int t2, String type) {
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

    public float[] graphSupport(int stepEvent, int random_event) {
        Bundle extras = getIntent().getExtras();
        float t_support = Float.valueOf(extras.getString(ModeSecond.t_support));
        ArrayList<Integer> event_mode = extras.getIntegerArrayList(ModeSecond.eventArray_);
        Integer[] eventArray = new Integer[event_mode.size()];//convert ArrayList to Integer[]
        event_mode.toArray(eventArray);
        for (int i = 0; i <= eventArray.length; i++) {
            if (i > stepEvent) break;
            if (stepEvent == 0) eventSupport.add(t_support);// прорисовка при запуске
        }
        eventSupport.add(t_support + random_event);//ArrayList с значениями температуры
        eventSupport.add(t_support);
        float[] floatArray = new float[eventSupport.size()];
        int i = 0;
        for (Float f : eventSupport) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    public float[] graphThird(int stepEvent, int randomEvent) {
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
                if (t1 > t2) {
                    graphFirstMod = graphT = countTemp(stepEvent, t1, t2, "down");
                } else {
                    graphFirstMod = graphT = countTemp(stepEvent, t1, t2, "up");
                }
            } else {
                graphT = graphThirdSupport(randomEvent);
            }
        }
        return graphT;
    }

    public float[] graphThirdSupport(int random_event) {
        ArrayList<Float> CountAllEvent = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        Float t_support = Float.valueOf(extras.getString(ModeThird.t2_support));
        ArrayList<Integer> event_mode = extras.getIntegerArrayList(ModeThird.eventArray_);
        Integer[] eventArray = new Integer[event_mode.size()];
        event_mode.toArray(eventArray);
        for (float i : graphFirstMod) {//заполняем событиями с 1го режима
            CountAllEvent.add(i);
        }
        for (int i = 0; i < eventSupport.size(); i++) {//добовляем события 2го режима, которые были раньше
            CountAllEvent.add(eventSupport.get(i));
        }
        eventSupport.add(t_support + random_event);
        eventSupport.add(t_support);
        CountAllEvent.add(t_support + random_event);
        CountAllEvent.add(t_support);
        float[] floatArray = new float[CountAllEvent.size()];
        int i = 0;
        for (Float f : CountAllEvent) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }
}