package com.iamyjx.takesave_picture;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MaskSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	// ����SurfaceHolder����
	private SurfaceHolder mSurfaceHolder;
	// ѭ�����
	private boolean loop = true;
	// ѭ�����
	private static final long TIME = 300;
	// ������
	private int mCount;
	// ���Ʒ�ʽ
	private int mode;

	public MaskSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSurfaceHolder = getHolder(); // ��ȡSurfaceHolder
		mSurfaceHolder.addCallback(this); // ��ӻص�
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT); // ����͸��
	}

	// ��surface����ʱ����
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mode = new Random().nextInt(2); // ���һ��[0-2)��
		new Thread(this).start(); // ��ʼ����
	}

	// ��surface�Ĵ�С�����ı�ʱ����
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	// ��surface����ʱ����
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
	}

	@Override
	public void run() {
		while (loop) {
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (mSurfaceHolder) {
				drawMask();
			}
		}
	}

	/**
	 * �����ɰ�
	 */
	private void drawMask() {
		// �����������õ�canvas
		Canvas mCanvas = mSurfaceHolder.lockCanvas();

		// ����surface���ٺ��̻߳����Խ������
		if (mSurfaceHolder == null || mCanvas == null)
			return;

		int w = mCanvas.getWidth();
		int h = mCanvas.getHeight();

		/* ����һ������ */
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true); // ���ÿ����
		mPaint.setColor(0x00000000); // ����͸����ɫ
		mCanvas.drawRect(0, 0, w, h, mPaint); // �ػ汳��

		setPaintColor(mPaint); // ѭ�����û�����ɫ
		mPaint.setStyle(Paint.Style.STROKE); // ���

		if (0 == mode) {
			drawHeart2D(mCanvas, mPaint, w / 2, h / 2, h / 2); // ��һ��2d����
		} else {
			drawHeart3D(mCanvas, mPaint); // ��һ��3d����
		}

		// ���ƺ���������ƺ�������������ʾ
		mSurfaceHolder.unlockCanvasAndPost(mCanvas);
	}

	/** ��һ��2d���ģ���Բ+sin���ߣ� */
	private void drawHeart2D(Canvas mCanvas, Paint mPaint, int centerX,
			int centerY, float height) {

		float r = height / 4;
		/* ������Բ��㴦 */
		float topX = centerX;
		float topY = centerY - r;

		/* ���ϰ�Բ */
		RectF leftOval = new RectF(topX - 2 * r, topY - r, topX, topY + r);
		mCanvas.drawArc(leftOval, 180f, 180f, false, mPaint);
		/* ���ϰ�Բ */
		RectF rightOval = new RectF(topX, topY - r, topX + 2 * r, topY + r);
		mCanvas.drawArc(rightOval, 180f, 180f, false, mPaint);

		/* �°���sin���� */
		float base = 3 * r;
		double argu = Math.PI / 2 / base;
		float y = base, value;
		while (y >= 0) {
			value = (float) (2 * r * Math.sin(argu * (base - y)));
			mCanvas.drawPoint(topX - value, topY + y, mPaint);
			mCanvas.drawPoint(topX + value, topY + y, mPaint);
			y -= 1;
		}

		// 1�����κ�����x2+(y-3��x2)2=1
		// >> x^2+(y-(x^2)^(1/3))^2=1
		//
		// 2�����εĸ��ֻ�����
		// >> http://woshao.com/article/1a855728bda511e0b40e000c29fa3b3a/
		//
		// 3���ѿ�����������ܡ������κ����Ļ���
		// >> http://www.cssass.com/blog/index.php/2010/808.html
	}

	/** ��һ��3d���� */
	private void drawHeart3D(Canvas mCanvas, Paint mPaint) {

		int w = mCanvas.getWidth();
		int h = mCanvas.getHeight();

		/* ��һ��3d���� */
		int i, j;
		double x, y, r;
		for (i = 0; i <= 90; i++) {
			for (j = 0; j <= 90; j++) {
				r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 20;
				x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i)
						+ w / 2;
				y = -r * Math.sin(Math.PI / 45 * j) + h / 4;
				mCanvas.drawPoint((float) x, (float) y, mPaint);
			}
		}
	}

	/** ѭ�����û�����ɫ */
	private void setPaintColor(Paint mPaint) {
		mCount = mCount < 100 ? mCount + 1 : 0;
		switch (mCount % 6) {
		case 0:
			mPaint.setColor(Color.BLUE);
			break;
		case 1:
			mPaint.setColor(Color.GREEN);
			break;
		case 2:
			mPaint.setColor(Color.RED);
			break;
		case 3:
			mPaint.setColor(Color.YELLOW);
			break;
		case 4:
			mPaint.setColor(Color.argb(255, 255, 181, 216));
			break;
		case 5:
			mPaint.setColor(Color.argb(255, 0, 255, 255));
			break;
		default:
			mPaint.setColor(Color.WHITE);
			break;
		}
	}

}