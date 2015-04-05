package nu.geeks.idlegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;


public class IDLEMAIN extends Activity {

    public static final String EXTRA_MSG = "EXTRA";

    Button b1,b2,b3, bSell1,bSell2,bSell3;
    TextView currentBalance, currentBuy, tB1, tB2, tB3, tTimer, tOwn1, tOwn2, tOwn3,
    tNet1, tNet2, tNet3;
    SeekBar bar;

    int[] stocks = {10,1000,10000};
    int[] shares = {0,0,0};
    int[] boughtFor = {0,0,0};

    int[] stocksAlive = {0,0,0}; //three states, 0 = active, 1 = dead, 2 = restarting
    Random rand;
    int balance, bet, days;

    int[] bOwn = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idlelayout);

        //TODO - add lost/gained value on stocks.

        //Initialize all buttons and texts.
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);

        bSell1 = (Button) findViewById(R.id.bSell1);
        bSell2 = (Button) findViewById(R.id.bSell2);
        bSell3 = (Button) findViewById(R.id.bSell3);

        currentBalance = (TextView) findViewById(R.id.tTotalMoney);
        currentBuy = (TextView) findViewById(R.id.tMoneyToBet);

        tB1 = (TextView) findViewById(R.id.tB1);
        tB2 = (TextView) findViewById(R.id.tB2);
        tB3 = (TextView) findViewById(R.id.tB3);

        tOwn1 = (TextView) findViewById(R.id.tOwnB1);
        tOwn2 = (TextView) findViewById(R.id.tOwnB2);
        tOwn3 = (TextView) findViewById(R.id.tOwnB3);

        tNet1 = (TextView) findViewById(R.id.tNet1);
        tNet2 = (TextView) findViewById(R.id.tNet2);
        tNet3 = (TextView) findViewById(R.id.tNet3);


        tTimer = (TextView) findViewById(R.id.tTimer);

        bar = (SeekBar) findViewById(R.id.seekBar);

        rand = new Random();

        //Set initial values for stocks and balance.
        balance = 100;
        updateStockValues();
        updateBalance();
        updateAmountOwn();
        updateCurrentProfits();
        tTimer.setText("Day 0");

        currentBuy.setText("Buy with  0 % (0 €)");

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(balance <= 0){
                    bet = 0;
                    currentBuy.setText("Buy with " + progress + "% (0 €)");
                }else {

                    double procent = progress * 0.01;
                    bet = (int) (balance * procent);
                    currentBuy.setText("Buy with " + progress + "% (" + bet + " €)");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //User presses buy business 1
        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                buy(0);

            }
        });

        //User presses buy business 2
        b2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                buy(1);

            }
        });

        //User presses buy business 3
        b3.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                buy(2);

            }
        });

        //User presses buy business 1
        bSell1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sell(0);

            }
        });

        //User presses buy business 2
        bSell2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sell(1);

            }
        });

        //User presses buy business 3
        bSell3.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sell(2);

            }
        });

        CountDownTimer timer = new CountDownTimer(2000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
               // tTimer.setText(Long.toString(millisUntilFinished / 10));
            }

            @Override
            public void onFinish() {
                days++;
                tTimer.setText("Day " + days);
                updateStockValues();
                updateCurrentProfits();
                start();
            }
        }.start();


    }

    private void updateBalance() {
        currentBalance.setText("You have " + balance + " €");

    }

    private void updateCurrentProfits(){
        int prof1 = (stocks[0]*shares[0] - boughtFor[0]);
        int prof2 = (stocks[1]*shares[1] - boughtFor[1]);
        int prof3 = (stocks[2]*shares[2] - boughtFor[2]);

        tNet1.setText("profit " + prof1 + " €");
        tNet2.setText("profit " + prof2 + " €");
        tNet3.setText("profit " + prof3 + " €");
    }

    private void updateAmountOwn(){
        tOwn1.setText(shares[0] + " shares");
        tOwn2.setText(shares[1] + " shares");
        tOwn3.setText(shares[2] + " shares");

    }

    private void sell(int stock){
        balance += shares[stock]*stocks[stock];
        shares[stock] = 0;
        boughtFor[stock] = 0;
        updateCurrentProfits();
        updateBalance();
        updateAmountOwn();
        updateBet();
        if(balance > 1000000){
            Intent intent = new Intent(this, IdleWin.class);
            int[] extra = {balance, days};
            intent.putExtra(EXTRA_MSG, extra);
            startActivity(intent);
        }
    }

    private void buy(int stock){
        if(stocks[stock] != 0) {
            int nshares = bet / stocks[stock]; //number of shares.
            shares[stock] += nshares; //Uppdate ownage
            balance -= nshares * stocks[stock];
            boughtFor[stock] += nshares * stocks[stock];
            updateAmountOwn();
            updateBalance();
            updateBet();
        }
    }

    private void updateBet(){
        double procent = bar.getProgress() * 0.01;
        bet = (int) (balance * procent);
        currentBuy.setText("Buy with " + bar.getProgress() + "% (" + bet + " €)");
    }

    /*
    Uptades the current stocks somewhat random.
     */
    private void updateStockValues(){

        if(stocksAlive[0] == 0){
            stocks[0] += (rand.nextInt(10) - 1);
        }else if(stocksAlive[0] == 1){
               stocksAlive[0]++;
        }else{
            stocks[0] = 10;
            shares[0] = 0;
            updateAmountOwn();
            stocksAlive[0] = 0;
            sell(0);
        }
        int stocktmp = stocks[1];

        if(stocksAlive[1] == 0){
            stocks[1] += (rand.nextInt(stocktmp * 2) - stocktmp);
        }else if(stocksAlive[1] == 1){
            stocksAlive[1] = 2;
        }else{
            stocks[1] = 1000;
            shares[1] = 0;
            updateAmountOwn();
            stocksAlive[1] = 0;
            sell(1);
        }
        stocktmp = stocks[2];
        if(stocksAlive[2] == 0){
            stocks[2] += (rand.nextInt(stocktmp) - (stocktmp/3));
        }else if(stocksAlive[2] == 1){
            stocksAlive[2]++;

        }else{
            stocks[2] = 10000;
            shares[2] = 0;
            updateAmountOwn();
            stocksAlive[2] = 0;
            sell(2);
        }



        for(int i = 0; i < 3; i++){
            if(stocks[i] <= 0 && stocksAlive[i] == 0) stocksAlive[i] = 1;
        }


        if(stocksAlive[0] == 0){
            tB1.setText("Slow'n'steady at " + stocks[0]);
        }
        else if(stocksAlive[0] == 1){
            tB1.setText("Stock dead!");
        }else{
            tB1.setText("New startup!");
        }
        if(stocksAlive[1] == 0){
            tB2.setText("Small'n'crazy at " + stocks[1]);
        }else if(stocksAlive[1]==1){
            tB2.setText("Stock dead!");
        }else{
            tB2.setText("New startup!");
        }
        if(stocksAlive[2] == 0){
            tB3.setText("Big'n'boring at " + stocks[2]);
        }        else if(stocksAlive[2] == 1){
            tB3.setText("Stock dead!");
        }else{
            tB3.setText("New Startup!");
        }
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
