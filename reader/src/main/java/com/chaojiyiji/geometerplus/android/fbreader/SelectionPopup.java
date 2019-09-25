/*
 * Copyright (C) 2007-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.chaojiyiji.geometerplus.android.fbreader;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ankireader.CardInfoActivity;
import com.chaojiyiji.geometerplus.fbreader.fbreader.ActionCode;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBReaderApp;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBView;
import com.chaojiyiji.geometerplus.fbreader.fbreader.TextBuildTraverser;
import com.chaojiyiji.geometerplus.fbreader.util.FixedTextSnippet;
import com.chaojiyiji.geometerplus.fbreader.util.TextSnippet;
import com.chaojiyiji.geometerplus.zlibrary.core.resources.ZLResource;
import com.chaojiyiji.geometerplus.zlibrary.text.view.ZLTextFixedPosition;
import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.common.util.LogUtil;

class SelectionPopup extends PopupPanel implements View.OnClickListener {
	final static String ID = "SelectionPopup";

	private FBReaderApp mReader;
	SelectionPopup(FBReaderApp fbReader) {
		super(fbReader);
		mReader = fbReader;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void createControlPanel(FBReader activity, RelativeLayout root) {
		if (myWindow != null && activity == myWindow.getContext()) {
			return;
		}

//		activity.getLayoutInflater().inflate(R.layout.selection_panel, root);
//		myWindow = (SimplePopupWindow)root.findViewById(R.id.selection_panel);
//
//		final ZLResource resource = ZLResource.resource("selectionPopup");
//		setupButton(R.id.selection_panel_copy, resource.getResource("copyToClipboard").getValue());
//		setupButton(R.id.selection_panel_share, resource.getResource("share").getValue());
//		setupButton(R.id.selection_panel_translate, resource.getResource("translate").getValue());
//		setupButton(R.id.selection_panel_bookmark, resource.getResource("bookmark").getValue());
//		setupButton(R.id.selection_panel_close, resource.getResource("close").getValue());

		//Anki自定义View
		activity.getLayoutInflater().inflate(R.layout.selection_panel_anki, root);
		myWindow = (SimplePopupWindow)root.findViewById(R.id.selection_panel_anki);

		final ZLResource resource = ZLResource.resource("selectionPopup");
		setupButton(R.id.selection_panel_copy, resource.getResource("copyToClipboard").getValue());
		setupButton(R.id.selection_panel_bookmark, resource.getResource("bookmark").getValue());
		setupButton(R.id.selection_panel_makecard, resource.getResource("makecard").getValue());
		setupButton(R.id.selection_panel_share, resource.getResource("share").getValue());
		setupButton(R.id.selection_panel_close, resource.getResource("close").getValue());
		setupButton(R.id.selection_panel_dictionary, "词典");

		myWindow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				movePopupWindow(initSelectStartY,initSelectEndY);
				myWindow.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	private void setupButton(int buttonId, String description) {
		final View button = myWindow.findViewById(buttonId);
		button.setOnClickListener(this);
		button.setContentDescription(description);
	}

	/*public void move(int selectionStartY, int selectionEndY) {
		if (myWindow == null) {
			return;
		}

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		final int verticalPosition;
		final int screenHeight = ((View)myWindow.getParent()).getHeight();
		final int diffTop = screenHeight - selectionEndY;
		final int diffBottom = selectionStartY;
		if (diffTop > diffBottom) {
			verticalPosition = diffTop > myWindow.getHeight() + 20
					? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
		} else {
			verticalPosition = diffBottom > myWindow.getHeight() + 20
					? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
		}

		layoutParams.addRule(verticalPosition);
		myWindow.setLayoutParams(layoutParams);
	}*/

	private int initSelectStartY;
	private int initSelectEndY;

	/**
	 * 移动选中文字菜单
	 * @param selectionStartY		选中文字top值
	 * @param selectionEndY		选中文字bottom值
	 */
	public void move(int selectionStartY, int selectionEndY) {
		if (myWindow == null) {
			this.initSelectStartY = selectionStartY;
			this.initSelectEndY = selectionEndY;
			return;
		}
		movePopupWindow(selectionStartY, selectionEndY);
	}

	/**
	 * 移动选中文字菜单
	 * @param selectionStartY		选中文字top值
	 * @param selectionEndY		选中文字bottom值
     */
	private void movePopupWindow(int selectionStartY, int selectionEndY) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		/** 整个屏幕的高度 **/
		int screenHeight = ((View)myWindow.getParent()).getHeight();
		/** myWindow在屏幕垂直居中显示的y值 **/
		float centerInParent = screenHeight/2 - myWindow.getHeight()/2;

		/** 选中文字上边剩余空间高度 **/
		int topFreeSpace = selectionStartY;
		/** 选中文字下边剩余空间高度 **/
		int bottomFreeSpace = screenHeight - selectionEndY;

		if (topFreeSpace < bottomFreeSpace) {
			LogUtil.e("movePopupWindow: 1.选中文字下面空间比文字上面空间大");
			// 选中文字下面空间比文字上面空间大
			if(bottomFreeSpace > myWindow.getHeight() + 20){
				// 并且下面空间能容下显示一个popupWindow,就将popupWindow显示在文字下面
				myWindow.setY(selectionEndY + 20);
				LogUtil.e("movePopupWindow: 2.并且下面空间能容下显示一个popupWindow,就将popupWindow显示在文字下面");
			}else{
				// 否则显示在文字上面
				myWindow.setY(centerInParent);
				LogUtil.e("movePopupWindow: 3.否则显示在文字上面");
			}
		} else if(topFreeSpace > bottomFreeSpace) {
			LogUtil.e("movePopupWindow: 4.选中文字下面空间比上面空间小");
			// 选中文字下面空间比上面空间小
			if(topFreeSpace > myWindow.getHeight() + 20){
				// 并且上面空间能容下显示一个popupWindow,就将popupWindow显示在文字上面
				myWindow.setY(selectionStartY - myWindow.getHeight() - 20);
				LogUtil.e("movePopupWindow: 5.并且上面空间能容下显示一个popupWindow,就将popupWindow显示在文字上面");
			}else{
				// 否则显示在文字下面
				myWindow.setY(centerInParent);
				LogUtil.e("movePopupWindow: 6.否则显示在文字下面");
			}
		}
		myWindow.setLayoutParams(layoutParams);
	}

	@Override
	protected void update() {
		if (myWindow != null) {
			LogUtil.e("update: myWindow.getHeight() = " + myWindow.getHeight());
			LogUtil.e("update: myWindow.getY() = " + myWindow.getY());
		}
	}

//	public void onClick(View view) {
//		switch (view.getId()) {
//			case R.id.selection_panel_copy:
//				Application.runAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD);
//				break;
//			case R.id.selection_panel_share:
//				Application.runAction(ActionCode.SELECTION_SHARE);
//				break;
//			case R.id.selection_panel_translate:
//				Application.runAction(ActionCode.SELECTION_TRANSLATE);
//				break;
//			case R.id.selection_panel_bookmark:
//				Application.runAction(ActionCode.SELECTION_BOOKMARK);
//				break;
//			case R.id.selection_panel_close:
//				Application.runAction(ActionCode.SELECTION_CLEAR);
//				break;
//		}
//		Application.hideActivePopup();
//	}

	//Anki自定义View点击事件
	public void onClick(View view) {
		int i = view.getId();
		if (i == R.id.selection_panel_copy) {
			// 复制
			Application.runAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD);

		} else if (i == R.id.selection_panel_bookmark) {
			// 笔记
			Application.runAction(ActionCode.SELECTION_BOOKMARK);

		}else if(i == R.id.selection_panel_dictionary){
			// 词典
			final FBView fbview = mReader.getTextView();
			final TextSnippet snippet = fbview.getSelectedSnippet();
			if (snippet == null) {
				return;
			}
			//选词信息的实现
			String text = snippet.getText();

			Intent intent = new Intent();
			intent.putExtra("key",text);
			intent.setAction("android.intent.action.BaiDuDictionaryActivity");
			myWindow.getContext().startActivity(intent);
			fbview.clearSelection();

		} else if (i == R.id.selection_panel_makecard) {
			// 制卡
//			Toast.makeText(myWindow.getContext(), "跳转至制卡页面", Toast.LENGTH_SHORT).show();
//				Application.runAction(ActionCode.SELECTION_MAKRCARD);方法一：跟源码相同的逻辑的方法
			//方法二：直接实现
			final FBView fbview = mReader.getTextView();
			final TextSnippet snippet = fbview.getSelectedSnippet();
			if (snippet == null) {
				return;
			}
			//制卡后所选单词或区域加入书签，变颜色
   			mReader.addSelectionBookmark();

			//选词信息的实现
			String text = snippet.getText();
			int mStartParagraphIndex = snippet.getStart().getParagraphIndex();
			// 字符起始下标
			int mStartElementIndex = snippet.getStart().getElementIndex();
			// 字符结束下标
			int mEndElementIndex = snippet.getEnd().getElementIndex();
			int mStartCharIndex = snippet.getStart().getCharIndex();
			int mEndParagraphIndex = snippet.getEnd().getParagraphIndex();

			int mEndCharIndex = snippet.getEnd().getCharIndex();

			int startP = mStartParagraphIndex;
			int endP = mEndParagraphIndex;
			int startE = mStartElementIndex;
			int endE = mEndElementIndex;
			int startC = mStartCharIndex;
			int endC = mEndCharIndex;

			//选句子：获取终点的位置
			int sizeOfTextCurrentParagraph = 0;
			if(fbview.getModel().getParagraphsNumber() == 1){
				sizeOfTextCurrentParagraph = fbview.sizeOfTextBeforeParagraph(endP);
			}else{
				sizeOfTextCurrentParagraph = fbview.sizeOfTextBeforeParagraph(endP + 1) - fbview.sizeOfTextBeforeParagraph(endP);
			}

			String nextWord = text;
			ZLTextFixedPosition realEndPosition = null;
			ZLTextFixedPosition startTextFixedPosition;
			ZLTextFixedPosition endTextFixedPosition;

			print(nextWord,endE,sizeOfTextCurrentParagraph);
			while (!(nextWord.endsWith(",") || nextWord.endsWith(".") || nextWord.endsWith("。") || nextWord.endsWith("...") || nextWord.endsWith("\"")
					|| nextWord.endsWith("?") || nextWord.endsWith("'") || nextWord.endsWith(":") || nextWord.equals("\n")
					|| TextUtils.isEmpty(nextWord) || endE >= sizeOfTextCurrentParagraph)) {
				print(nextWord,endE,sizeOfTextCurrentParagraph);
				endE += 1;
				startTextFixedPosition = new ZLTextFixedPosition(endP, endE, 0);
				endTextFixedPosition = new ZLTextFixedPosition(endP, endE, 1);
//					Log.e("nextPosition", "startPosition :" + endP +","+ endE + ",0");
//					Log.e("nextPosition", "endPosition :" + endP +","+ endE + ",1");
				final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
				traverser.traverse(startTextFixedPosition, endTextFixedPosition);
				TextSnippet NextSnippet = new FixedTextSnippet(startTextFixedPosition, endTextFixedPosition, traverser.getText());
				nextWord = NextSnippet.getText();
				Log.e("nextWord", nextWord.toString());
//					realEndPosition = endTextFixedPosition;
				realEndPosition = new ZLTextFixedPosition(endP, endE, nextWord.length() - 1);
			}
			if (nextWord == text) {
				realEndPosition = new ZLTextFixedPosition(endP, endE, endC);
			}

			//选句子：获得起点位置
			String previousWord = " ";
			ZLTextFixedPosition realStartPosition = null;
			print2(previousWord,startE);
			while (!(previousWord.endsWith(",") || previousWord.endsWith(".") || previousWord.endsWith("。") || previousWord.endsWith("...") || previousWord.endsWith("\"")
					|| previousWord.endsWith("?") || previousWord.endsWith("'") || previousWord.endsWith(":") || startE <= 0)) {
				startE -= 1;
				startTextFixedPosition = new ZLTextFixedPosition(startP, startE, 0);
				endTextFixedPosition = new ZLTextFixedPosition(startP, startE, 1);
//					Log.e("previousPosition", "startPosition :" + startP +","+ startE + ",0");
//					Log.e("previousPosition", "endPosition :" + startP +","+ startE + ",1");
				final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
				traverser.traverse(startTextFixedPosition, endTextFixedPosition);
				TextSnippet NextSnippet = new FixedTextSnippet(startTextFixedPosition, endTextFixedPosition, traverser.getText());
				previousWord = NextSnippet.getText();
				Log.e("previousWord", previousWord.toString());
				realStartPosition = new ZLTextFixedPosition(startP, startE + 1, 0);
			}


			//选中整个句子,获得下标和文本内容
			final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
			traverser.traverse(realStartPosition, realEndPosition);
			TextSnippet SentenceSnippet = new FixedTextSnippet(realStartPosition, realEndPosition, traverser.getText());
			String sentence = SentenceSnippet.getText();
			Log.e("sentence", sentence.toString());

			int StartParagraphOfSentence = SentenceSnippet.getStart().getParagraphIndex();
			int StartElementOfSentence = SentenceSnippet.getStart().getElementIndex();
			int StartCharOfSentence = SentenceSnippet.getStart().getCharIndex();
			int EndParagraphOfSentence = SentenceSnippet.getEnd().getParagraphIndex();
			int EndElementOfSentence = SentenceSnippet.getEnd().getElementIndex();
			int EndCharOfSentence = SentenceSnippet.getEnd().getCharIndex();


			//传递数据到CardInfoActivity
			final Intent intent = new Intent(myWindow.getContext(), CardInfoActivity.class);
			intent.putExtra("TEXT", text);
			intent.putExtra("StartParagraphIndex", mStartParagraphIndex);
			intent.putExtra("StartElementIndex", mStartElementIndex);
			intent.putExtra("StartCharIndex", mStartCharIndex);
			intent.putExtra("EndParagraphIndex", mEndParagraphIndex);
			intent.putExtra("EndElementIndex", mEndElementIndex);
			intent.putExtra("EndCharIndex", mEndCharIndex);

			intent.putExtra("sentence", sentence);
			intent.putExtra("StartParagraphOfSentence", StartParagraphOfSentence);
			intent.putExtra("StartElementOfSentence", StartElementOfSentence);
			intent.putExtra("StartCharOfSentence", StartCharOfSentence);
			intent.putExtra("EndParagraphOfSentence", EndParagraphOfSentence);
			intent.putExtra("EndElementOfSentence", EndElementOfSentence);
			intent.putExtra("EndCharOfSentence", EndCharOfSentence);
//			myWindow.getContext().startActivity(intent);

			//使用静态广播启动制卡页面
			String bookname = mReader.getCurrentBook().getPath().substring(mReader.getCurrentBook().getPath().lastIndexOf("/")+1).replace(".txt","");
			String type = bookname.substring(bookname.lastIndexOf("@") + 1);
			int typeInt = Integer.parseInt(type);
			Intent broadcastIntent = new Intent();
			broadcastIntent.putExtra("TEXT", text);
			broadcastIntent.putExtra("sentence", sentence);
			broadcastIntent.putExtra("BOOK_NAME", bookname);
			broadcastIntent.putExtra("type", typeInt);

			if(typeInt == 0){
				// 填空模板
				String sentenceHtml = setSelectedSnippetHighlighted(fbview, mStartElementIndex, mEndElementIndex, realStartPosition, realEndPosition);
				broadcastIntent.putExtra("sentenceHtml", sentenceHtml);
				broadcastIntent.putExtra("startElementIndex", mStartElementIndex);
				broadcastIntent.putExtra("endElementIndex", mEndElementIndex);
				broadcastIntent.setAction("android.intent.action.MakeCardInfoActivity");
				myWindow.getContext().startActivity(broadcastIntent);
			}else{
				// 英语简约版
				broadcastIntent.setAction("com.text.info");
				myWindow.getContext().sendBroadcast(broadcastIntent);
			}

		} else if (i == R.id.selection_panel_share) {
			// 分享
			String sentence_share = selectSentence();
			new ShareAction((Activity) myWindow.getContext()).withText(sentence_share)
					.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE,
							SHARE_MEDIA.DOUBAN, SHARE_MEDIA.WEIXIN_CIRCLE)
					.setCallback(umShareListener).open();


		} else if (i == R.id.selection_panel_close) {
			// 取消
			Application.runAction(ActionCode.SELECTION_CLEAR);

			//尝试给QQ好友分享图片
//				UMImage image = new UMImage((Activity) myWindow.getContext(), R.drawable.plugin_pdf);
//				new ShareAction((Activity) myWindow.getContext()).setPlatform(SHARE_MEDIA.QQ).withMedia(image).share();
//				new ShareAction((Activity) myWindow.getContext()).setPlatform(SHARE_MEDIA.QQ).withText("我的测试文字").
//						withTitle("我的自定义标题").withTargetUrl("http://www.ankichina.net").share();

		}
		Application.hideActivePopup();
	}

	/**
	 * 将选中的词在所在的句子中设置为高亮
	 * @param fbview
	 * @param mStartElementIndex    	选中文字起始下标
	 * @param mEndElementIndex    	选中文字结束下标
	 * @param realStartPosition    	选中词所在句子的起始位置信息（包含 该句子第一个元素的下标，该句子所在段落下标）
	 * @param realEndPosition			选中词所在句子的结束位置信息（包含 该句子最后一个元素的下标，该句子所在段落下标）
     * @return							返回html格式字符串	如：不同商品都有价值，所以才能够按照一定<span><font color=\"#3696e9\"><u>商品</u></font></span>相互交
     */
	private String setSelectedSnippetHighlighted(FBView fbview, int mStartElementIndex, int mEndElementIndex,
												 ZLTextFixedPosition realStartPosition, ZLTextFixedPosition realEndPosition) {

		TextBuildTraverser textBuildTraverser = null;
		ZLTextFixedPosition start = null;
		ZLTextFixedPosition end = null;
		FixedTextSnippet fixedTextSnippet = null;
		StringBuilder sb = new StringBuilder();
		String markStart = "<span><font color=\"#3696e9\"><u>";
		String markEnd = "</u></font></span>";
		int paragraphIndex = realStartPosition.getParagraphIndex();
		for (int j = realStartPosition.getElementIndex(); j <= realEndPosition.getElementIndex(); j++) {
			textBuildTraverser = new TextBuildTraverser(fbview);
			// 截取句子中的每一个元素（一个元素包含一个文字或一个文字 + 字符）
			start = new ZLTextFixedPosition(paragraphIndex, j, 0);
            end = new ZLTextFixedPosition(paragraphIndex, j, 0);
            textBuildTraverser.traverse(start, end);
            fixedTextSnippet = new FixedTextSnippet(start, end, textBuildTraverser.getText());
			String text = fixedTextSnippet.getText();
			if(j == mStartElementIndex){
				// 在选中文字前面加上html标签
				sb.append(markStart);
                sb.append(text);
            }else if (j == mEndElementIndex){
				// 在选中文字后面加上html标签
                sb.append(text);
                sb.append(markEnd);
            }else{
                sb.append(text);
            }
            LogUtil.e("fixedTextSnippet.getText() = " + text);
        }
		return sb.toString();
	}

	private void print2(String previousWord,int startE) {
		boolean b = previousWord.endsWith(",");
		boolean b1 = previousWord.endsWith(".");
		boolean b2 = previousWord.endsWith("...");
		boolean b3 = previousWord.endsWith("\"");
		boolean b4 = previousWord.endsWith("?");
		boolean b5 = previousWord.endsWith("'");
		boolean b6 = previousWord.endsWith(":");
		boolean b7 = startE <= 0;
		boolean b8 = !(b || b1 || b2 || b3 || b4 || b5 || b6 || b7);
		if(b8){

		}
	}

	private void print(String nextWord, int endE, int sizeOfTextCurrentParagraph) {
		boolean b = nextWord.endsWith(",");
		boolean b1 = nextWord.endsWith(".");
		boolean b2 = nextWord.endsWith("...");
		boolean b3 = nextWord.endsWith("\"");
		boolean b4 = nextWord.endsWith("?");
		boolean b5 = nextWord.endsWith("'");
		boolean b6 = nextWord.endsWith(":");
		boolean equals = nextWord.equals("\n");
		boolean b7 = TextUtils.isEmpty(nextWord);
		boolean b8 = endE >= sizeOfTextCurrentParagraph;
		boolean b9 = !(b || b1 || b2 || b3 || b4 || b5 || b6 || equals || b7 || b8);
		if(b9){

		}

	}

	//友盟分享回调
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			com.umeng.socialize.utils.Log.e("plat","platform"+platform);

			Toast.makeText(FBReaderApplication.getInstance(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(FBReaderApplication.getInstance(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			if(t!=null){
				com.umeng.socialize.utils.Log.e("throw","throw:"+t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(FBReaderApplication.getInstance(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};


	/**
	 * 选词的实现
	 * @return 返回选中的文本
	 */
	private String selectWord(){
		final FBView fbview = mReader.getTextView();
		final TextSnippet snippet = fbview.getSelectedSnippet();
		if (snippet == null) {
			return null;
		}
		String text = snippet.getText();
		return text;
	}

	/**
	 *  选句子的实现
	 *  @return 返回选中的文本所在的整个句子
	 */
	private String selectSentence(){
		final FBView fbview = mReader.getTextView();
		final TextSnippet snippet = fbview.getSelectedSnippet();
		if (snippet == null) {
			return null;
		}

		//选词信息的实现
		String text = snippet.getText();
		int mStartParagraphIndex = snippet.getStart().getParagraphIndex();
		int mStartElementIndex = snippet.getStart().getElementIndex();
		int mStartCharIndex = snippet.getStart().getCharIndex();
		int mEndParagraphIndex = snippet.getEnd().getParagraphIndex();
		int mEndElementIndex = snippet.getEnd().getElementIndex();
		int mEndCharIndex = snippet.getEnd().getCharIndex();

		int startP = mStartParagraphIndex;
		int endP = mEndParagraphIndex;
		int startE = mStartElementIndex;
		int endE = mEndElementIndex;
		int startC = mStartCharIndex;
		int endC = mEndCharIndex;

		//选句子：获取终点的位置
		int sizeOfTextCurrentParagraph = fbview.sizeOfTextBeforeParagraph(endP + 1) - fbview.sizeOfTextBeforeParagraph(endP);
		String nextWord = text;
		ZLTextFixedPosition realEndPosition = null;
		ZLTextFixedPosition startTextFixedPosition;
		ZLTextFixedPosition endTextFixedPosition;
		while(!(nextWord.endsWith(",")|| nextWord.endsWith(".")|| nextWord.endsWith("...")|| nextWord.endsWith("\"")
				|| nextWord.endsWith("?")|| nextWord.endsWith("'")|| nextWord.endsWith(":")|| nextWord.equals("\n")
				|| TextUtils.isEmpty(nextWord)|| endE >= sizeOfTextCurrentParagraph)){
			endE += 2;
			startTextFixedPosition = new ZLTextFixedPosition(endP, endE, 0);
			endTextFixedPosition = new ZLTextFixedPosition(endP, endE, 1);
//			Log.e("nextPosition", "startPosition :" + endP +","+ endE + ",0");
//			Log.e("nextPosition", "endPosition :" + endP +","+ endE + ",1");
			final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
			traverser.traverse(startTextFixedPosition, endTextFixedPosition);
			TextSnippet NextSnippet = new FixedTextSnippet(startTextFixedPosition, endTextFixedPosition, traverser.getText());
			nextWord = NextSnippet.getText();
			Log.e("nextWord", nextWord.toString());
			realEndPosition =  new ZLTextFixedPosition(endP, endE, nextWord.length()-1);
		}
		if(nextWord == text){
			realEndPosition =  new ZLTextFixedPosition(endP, endE, endC);
		}

		//选句子：获得起点位置
		String previousWord = " ";
		ZLTextFixedPosition realStartPosition = null;
		while(!(previousWord.endsWith(",")|| previousWord.endsWith(".")|| previousWord.endsWith("...")|| previousWord.endsWith("\"")
				|| previousWord.endsWith("?")|| previousWord.endsWith("'")|| previousWord.endsWith(":")|| startE <= 0)){
			startE -= 2;
			startTextFixedPosition = new ZLTextFixedPosition(startP, startE, 0);
			endTextFixedPosition = new ZLTextFixedPosition(startP, startE, 1);
//			Log.e("previousPosition", "startPosition :" + startP +","+ startE + ",0");
//			Log.e("previousPosition", "endPosition :" + startP +","+ startE + ",1");
			final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
			traverser.traverse(startTextFixedPosition, endTextFixedPosition);
			TextSnippet NextSnippet = new FixedTextSnippet(startTextFixedPosition, endTextFixedPosition, traverser.getText());
			previousWord = NextSnippet.getText();
			Log.e("previousWord", previousWord.toString());
			realStartPosition = new ZLTextFixedPosition(startP, startE + 2, 0);
		}


		//选中整个句子,获得下标和文本内容
		final TextBuildTraverser traverser = new TextBuildTraverser(fbview);
		traverser.traverse(realStartPosition, realEndPosition);
		TextSnippet SentenceSnippet = new FixedTextSnippet(realStartPosition, realEndPosition, traverser.getText());
		String sentence = SentenceSnippet.getText();
		Log.e("sentence", sentence.toString());
		return sentence;
	}

	//新增方法
	/*public void show(View view, int startY, int endY) {
		Log.w("info", "---->>> show -- startY : y = " + startY + " : " + endY);

		final int screenHeight = view.getHeight();
		final int screenWigth  = view.getWidth();
		int topHeight = startY;
		int bottomHeight = screenHeight - endY;
		int txtHeight = (endY - startY);
		int panpel = Utils.convertDpToPx(FBReaderApplication.getInstance(), 80);

		if (topHeight > bottomHeight) {
			Log.e("info", "顶部显示");
			if (topHeight - panpel > 20) {
				// 顶部显示 0 (topHeight - panpel)
				Log.e("inft","1");
				opupWindow.showAtLocation(view,  Gravity.NO_GRAVITY, (endY+startY)/2, (topHeight - panpel));
			} else {
				//0 0
				Log.e("inft","2");
				opupWindow.showAtLocation(view,  Gravity.NO_GRAVITY, 0, 0);
			}
		} else {
			Log.e("info", "底部显示");
			// 底部显示
			//0 endy
			opupWindow.showAtLocation(view,  Gravity.NO_GRAVITY, (endY+startY)/2+80, endY);
		}
//		(endY+startY)/2+80, (endY+startY)/2

	}*/

}
