package com.handstudio.android.hzgrapher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.AvoidXfermode;
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
	File fileName = null;
	String sdState = android.os.Environment.getExternalStorageState();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		btn_event = (Button) findViewById(R.id.btn_event);
		maths_oll();

		setCurveGraph();

	}


	void writeFileSD() {
		Bundle extras = getIntent().getExtras();
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));


		if (t1 < t2) {
			writeFileSD_plus();
		} else if (t1 > t2) {
			writeFileSD_minus();
		}
	}

	void writeFileSD_plus() {

		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

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
		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 < t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

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

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Float N_loss_productivity=Float.valueOf(extras.getString(Mode_first.n_loss));

		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

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


		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 > t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void setCurveGraph() {
		//all setting
		CurveGraphVO vo = makeCurveGraphAllSetting();

		//default setting
//		CurveGraphVO vo = makeCurveGraphDefaultSetting();

		layoutGraphView.addView(new CurveGraphView(this, vo));
	}


	/**
	 * make Curve graph using options
	 *
	 * @return
	 */


	private CurveGraphVO makeCurveGraphAllSetting() {

		//BASIC LAYOUT SETTING
		//padding
		int paddingBottom = CurveGraphVO.DEFAULT_PADDING;
		int paddingTop = CurveGraphVO.DEFAULT_PADDING;
		int paddingLeft = CurveGraphVO.DEFAULT_PADDING;
		int paddingRight = CurveGraphVO.DEFAULT_PADDING;

		//graph margin
		int marginTop = CurveGraphVO.DEFAULT_MARGIN_TOP;
		int marginRight = CurveGraphVO.DEFAULT_MARGIN_RIGHT;

		//max value
		//int maxValue 		= CurveGraphVO.DEFAULT_MAX_VALUE;

		int maxValue = 20;//max температура


		//increment
		int increment = 1;// по оси y 1 т.к. отслеживаем изменение температуры на 1 градус

		//GRAPH SETTING


		// алгоритм для поднятия температуры


		String[] legendArr = {Arrays.toString(axis_oll())};
		List<CurveGraph> arrGraph = new ArrayList<CurveGraph>();
		arrGraph.add(new CurveGraph("Gogo", 0xaa10ffff, graphT_oll()));


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

	/*
	так вот

	 */
	//поднятия температуры
	public int[] axis() {
		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

		int maxValue = 20;//max температура
		int tm = 0;//Модельное время, так же используется для создания массивов
		float Nr;//реальноя производительность кондиционера
		float F = a * b;//площадь

		//	if (t1<t2){// для поднятия температуры
		for (int i = 0; t1add <= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
			tm++;
			t1add++;
		}
		float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
		int[] axis = new int[tm];// отсчет для оси х
		double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
		double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус

		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 <= t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж


			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;

			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Nr = N_heat_productivity - q;
			time[i] = Q[i] / Nr;
			t1++;
		}

		return axis;
	}

	//уменьшения температуры
	public int[] axis_minus() {
		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

		int maxValue = 20;//max температура
		int tm = 0;//Модельное время, так же используется для создания массивов
		float Nr;//реальноя производительность кондиционера
		float F = a * b;//площадь

		for (int i = 0; t1add >= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
			tm++;
			t1add--;
		}
		float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
		int[] axis = new int[tm];// отсчет для оси х
		double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
		double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
		double[] Nr_oll = new double[tm];//реальная производительность кондиционера
		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 >= t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;

			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Nr = N_heat_productivity - q;
			time[i] = Q[i] / Nr;
			t1--;
		}

		return axis;
	}

	//определяем axis
	public int[] axis_oll() {
		Bundle extras = getIntent().getExtras();
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));
		int tm = 0;//Модельное время, так же используется для создания массивов
		for (int i = 0; t1add <= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать// отсавляем <= чтобы график рисовался на 1 гадус больше
			tm++;
			t1add++;
		}
		int[] axis = new int[tm];// отсчет для оси х

		if (t1 < t2) {
			axis = axis();
		} else if (t1 > t2) {
			axis = axis_minus();
		}
		return axis;

	}

	//поднятия температуры
	public float[] graphT() {
		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));
		tv_21.setText("Количество теплоты необходимое для нагревания");
		tv_31.setText("Время, необходимое на обогрев на 1 градус");
		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

		int maxValue = 20;//max температура
		int tm = 0;//Модельное время, так же используется для создания массивов
		float Nr;//реальноя производительность кондиционера
		float F = a * b;//площадь

		//	if (t1<t2){// для поднятия температуры
		for (int i = 0; t1add <= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
			tm++;
			t1add++;
		}
		float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
		int[] axis = new int[tm];// отсчет для оси х
		double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
		double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
		double[] Nr_oll = new double[tm];//реальная производительность кондиционера

		for (int i = 0; t1 <= t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			double m = p * v; //масса воздуха
			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;
			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Nr = N_heat_productivity - q;
			Nr_oll[i] = Nr;
			time[i] = Q[i] / Nr;

			t1++;
		}
		return graphT;
	}

	public float[] grapht_minus() {
		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));

		tv_21.setText("Количество теплоты необходимое для остываня");
		tv_31.setText("Время, необходимое на охлождение на 1 градус");

		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

		int maxValue = 20;//max температура
		int tm = 0;//Модельное время, так же используется для создания массивов
		float Nr;//реальноя производительность кондиционера
		float F = a * b;//площадь

		for (int i = 0; t1add >= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать
			tm++;
			t1add--;
		}
		float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге
		int[] axis = new int[tm];// отсчет для оси х
		double[] Q = new double[tm];// количество теплоты/холода, которое необходимо для нагревания/охлаждения
		double[] time = new double[tm]; //время, которое необходимо для нагревания Q на один градус
		double[] Nr_oll = new double[tm];//реальная производительность кондиционера
		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 >= t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;

			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Nr = N_heat_productivity - q;
			time[i] = Q[i] / Nr;


			t1--;
		}

		return graphT;
	}

	public float[] graphT_oll() {
		Bundle extras = getIntent().getExtras();
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

		int tm = 0;//Модельное время, так же используется для создания массивов
		for (int i = 0; t1add <= t2; i++) {//находим кол-во элементов массива который необходимо нарисовать// отсавляем <= чтобы график рисовался на 1 гадус больше
			tm++;
			t1add++;
		}
		float[] graphT = new float[tm];//Хранятся данные о температуре на заданом шаге

		if (t1 < t2) {
			graphT = graphT();
		} else if (t1 > t2) {
			graphT = grapht_minus();
		}
		return graphT;

	}

	public void maths_plus() {

		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer V = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

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
		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 < t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * V; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж

			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;
			t_street_random_array[i] = t_street_random;
			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Log.e(LOG_TAG, "теплопотери " + q + " изменение температуры " + t_street_random);

			Q_heat_loss[i] = q;
			Nr = N_heat_productivity - q;
			Nr_oll[i] = Nr;
			time[i] = Q[i] / Nr;

			tv_1.setText("" + t1);
			tv_2.setText("" + Q[i]);
			tv_6.setText("" + t_street_random);
			tv_5.setText("" + q);
			tv_4.setText("" + Nr);
			tv_3.setText("" + time[i]);

			t1++;
		}
	}

	public void maths_minus() {

		Bundle extras = getIntent().getExtras();

		Float BD = Float.valueOf(extras.getString(Mode_first.p));
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t1add = Integer.valueOf(extras.getString(Mode_first.t1));//дополнительная переменная для цикла
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));
		Float c = Float.valueOf(extras.getString(Mode_first.c));
		Float N_heat_productivity = Float.valueOf(extras.getString(Mode_first.n));
		Float N_loss_productivity=Float.valueOf(extras.getString(Mode_first.n_loss));
		Integer a = Integer.valueOf(extras.getString(Mode_first.a));
		Integer b = Integer.valueOf(extras.getString(Mode_first.b));
		Integer c_height = Integer.valueOf(extras.getString(Mode_first.c_height));

		Integer t_street = Integer.valueOf(extras.getString(Mode_first.t_street));
		Float nn = Float.valueOf(extras.getString(Mode_first.nn));
		Float R0 = Float.valueOf(extras.getString(Mode_first.R0));
		Float B = Float.valueOf(extras.getString(Mode_first.B));


		Float q;//Q_heat_loss=F(t1-t_street)*(1+∑B)*n/R0

		Integer v = a * b * c_height;//обьем
		//Float N_cooling_productivity=Float.valueOf(extras.getString(Mode_first.n));

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


		Log.e(LOG_TAG, "значени count " + tm);

		for (int i = 0; t1 > t2; i++) {
			graphT[i] = t1;
			axis[i] = i;
			double tKelvin = t1 + 273.15;// температура в Кельвинах
			double p = 0.473 * (BD / tKelvin);// плотность
			Log.e(LOG_TAG, "значения плотности   " + p);
			double m = p * v; //масса воздуха
			Log.e(LOG_TAG, "значения массы   " + m);

			Q[i] = m * c * 1000;//домножаем на 1000 т.к. нужно перевести кДж в Дж
			int t_street_random = -1 + (int) (Math.random() * ((2) + 1));
			t_street += t_street_random;
			t_street_random_array[i] = t_street_random;

			q = Float.valueOf(F * (t1 - t_street) * (1 + B) * nn / R0);
			Log.e(LOG_TAG, "теплопотери для понижения" + q + " изменение температуры " + t_street_random);


			Q_heat_loss[i] = q;

			Nr = N_loss_productivity - q;
			Nr_oll[i] = Nr;
			time[i] = Q[i] / Nr;

			tv_1.setText("" + t1);
			tv_2.setText("" + Q[i]);
			tv_6.setText("" + t_street_random);
			tv_5.setText("" + q);
			tv_4.setText("" + Nr);
			tv_3.setText("" + time[i]);

			t1--;

		}

	}
	public void maths_oll(){
		Bundle extras = getIntent().getExtras();
		Integer t1 = Integer.valueOf(extras.getString(Mode_first.t1));
		Integer t2 = Integer.valueOf(extras.getString(Mode_first.t2));

		if (t1 < t2) {
			maths_plus();
		} else if (t1 > t2) {
			maths_minus();
		}
	}
}




