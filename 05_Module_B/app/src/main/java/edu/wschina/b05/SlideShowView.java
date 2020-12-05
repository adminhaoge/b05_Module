package edu.wschina.b05;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlideShowView extends FrameLayout {
    //网络图片数组
    private String[] imagesResId;
    //开启自动轮播
    private static  boolean isAutoPlay = true;
    //定时器
    private ScheduledExecutorService scheduledExecutorService;
    private List<ImageView> imagesViewList;
    private List<View> dotViewList;
    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    private ImageView imageView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };



    public SlideShowView(@NonNull Context context) {
        this(context,null);
    }

    public SlideShowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideShowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initUi(context);
        if (isAutoPlay){
            startPlay();
        }else {
            stopPlay();
        }
    }

    private void initUi(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_slidershow,this,true);
        for (String imageUrl : imagesResId) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(imageUrl).into(imageView);
            imagesViewList.add(imageView);
        }
        dotViewList.add(findViewById(R.id.v_dot1));
        dotViewList.add(findViewById(R.id.v_dot2));
        dotViewList.add(findViewById(R.id.v_dot3));

        viewPager = findViewById(R.id.viewPage);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPageAdapter());
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (viewPager){
                    currentItem = (currentItem+1)%imagesViewList.size();
                    handler.obtainMessage().sendToTarget();
                }
            }
        },0,2, TimeUnit.SECONDS);
    }


    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }


    class MyPageAdapter extends PagerAdapter{

        //这个方法是从ViewGroup中移除当前view
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(imagesViewList.get(position));
        }

        //return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager)container).addView(imagesViewList.get(position));
            return imagesViewList.get(position);
        }

        @Override
        public int getCount() {
            return imagesViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        boolean isAutoPlay = false;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for (int i = 0 ; i<dotViewList.size() ; i++){
                if (i == position){
                    ((View)dotViewList.get(i)).setBackgroundResource(R.drawable.ic_baseline_brightness_1_24_select);
                }else {
                    dotViewList.get(i).setBackgroundResource(R.drawable.ic_baseline_brightness_1_24);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state){
                case 0:
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() -1 && !isAutoPlay){
                        viewPager.setCurrentItem(0);
                    }else if (viewPager.getCurrentItem() == 0 && !isAutoPlay){
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() -1);
                    }
                    break;
            }
        }
    }

    private void initData() {
        imagesResId = new String[]{
           "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2075903360,405209795&fm=26&gp=0.jpg",
           "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2565443740,1354606035&fm=26&gp=0.jpg",
           "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=246854078,251965345&fm=26&gp=0.jpg"
         };
        imagesViewList = new ArrayList<>();
        dotViewList = new ArrayList<>();
    }
}
