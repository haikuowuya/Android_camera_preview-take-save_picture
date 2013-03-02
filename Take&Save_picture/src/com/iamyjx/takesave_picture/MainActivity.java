package com.iamyjx.takesave_picture;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	//覆盖创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //菜单选项被选中时
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      switch (item.getItemId()) {
        case R.id.action_settings:
          intent=new Intent(this, SettingActivity.class);
          startActivity(intent);
          break;
        default:
          return super.onOptionsItemSelected(item);
      }
      return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (2 == requestCode) { // 自定义相机返回处理
			// 拍照成功后，响应码是20
			if (resultCode == 20) {
				Bundle bundle = data.getExtras();
				// 获得照片uri
				Uri uri = Uri.parse(bundle.getString("uriStr"));
				// 获得拍照时间
				long dateTaken = bundle.getLong("dateTaken");
				try {
					// 从媒体数据库获取该照片
					Bitmap cameraBitmap = MediaStore.Images.Media.getBitmap(
							getContentResolver(), uri);
					previewBitmap(cameraBitmap); // 预览图像
					// 从媒体数据库删除该照片（按拍照时间）
					getContentResolver().delete(
							CameraActivity.IMAGE_URI,
							MediaStore.Images.Media.DATE_TAKEN + "="
									+ String.valueOf(dateTaken), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// takeBtn2.setClickable(true);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startCamera(View view) {
		// 调用自定义相机
		Intent intent = new Intent(this, CameraActivity.class);
		startActivityForResult(intent, 2);

	}

	private void previewBitmap(Bitmap bitmap) {
		ImageView view = (ImageView) findViewById(R.id.previewBitmap);
		view.setImageBitmap(bitmap);
	}
}
