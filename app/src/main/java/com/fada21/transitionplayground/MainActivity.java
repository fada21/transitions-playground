package com.fada21.transitionplayground;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private boolean toggle;
    private RelativeLayout sceneRoot;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sceneRoot = (RelativeLayout) findViewById(R.id.activity_main);
        setButton();

    }

    private void setButton() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (toggle) {
                    reset();
                } else {
                    animate();
                }
                toggle = !toggle;
            }
        });
    }

    void animate() {
        TransitionManager.beginDelayedTransition(sceneRoot);
        final View childLeft = sceneRoot.findViewById(R.id.left);
        animateLeft(childLeft);
        View childRight = sceneRoot.findViewById(R.id.right);
        animateRight(childRight);
    }

    private void animateColor(final View view, @ColorRes int color) {
        ColorDrawable background = (ColorDrawable) view.getBackground();
        int colorFrom = background.getColor();
        int colorTo = ContextCompat.getColor(this, color);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private void animateLeft(View child) {
        animateColor(child, R.color.colorPrimary);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height *= 4;
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        child.setLayoutParams(params);
    }

    private void animateRight(View child) {
        animateColor(child, R.color.colorAccent);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
        params.width *= 2;
        params.height *= 2;
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        child.setLayoutParams(params);
    }

    private void reset() {
        Scene scene = Scene.getSceneForLayout(sceneRoot, R.layout.activity_main, this);
        scene.setEnterAction(new Runnable() {
            @Override public void run() {
                setButton();
            }
        });
        TransitionManager.go(scene, new ChangeBounds());
    }
}
