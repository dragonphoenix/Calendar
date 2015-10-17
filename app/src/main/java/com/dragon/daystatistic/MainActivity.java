package com.dragon.daystatistic;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.daystatistic.utils.Utils;
import com.dragon.daystatistic.utils.YearMonthDay;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {
    private EditText mTotalMoney;
    private Button mCalBtn;
    private TextView mTotalDays;
    private DatePicker mMonth1;
    private DatePicker mMonth2;
    private DSApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (DSApplication)getApplicationContext();
        mTotalMoney = (EditText)findViewById(R.id.money_input);
        mCalBtn = (Button)findViewById(R.id.cal_btn);
        mTotalDays = (TextView)findViewById(R.id.total_days);
        mMonth1 = (DatePicker)findViewById(R.id.month1);
        mMonth2 = (DatePicker)findViewById(R.id.month2);

        setTotalDays(mApp.getDays());
        mCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTotalMoney.getText().toString();
                if (Utils.isNumeric(text)){
                    float money = Float.valueOf(text);
                    int year = YearMonthDay.getCurrentYear();
                    int year1 = mMonth1.getYear();
                    int month1 = mMonth1.getMonth() + 1;
                    int year2 = mMonth2.getYear();
                    int month2 = mMonth2.getMonth() + 1;
                    int m1days = mApp.getDays(year1, month1);
                    int m2days = mApp.getDays(year2, month2);
                    if (!((year1 == year2) && (month1 == month2))) {
                        int days = YearMonthDay.getDaysOfMonth(year, month1) + YearMonthDay.getDaysOfMonth(year, month2);
                        money = (money / days) * (days - m1days - m2days)/2 + (money / days) * (m1days + m2days) ;
                        String totalDays = "总共" + (m1days + m2days) + "天。应付" + money + "元。";
                        mTotalDays.setText(totalDays);
                    }
                    else{
                        Toast.makeText(mApp, "请选择不同的月份。", Toast.LENGTH_LONG).show();
                        setTotalDays(m1days);
                    }
                }
                else {
                    Toast.makeText(mApp, "记得填水电费哦。", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setTotalDays(int days) {
        String totalDays = "总共" + days +"天";
        mTotalDays.setText(totalDays);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTotalDaysClick(View view){
        setTotalDays(mApp.getDays());
    }
}
