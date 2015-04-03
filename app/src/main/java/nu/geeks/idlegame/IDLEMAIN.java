package nu.geeks.idlegame;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;


public class IDLEMAIN extends Activity {

    Button b1,b2,b3;
    TextView currentBalance, currentBuy, tB1, tB2, tB3, tTimer;
    SeekBar bar;

    int[] stocks = {500,500,500};
    boolean[] stocksAlive = {true,true,true};
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idlelayout);

        //TODO add buttons to sell
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);

        currentBalance = (TextView) findViewById(R.id.tTotalMoney);
        currentBuy = (TextView) findViewById(R.id.tMoneyToBet);
        tB1 = (TextView) findViewById(R.id.tB1);
        tB2 = (TextView) findViewById(R.id.tB2);
        tB3 = (TextView) findViewById(R.id.tB3);

        tTimer = (TextView) findViewById(R.id.tTimer);

        bar = (SeekBar) findViewById(R.id.seekBar);

        rand = new Random();

        updateValues();

        //TODO timer is weird, stops at 1 for a while
        CountDownTimer timer = new CountDownTimer(5000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                tTimer.setText(Long.toString(millisUntilFinished / 10));
            }

            @Override
            public void onFinish() {
                updateValues();
                start();
            }
        }.start();


    }

    private void updateValues(){
        for(int i = 0; i < 3; i++){
            int superLuck = rand.nextInt(1000);
            int val;
            if(superLuck < 10){
                if(stocksAlive[i]) stocks[i] = rand.nextInt(5000) + 500;
            }else {
                if(stocksAlive[i]) stocks[i] = rand.nextInt(stocks[i] * 2);
            }

            if(stocks[i] == 0) stocksAlive[i] = false;
        }
        tB1.setText("Business 1 stock at " + stocks[0]);
        tB2.setText("Business 2 stock at " + stocks[1]);
        tB3.setText("Business 3 stock at " + stocks[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_idlemain, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
