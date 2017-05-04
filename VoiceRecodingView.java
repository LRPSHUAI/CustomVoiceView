
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 录音中音谱
 * Created by LRP1989 on 2017/5/4.
 */
public class VoiceRecodingView extends View {
    /**
     * 线条的长度
     */
    private float ARC_LINE_LENGTH;
    /**
     * 线条的宽度
     */
    private float ARC_LINE_WIDTH;
    /**
     * 组件的宽，高
     */
    private float width, height;
    /**
     * 绘制线的画笔
     */
    private Paint linePaint;
    /**
     * 渐变半径
     */
    private float circleRadius = 1;
    /**
     * 渐变中心位置
     */
    private float centerX, centerY;
    private ArrayList<Integer> tempList = new ArrayList<>();
    private List<Integer> lines = new ArrayList<>();
    private final int start__color = Color.rgb(82, 241, 235);
    private final int end__color = Color.rgb(82, 191, 241);
    private Random random = new Random();

    public VoiceRecodingView(Context context) {
        super(context);
        init();
    }

    public VoiceRecodingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoiceRecodingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        init();
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();

            //计算渐变半径和中点
            circleRadius = Math.min(width, height) / 2;
            centerX = width / 2;
            centerY = height / 2;
            //计算每根线宽度
            ARC_LINE_WIDTH = width / 40;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制集合内的线
        if (circleRadius < 1) {
            circleRadius = 1;
        }
        linePaint.setShader(new RadialGradient(centerX, centerY, circleRadius, end__color, start__color, Shader.TileMode.CLAMP));
        linePaint.setStrokeWidth(ARC_LINE_WIDTH - 2);
        float startX = 0.0f;
        for (int i = 0; i < lines.size(); i++) {
            if (startX <= width) {
                int lin = lines.get(i);
                float lineStartX = startX;
                float lineStartY = centerY - lin;
                float lineStopX = startX;
                float lineStopY = centerY + lin;

                canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);
                startX += ARC_LINE_WIDTH;
            }
        }
    }

    /**
     * 设置声音级别
     *
     * @param level
     */
    public void setVoiceValum(int level) {
        lines.clear();
        if (level > 0) {
            ARC_LINE_LENGTH = circleRadius * level / 7;
            tempList.clear();
            int v = (int) (width / 2 / ARC_LINE_WIDTH);
            float v1 = ARC_LINE_LENGTH / v;
            int v3 = (int) v1 * 10;
            int v4 = (int) v1 * 6;
            int v5 = (int) v1 * 13;
            int v6 = (int) v1 * 3;

            for (int i = 0; i < 20; i++) {
                int temp = 0;
                if (i > 18) {
                    temp = (int) (v1 * i);
                } else if (i > 13) {
                    temp = random.nextInt(v3);
                } else if (i > 9) {
                    temp = random.nextInt(v4);
                } else if (i > 5) {
                    temp = random.nextInt(v5);
                } else if (i >= 0) {
                    temp = random.nextInt(v6);
                }
                if (temp < 1) {
                    temp = 1;
                }
                lines.add(temp);
                tempList.add(0, temp);
            }
            lines.addAll(tempList);
        } else {
            for (int i = 0; i < 40; i++) {
                lines.add(1);
            }
        }
        this.invalidate();
    }

}