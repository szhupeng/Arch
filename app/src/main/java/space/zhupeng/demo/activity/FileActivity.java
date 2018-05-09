package space.zhupeng.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import space.zhupeng.arch.activity.BaseActivity;
import space.zhupeng.arch.utils.FileProviderCompat;
import space.zhupeng.arch.widget.dialog.UpgradeDialog;
import space.zhupeng.demo.R;

public class FileActivity extends BaseActivity {

    private ImageView ivPic;
    private Uri mOutPut;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_file;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        findViewById(R.id.btn_take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeDialog.Builder builder = new UpgradeDialog.Builder(FileActivity.this);
                builder.setDownloadUrl("http://olg4na3ji.bkt.clouddn.com/B27E5E4BCFECC71E_1525673551003_3.6.4.apk")
                        .setAppPath("haha.apk")
                        .setDownloadInBack(false)
                        .setUpgradeLog("哈哈哈")
                        .build().show(getSupportFragmentManager());
            }
        });

        ivPic = findViewById(R.id.iv_pic);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
    }

    public void openCamera() {
        final File output = getOutputUri();
        mOutPut = FileProviderCompat.getUriForFile(getContext(), output);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FileProviderCompat.grantPermissions(getContext(), intent, mOutPut, true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPut); //将拍取的照片保存到指定URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 100);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, 200);
        } catch (Exception e) {
            intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, 200);
        }
    }

    public File getOutputUri() {
        File mediaStorageDir = new File(getFilesDir(), "xgs");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final File file = new File(mediaStorageDir, "IMG_" + timeStamp + ".jpg");

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (100 == requestCode && RESULT_OK == resultCode) {
            try {
                Uri uri = data.getData();
                String path = FileProviderCompat.getPathWithUri(getContext(), uri);
                Glide.with(this).load(path).into(ivPic);
            } catch (Exception e) {
                String path = FileProviderCompat.getPathWithUri(getContext(), mOutPut);
                Glide.with(this).load(mOutPut).into(ivPic);
            }
        } else if (200 == requestCode && RESULT_OK == resultCode) {
            Uri uri = data.getData();
            String path = FileProviderCompat.getPathWithUri(getContext(), uri);
            Glide.with(this).load(path).into(ivPic);
        }
    }
}
