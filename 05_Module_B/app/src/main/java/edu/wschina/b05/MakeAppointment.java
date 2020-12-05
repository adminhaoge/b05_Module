package edu.wschina.b05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MakeAppointment extends AppCompatActivity {

    private TextView table_type;
    private TextView date_txt;
    private TextView time_slot;
    private RecyclerView mack_cart;
    private SharedPreferences cart_json;
    private String timeSlot;
    private String date;
    private String seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        initView();
        initShopping();
        initShared();
        ExhibitionText();
    }

    private void initShared() {
        cart_json = getSharedPreferences("CART_JSON", 0);
        timeSlot = cart_json.getString("timeSlot", null);
        date = cart_json.getString("date", null);
        seat = cart_json.getString("seat", null);
    }

    private void ExhibitionText() {
        date_txt.setText(date+" 日期");
        time_slot.setText(timeSlot+" 时间段");
        table_type.setText(seat+" 餐桌类型");
    }

    private void initView() {
        table_type = findViewById(R.id.txt_table_type);
        date_txt = findViewById(R.id.txt_date);
        time_slot = findViewById(R.id.txt_time_slot);
        mack_cart = findViewById(R.id.recycle_mack_cart);
    }

    private void initShopping() {
        CartProvider cartProvider = new CartProvider(this);
        List<ShoppingCart> carts = cartProvider.getAll();
        if (carts != null){
            Adapter_Dialog adapter_dialog = new Adapter_Dialog(this,carts);
            mack_cart.setAdapter(adapter_dialog);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            mack_cart.setLayoutManager(gridLayoutManager);
            mack_cart.setItemAnimator(new DefaultItemAnimator());
        }
    }
}