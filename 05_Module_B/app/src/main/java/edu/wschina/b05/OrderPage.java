package edu.wschina.b05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView cart;
    private TextView countMoney;
    private List<MenuBean> menuBeans;
    private Adapter_OrdePage adapter_ordePage;
    private Adapter_Dialog dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        initView();
        initOkhttp();
        CartClick();
    }

    private void CartClick() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
    }

    private void initDialog() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_items, null,false);
        RecyclerView dialog_recycle = inflate.findViewById(R.id.dialog_recycle);
        TextView money = inflate.findViewById(R.id.money_button_TotalPrice);
        Button bt_qk = inflate.findViewById(R.id.count_qk);
        Button bt_jie = inflate.findViewById(R.id.count_btn);

        CartProvider cartProvider = new CartProvider(this);
        List<ShoppingCart> carts = cartProvider.getAll();
        if (carts != null){
            dialogs = new Adapter_Dialog(OrderPage.this,money,carts);
            dialog_recycle.setAdapter(dialogs);
            GridLayoutManager manager = new GridLayoutManager(getApplication(),1);
            dialog_recycle.setLayoutManager(manager);
            dialog_recycle.setItemAnimator(new DefaultItemAnimator());
        }
        Dialog dialogwinds = new Dialog(this,R.style.FullScreenDialogStyle);
        dialogwinds.setContentView(inflate);
        Window dialogWindow = dialogwinds.getWindow();
        //dialogWindow.getDecorView().setPadding(0, 0, 0, 0);// 边距设为0
        //dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = 1200; // 高度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 弹出dialog
        dialogwinds.show();

        bt_qk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartProvider.clear();
                dialogs.clearData();
                dialogwinds.cancel();
            }
        });
        bt_jie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),CountDown.class));
                finish();
                dialogwinds.cancel();
            }
        });
    }

    private void initOkhttp() {
        OkHttpClient client = new OkHttpClient();
        Request build = new Request.Builder()
                .url("http://192.168.0.110/demo.json")
                .build();
           client.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    showReqiestJson(response.body().string());
                }
            });

    }

    private void showReqiestJson(String string) {
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    menuBeans = new ArrayList<>();
                    for (int i = 0 ; i<data.length();i++){
                        JSONObject dataJSONObject = data.getJSONObject(i);
                        int id = dataJSONObject.getInt("id");
                        String image_url = dataJSONObject.getString("image_url");
                        String name = dataJSONObject.getString("name");
                        int price = dataJSONObject.getInt("price");
                        MenuBean menuBean = new MenuBean(id, image_url, name, price);
                        menuBeans.add(menuBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_ordePage = new Adapter_OrdePage(OrderPage.this,menuBeans,countMoney);
                                recyclerView.setAdapter(adapter_ordePage);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(), 1);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        cart = findViewById(R.id.img_cart);
        countMoney = findViewById(R.id.money_TotalPrice);
    }
}