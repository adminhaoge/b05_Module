package edu.wschina.b05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CountDown extends AppCompatActivity {

    private RecyclerView cart;
    private TextView time;
    private TextView price;
    private Button payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        initView();
        initTime();
        initShopping();
        intentPayment();
    }

    private void intentPayment() {
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),MakeAppointment.class));
                finish();
            }
        });
    }

    private void initShopping() {
        CartProvider cartProvider = new CartProvider(this);
        List<ShoppingCart> carts = cartProvider.getAll();
        if (carts != null){
            Adapter_Dialog adapter_dialog = new Adapter_Dialog(this, price, carts);
            cart.setAdapter(adapter_dialog);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            cart.setLayoutManager(gridLayoutManager);
            cart.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initTime() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("请在"+millisUntilFinished/1000+"秒内完成支付");
            }

            @Override
            public void onFinish() {
                time.setText("时间已到");
            }
        }.start();
    }

    private void initView() {
        time = findViewById(R.id.txt_time);
        cart = findViewById(R.id.recycle_cart);
        price = findViewById(R.id.price);
        payment = findViewById(R.id.bt_payment);
    }
}