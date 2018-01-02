package com.rinon.shannoncode.animation

import android.animation.*
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.ViewAnimationUtils
import android.transition.Transition
import android.transition.TransitionValues
import android.util.ArrayMap
import com.rinon.shannoncode.R

class CircleTransition(val context: Context, attrs:AttributeSet) : Transition(context, attrs) {

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view

        //親view内での相対位置（左上、右下）の取得
        val rect = Rect(view.left, view.top, view.right, view.bottom)
        transitionValues.values.put("bounds", rect)
        Log.d("Anim:bounds", rect.toString())

        //画面上での位置取得
        val position = IntArray(2)
        view.getLocationInWindow(position)
        transitionValues.values.put("position", position)
        Log.d("Anim:position", "[" + position[0] + "." + position[1] + "]")
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        val view = transitionValues.view

        if (view.width <= 0 || view.height <= 0) {
            //viewサイズがないのでいらない
            return
        }

        //viewにサイズがある状態
        captureValues(transitionValues)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        transitionValues.values.put("image", bitmap)

        /*
        val color = (view.background as ColorDrawable?)?: throw NullPointerException("color is null")
        transitionValues.values.put("color", color.color)
        Log.d("Aim:color", "a" + color.toString())
        */
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        val view = transitionValues.view

        if (view.width <= 0 || view.height <= 0) {
            //viewサイズがないのでいらない
            return
        }
        //viewにサイズがある状態
        captureValues(transitionValues)
    }

    /**
     * @param sceneRoot 遷移先activity全体
     * @return アニメーション
     */
    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?,
                                endValues: TransitionValues?): Animator? {
        //dataの取り出し作業ーーーーーーーーーーーーーーーーーーーーー
        if (startValues == null || endValues == null) {
            //valueがない奴はいらない
            return null
        }

        val startBounds = startValues.values["bounds"] as Rect?
        val endBounds = endValues.values["bounds"] as Rect?

        if (startBounds == null || endBounds == null || startBounds == endBounds) {
            //位置に差がないviewはアニメーションいらない
            return null
        }

        val startImage = startValues.values["image"] as Bitmap
       // val color = startValues.values["color"] as Int

        val startView = addViewToOverlay(sceneRoot, startImage.width, startImage.height,
                BitmapDrawable(startImage))
        val startBallView = addViewToOverlay(sceneRoot, startImage.width, startImage.height,
                ColorDrawable(ContextCompat.getColor(context, R.color.shannon)))

        val endView = endValues.view
        val endBallView = addViewToOverlay(sceneRoot, endView.width, endView.height,
                ColorDrawable(ContextCompat.getColor(context, R.color.shannon)))
        startBallView.alpha = 0f

        //startViewの位置を計算ーーーーーーーーーーーーーーーーーーーー
        val sceneRootPosition = IntArray(2)
        sceneRoot.getLocationInWindow(sceneRootPosition)

        //保存しておいた位置
        val startPosition = startValues.values["position"] as IntArray
        val endPosition = endValues.values["position"] as IntArray

        //保存値から現在値を引いて差分を計算
        val transStartX = (startPosition[0] - sceneRootPosition[0]).toFloat()
        val transStartY = (startPosition[1] - sceneRootPosition[1]).toFloat()
        val transEndX = (endPosition[0] + (endView.width - startView.width) / 2).toFloat()
        val transEndY = (endPosition[1] + (endView.height - startView.height) / 2).toFloat()

        //計算結果をviewに適用
        startView.translationX = transStartX
        startView.translationY = transStartY
        startBallView.translationX = transStartX
        startBallView.translationY = transStartY

        endBallView.translationX = (endPosition[0] - sceneRootPosition[0]).toFloat()
        endBallView.translationY = (endPosition[1] - sceneRootPosition[1]).toFloat()

        //revealアニメーションを生成
        val revealStartView = createCircularReveal(startView, calculateMaxRadius(startView),
                Math.min(calculateMinRadius(startView) / 2.0f, calculateMinRadius(endView) / 2.0f))

        val revealStartBallView = createCircularReveal(startBallView, calculateMaxRadius(startView),
                Math.min(calculateMinRadius(startView) / 2.0f, calculateMinRadius(endView) / 2.0f))

        val revealEndBallViewOpen = createCircularReveal(endBallView,
                Math.min(calculateMinRadius(startView) / 2.0f, calculateMinRadius(endView) / 2.0f),
                calculateMaxRadius(endView))

        //fadeするアニメーション
        val fadeOutStartView = ObjectAnimator.ofFloat(startView, View.ALPHA, 1f, 0f)
        val fadeInStartBallView = ObjectAnimator.ofFloat(startBallView, View.ALPHA, 0f, 1f)
        val fadeInEndView = ObjectAnimator.ofFloat(endView, View.ALPHA, 0f, 1f)
        val fadeOutEndBallView = ObjectAnimator.ofFloat(endBallView, View.ALPHA, 1f, 0f)

        //各Viewの初期状態を設定
        endView.alpha = 0f
        endBallView.alpha = 0f

        //移動するアニメーション
        //はじめとおわりの位置を計算（対象viewの中央）
        val moveStartView = ObjectAnimator.ofFloat(startView, View.TRANSLATION_X, View.TRANSLATION_Y,
                pathMotion.getPath(transStartX, transStartY, transEndX, transEndY))
        val moveStartBallView = ObjectAnimator.ofFloat(startBallView, View.TRANSLATION_X, View.TRANSLATION_Y,
                pathMotion.getPath(transStartX, transStartY, transEndX, transEndY))

        //アニメーションを組み合わせる
        val transitionAnimation = AnimatorSet()
        val transitionStep1 = AnimatorSet()
        val transitionStep2 = AnimatorSet()
        val transitionStep3 = AnimatorSet()

        transitionAnimation.playSequentially(transitionStep1, transitionStep2, transitionStep3)

        //step1: 移動元Viewを丸に変形しながら、移動先Viewの中央まで移動しつつ、画像はフェードアウト、背景フェードイン
        transitionStep1.playTogether(revealStartView, revealStartBallView, moveStartView,
                moveStartBallView, fadeOutStartView, fadeInStartBallView)
        transitionStep1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 0f
                endBallView.alpha = 1f
            }
        })

        //step2: 背景色の丸を移動先View全体にかかるようにreveal
        transitionStep2.playTogether(revealEndBallViewOpen)
        transitionStep2.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 0f
                endBallView.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 0f
                endBallView.alpha = 1f
            }
        })

        //step3: 背景色をフェードアウトさせつつ、移動先Viewをフェードイン
        transitionStep3.playTogether(fadeOutEndBallView, fadeInEndView)
        transitionStep3.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 0f
                endBallView.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 1f
                endBallView.alpha = 0f
            }
        })

        transitionAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                startView.alpha = 0f
                startBallView.alpha = 0f
                endView.alpha = 1f
                endBallView.alpha = 0f
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)

                //開始時の各Viewのalpha設定
                startView.alpha = 1f
                startBallView.alpha = 0f
                endView.alpha = 0f
                endBallView.alpha = 0f
                Log.d("a", "a")
            }
        })
        return transitionAnimation
    }

    /**
     * from playanimation
     */
    private fun addViewToOverlay(sceneRoot: ViewGroup, width: Int, height: Int, background: Drawable): View {
        val view = NoOverlapView(sceneRoot.context)
        view.background = background
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, width, height)
        sceneRoot.overlay.add(view)
        return view
    }

    private fun createCircularReveal(view: View, startRadius: Float, endRadius: Float): Animator {
        val centerX = view.width / 2
        val centerY = view.height / 2

        val reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius)
        return NoPauseAnimator(reveal)
    }

    //viewに対する外接円の半径を計算する
    private fun calculateMaxRadius(view: View): Float {
        val widthSquared = (view.width * view.width).toFloat()
        val heightSquared = (view.height * view.height).toFloat()
        return Math.sqrt((widthSquared + heightSquared).toDouble()).toFloat() / 2
    }

    //viewに対する内接円の半径を計算する
    private fun calculateMinRadius(view: View): Int {
        return Math.min(view.width / 2, view.height / 2)
    }

    private class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
        private val mListeners = ArrayMap<AnimatorListener, AnimatorListener>()

        override fun addListener(listener: Animator.AnimatorListener) {
            val wrapper = AnimatorListenerWrapper(this, listener)
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper)
                mAnimator.addListener(wrapper)
            }
        }

        override fun cancel() {
            mAnimator.cancel()
        }

        override fun end() {
            mAnimator.end()
        }

        override fun getDuration(): Long {
            return mAnimator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return mAnimator.interpolator
        }

        override fun getListeners(): ArrayList<Animator.AnimatorListener> {
            return ArrayList(mListeners.keys)
        }

        override fun getStartDelay(): Long {
            return mAnimator.startDelay
        }

        override fun isPaused(): Boolean {
            return mAnimator.isPaused
        }

        override fun isRunning(): Boolean {
            return mAnimator.isRunning
        }

        override fun isStarted(): Boolean {
            return mAnimator.isStarted
        }

        override fun removeAllListeners() {
            mListeners.clear()
            mAnimator.removeAllListeners()
        }

        override fun removeListener(listener: Animator.AnimatorListener) {
            val wrapper = mListeners[listener]
            if (wrapper != null) {
                mListeners.remove(listener)
                mAnimator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            mAnimator.duration = durationMS
            return this
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            mAnimator.interpolator = timeInterpolator
        }

        override fun setStartDelay(delayMS: Long) {
            mAnimator.startDelay = delayMS
        }

        override fun setTarget(target: Any?) {
            mAnimator.setTarget(target)
        }

        override fun setupEndValues() {
            mAnimator.setupEndValues()
        }

        override fun setupStartValues() {
            mAnimator.setupStartValues()
        }

        override fun start() {
            mAnimator.start()
        }
    }

    private class AnimatorListenerWrapper(private val mAnimator: Animator, private val mListener: Animator.AnimatorListener) : Animator.AnimatorListener {

        override fun onAnimationStart(animator: Animator) {
            mListener.onAnimationStart(mAnimator)
        }

        override fun onAnimationEnd(animator: Animator) {
            mListener.onAnimationEnd(mAnimator)
        }

        override fun onAnimationCancel(animator: Animator) {
            mListener.onAnimationCancel(mAnimator)
        }

        override fun onAnimationRepeat(animator: Animator) {
            mListener.onAnimationRepeat(mAnimator)
        }
    }

    private class NoOverlapView(context: Context) : View(context) {

        override fun hasOverlappingRendering(): Boolean {
            return false
        }
    }
}