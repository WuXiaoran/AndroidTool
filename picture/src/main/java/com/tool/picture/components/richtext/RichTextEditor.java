package com.tool.picture.components.richtext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tool.picture.R;
import com.tool.picture.utils.Util;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/22 9:35
 * @描述          编辑富文本
 **/
public class RichTextEditor extends RichText {

	private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
	private OnKeyListener keyListener; // 所有EditText的软键盘监听器
	private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
	private EditText lastFocusEdit; // 最近被聚焦的EditText
	private int editNormalPadding = 0; // 添加的edittext的左右边距

	public RichTextEditor(Context context) {
		this(context, null);
	}

	public RichTextEditor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 初始化键盘退格监听
		// 主要用来处理点击回删按钮时，view的一些列合并操作
		keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
					EditText edit = (EditText) v;
					onBackspacePress(edit);
				}
				return false;
			}
		};

		// 输入框焦点获取
		focusListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					lastFocusEdit = (EditText) v;
				}
			}
		};

		// 创建首个输入框
		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		EditText firstEdit = createEditText(getDefaultText(), Util.dp2px(context, EDIT_PADDING));
		allLayout.addView(firstEdit, firstEditParam);
		lastFocusEdit = firstEdit;
	}

	/**
	 * 处理软键盘backSpace回退事件
	 * @param editTxt		光标所在的文本输入框
	 */
	private void onBackspacePress(EditText editTxt) {
		int startSelection = editTxt.getSelectionStart();
		// 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
		if (startSelection == 0) {
			int editIndex = allLayout.indexOfChild(editTxt);
			View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex - 1 < 0,则返回的是null
			if (null != preView) {
				if (preView instanceof RelativeLayout) {
					// 光标EditText的上一个view对应的是图片
					onImageCloseClick(preView);
				} else if (preView instanceof EditText) {
					// 光标EditText的上一个view对应的还是文本框EditText
					String str1 = editTxt.getText().toString();
					EditText preEdit = (EditText) preView;
					String str2 = preEdit.getText().toString();
					allLayout.removeView(editTxt);
					// 文本合并
					preEdit.setText(str2 + str1);
					preEdit.requestFocus();
					preEdit.setSelection(str2.length(), str2.length());
					lastFocusEdit = preEdit;
				}
			}
		}
	}

	@Override
	protected void onImageCloseClick(View view) {
		super.onImageCloseClick(view);
		mergeEditText(); // 合并上下EditText内容
	}

	/**
	 * 生成文本输入框
	 * @param hint			提示文本
	 * @param paddingTop	内边距高度
	 * @return				新的空输入框
	 */
	public EditText createEditText(String hint, int paddingTop) {
		EditText editText = (EditText) inflater.inflate(R.layout.tool_rich_edittext, null);
		editText.setOnKeyListener(keyListener);
		editText.setTag(viewTagIndex++);
		editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
		editText.setHint(hint);
		editText.setOnFocusChangeListener(focusListener);
		return editText;
	}

	/**
	 * 插入一张图片
	 * @param imagePath	图片路径
	 * @param type			图片具体类型，可能是图片或者是视频的首帧
	 */
	public void insertImage(String imagePath,String type) {
		insertImage(imagePath,type,0,0);
	}

	/**
	 * 插入一张图片
	 * @param imagePath	图片路径
	 * @param type			图片具体类型，可能是图片或者是视频的首帧
	 * @param width 		预设宽度
	 * @param height 		预设高度
	 */
	public void insertImage(String imagePath,String type,int width,int height) {
		// lastFocusEdit获取焦点的EditText
		String lastEditStr = lastFocusEdit.getText().toString();
		int cursorIndex = lastFocusEdit.getSelectionStart(); // 获取光标所在位置
		String editStr1 = lastEditStr.substring(0, cursorIndex).trim(); // 获取光标前面的字符串
		String editStr2 = lastEditStr.substring(cursorIndex).trim(); // 获取光标后的字符串
		int lastEditIndex = allLayout.indexOfChild(lastFocusEdit); // 获取焦点的EditText所在位置
		if (lastEditStr.length() == 0) {
			// 如果当前获取焦点的EditText为空，直接在EditText下方插入图片，并且在图片后面插入空的EditText
			addEditTextAtIndex(lastEditIndex + 1, "");
			addImageViewAtIndex(lastEditIndex + 1, imagePath,type,width,height);
		} else if (editStr1.length() == 0) {
			// 如果光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
			addImageViewAtIndex(lastEditIndex, imagePath,type,width,height);
			// 同时插入一个空的EditText，防止插入多张图片无法写文字
			addEditTextAtIndex(lastEditIndex + 1, "");
		} else if (editStr2.length() == 0) {
			// 如果光标已经顶在了editText的最末端，则需要添加新的imageView和EditText
			addEditTextAtIndex(lastEditIndex + 1, "");
			addImageViewAtIndex(lastEditIndex + 1, imagePath,type,width,height);
		} else {
			// 如果光标已经顶在了editText的最中间，则需要分割字符串，分割成两个EditText，并在两个EditText中间插入图片
			// 把光标前面的字符串保留，设置给当前获得焦点的EditText（此为分割出来的第一个EditText）
			lastFocusEdit.setText(editStr1);
			// 把光标后面的字符串放在新创建的EditText中（此为分割出来的第二个EditText）
			addEditTextAtIndex(lastEditIndex + 1, editStr2);
			// 在第二个EditText的位置插入一个空的EditText，以便连续插入多张图片时，有空间写文字，第二个EditText下移
			addEditTextAtIndex(lastEditIndex + 1, "");
			// 在空的EditText的位置插入图片布局，空的EditText下移
			addImageViewAtIndex(lastEditIndex + 1, imagePath,type,width,height);
		}
		hideKeyBoard();
	}

	/**
	 * 单纯插入图片，多用于视频文章上传。通常会清空所有视图，直接添加一张图片
	 * @param imagePath	图片路径
	 * @param type			图片具体类型，可能是图片或者是视频的首帧
	 */
	public void insertSimpleImage(String imagePath,String type) {
		insertSimpleImage(imagePath,type,0,0);
	}

	/**
	 * 单纯插入图片，多用于视频文章上传。通常会清空所有视图，直接添加一张图片
	 * @param imagePath	图片路径
	 * @param type			图片具体类型，可能是图片或者是视频的首帧
	 * @param width 		预设宽度
	 * @param height 		预设高度
	 */
	public void insertSimpleImage(String imagePath,String type,int width,int height) {
		clearAllLayout();
		addImageViewAtIndex(0,imagePath,type,width,height);
		hideKeyBoard();
	}

	/**
	 * 隐藏小键盘
	 */
	public void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
	}

	/**
	 * 在特定位置插入EditText
	 * @param index		位置
	 * @param editStr		EditText显示的文字
	 */
	public void addEditTextAtIndex(final int index, CharSequence editStr) {
		EditText editText = createEditText(getAddText(), Util.dp2px(activity,EDIT_PADDING));
		// 判断插入的字符串是否为空，如果没有内容则显示hint提示信息
		if (editStr != null && editStr.length() > 0){
			editText.setText(editStr);
		}
		// 创建的editText都会执行焦点回调，确保每次获取焦点的时候都能将lastFocusEdit更新
		editText.setOnFocusChangeListener(focusListener);
		allLayout.addView(editText, index);
		// 插入新的EditText之后，修改lastFocusEdit的指向
		lastFocusEdit = editText;
		lastFocusEdit.requestFocus();
		lastFocusEdit.setSelection(editStr.length(), editStr.length());
	}

	/**
	 * 图片删除的时候，如果上下方都是EditText，则合并处理
	 */
	private void mergeEditText() {
		View preView = allLayout.getChildAt(disappearingImageIndex - 1);
		View nextView = allLayout.getChildAt(disappearingImageIndex);
		if (preView != null && preView instanceof EditText && null != nextView
				&& nextView instanceof EditText) {
			EditText preEdit = (EditText) preView;
			EditText nextEdit = (EditText) nextView;
			String str1 = preEdit.getText().toString();
			String str2 = nextEdit.getText().toString();
			String mergeText = "";
			if (str2.length() > 0) {
				mergeText = str1 + "\n" + str2;
			} else {
				mergeText = str1;
			}
			allLayout.setLayoutTransition(null);
			allLayout.removeView(nextEdit);
			preEdit.setText(mergeText);
			preEdit.requestFocus();
			preEdit.setSelection(str1.length(), str1.length());
		}
	}

	public EditText getLastFocusEdit() {
		return lastFocusEdit;
	}

	public void setLastFocusEdit(EditText lastFocusEdit) {
		this.lastFocusEdit = lastFocusEdit;
	}

}
