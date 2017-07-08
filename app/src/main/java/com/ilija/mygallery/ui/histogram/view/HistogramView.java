package com.ilija.mygallery.ui.histogram.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by ikac on 7/8/17.
 */

public class HistogramView extends View {

    private int maxY = 0;

    private int[] red = new int[256];
    private int[] green = new int[256];
    private int[] blue = new int[256];

    private Paint paint = new Paint();

    public HistogramView(Context context, Bitmap image) {
        super(context);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                red[Color.red(pixel)] += 1;
                if (red[Color.red(pixel)] > maxY) {
                    maxY = red[Color.red(pixel)];
                }
                green[Color.green(pixel)] += 1;
                if (green[Color.green(pixel)] > maxY) {
                    maxY = green[Color.green(pixel)];
                }
                blue[Color.blue(pixel)] += 1;
                if (blue[Color.blue(pixel)] > maxY) {
                    maxY = blue[Color.blue(pixel)];
                }
            }
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();

        int offset = width / 256;
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < 256; i++) {
            paint.reset();
            if (red[i] > green[i]) {
                if (red[i] > blue[i]) {
                    if (green[i] > blue[i]) {
                        // rgb
                        paint.setColor(Color.RED);
                        int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);

                        paint.setColor(Color.GREEN);
                        int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);

                        paint.setColor(Color.BLUE);
                        int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);
                    } else {
                        // rbg
                        paint.setColor(Color.RED);
                        int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);

                        paint.setColor(Color.BLUE);
                        int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);

                        paint.setColor(Color.GREEN);
                        int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                        canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);
                    }
                } else {
                    // brg
                    paint.setColor(Color.BLUE);
                    int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);

                    paint.setColor(Color.RED);
                    int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);

                    paint.setColor(Color.GREEN);
                    int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);
                }
            } else if (green[i] > blue[i]) {
                if (red[i] > blue[i]) {
                    // grb
                    paint.setColor(Color.GREEN);
                    int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);

                    paint.setColor(Color.RED);
                    int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);

                    paint.setColor(Color.BLUE);
                    int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);
                } else {
                    // gbr
                    paint.setColor(Color.GREEN);
                    int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);

                    paint.setColor(Color.BLUE);
                    int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);

                    paint.setColor(Color.RED);
                    int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                    canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);
                }
            } else {
                // bgr
                paint.setColor(Color.BLUE);
                int yValueBlue = (int) (((double) blue[i] / (double) maxY ) * height);
                canvas.drawRect(i * offset, height, i * offset + offset, yValueBlue, paint);

                paint.setColor(Color.GREEN);
                int yValueGreen = (int) (((double) green[i] / (double) maxY ) * height);
                canvas.drawRect(i * offset, height, i * offset + offset, yValueGreen, paint);

                paint.setColor(Color.RED);
                int yValueRed = (int) (((double) red[i] / (double) maxY ) * height);
                canvas.drawRect(i * offset, height, i * offset + offset, yValueRed, paint);
            }
        }

    }

}
