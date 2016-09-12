package com.delelong.diandian.numberPicker;

import android.view.Gravity;
import android.widget.Toast;

import com.delelong.diandian.utils.MyApp;


public class ToastUtil {

	public static void show(Object obj) {
		if(obj != null){
			Toast toast = Toast.makeText(MyApp.getInstance(), obj.toString(),Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Toast toast = Toast.makeText(MyApp.getInstance(), "null 空字符",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

}
