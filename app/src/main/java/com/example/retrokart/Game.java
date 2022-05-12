package com.example.retrokart;

class Game {
    Player p;
    Level l;
    float nearPlane;
    float fovDeg;
    float[] a = new float[3];
    boolean gyro = false;
    int[] btsts = new int[4];
    Game() {
        this.p = new Player();
        this.l = new Level();
        this.nearPlane = 0.04f;
        this.fovDeg = 75f;
        this.p.pos.x = l.tileSize*l.xLen/2f;
        this.p.pos.y = l.tileSize*6f;
    }

    protected float fv(float v) {
        float n = this.nearPlane;
        return n/(0.6f - v);
    }

    protected void action() {
        btsts = CustomButton.buttonStates;
        //視点を回転
        if (gyro) {
            a = MainActivity.a_vals;
            p.angle += a[1]/10f;
        } else {
            if (btsts[0] == 1) {
                p.angle -= (float) Math.PI/180f;
            }
            if (btsts[1] == 1) {
                p.angle += (float) Math.PI/180f;
            }
        }

        //プレイヤー移動
        Vec2 vf = p.v.rotate(p.angle + (float) (fovDeg*Math.PI/360f));
        if (btsts[2] == 1) {
            if (l.tileAtScreen(p.pos.add(vf).x, p.pos.add(vf).y) == 1) {
                p.pos = p.pos.add(vf);
            } else if (l.tileAtScreen(p.pos.add(vf).x, p.pos.y) == 1) {
                p.pos.x += vf.x/1.5f;
            } else if (l.tileAtScreen(p.pos.x, p.pos.add(vf).y) == 1) {
                p.pos.y += vf.y/1.5f;
            }
        }
        if (btsts[3] == 1) {
            if (l.tileAtScreen(p.pos.sub(vf).x, p.pos.sub(vf).y) == 1) {
                p.pos = p.pos.sub(vf);
            } else if (l.tileAtScreen(p.pos.sub(vf).x, p.pos.y) == 1) {
                p.pos.x -= vf.x/1.5f;
            } else if (l.tileAtScreen(p.pos.x, p.pos.sub(vf).y) == 1) {
                p.pos.y -= vf.y/1.5f;
            }
        }
    }
}

class Player {
    Vec2 pos,v;
    float angle;
    Player() {
        this.pos = new Vec2(0, 0f);
        this.v = new Vec2(2f, 0f);
        this.angle = (float) (-37.5f*Math.PI/180f);
    }
}

class Level {
    String tiles;
    int xLen,yLen;
    float tileSize,dotSize,gWidth,gHeight;
    Level() {
        this.tiles = "00000022220000000000002222000000" +
                     "00000211112000000000021111200000" +
                     "00002111111200000000211111120000" +
                     "00021112211120000002111221112000" +
                     "00021120021112222221112002112000" +
                     "00021120002111111111120002112000" +
                     "00021120000211111111200002112000" +
                     "00021120000022222222000002112000" +
                     "00021120000000000000000002112000" +
                     "00021120000000000000000002112000" +
                     "00021112000000000000000021112000" +
                     "00002111200000000000000211120000" +
                     "00000211120000000000002111200000" +
                     "00000021112000000000021112000000" +
                     "00000002111200000000211120000000" +
                     "00000000211200000000211200000000" +
                     "00000000211200000000211200000000" +
                     "00000002111200000000211120000000" +
                     "00000021112000000000021112000000" +
                     "00000211120000000000002111200000" +
                     "00002111200000000000000211120000" +
                     "00021112000000000000000021112000" +
                     "00021120000000000000000002112000" +
                     "00021120000000000000000002112000" +
                     "00021120000022222222000002112000" +
                     "00021120000211111111200002112000" +
                     "00021120002111111111120002112000" +
                     "00021120021112222221112002112000" +
                     "00021112211120000002111221112000" +
                     "00002111111200000000211111120000" +
                     "00000211112000000000021111200000" +
                     "00000022220000000000002222000000";
        this.xLen = 32;
        this.yLen = 32;
        this.tileSize = Math.min(
                MainActivity.sHeight / (float) yLen,
                MainActivity.sWidth / (float) xLen
        );
        this.dotSize = tileSize/3f;
        this.gWidth = tileSize*xLen;
        this.gHeight = tileSize*yLen;
    }

    /**
     *
     * @param x x座標(マップ上)
     * @param y y座標(マップ上)
     * @return 左上を(0,0)として(x,y)のタイルの数字
     */
    protected int tileAt(int x, int y) {
        if (x < 0 || x >= this.xLen || y < 0 || y >= this.yLen) return 0;
        return Character.getNumericValue(this.tiles.charAt(this.xLen*y + x));
    }

    /**
     *
     * @param x x座標(画面上)
     * @param y y座標(画面上)
     * @return 左上を(0,0)として座標(x,y)にあるタイルの数字
     */
    protected int tileAtScreen(float x, float y) {
        return this.tileAt((int) (x/this.tileSize), (int) (y/this.tileSize));
    }
}
