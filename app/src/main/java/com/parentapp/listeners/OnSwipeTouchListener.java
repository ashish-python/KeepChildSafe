package com.parentapp.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_DISTANCE_THRESHOLD = 70;
        private static final int SWIPE_VELOCITY_THRESHOLD = 70;
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null){
                return false;
            }
            boolean result = false;
            try{
                float diffY = e2.getRawY() - e1.getRawY();
                float diffX = e2.getRawX() - e1.getRawX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        return true;
                    }
                }
                else{
                    if (Math.abs(diffY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        return true;
                }   }
            }
            catch (Exception e){

            }

            return result;

        }
    }

    public void onSwipeRight(){}

    public void onSwipeLeft(){}

    public void onSwipeBottom(){}

    public void onSwipeTop(){}
}
