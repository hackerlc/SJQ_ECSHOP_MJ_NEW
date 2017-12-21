package com.ecjia.view.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RedPaperModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.RedPaperDetailDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class AddRedpaperActivity extends BaseActivity implements HttpResponse, RedPaperDetailDialog
        .OnButtonClickListener {
    private EditText et_input;
    private Button redpaper_add;
    private RedPaperModel redPaperModel;
    private RedPaperDetailDialog redPaperDetailDialog;
    private LinearLayout root_view;
    private View buttom_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_redpaper);
        EventBus.getDefault().register(this);
        initView();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            et_input.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    };

    private void initView() {
        initTopView();
        root_view = (LinearLayout) findViewById(R.id.root_view);
        buttom_view = findViewById(R.id.buttom_view);
        redPaperModel = new RedPaperModel(this);
        redPaperModel.addResponseListener(this);

        et_input = (EditText) findViewById(R.id.add_red_paper_input);
        redpaper_add = (Button) findViewById(R.id.add_redpaper_ok);
        redpaper_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_input.getText().toString())) {
                    ToastView toastView = new ToastView(AddRedpaperActivity.this, res.getString(R.string.num_null));
                    toastView.setGravity(Gravity.CENTER,0,0);
                    toastView.show();
                } else {
                    CloseKeyBoard();
                    redPaperModel.getBonusValidate(et_input.getText().toString());
                }
            }
        });
        redPaperDetailDialog = new RedPaperDetailDialog(this);
        redPaperDetailDialog.setOnItemClickListener(this);
        controlKeyboardLayout(root_view, buttom_view);

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.BONUS_VALIDATE) {
            if (status.getSucceed() == 1) {
                redPaperDetailDialog.bonusinfo = redPaperModel.bonusinfo;
                redPaperDetailDialog.setDataByType(2);
                redPaperDetailDialog.show();
            } else {
                redPaperDetailDialog.setDataByType(1);
                redPaperDetailDialog.show();
            }
        } else if (url == ProtocolConst.BONUS_BIND) {
            if (status.getSucceed() == 1) {
                redPaperDetailDialog.setDataByType(3);
            }
        }
    }

    @Override
    public void initTopView() {
        topView = (ECJiaTopView) findViewById(R.id.addredpaper_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_GONE);
        topView.setTitleText(R.string.mypurse_add_redpper);
    }

    @Override
    public void onButtonItemClick(View v, int type) {
        if (type == 1) {
            handler.sendMessageDelayed(new Message(), 200);
        } else if (type == 2) {
            redPaperModel.bindBonus(et_input.getText().toString());
        } else if (type == 3) {
            et_input.setText("");
            EventBus.getDefault().post(new MyEvent("red_paper_refresh"));
        }
    }

    public void onEvent(MyEvent event) {


    }

    // 关闭键盘
    public void CloseKeyBoard() {
        et_input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

}
