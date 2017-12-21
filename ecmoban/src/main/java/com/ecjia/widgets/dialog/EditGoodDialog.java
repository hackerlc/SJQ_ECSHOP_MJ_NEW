package com.ecjia.widgets.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ecmoban.android.sijiqing.R;

public class EditGoodDialog {
	private Dialog mDialog;
	public EditText edit;
	public Button negative;
	public Button positive;

	public EditGoodDialog(Context c,String goodnum) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View view = inflater.inflate(R.layout.shopcart_goodnum_edit, null);
		mDialog = new Dialog(c, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		edit = (EditText) view.findViewById(R.id.shop_goods_edit);
		edit.setText(goodnum);
        edit.setSelection(goodnum.length());//将光标移至文字末尾
		negative = (Button) view.findViewById(R.id.shop_goods_cancel);
		positive = (Button) view.findViewById(R.id.shop_goods_ok);

	}

	public void show() {
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}
}
