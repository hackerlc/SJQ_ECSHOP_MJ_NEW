package com.ecjia.view.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.widgets.dialog.EditPictureDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.LG;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.consts.ProtocolConst;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class CommentCreateActivity extends BaseActivity implements OnClickListener, HttpResponse {

    private TextView title;
    private ImageView back;
    private String rec_id;
    private CommentModel commentModel;

    private EditText comment_edit;
    private RatingBar comment_ratingbar;
    private TextView comment_send;
    private Button comment_checkbox;
    private ImageView comment_goods_img;
    private TextView comment_goods_title;
    private TextView comment_goods_price;
    private boolean isChecked = false;
    private int intentType = 0;
    private int is_anonymous = 0;
    private LinearLayout ll_comment_create_bottom;

    private TextView close_keyboardView;

    private static final int REQUEST_CODE_CAMERO = 731;//相机
    private static final int REQUEST_CODE_PHOTOS = 732;//相册

    private File tempfile;
    String mDirectoryname;

    private ImageView iv_upload1, iv_upload2, iv_upload3, iv_upload4, iv_upload5;
    private TextView tv_upload1, tv_upload2, tv_upload3, tv_upload4, tv_upload5,tv_first_upload;
    private ImageView iv_del_pic1, iv_del_pic2, iv_del_pic3, iv_del_pic4, iv_del_pic5;
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> delImgViews = new ArrayList<ImageView>();
    private ArrayList<TextView> textViews = new ArrayList<TextView>();
    private EditPictureDialog dialog;
    private String fileName;
    private ArrayList<Bitmap> bmlist = new ArrayList<Bitmap>();
    private ArrayList<String> pathlist = new ArrayList<String>();
    private ArrayList<String> pathkeylist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_create);
        PushAgent.getInstance(this).onAppStart();

        initCameroPath();

        Intent intent = getIntent();

        rec_id = intent.getStringExtra("rec_id");
        intentType = intent.getIntExtra("type", 0);
        LG.i("rec_id========" + rec_id);
        LG.i("intentType========" + intentType);
        commentModel = new CommentModel(this);
        commentModel.addResponseListener(this);
        initView();
        if (intentType == 2 || intentType == 0) {
            commentModel.getOrderCommentsDetail(rec_id);
        }
    }

    public void initView() {
        title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = getBaseContext().getResources();
        title.setText(resource.getString(R.string.gooddetail_commit));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                finish();
            }
        });
        close_keyboardView = (TextView) findViewById(R.id.comment_create_close_keyboard);
        close_keyboardView.setOnClickListener(this);
        comment_edit = (EditText) findViewById(R.id.comment_edit);
        comment_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    close_keyboardView.setVisibility(View.VISIBLE);
                    close_keyboardView.setEnabled(true);
                } else {
                    close_keyboardView.setVisibility(View.GONE);
                    close_keyboardView.setEnabled(false);

                }
            }
        });
        comment_ratingbar = (RatingBar) findViewById(R.id.comment_ratingbar);
        comment_send = (TextView) findViewById(R.id.comment_send);
        comment_checkbox = (Button) findViewById(R.id.comment_checkbox);
        comment_goods_img = (ImageView) findViewById(R.id.comment_goods_img);
        comment_goods_title = (TextView) findViewById(R.id.comment_goods_title);
        comment_goods_price = (TextView) findViewById(R.id.comment_goods_price);

        ll_comment_create_bottom = (LinearLayout) findViewById(R.id.ll_comment_create_bottom);

        comment_goods_title.setText(getIntent().getStringExtra("goods_name"));

        if(FormatUtil.formatStrToFloat(getIntent().getStringExtra("goods_price"))==0){
            comment_goods_price.setText("免费");
        }else{
            comment_goods_price.setText(getIntent().getStringExtra("goods_price"));
        }

        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("goods_img"), comment_goods_img);
        comment_send.setOnClickListener(this);
        comment_checkbox.setOnClickListener(this);

        tv_first_upload = (TextView) findViewById(R.id.tv_first_upload);

        iv_upload1 = (ImageView) findViewById(R.id.iv_upload1);
        tv_upload1 = (TextView) findViewById(R.id.tv_upload1);
        iv_del_pic1 = (ImageView) findViewById(R.id.iv_del_pic1);
        tv_upload1.setOnClickListener(this);
        iv_del_pic1.setOnClickListener(this);
        imageViews.add(iv_upload1);
        textViews.add(tv_upload1);
        delImgViews.add(iv_del_pic1);

        iv_upload2 = (ImageView) findViewById(R.id.iv_upload2);
        tv_upload2 = (TextView) findViewById(R.id.tv_upload2);
        iv_del_pic2 = (ImageView) findViewById(R.id.iv_del_pic2);
        tv_upload2.setOnClickListener(this);
        iv_del_pic2.setOnClickListener(this);
        imageViews.add(iv_upload2);
        textViews.add(tv_upload2);
        delImgViews.add(iv_del_pic2);

        iv_upload3 = (ImageView) findViewById(R.id.iv_upload3);
        tv_upload3 = (TextView) findViewById(R.id.tv_upload3);
        iv_del_pic3 = (ImageView) findViewById(R.id.iv_del_pic3);
        tv_upload3.setOnClickListener(this);
        iv_del_pic3.setOnClickListener(this);
        imageViews.add(iv_upload3);
        textViews.add(tv_upload3);
        delImgViews.add(iv_del_pic3);

        iv_upload4 = (ImageView) findViewById(R.id.iv_upload4);
        tv_upload4 = (TextView) findViewById(R.id.tv_upload4);
        iv_del_pic4 = (ImageView) findViewById(R.id.iv_del_pic4);
        tv_upload4.setOnClickListener(this);
        iv_del_pic4.setOnClickListener(this);
        imageViews.add(iv_upload4);
        textViews.add(tv_upload4);
        delImgViews.add(iv_del_pic4);

        iv_upload5 = (ImageView) findViewById(R.id.iv_upload5);
        tv_upload5 = (TextView) findViewById(R.id.tv_upload5);
        iv_del_pic5 = (ImageView) findViewById(R.id.iv_del_pic5);
        tv_upload5.setOnClickListener(this);
        iv_del_pic5.setOnClickListener(this);
        imageViews.add(iv_upload5);
        textViews.add(tv_upload5);
        delImgViews.add(iv_del_pic5);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void sendComment() {
        String comment = comment_edit.getText().toString();
        int rank = (int) comment_ratingbar.getRating();
        if (rank == 0) {
            ToastView toast = new ToastView(CommentCreateActivity.this, R.string.comment_toast_no_star);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (comment.trim().isEmpty()) {
                ToastView toast = new ToastView(CommentCreateActivity.this, R.string.comment_toast_no_content);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                comment_edit.setText("");
            } else {

                if (isChecked) {
                    is_anonymous = 1;
                } else {
                    is_anonymous = 0;
                }
                commentModel.createComments(rec_id, comment, rank, is_anonymous,pathkeylist,pathlist);
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.comment_send:
                sendComment();
                break;
            case R.id.comment_checkbox:
                changCheck(isChecked);
                break;
            case R.id.top_view_back:
                finish();
                break;
            case R.id.comment_create_close_keyboard:
                closeKeyBoard();
                break;
            case R.id.tv_upload1:
                upload(1);
                break;
            case R.id.iv_del_pic1:
                delView(1);
                break;
            case R.id.tv_upload2:
                upload(2);
                break;
            case R.id.iv_del_pic2:
                delView(2);
                break;
            case R.id.tv_upload3:
                upload(3);
                break;
            case R.id.iv_del_pic3:
                delView(3);
                break;
            case R.id.tv_upload4:
                upload(4);
                break;
            case R.id.iv_del_pic4:
                delView(4);
                break;
            case R.id.tv_upload5:
                upload(5);
                break;
            case R.id.iv_del_pic5:
                delView(5);
                break;
        }

    }

    private void upload(int i) {
        dialog = new EditPictureDialog(this);
        dialog.ll_title.setVisibility(View.VISIBLE);
        dialog.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromCapture();
                dialog.dismiss();
            }
        });
        dialog.fromPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromPhotos();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 获取相册
     */
    private void getPictureFromPhotos() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PHOTOS);
    }


    /**
     * 初始化存储目录
     */
    void initCameroPath() {
        //获取根目录
        File DatalDir = Environment.getExternalStorageDirectory();

        //创建相册
        File myDir = new File(DatalDir, AndroidManager.PICTURE_CACHE);
        myDir.mkdirs();

        //相册的目录地址
        mDirectoryname = DatalDir.toString() + AndroidManager.PICTURE_CACHE;
    }

    /**
     * 拍照
     */
    public void getPictureFromCapture() {

        //存储文件的名称
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date());

        //文件名规则 id_20160831_155926_order_comment.jpg
        fileName = rec_id + "_" + time + "_order_comment.jpg";
        tempfile = new File(mDirectoryname, fileName);
        if (tempfile.isFile()) {
            tempfile.delete();
        }
        Uri image_uri = Uri.fromFile(tempfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERO);
    }


    private void delView(int position) {
        int size = bmlist.size();
        if (position <= size) {
            bmlist.remove(position - 1);
            pathlist.remove(position - 1);
            pathkeylist.remove(position - 1);
        }
        addView();
        for (int i = imageViews.size() - 1; i > bmlist.size() - 1; i--) {
            imageViews.get(i).setVisibility(View.INVISIBLE);
            delImgViews.get(i).setVisibility(View.INVISIBLE);
            if (i == 0) {
                imageViews.get(i).setVisibility(View.INVISIBLE);
                delImgViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(i).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(i).setVisibility(View.INVISIBLE);
                delImgViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(bmlist.size()).setVisibility(View.VISIBLE);
            }
        }
    }

    private void addView() {
        int size = bmlist.size();
        for (int i = 0; i < size; i++) {
            imageViews.get(i).setImageBitmap(bmlist.get(i));
            imageViews.get(i).setVisibility(View.VISIBLE);
            delImgViews.get(i).setVisibility(View.VISIBLE);
            textViews.get(i).setVisibility(View.INVISIBLE);
        }
        if (size < textViews.size()) {
            textViews.get(size).setVisibility(View.VISIBLE);
        }
        if(size==0){
            tv_first_upload.setVisibility(View.VISIBLE);
        }else{
            tv_first_upload.setVisibility(View.GONE);
        }
    }

    public void changCheck(boolean isChecked) {
        if (isChecked) {
            comment_checkbox.setBackgroundResource(R.drawable.comment_checkbox_false);
            this.isChecked = false;
        } else {
            comment_checkbox.setBackgroundResource(R.drawable.comment_checkbox_true);
            this.isChecked = true;
        }

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url == ProtocolConst.COMMENTS_CREATE) {
            if (status.getSucceed() == 1) {
                Event event = new MyEvent("comment_succeed");
                EventBus.getDefault().post(event);
                ToastView toast = new ToastView(CommentCreateActivity.this, R.string.comment_create_succeed);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(1000);
                toast.show();
                closeKeyBoard();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                ToastView toast = new ToastView(CommentCreateActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(1000);
                toast.show();
            }
        } else if (url == ProtocolConst.ORDERS_COMMENT_DETAIL) {
            if (status.getSucceed() == 1) {
                comment_edit.setText(commentModel.comment_content);
                comment_edit.setEnabled(false);
                comment_ratingbar.setRating(Float.valueOf(commentModel.comment_goods));
                comment_ratingbar.setIsIndicator(true);

                if (commentModel.comment_pic_list.size() > 0) {
                    for (int i = 0; i < commentModel.comment_pic_list.size(); i++) {
                        imageViews.get(i).setVisibility(View.VISIBLE);
                        imageViews.get(i).setClickable(false);
                        textViews.get(i).setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(commentModel.comment_pic_list.get(i), imageViews.get(i));
                    }
                    ll_comment_create_bottom.setVisibility(View.GONE);
                    tv_first_upload.setVisibility(View.GONE);
                } else {

                }
            } else {
                ToastView toast = new ToastView(CommentCreateActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(1000);
                toast.show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTOS && resultCode == RESULT_OK) {//获取相册的照片
            Uri originalUri = data.getData();        //获得图片的uri
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是Android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            String imageFilePath = cursor.getString(column_index);
            dealUploadPic(imageFilePath);

        } else if (requestCode == REQUEST_CODE_CAMERO && resultCode == RESULT_OK) {
            String imageFilePath = tempfile.getPath();
            dealUploadPic(imageFilePath);
        }

    }

    // 关闭键盘

    private void dealUploadPic(String imageFilePath) {
        int width = 300;
        int height = 300;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//设置解码只是为了获取图片的width和height值,而不是真正获取图片
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);//解码后可以options.outWidth和options.outHeight来获取图片的尺寸
        int widthRatio = (int) Math.ceil(options.outWidth / width);//获取宽度的压缩比率
        int heightRatio = (int) Math.ceil(options.outHeight / height);//获取高度的压缩比率

        if (widthRatio > 1 || heightRatio > 1) {//只要其中一个的比率大于1,说明需要压缩
            if (widthRatio >= heightRatio) {//取options.inSampleSize为宽高比率中的最大值
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        options.inJustDecodeBounds = false;//设置为真正的解码图片
        bitmap = BitmapFactory.decodeFile(imageFilePath, options);//解码图片
        bmlist.add(bitmap);
        String key="image_" + pathlist.size();
        commentModel.saveBitmap(mDirectoryname, key+".jpg", bitmap);
        pathkeylist.add(key);
        pathlist.add( mDirectoryname+"/"+key+".jpg");

        addView();
    }

    public void closeKeyBoard() {
        comment_edit.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment_edit.getWindowToken(), 0);
    }
}
