package com.mikhaellopez.circularfillableloaders;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Mikhael LOPEZ on 09/10/2015.
 */
public class CircularFillableLoaders extends ImageView {
    // Default values
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    public static final int DEFAULT_WAVE_COLOR = Color.BLACK;
    public static final int DEFAULT_BORDER_WIDTH = 10;

    // Dynamic Properties
    private int canvasSize;
    private float amplitudeRatio;
    private int waveColor;

    // Properties
    private float waterLevelRatio = 1f;
    private float waveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private float defaultWaterLevel;

    // Object used to draw
    private Bitmap image;
    private Drawable drawable;
    private Paint paint;
    private Paint borderPaint;
    private Paint wavePaint;
    private BitmapShader waveShader;
    private Matrix waveShaderMatrix;

    // Animation
    private AnimatorSet animatorSetWave;

    //region Constructor & Init Method
    public CircularFillableLoaders(final Context context) {
        this(context, null);
    }

    public CircularFillableLoaders(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularFillableLoaders(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // Init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        // Init Wave
        waveShaderMatrix = new Matrix();
        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);

        // Init Border
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        // Init Animation
        initAnimation();

        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularFillableLoaders, defStyleAttr, 0);

        // Init Wave
        waveColor = attributes.getColor(R.styleable.CircularFillableLoaders_cfl_wave_color, DEFAULT_WAVE_COLOR);
        float amplitudeRatioAttr = attributes.getFloat(R.styleable.CircularFillableLoaders_cfl_wave_amplitude, DEFAULT_AMPLITUDE_RATIO);
        amplitudeRatio = (amplitudeRatioAttr > DEFAULT_AMPLITUDE_RATIO) ? DEFAULT_AMPLITUDE_RATIO : amplitudeRatioAttr;
        setProgress(attributes.getInteger(R.styleable.CircularFillableLoaders_cfl_progress, 0));

        if (attributes.getBoolean(R.styleable.CircularFillableLoaders_cfl_border, true)) {
            float defaultBorderSize = DEFAULT_BORDER_WIDTH * getContext().getResources().getDisplayMetrics().density;
            borderPaint.setStrokeWidth(attributes.getDimension(R.styleable.CircularFillableLoaders_cfl_border_width, defaultBorderSize));
        } else {
            borderPaint.setStrokeWidth(0);
        }

    }
    //endregion

    //region Draw Method
    @Override
    public void onDraw(Canvas canvas) {
        // Load the bitmap
        loadBitmap();

        // Check if image isn't null
        if (image == null)
            return;

        if (!isInEditMode()) {
            canvasSize = canvas.getWidth();
            if (canvas.getHeight() < canvasSize) {
                canvasSize = canvas.getHeight();
            }
        }

        // Draw Image Circular
        int circleCenter = canvasSize / 2;
        canvas.drawCircle(circleCenter, circleCenter, circleCenter - borderPaint.getStrokeWidth(), paint);

        // Draw Wave
        // modify paint shader according to mShowWave state
        if (waveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (wavePaint.getShader() == null) {
                wavePaint.setShader(waveShader);
            }

            // sacle shader according to waveLengthRatio and amplitudeRatio
            // this decides the size(waveLengthRatio for width, amplitudeRatio for height) of waves
            waveShaderMatrix.setScale(1, amplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, defaultWaterLevel);
            // translate shader according to waveShiftRatio and waterLevelRatio
            // this decides the start position(waveShiftRatio for x, waterLevelRatio for y) of waves
            waveShaderMatrix.postTranslate(waveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - waterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            waveShader.setLocalMatrix(waveShaderMatrix);

            // Draw Border
            borderPaint.setColor(waveColor);
            float borderWidth = borderPaint.getStrokeWidth();
            if (borderWidth > 0) {
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, (getWidth() - borderWidth) / 2f - 1f, borderPaint);
            }

            // Draw Wave
            float radius = getWidth() / 2f - borderWidth;
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, wavePaint);
        } else {
            wavePaint.setShader(null);
        }
    }

    private void loadBitmap() {
        if (this.drawable == getDrawable())
            return;

        this.drawable = getDrawable();
        this.image = drawableToBitmap(this.drawable);
        updateShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasSize = w;
        if (h < canvasSize)
            canvasSize = h;
        if (image != null)
            updateShader();
    }

    private void updateShader() {
        if (this.image == null)
            return;

        // Crop Center Image
        image = cropBitmap(image);

        // Create Shader
        BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // Center Image in Shader
        Matrix matrix = new Matrix();
        matrix.setScale((float) canvasSize / (float) image.getWidth(), (float) canvasSize / (float) image.getHeight());
        shader.setLocalMatrix(matrix);

        // Set Shader in Paint
        paint.setShader(shader);

        // Update Wave Shader
        updateWaveShader();
    }

    private void updateWaveShader() {
        double defaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        float defaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        defaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        float defaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(adjustAlpha(waveColor, 0.3f));
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * defaultAngularFrequency;
            float beginY = (float) (defaultWaterLevel + defaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
            waveY[beginX] = beginY;
        }

        wavePaint.setColor(waveColor);
        final int wave2Shift = (int) (defaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        waveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        this.wavePaint.setShader(waveShader);
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        Bitmap bmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(), bitmap.getHeight());
        } else {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(), bitmap.getWidth());
        }
        return bmp;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
            return null;

        try {
            // Create Bitmap object out of the drawable
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Simply return null of failed bitmap creations
            Log.e(getClass().toString(), "Encountered OutOfMemoryError while generating bitmap!");
            return null;
        }
    }
    //endregion

    //region Mesure Method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        int imageSize = (width < height) ? width : height;
        setMeasuredDimension(imageSize, imageSize);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // The parent has not imposed any constraint on the child.
            result = canvasSize;
        }
        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = canvasSize;
        }
        return (result + 2);
    }
    //endregion

    //region Set Attr Method
    public void setColor(int color) {
        waveColor = color;
        updateWaveShader();
        invalidate();
    }

    public void setBorderWidth(float width) {
        borderPaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (this.amplitudeRatio != amplitudeRatio) {
            this.amplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public void setProgress(int progress) {
        // vertical animation.
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", waterLevelRatio, 1f - ((float) progress / 100));
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }
    //endregion

    //region Animation
    private void startAnimation() {
        if (animatorSetWave != null) {
            animatorSetWave.start();
        }
    }

    private void initAnimation() {
        // horizontal animation.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        animatorSetWave = new AnimatorSet();
        animatorSetWave.play(waveShiftAnim);
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     */
    private void setWaveShiftRatio(float waveShiftRatio) {
        if (this.waveShiftRatio != waveShiftRatio) {
            this.waveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     */
    private void setWaterLevelRatio(float waterLevelRatio) {
        if (this.waterLevelRatio != waterLevelRatio) {
            this.waterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    private void cancel() {
        if (animatorSetWave != null) {
            animatorSetWave.end();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        startAnimation();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancel();
        super.onDetachedFromWindow();
    }
    //endregion

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}