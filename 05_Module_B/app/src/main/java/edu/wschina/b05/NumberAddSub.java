package edu.wschina.b05;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.lifecycle.ViewModel;

public class NumberAddSub extends LinearLayout implements View.OnClickListener {
    private int value;
    private int minValue;
    private int maxValue;
    private Button sub;
    private Button add;
    private TextView amount;
    private TintTypedArray array;

    public NumberAddSub(@NonNull Context context) {
        this(context,null);
    }

    public NumberAddSub(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberAddSub(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(context,attrs,defStyleAttr);
    }
    @SuppressLint("RestrictedApi")
    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null){

            array = TintTypedArray.obtainStyledAttributes(context, attrs,
                    R.styleable.NumberAddSub, defStyleAttr, 0);

            int value = array.getInt(R.styleable.NumberAddSub_value, 0);
            setValue(value);

            int minValue = array.getInt(R.styleable.NumberAddSub_minValue, 0);
            setMinValue(minValue);

            int maxValue = array.getInt(R.styleable.NumberAddSub_maxValue, 0);
            setMaxValue(maxValue);

            array.recycle();

        }
    }


    private void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private int getMaxValue() {
        return maxValue;
    }

    private void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    private int getMinValue() {
        return minValue;
    }

    public int getValue(){
        String amount_txt = amount.getText().toString();
        if (amount_txt != null || !"".equals(amount_txt)){
            value = Integer.parseInt(amount_txt);
            return value;
        }
        return 0;
    }

    public void setValue(int value) {
        amount.setText(value+"");
        this.value = value;
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.custom_bt_add_sub, this, true);
        sub = inflate.findViewById(R.id.btnDecrease);
        add = inflate.findViewById(R.id.btnIncrease);
        amount = inflate.findViewById(R.id.etAmount);
        sub.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDecrease){
            numSub();
            if (clickListener != null){
                clickListener.onSubClick(value);
            }
        }else if (v.getId() == R.id.btnIncrease){
            numAdd();
            if (clickListener != null){
                clickListener.onAddClick(value);
            }
        }
    }

    public void numAdd() {
        value += 1;
        amount.setText(value+"");
    }

    public void numSub() {
        if (value > minValue){
            value = value -1 ;
            amount.setText(value+"");
        }
    }

    public ButtonClickListener clickListener;

    public void setOnButtonClickListener(ButtonClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ButtonClickListener{
        void onAddClick(int value);
        void onSubClick(int value);
    }

}
