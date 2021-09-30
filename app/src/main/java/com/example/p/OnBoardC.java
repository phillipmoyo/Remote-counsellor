package com.example.p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.p.helpers.SlideAdapter;
import com.example.p.helpers.SlideAdapterC;

public class OnBoardC extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;

    Intent count;
    SlideAdapterC sliderAdapter;
    TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_c);

        viewPager = findViewById(R.id.sliderC);
        dotsLayout = findViewById(R.id.dotsC);

        sliderAdapter = new SlideAdapterC(this);

        count = getIntent();
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    private void addDots(int position) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);//size

            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.color_purpul));
        }
    }
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void skipSlides2(View v)
    {
        if(v.getId() == R.id.skip2)
        {
            Intent i = new Intent(getApplicationContext(), listOfUsers.class);
            i.putExtra("counsellor",count.getStringExtra("counsellor"));
            startActivity(i);
            finish();
        }
    }
}
