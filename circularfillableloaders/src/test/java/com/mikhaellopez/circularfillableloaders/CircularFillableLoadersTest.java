package com.mikhaellopez.circularfillableloaders;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

/**
 * Created by mohd.farid@devfactory.com on 20/01/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CircularFillableLoaders.class, ObjectAnimator.class, AnimatorSet.class, View.class})
public class CircularFillableLoadersTest {

    private CircularFillableLoaders loaders;

    @Mock
    private Context context;

    @Mock
    private AnimatorSet animatorSet;

    @Mock
    private Paint paint;

    @Mock
    private TypedArray typedArray;

    @Before
    public void setUp() throws Exception {
        doNothing().when(paint).setAntiAlias(true);
        PowerMockito.whenNew(Paint.class).withNoArguments().thenReturn(paint);

        PowerMockito.mockStatic(ObjectAnimator.class);
        ObjectAnimator objectAnimator = mock(ObjectAnimator.class);
        Mockito.when(ObjectAnimator.ofFloat(any(), eq("waveShiftRatio"), eq(0f), eq(1f))).thenReturn(objectAnimator);

        ObjectAnimator objectAnimator2 = mock(ObjectAnimator.class);
        Mockito.when(ObjectAnimator.ofFloat(any(), eq("waterLevelRatio"), anyFloat(), anyFloat())).thenReturn(objectAnimator2);

        PowerMockito.whenNew(AnimatorSet.class).withNoArguments().thenReturn(animatorSet);

        Mockito.when(context.obtainStyledAttributes(any(AttributeSet.class), eq(R.styleable.CircularFillableLoaders), anyInt(), eq(0))).thenReturn(typedArray);

        loaders = new CircularFillableLoaders(context);

        //pre-test assertions
        assertEquals(animatorSet, getInternalState(loaders, "animatorSetWave"));
        assertEquals(paint, getInternalState(loaders, "paint"));
    }

    @Test
    public void testOnSizeChanged() throws Exception {
        //when
        loaders.onSizeChanged(10, 20, 20, 40);

        //then
        assertEquals(10, getInternalState(loaders, "canvasSize"));

        //when
        loaders.onSizeChanged(20, 10, 40, 20);

        //then
        assertEquals(10, getInternalState(loaders, "canvasSize"));
    }


    @Test
    public void testSetColor() throws Exception {
        //when
        loaders.setColor(10);

        //then
        assertEquals(10, getInternalState(loaders, "waveColor"));
    }

    @Test
    public void testSetBorderWidth() throws Exception {
        //when
        loaders.setBorderWidth(100);

        //then
        verify(paint).setStrokeWidth(100);
    }

    @Test
    public void testOnAttachedToWindow() throws Exception {
        //given
        verify(animatorSet, new Times(1)).start();

        //when
        loaders.onAttachedToWindow();

        //then
        verify(animatorSet, new Times(2)).start();
    }

    @Test
    public void testOnDetachedFromWindow() throws Exception {
        //when
        loaders.onDetachedFromWindow();

        //then
        verify(animatorSet).end();
    }
}