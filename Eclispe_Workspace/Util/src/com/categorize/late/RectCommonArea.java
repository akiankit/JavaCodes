package com.categorize.late;

class Rect {
    int topx, topy;

    int botx, boty;

    public Rect(int topx, int topy, int botx, int boty) {
        this.botx = botx;
        this.boty = boty;
        this.topx = topx;
        this.topy = topy;
    }
}
public class RectCommonArea {

    public static void main(String[] args) {
        Rect rectA = new Rect(10, 20, 20, 10);
        Rect rectB = new Rect(10, 20, 20, 10);
        // Rect rectB = new Rect(30, 55, 55, 30);
        System.out.println(hasRectCommonArea(rectA, rectB));
    }

    // Not sure if this is right. Verified by considering some scenarios manually.
    // Could not find proper test cases to verify it.
    public static int hasRectCommonArea(Rect A, Rect B) {
        if (B.topx >= A.botx && B.boty >= A.topy)
            return 0;
        if (B.botx <= A.topx && B.boty <= A.topy)
            return 0;
        if (B.botx >= A.botx && B.topy <= A.boty)
            return 0;
        if (B.botx <= A.topx && B.topy <= A.boty)
            return 0;
        return 1;
    }

}
