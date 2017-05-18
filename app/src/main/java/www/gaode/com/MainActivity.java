package www.gaode.com;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainAc";
    @ViewInject(R.id.take_pic)
    private Button take_pic;
    @ViewInject(R.id.choice_pic)
    private Button choice_pic;
    private Uri image_uri;
    @ViewInject(R.id.action_image)
    private ImageView action_image;
    private FileOutputStream fileout;

    private static final int TACK_PHONTO = 1;
    private static final int SELETE_PHONTO = 2;

    @Event(type = View.OnClickListener.class, value = R.id.choice_pic)
    private void clickChoicePic(View v) {
        List<PermissonItem> pelist = new ArrayList<>();
        pelist.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写内存卡", R.mipmap.ic_launcher));
        HiPermission.create(this)
                .permissions(pelist)
                .style(R.style.PermissionBlueStyle)
                .title("请求权限")
                .msg("请给予权限不会滥用为了让你方便")
                .filterColor(ContextCompat.getColor(this, R.color.permissionColorGreen))
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {
                        Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                        in.setType("image/*");
                        startActivityForResult(in, SELETE_PHONTO);
                    }

                    @Override
                    public void onDeny(String s, int i) {

                    }

                    @Override
                    public void onGuarantee(String s, int i) {

                    }
                });
    }

    @Event(type = View.OnClickListener.class, value = R.id.take_pic)
    private void clickTakePic(View v) {
        List<PermissonItem> permissionList = new ArrayList<>();
        permissionList.add(new PermissonItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        HiPermission.create(MainActivity.this)
                .permissions(permissionList)
                .style(R.style.PermissionBlueStyle)
                .title("请求权限")
                .msg("请给予权限不会滥用为了让你方便")
                .filterColor(ContextCompat.getColor(this, R.color.permissionColorGreen))
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.e(TAG, "onClose: ");
                    }

                    @Override
                    public void onFinish() {
                        Log.e(TAG, "onFinish: ");
                        File outputpic = new File(getExternalCacheDir(), "out_img.jpg");
                        try {
                            if (outputpic.exists()) {
                                outputpic.delete();
                            }
                            outputpic.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            image_uri = FileProvider.getUriForFile(MainActivity.this, "com.gaode.www.iamge", outputpic);
                        } else {
                            image_uri = Uri.fromFile(outputpic);
                        }
                        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        in.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                        startActivityForResult(in, TACK_PHONTO);
                    }

                    @Override
                    public void onDeny(String s, int i) {
                        Log.e(TAG, "onDeny: ");
                    }

                    @Override
                    public void onGuarantee(String s, int i) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //这是相机
            case TACK_PHONTO:
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(image_uri));
                    Log.e(TAG, "onActivityResult: " + image_uri.getPath() + " " + bitmap.getByteCount());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                File imagefile = new File(getExternalCacheDir() + "/takeph.jpg");
                if (imagefile.exists()) {
                    imagefile.delete();
                }
                try {
                    imagefile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileout = new FileOutputStream(imagefile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileout);

                    File temp_file = new File(getExternalCacheDir() + "/tmp.jpg");
                    if (temp_file.exists()) {
                        temp_file.delete();
                    }
                    try {
                        temp_file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PicUtile.createCompressBitmap(imagefile.getAbsolutePath(), temp_file);
                    action_image.setImageDrawable(Drawable.createFromPath(temp_file.getAbsolutePath()));
                    fileout.flush();
                    fileout.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //插入手机相册有问题
//                try {
//                    MediaStore.Images.Media.insertImage(getContentResolver(), imagefile.getPath(), imagefile.getName(), null);//图片插入到系统图库
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, image_uri));//通知图库刷新


                //如果要进行压缩则用picutile,
//                PicUtile.createCompressBitmap(image_uri.getPath(), imagefile);


                break;
            case SELETE_PHONTO:
                if (data != null) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleimagekit(data);
                    } else {
                        handleimage(data);
                    }
                }

                break;
            default:
                return;
        }
    }

    @TargetApi(19)
    public void handleimagekit(Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            Log.e("getDocumentId(uri) :", "" + docId);
            Log.e("uri.getAuthority() :", "" + uri.getAuthority());
            //当uri是document的时候系统对id进行了封装所以需要进行拆分
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果返回的content就用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file则通过getpath直接获取
            imagePath = uri.getPath();
        }
        action_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }

    public void handleimage(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        action_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }


    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cusor = getContentResolver().query(uri, null, selection, null, null);
        if (cusor != null) {
            if (cusor.moveToFirst()) {
                path = cusor.getString(cusor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cusor.close();
        }
        return path;
    }

}
