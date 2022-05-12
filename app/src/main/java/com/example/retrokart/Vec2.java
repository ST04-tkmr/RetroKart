package com.example.retrokart;

class Vec2 {
    float x,y;

    Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected Vec2 add(Vec2 b) {
        Vec2 a = this;
        return new Vec2(a.x + b.x, a.y + b.y);
    }

    protected Vec2 sub(Vec2 b) {
        Vec2 a = this;
        return new Vec2(a.x - b.x, a.y - b.y);
    }

    protected Vec2 mul(float s) {
        return new Vec2(s*this.x, s*this.y);
    }

    protected Vec2 div(float s) { return new Vec2(this.x/s, this.y/s); }

    /**
     *
     * @return 内積
     */
    protected float dot(Vec2 b) {
        Vec2 a = this;
        return a.x*b.x + a.y*b.y;
    }

    protected float mag() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     *
     * @param rad 回転させたい角度(単位はラジアン)
     */
    protected Vec2 rotate(float rad) {
        Vec2 a  = this;
        return new Vec2(
                (float) (Math.cos(rad)*a.x - Math.sin(rad)*a.y),
                (float) (Math.sin(rad)*a.x + Math.cos(rad)*a.y)
        );
    }

    /**
     *
     * @return 正規化されたベクトル
     */
    protected Vec2 norm() { return this.mul(1/this.mag()); }

    protected Vec2 copy() {
        return new Vec2(this.x, this.y);
    }

    protected boolean equals(Vec2 b) {
        Vec2 a = this;
        return a.x == b.x && a.y == b.y;
    }
}
