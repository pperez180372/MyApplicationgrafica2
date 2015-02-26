package com.example.pperez.myapplicationgrafica2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.graphics.Color;
import android.widget.FrameLayout;

/**
 * Created by amuii on 2/26/15.
 */
public class signalView extends View {

        Paint paint = new Paint();
        Context c;
        float x;
        float y;

        public signalView(Context context) {
            super(context);
            c = context;
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            x = 100.0f;
            y = 100.0f;

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));
            FrameLayout ll = (FrameLayout) ((MainActivity)c).findViewById(R.id.Grafica);
            int wd=ll.getWidth();
            int hd=ll.getHeight();

            Bitmap bg = Bitmap.createBitmap(wd,hd, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawRect(50, 50, 200, 200, paint);
            ll.setBackgroundDrawable(new BitmapDrawable(bg));

        }

        @Override
        public void onDraw(Canvas canvas) {

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(10,20, 20, paint);

        }



}
/*
public class MyView extends View {
    public MyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(x / 2, y / 2, radius, paint);
    }
}
*/
