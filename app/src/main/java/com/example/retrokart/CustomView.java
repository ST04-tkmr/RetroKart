package com.example.retrokart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class CustomView extends View {
    float sWidth,sHeight,wMargin,hMargin,gWidth,gHeight,scale;
    Paint background = new Paint();
    Paint mapBack = new Paint();
    Paint white = new Paint();
    Paint beam = new Paint();
    Paint red = new Paint();
    Paint backFrame = new Paint();
    Paint black = new Paint();
    Game game;
    Level l;
    Player p;
    Vec2 L,R,len,a,b;
    int bsb;
    double time = 0.00;
    boolean gameStart = false;

    public CustomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sWidth = MainActivity.sWidth;
        sHeight = MainActivity.sHeight;
        background.setStyle(Paint.Style.FILL);
        background.setColor(Color.BLACK);
        background.setAlpha(200);
        mapBack.setColor(Color.BLACK);
        mapBack.setStyle(Paint.Style.STROKE);
        mapBack.setStrokeWidth(9f);
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        white.setTextSize(50f);
        beam.setColor(Color.YELLOW);
        beam.setStrokeWidth(3f);
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);
        backFrame.setColor(Color.BLUE);
        backFrame.setStyle(Paint.Style.STROKE);
        backFrame.setStrokeWidth(3f);
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        len = new Vec2(250f, 0f);
        a = new Vec2(0f, 0f);
    }

    protected void setGame(Game game) {
        this.game = game;
        this.l = game.l;
        this.p = game.p;
        this.gWidth = l.gWidth;
        this.gHeight = l.gHeight;
        this.wMargin = sWidth - gWidth;
        this.hMargin = sHeight - gHeight;
        if (wMargin < hMargin) {
            this.scale = hMargin/gHeight;
        } else if (hMargin < wMargin) {
            this.scale = (wMargin/2f)/gWidth;
        }
        b = new Vec2(l.tileSize/2f, l.tileSize/2f);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gameStart) game.action();
        L = len.rotate(p.angle); //視界の左端
        R = L.rotate((float) (game.fovDeg*Math.PI/180f)); //視界の右端
        bsb = CustomButton.buttonStates[4]; //後ろ確認ボタン

        //視界描画
        canvas.save();
        canvas.translate(wMargin/2f, hMargin);

        canvas.drawRect(0, 0, gWidth, gHeight, background);
        for (int p2=0;p2<gHeight;p2+=l.dotSize) {
            for (int p1=0;p1<gWidth;p1+=l.dotSize) {
                float h = p1/gWidth;
                float v = game.fv((gHeight - p2)/gHeight);
                Vec2 s = L.mul(v*(1-h)).add(R.mul(v*h));
                if (v < 0 && bsb == 0) continue;
                if (v < 0) s.mul(-1f);
                float t1 = s.add(p.pos).x;
                float t2 = s.add(p.pos).y;
                int t = l.tileAtScreen(t1, t2);
                if (t == 0) continue;
                if (t == 2) {
                    float w = l.tileSize;
                    a.x = t1%w;
                    a.y = t2%w;
                    if (!(a.sub(b).mag() < w/2.5f)) continue;
                }
                if (v < 0 && bsb == 1) {
                    canvas.save();
                    canvas.translate(gWidth, gHeight*0.4f);
                    canvas.rotate(180f);
                    canvas.drawRect(p1, p2, p1 + l.dotSize - l.dotSize/3f, p2 + l.dotSize - l.dotSize/3f, white);
                    canvas.restore();
                    canvas.drawRect(0, 0, gWidth, gHeight*0.4f, backFrame);
                }
                if (v > 0) canvas.drawRect(
                        p1, p2,
                        p1 + l.dotSize - l.dotSize/3f, p2 + l.dotSize - l.dotSize/3f,
                        white
                );
            }
        }

        canvas.restore();

        //マップ描画
        canvas.save();
        canvas.scale(scale, scale);

        canvas.drawRect(0, 0, gWidth, gHeight, background);
        for (int y=0;y<l.yLen;y++) {
            for (int x=0;x<l.xLen;x++) {
                int t = l.tileAt(x, y);
                if (t == 1) {
                    canvas.drawRect(
                            l.tileSize*x, l.tileSize*y,
                            l.tileSize*x + l.tileSize, l.tileSize*y + l.tileSize,
                            white
                    );
                }
                if (t == 2) {
                    canvas.drawCircle(
                            l.tileSize*x + l.tileSize/2f,
                            l.tileSize*y + l.tileSize/2f,
                            l.tileSize/2.5f, white
                    );
                }
            }
        }
        canvas.drawLine(
                p.pos.x, p.pos.y,
                p.pos.x + L.x, p.pos.y + L.y, beam
        );
        canvas.drawLine(
                p.pos.x, p.pos.y,
                p.pos.x + R.x, p.pos.y + R.y, beam
        );
        canvas.drawLine(
                p.pos.add(L).x, p.pos.add(L).y,
                p.pos.add(L).x + R.sub(L).x, p.pos.add(L).y + R.sub(L).y,
                beam
        );
        for (int p1=0;p1<gWidth;p1+=48) {
            float h = p1/gWidth;
            Vec2 t = L.mul(1f - h).add(R.mul(h));
            canvas.drawLine(
                    p.pos.x, p.pos.y,
                    p.pos.x + t.x, p.pos.y + t.y, beam
            );
        }
        for (int p2=0;p2<gHeight;p2+=48) {
            float v = game.fv((gHeight - p2)/gHeight);
            if (Math.abs(v) > 1 || v < 0) continue;
            Vec2 L2 = L.mul(v);
            Vec2 R2 = R.mul(v);
            canvas.drawLine(
                    p.pos.add(L2).x, p.pos.add(L2).y,
                    p.pos.add(L2).x + R2.sub(L2).x, p.pos.add(L2).y + R2.sub(L2).y,
                    beam
            );
        }
        canvas.drawCircle(p.pos.x, p.pos.y, l.tileSize/1.5f, red);

        canvas.drawRect(0, 0, gWidth, gHeight, mapBack);

        canvas.restore();

        canvas.drawText(String.format(Locale.getDefault(), "%.2f", time), hMargin/2f + gWidth, 50, white);
    }
}
