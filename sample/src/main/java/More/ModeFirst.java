package more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.handstudio.android.hzgrapher.R;
import com.handstudio.android.hzgrapher.V;

public class ModeFirst extends Activity implements View.OnClickListener {

    private final static int ACTION_EDIT_V = 101;//идентификатор запроса к V

    public final static String p = "p";
    public final static String t1 = "t1";
    public final static String t2 = "t2";
    public final static String c = "c";
    public final static String n = "n";
    public final static String a = "a";
    public final static String b = "b";
    public final static String c_height = "c_height";
    public final static String n_loss = "n_loss";
    public final static String t_street = "t_street";
    public final static String nn = "nn";
    public final static String R0 = "R0";
    public final static String B = "B";

    private EditText et_p;
    private EditText et_t1;
    private EditText et_t2;
    private EditText et_c;
    private EditText et_n;//пеплопроизводительность
    private EditText et_a;//ширина
    private EditText et_b;//длина
    private EditText et_c_height;//высота
    private EditText et_n_loss;//холодопроизовдительность
    private EditText et_t_street;//температура на улице
    private EditText et_nn; //коэффицент
    private EditText et_R0;//сопротивление теплопередачи
    private EditText et_B;//теплопотери дополнительные

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_first);
        et_p = (EditText) findViewById(R.id.et_p);
        et_t1 = (EditText) findViewById(R.id.et_t1);
        et_t2 = (EditText) findViewById(R.id.et_t2);
        et_c = (EditText) findViewById(R.id.et_c);
        et_n = (EditText) findViewById(R.id.et_n);
        et_a = (EditText) findViewById(R.id.et_a);
        et_b = (EditText) findViewById(R.id.et_b);
        et_c_height = (EditText) findViewById(R.id.et_c_height);
        et_n_loss = (EditText) findViewById(R.id.et_n_loss);
        et_t_street = (EditText) findViewById(R.id.et_t_street);
        et_nn = (EditText) findViewById(R.id.et_nn);
        et_R0 = (EditText) findViewById(R.id.et_R0);
        et_B = (EditText) findViewById(R.id.et_B);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (et_p.getText().length() == 0
                        || et_t1.getText().length() == 0 || et_t2.getText().length() == 0 || et_c.getText().length() == 0
                        || et_n.getText().length() == 0
                        || et_a.getText().length() == 0 || et_b.getText().length() == 0
                        || et_c_height.getText().length() == 0 || et_n_loss.getText().length() == 0
                        || et_t_street.getText().length() == 0 || et_nn.getText().length() == 0
                        || et_R0.getText().length() == 0 || et_B.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

                else if (Float.valueOf(et_p.getText().toString()) < 405 || Float.valueOf(et_p.getText().toString()) > 854.6)
                    Toast.makeText(getApplicationContext(), "405 < p < 854.6", Toast.LENGTH_SHORT).show();

                else if (Float.valueOf(et_c.getText().toString()) < 1.005 || Float.valueOf(et_c.getText().toString()) > 1.0301)
                    Toast.makeText(getApplicationContext(), "1.005 < c < 1.0301", Toast.LENGTH_SHORT).show();

                else
                    startActivity(CurveGraphActivity.class);
                break;

            case R.id.btn_add_R0:
                Intent ii = new Intent(this, V.class);
                startActivityForResult(ii, ACTION_EDIT_V);
                break;
        }
    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        i.putExtra(p, et_p.getText().toString());
        i.putExtra(t1, et_t1.getText().toString());
        i.putExtra(t2, et_t2.getText().toString());
        i.putExtra(c, et_c.getText().toString());
        i.putExtra(n, et_n.getText().toString());
        i.putExtra(a, et_a.getText().toString());
        i.putExtra(b, et_b.getText().toString());
        i.putExtra(c_height, et_c_height.getText().toString());
        i.putExtra(n_loss, et_n_loss.getText().toString());
        i.putExtra(t_street, et_t_street.getText().toString());
        i.putExtra(nn, et_nn.getText().toString());
        i.putExtra(R0, et_R0.getText().toString());
        i.putExtra(B, et_B.getText().toString());
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_EDIT_V) {// может приходить ответ с разных activity
            if (resultCode == RESULT_OK) {
                String vvv = data.getStringExtra(V.ANSWER_V);
                et_R0.setText("" + vvv);
            }
        }
    }
}
