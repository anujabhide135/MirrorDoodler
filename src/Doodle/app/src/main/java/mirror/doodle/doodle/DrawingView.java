package mirror.doodle.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;


public class DrawingView extends View implements ViewListener{
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float centerLine = 0.0f;
    private Path[] paths;
    float[] x,y;
    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }
    private void setupDrawing(){
        drawPath = new Path();
        BitmapFactory.Options opt=new  BitmapFactory.Options();
        Bitmap patternBMP = BitmapFactory.decodeResource(getResources(),R.drawable.pink,opt);
        opt.inSampleSize=2;
        /*BitmapShader patternBMPshader = new BitmapShader(patternBMP,
                BitmapShader.TileMode.REPEAT, BitmapShader.TileMode.REPEAT);*/
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        //drawPaint.setShader(patternBMPshader);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        x = new float[8];
        y = new float[8];
        paths = new Path[8];
        for(int i=0;i<8;i++)
            paths[i] = new Path();


    }
    
    private void populatePath(float x, float y, float c){
        int theta;
        int i=0,j=0;
        if(y-c==0 || x-c==0){
            theta = 0;
            
        }
        else
            theta = (int)toDegrees(atan(Math.abs(y-c)/Math.abs(x-c)));
        if(x>=c && y<=c)
            theta+=0;
        else if(x<=c && y<=c)
            theta+=90;
        else if(x<=c && y>=c)
            theta+=180;
        else
            theta+=270;

        float dx, dy;
        if(theta>=0 && theta<=45){
            dx = Math.abs(c-y);
            dy = Math.abs(Constants.width-x);
        }
        else if(theta>=46 && theta<=90){
            dx = Math.abs(c-x);
            dy = y;
        }
        else if(theta>=91 && theta<=135){
            dx = Math.abs(c-x);
            dy = y;
        }
        else if(theta>=136 && theta<=180){
            dx = Math.abs(c-y);
            dy = x;
        }
        else if(theta>=181 && theta<=225){
            dx = Math.abs(c-y);
            dy = x;
        }
        else if(theta>=226 && theta<=270){
            dx = Math.abs(c-x);
            dy = Math.abs(Constants.width-y);
        }
        else if(theta>=271 && theta<=315){
            dx = Math.abs(c-x);
            dy = Math.abs(Constants.width-y);
        }
        else{
            dx = Math.abs(c-y);
            dy = Math.abs(Constants.width-x);
        }
        this.x[i++] = c-dx;
        this.y[j++] = dy;
        this.x[i++] = c+dx;
        this.y[j++] = dy;

        this.x[i++] = Constants.width-dy;
        this.y[j++] = c-dx;
        this.x[i++] = Constants.width-dy;
        this.y[j++] = c+dx;

        this.x[i++] = c+dx;
        this.y[j++] = Constants.width-dy;
        this.x[i++] = c-dx;
        this.y[j++] = Constants.width-dy;

        this.x[i++] = dy;
        this.y[j++] = c+dx;
        this.x[i++] = dy;
        this.y[j++] = c-dx;




    }
    private void radial(float x_, float y_, float c){

        x[0] = x_-c;
        y[0] = y_-c;

        float factor = 1.0f/(float)Math.sqrt(2.0);
        int i=1;
        for(; i<8; i++){
            x[i] = x[i-1]*(float)Math.cos(Math.toRadians(45)) - y[i-1]*(float)Math.sin(Math.toRadians(45));
            y[i] = x[i-1]*(float)Math.sin(Math.toRadians(45)) + y[i-1]*(float)Math.cos(Math.toRadians(45));
           /* x[i] = factor*(x[i-1]-y[i-1]);
            y[i] = factor*(x[i-1]+y[i-1]);*/
            x[i-1] += c;
            y[i-1] += c;
        }
        x[i-1] += c;
        y[i-1] += c;
    }
    private void setPaths(float x_, float y_, float c){
        float dx = Math.abs(x_-c);
        float dy = Math.abs(y_-c);
        x[0] = dx;
        y[0] = dy;

        x[1] = -dx;
        y[1] = -dy;

        x[2] = -dx;
        y[2] = dy;

        x[3] = dx;
        y[3] = -dy;

        x[4] = dy;
        y[4] = dx;

        x[5] = -dy;
        y[5] = -dx;

        x[6] = -dy;
        y[6] = dx;

        x[7] = dy;
        y[7] = -dx;

        for(int i=0; i<8; i++){
            x[i] += c;
            y[i] += c;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drawPaint.setColor(Color.parseColor(Constants.currColor));
        float touchX = event.getX();
        float touchX_;
        float touchY = event.getY();
        float centerLine = Constants.xAxis*1.0f;
        // populatePath(touchX, touchY, centerLine);
        radial(touchX, touchY, centerLine);
        int i=0,j=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //drawPath.moveTo(touchX, touchY);

                for(Path mirroredPath : paths)
                    mirroredPath.moveTo(x[i++],y[j++]);

                break;
            case MotionEvent.ACTION_MOVE:
                //drawPath.lineTo(touchX, touchY);
                 i=0; j=0;
                //mirroredPath.lineTo(touchX_,touchY);
                for(Path mirroredPath : paths)
                    mirroredPath.lineTo(x[i++],y[j++]);
                break;
            case MotionEvent.ACTION_UP:
                i=0; j=0;
                for(Path mirroredPath : paths)
                    drawCanvas.drawPath(mirroredPath, drawPaint);
                for(Path mirroredPath : paths)
                    mirroredPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        for(Path mirroredPath : paths)
            canvas.drawPath(mirroredPath, drawPaint);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }

    @Override
    public void clear(Context c) {

    }
    public void resetCanvas(){
        canvasBitmap = Bitmap.createBitmap(canvasBitmap.getWidth(), canvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }
}
