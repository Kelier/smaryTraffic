package com.bus.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bus.application.MyApplication;
import com.bus.form.R;
import com.bus.setting.SettingCity;
import com.bus.setting.SettingFeed;
import com.bus.setting.SettingMore;
import com.bus.setting.SettingStar;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bus.form.R.color.cardview_shadow_start_color;


public class BodyMeFragment extends Fragment {
    private Fragment fragment;

    private MyApplication application;
    private ImageView ivUser;
    private TextView my_nick;

    private RelativeLayout person_city;
    private RelativeLayout person_star;
    private RelativeLayout person_feed;
    private RelativeLayout person_setting;

    //版本比较：是否是4.4及以上版本
    final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // 启动Activity请求码
    private static final int CODE_CAMERA_REQUEST = 1;
    private static final int CODE_GALLERY_REQUEST = 0;
    private static final int CODE_RESULT_REQUEST = 2;
    // 权限请求码
    private static final int PERMISSION_REQUEST_CAMERA = 0x100 ;
    private static final int PERMISSION_REQUEST_GALLERY = 0x101 ;

    // 选择头像弹窗
    private PopupWindow popupwindow ;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.activity_body_me_fragment,null);
        fragment = this ;
        application = (MyApplication) getActivity().getApplication();
        changePersoninfo(rootview);
        transferItem(rootview);
        return rootview;
    }

    private void transferItem(View view) {
        person_city= (RelativeLayout) view.findViewById(R.id.person_city);
        person_star= (RelativeLayout) view.findViewById(R.id.person_star);
        person_feed= (RelativeLayout) view.findViewById(R.id.person_feed);
        person_setting= (RelativeLayout) view.findViewById(R.id.person_setting);
        person_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingCity.class));
            }
        });
        person_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingStar.class));
            }
        });
        person_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingFeed.class));
            }
        });
        person_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingMore.class));
            }
        });
    }

    private void changePersoninfo(View rootview) {
        ivUser= (CircleImageView) rootview.findViewById(R.id.person_icon);
        my_nick= (TextView) rootview.findViewById(R.id.person_nick);
        // 判断是否存在用户设置过的头像文件
        if(new File(application.getUserPhoto()).exists()) {
            ivUser.setImageBitmap(BitmapFactory.decodeFile(application.getUserPhoto()));
        }
        else {
            // 本地没有头像
            // 如果当前用户已经登录，需要到服务器上下载头像，显示
        }
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindowView();
            }

        });

        my_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTitleDialog();
            }
        });

    }

    /**
     * 改变昵称
     */
    private void inputTitleDialog() {
        final EditText inputServer = new EditText(getContext());
        inputServer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        inputServer.setBackground(null);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("您的姓名").setIcon(
                R.mipmap.ic_person_black_48dp_down).setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        my_nick.setText(inputName);
                    }
                });
        builder.show();
    }


    /**
     * 初始化并显示头像选择弹窗
     */
    public void showPopupWindowView() {
        if(null == popupwindow) {
            // // 获取PopupWindow布局View
            View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.pop_chooseimg, null);
            // 创建PopupWindow实例
            popupwindow = new PopupWindow(view,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
            popupwindow.setFocusable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            popupwindow.setBackgroundDrawable(dw);
            // 触摸popupwindow外部，popupwindow消失
            popupwindow.setOutsideTouchable(true);


            popupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //popupwindow消失的时候恢复成原来的透明度
                    bgAlpha(1f);
                }
            });
            // 查找按钮设置监听器
            Button btnCamera = (Button) view.findViewById(R.id.popup_btn_camera);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            choseHeadImageFromCameraCapture();
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String CAMERA_PERMISSION = Manifest.permission.CAMERA;
                            if (ContextCompat.checkSelfPermission(getActivity(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                                //授予权限了
                                choseHeadImageFromCameraCapture();
                            } else {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), CAMERA_PERMISSION)) {
                                    //还没有完全禁止
                                    ActivityCompat.requestPermissions(getActivity()

                                            , new String[]{CAMERA_PERMISSION}, PERMISSION_REQUEST_CAMERA);
                                } else {
                                    //完全禁止了
                                    showPermissionDialog();
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //点击空白区域
                    if (popupwindow != null) {
                        popupwindow.dismiss();
                    }
                }
            });


            Button btnGallery = (Button) view.findViewById(R.id.popup_btn_gallery);
            btnGallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        choseHeadImageFromGallery();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String EXTERNAL_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        if (ContextCompat.checkSelfPermission(getContext(), EXTERNAL_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                            //授予权限了
                            choseHeadImageFromGallery();
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), EXTERNAL_PERMISSION)) {
                                //还没有完全禁止
                                ActivityCompat.requestPermissions(getActivity(), new String[]{EXTERNAL_PERMISSION}, PERMISSION_REQUEST_GALLERY);
                            } else {
                                //完全禁止了
                                showPermissionDialog();
                            }
                        }

                    }
                    if (popupwindow != null) {
                        popupwindow.dismiss();
                    }
                }
            });
            Button btnCancel = (Button) view.findViewById(R.id.popup_btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupwindow != null) {
                        popupwindow.dismiss();
                    }
                    return;
                }
            });
        }

        // 在底部显示
        popupwindow.showAtLocation(getView().findViewById(R.id.body_mefrag),
                Gravity.BOTTOM, 0, 0);
        // 窗体半透明
        bgAlpha(0.5f);//0.0-1.0
    }

    /**
     * 如果使用了startActivityForResult，则被启动的Activity回传数据后自动回掉onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                //选择相册返回值
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
//                    File tempFile = new File(application.getTempPhoto());
//                    if(!tempFile.exists())
//                        //创建文件
//                        try {
//                            tempFile.createNewFile();
//                        } catch (IOException e) {
//                            Toast.makeText(getApplication(), "图片保存失败!", Toast.LENGTH_LONG).show();
//                        }
//                    cropRawPhoto(Uri.fromFile(tempFile));
                    cropRawPhoto(Uri.fromFile(new File(application.getTempPhoto())));
                } else {
                    Toast.makeText(getActivity().getApplication(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    //将Uri图片转换为Bitmap
                    try {
                        Bitmap bitmap2 = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uritempFile));
                        setImageToHeadView(bitmap2);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getActivity().getApplication(), "图片剪切失败!", Toast.LENGTH_LONG)
                                .show();
                        e.printStackTrace();
                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    //响应权限请求事件的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA:   // 拍照权限的请求码
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choseHeadImageFromCameraCapture();
                    //点击空白区域
                    if(popupwindow!=null){
                        popupwindow.dismiss();
                    }

                } else {
                    showPermissionDialog();
                }
                break;
            case PERMISSION_REQUEST_GALLERY:   // 使用相册权限的请求码
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choseHeadImageFromGallery();
                    //点击空白区域
                    if(popupwindow!=null){
                        popupwindow.dismiss();
                    }

                } else {
                    showPermissionDialog();
                }
                break;

        }
    }

    // 完全禁止时弹框
    private static final String PACKAGE_URL_SCHEME = "package:";
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("帮助");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
//                setResult(PERMISSIONS_DENIED);

            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }
    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件

        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                .fromFile(new File(application.getTempPhoto())));


        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }
    /**
     * 裁剪原始的图片
     */
    Uri uritempFile ;
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        /*// outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);*/
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.fromFile(new File(application.getTempPhoto()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Bitmap bitmap) {
        if (bitmap != null) {
            ivUser.setImageBitmap(bitmap);
            try {
                saveFile(bitmap , application.getUserPhoto()) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //////////////////////
//            参数bitmap使用设置的头像
//                    将Bitmap转换为File或者OutputStream
//                    上传到服务器
            //////////////////////

        }
    }
    /**
     * 将Bitmap转换成文件
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm,String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
    /**
     * 将Bitmap转换成文件
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm,String fileName) throws IOException {
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    /**
     * 改变窗体透明度方法
     * @param alpha
     */
    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

}
