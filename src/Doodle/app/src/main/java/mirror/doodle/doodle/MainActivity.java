package mirror.doodle.doodle;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener,View.OnClickListener {

    private DrawingView DrawingView;
    private List<ImageButton> colors;
    private String[] colorValues = {"#000000","#FFFFFF","#FF0000","#339933","#0000FF",
            "#99FF33","#990099","#FF3399","#FFFF66","#FFD633"

    };
    private ImageButton selectedColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawingView = (DrawingView) findViewById(R.id.drawing);
        DisplayMetrics displaymetrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        Constants.width = width;
        Constants.height = width;
        Constants.xAxis = width/2;
        DrawingView.setLayoutParams(new LinearLayout.LayoutParams(width, width));

        colors = new ArrayList<>();


        int colorWidth = (width-60)/5;
        int colorHeight = (displaymetrics.heightPixels-width-60)/3;


        List<LinearLayout> palettes = new ArrayList<>();
        palettes.add((LinearLayout)findViewById(R.id.palette1));
        palettes.add((LinearLayout)findViewById(R.id.palette2));
        selectedColor = (ImageButton)findViewById(R.id.selectedColor);
        //palettes.add((LinearLayout)findViewById(R.id.palette3));
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(Color.BLACK);
        selectedColor.setBackground(gd);
        int j = 0;
        for(LinearLayout palette : palettes){
            for(int i=1;i<=5;i++, j++){
                ImageButton imageButton = new ImageButton(this);
                imageButton.setBackgroundColor(Color.parseColor(colorValues[j]));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(colorWidth,colorWidth);
                params.setMargins(10,10,0,0);
                imageButton.setLayoutParams(params);
                imageButton.setTag(new String(colorValues[j]));
                imageButton.setOnClickListener(this);
                palette.addView(imageButton);
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Toast.makeText(this,view.getTag().toString(), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(this,view.getTag().toString(), Toast.LENGTH_LONG).show();
        Constants.currColor = view.getTag().toString();
        GradientDrawable gd = (GradientDrawable)selectedColor.getBackground();
        gd.setColor(Color.parseColor(view.getTag().toString()));
        selectedColor.setBackground(gd);

    }
    public void clearCanvas(View v){
        DrawingView.resetCanvas();
    }
}