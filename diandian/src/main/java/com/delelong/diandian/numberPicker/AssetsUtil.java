package com.delelong.diandian.numberPicker;

import android.content.Context;

import java.io.InputStream;

public class AssetsUtil {
	
	// 从assets 文件夹中获取文件并读取数据
	public static String getFromAssets(Context context,String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = new String(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
