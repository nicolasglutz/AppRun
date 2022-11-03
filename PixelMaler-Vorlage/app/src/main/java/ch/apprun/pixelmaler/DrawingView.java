package ch.apprun.pixelmaler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Die DrawingView ist für die Darstellung und Verwaltung der Zeichenfläche
 * zuständig.
 */
public class DrawingView extends View {

    private static final int GRID_SIZE = 13;

    private Path drawPath = new Path();
    private Paint drawPaint = new Paint();
    private Paint linePaint = new Paint();
    private boolean isErasing = false;

    private int gridWidth = 0;
    private int gridHeight = 0;

    private int stepSizeX = 0;
    private int stepSizeY = 0;

    private boolean isFirstRun = true;

    public static ArrayList<Pixel> pixels = new ArrayList<>();

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint.setColor(0xFF666666);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1.0f);
        linePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(isFirstRun){
            this.gridWidth = this.getWidth();
            this.gridHeight = this.getHeight();

            stepSizeX = (int) Math.ceil((double) gridWidth / GRID_SIZE);
            stepSizeY = (int) Math.ceil((double) gridHeight / GRID_SIZE);

            isFirstRun = false;
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            canvas.drawLine(0, stepSizeY*i, this.gridWidth, stepSizeY*i ,linePaint);
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            canvas.drawLine(stepSizeX*i, 0, stepSizeX*i, this.gridWidth ,linePaint);
        }

//
//        if(currentTouchY != 0 && currentTouchX != 0){
//            int yCell = (int)currentTouchY / stepSizeY;
//            int xCell = (int)currentTouchX / stepSizeX;
//
//
//            drawPaint.setStyle(Paint.Style.FILL);
//            canvas.drawRect(new Rect(xCell * stepSizeX,yCell * stepSizeY,xCell * stepSizeX + stepSizeX,yCell * stepSizeY + stepSizeY),drawPaint);
//            drawPaint.setStyle(Paint.Style.STROKE);
//        }

        for(int i = 0; i < this.pixels.size(); i++){
//            drawPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(new Rect(this.pixels.get(i).getxNr() * stepSizeX,this.pixels.get(i).getyNr() * stepSizeY,this.pixels.get(i).getxNr() * stepSizeX + stepSizeX,this.pixels.get(i).getyNr() * stepSizeY + stepSizeY),this.pixels.get(i).getDrawPaint());
//            drawPaint.setStyle(Paint.Style.STROKE);
        }

        // Zeichnet einen Pfad der dem Finger folgt
//        canvas.drawPath(drawPath, drawPaint);
    }

    float currentTouchX = 0;
    float currentTouchY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                // TODO wir müssen uns die berührten Punkte zwischenspeichern

                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);

                currentTouchX = touchX;
                currentTouchY = touchY;

                Pixel pixel = new Pixel((int)currentTouchX / stepSizeX,(int)currentTouchY / stepSizeY,new Paint(drawPaint));

                boolean alreadyExists = false;

                for(int i = 0; i < this.pixels.size(); i++){
                    if(this.pixels.get(i).getxNr() == pixel.getxNr() && this.pixels.get(i).getyNr() == pixel.getyNr()){
                        if(isErasing()){
                            this.pixels.remove(i);
                        }
                            alreadyExists = true;


                    }
                }
                if(alreadyExists == false && !isErasing()){
                    this.pixels.add(pixel);
                }



                // TODO wir müssen uns die berührten Punkte zwischenspeichern

                break;
            case MotionEvent.ACTION_UP:

                // TODO Jetzt können wir die zwischengespeicherten Punkte auf das
                // Gitter umrechnen und zeichnen, bzw. löschen, falls wir isErasing
                // true ist (optional)



//                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void addPixelToList(List<Pixel> pixels, float touchX, float touchY )
    {
       // touchX
    }

    public void startNew() {

        // TODO Gitter löschen

        invalidate();
    }

    public void setErase(boolean isErase) {
        isErasing = isErase;
        if (isErasing) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setXfermode(null);
        }
    }

    public boolean isErasing() {
        return isErasing;
    }

    public void setColor(String color) {
//        invalidate();
        drawPaint.setColor(Color.parseColor(color));
    }
}
