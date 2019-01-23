package com.tool.picture.components.richtext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.picture.R;
import com.tool.picture.utils.Util;

import java.util.List;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/22 9:35
 * @描述          预览富文本
 **/
public class RichTextView extends RichText {

	private static final int TEXT_PADDING = 10; // textview常规padding是10dp
	private TextView lastFocusText; // 最近被聚焦的TextView
	private int editNormalPadding = 0; // 添加的edittext的左右边距

	public RichTextView(Context context) {
		this(context, null);
	}

	public RichTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 创建首个文本
		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		TextView firstText = createTextView(getEmptyText(), Util.dp2px(context, TEXT_PADDING));
		allLayout.addView(firstText, firstEditParam);
		lastFocusText = firstText;
	}

	/**
	 * 在特定位置插入TextView
	 * @param index		位置
	 * @param editStr		TextView显示的文字
	 */
	private void addTextViewAtIndex(final int index, CharSequence editStr) {
		TextView textView = createTextView("", Util.dp2px(activity,TEXT_PADDING));
		textView.setText(editStr);
		allLayout.addView(textView, index);
	}

	/**
	 * 生成文本输入框
	 * @param hint				提示语
	 * @param paddingTop		上边距
	 * @return
	 */
	private TextView createTextView(String hint, int paddingTop) {
		TextView textView = (TextView) inflater.inflate(R.layout.tool_rich_textview, null);
		textView.setTag(viewTagIndex++);
		textView.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
		textView.setHint(hint);
		return textView;
	}

	/**
	 * 外部调用，设置所有view的数据
	 * @param data		view的数据，格式必须一一对应
	 */
	public void setViewData(List<ViewData> data) {
		for (ViewData datum : data) {
			if (TEXT.equals(datum.getType()) || EDIT.equals(datum.getType())){
				addTextViewAtIndex(-1,datum.getInputStr());
			}else if (IMAGE.equals(datum.getType()) || VIDEO.equals(datum.getType())){
				addImageViewAtIndex(-1,datum.getPath(),datum.getType(),datum.getResourceWidth(),datum.getResourceHeight());
			}
		}
	}

	public TextView getLastFocusEdit() {
		return lastFocusText;
	}

	public void setLastFocusEdit(TextView lastFocusText) {
		this.lastFocusText = lastFocusText;
	}

}
