package com.feisukj.base.baseclass;

import android.os.Environment;
import com.feisukj.base.BaseApplication;
import java.io.File;

public class FileAccessor {

	public static final String TAG = FileAccessor.class.getName();
	public static String EXTERNAL_STOREPATH = getExternalStorePath();


	public static final String All_ROOT_DIR = getExternalStorePath()
			+ "/feisu";

	public static final String APP_ROOT_DIR = getExternalStorePath()
			+ "/feisu/project";

	public static final String Image_Root_Dir = getExternalStorePath()
			+ "/feisu/project/image";

	public static final String LOCAL_PATH = APP_ROOT_DIR + "/config.txt";

	public static final String Apk_File = getExternalStorePath()
			+ "/feisu/project/apk";

	public static final String Video_Download = getExternalStorePath()
			+ "/feisu/project/video_down";


	public static final String Image_Download = getExternalStorePath()
			+ "/feisu/project/image_down";

	public static final String File_Down = getExternalStorePath()
			+ "/feisu/project/file_down";

	public static final String Log_Crash = getExternalStorePath()
			+ "/feisu/project/crash";

	/**
	 * 初始化应用文件夹目录
	 */
	public static void initFileAccess() {

		
		File allRootDir = new File(All_ROOT_DIR);
		if (!allRootDir.exists()) {
			allRootDir.mkdir();
		}
		
		
		File rootDir = new File(APP_ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}

		

		File imageRootDir = new File(Image_Root_Dir);
		if (!imageRootDir.exists()) {
			imageRootDir.mkdir();
		}



		File imgDownDir = new File(Image_Download);
		if (!imgDownDir.exists()) {
			imgDownDir.mkdir();
		}

		File videoDownDir = new File(Video_Download);
		if (!videoDownDir.exists()) {
			videoDownDir.mkdir();
		}

		File apkDir = new File(Apk_File);
		if (!apkDir.exists()) {
			apkDir.mkdir();
		}

		File fileDownDir = new File(File_Down);
		if (!fileDownDir.exists()) {
			fileDownDir.mkdir();
		}

		File logDir = new File(Log_Crash);
		if (!logDir.exists()) {
			logDir.mkdir();
		}

	}

	/**
	 * 是否有外存卡
	 *
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 外置存储卡的路径
	 * 
	 * @return
	 */
	public static String getExternalStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	/**
	 * 图片公用路径
	 * */
	public static String getExternalPublicImageStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES).getAbsolutePath();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static String getAppContextPath() {
		return BaseApplication.getApplication().getFilesDir().getAbsolutePath();
	}


	/**
	 * 删除一个文件夹以及其中所有文件
	 * @param dir
	 */
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			int size = 0;
			if (children != null) {
				size = children.length;
				for (int i = 0; i < size; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
		}
		if (dir == null) {
			return true;
		} else {
			return dir.delete();
		}
	}

	/**删除一个文件*/
	public static boolean delFile(String filePath) {
		if (filePath.isEmpty())return true;
		File file = new File(filePath);
		if (!file.exists()) {
			return true;
		}
		return file.delete();
	}

}
