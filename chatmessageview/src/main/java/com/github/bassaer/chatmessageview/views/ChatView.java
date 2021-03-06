package com.github.bassaer.chatmessageview.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.bassaer.chatmessageview.R;
import com.github.bassaer.chatmessageview.models.Attribute;
import com.github.bassaer.chatmessageview.models.Message;

/**
 * Chat view with edit view and send button
 * Created by nakayama on 2016/08/08.
 */
public class ChatView extends LinearLayout {
    private MessageView mMessageView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private ImageButton mOptionButton;
    private SwipeRefreshLayout mChatContainer;
    private InputMethodManager mInputMethodManager;
    private int mSendIconId = R.drawable.ic_action_send;
    private int mOptionIconId = R.drawable.ic_action_add;
    private int mSendIconColor = ContextCompat.getColor(getContext(), R.color.lightBlue500);
    private int mOptionIconColor = ContextCompat.getColor(getContext(), R.color.lightBlue500);
    private boolean mAutoScroll = true;
    private boolean mAutoHidingKeyboard = true;
    private Attribute mAttribute;

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttribute = new Attribute(context, attrs);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttribute = new Attribute(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View layout = LayoutInflater.from(context).inflate(R.layout.chat_view, this);
        mMessageView = layout.findViewById(R.id.message_view);
        mInputText = layout.findViewById(R.id.message_edit_text);
        mSendButton = layout.findViewById(R.id.send_button);
        mOptionButton = layout.findViewById(R.id.option_button);
        mChatContainer = layout.findViewById(R.id.chat_container);
        mChatContainer.setEnabled(false);

        mMessageView.init(mAttribute);

        mMessageView.setFocusableInTouchMode(true);
        //if touched Chat screen
        mMessageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
            }
        });

        //if touched empty space
        mChatContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });


        mMessageView.setOnKeyboardAppearListener(new MessageView.OnKeyboardAppearListener() {
            @Override
            public void onKeyboardAppeared(boolean hasChanged) {
                //Appeared keyboard
                if (hasChanged) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Scroll to end
                            mMessageView.scrollToEnd();
                        }
                    }, 500);
                }
            }
        });

    }

    /**
     * Hide software keyboard
     */
    public void hideKeyboard() {
        //Hide keyboard
        mInputMethodManager.hideSoftInputFromWindow(
                mMessageView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        //Move focus to background
        mMessageView.requestFocus();
    }

    public void setLeftBubbleColor(int color) {
        mMessageView.setLeftBubbleColor(color);
    }

    public void setRightBubbleColor(int color) {
        mMessageView.setRightBubbleColor(color);
    }


    /**
     * Set message to right side
     * @param message Sent message
     */
    public void send(Message message) {
        mMessageView.setMessage(message);

        //Hide keyboard after post
        if (mAutoHidingKeyboard) {
            hideKeyboard();
        }
        //Scroll to bottom after post
        if (mAutoScroll) {
            mMessageView.scrollToEnd();
        }
    }

    /**
     * Set message to left side
     * @param message Received message
     */
    public void receive(Message message) {
        mMessageView.setMessage(message);
        if (mAutoScroll) {
            mMessageView.scrollToEnd();
        }
    }

    public void setInputText(String input) {
        mInputText.setText(input);
    }

    public String getInputText() {
        return mInputText.getText().toString();
    }

    public void setOnClickSendButtonListener(View.OnClickListener listener) {
        mSendButton.setOnClickListener(listener);
    }

    public void setOnClickOptionButtonListener(View.OnClickListener listener) {
        mOptionButton.setOnClickListener(listener);
    }

    public void setBackgroundColor(int color) {
        mMessageView.setBackgroundColor(color);
        mChatContainer.setBackgroundColor(color);
    }

    public void setSendButtonColor(int color) {
        mSendIconColor = color;
        mSendButton.setImageDrawable(getColoredDrawable(color, mSendIconId));
    }

    public void setOptionButtonColor(int color) {
        mOptionIconColor = color;
        mOptionButton.setImageDrawable(getColoredDrawable(color, mOptionIconId));
    }

    public Drawable getColoredDrawable(int color, int iconId) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        Drawable icon = ContextCompat.getDrawable(getContext(), iconId);
        Drawable wrappedDrawable = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(wrappedDrawable, colorStateList);
        return wrappedDrawable;
    }

    public void setSendIcon(int resId) {
        mSendIconId = resId;
        setSendButtonColor(mSendIconColor);
    }

    public void setOptionIcon(int resId) {
        mOptionIconId = resId;
        setOptionButtonColor(mOptionIconColor);
    }

    public void setInputTextHint(String hint) {
        mInputText.setHint(hint);
    }

    public void setUsernameTextColor(int color) {
        mMessageView.setUsernameTextColor(color);
    }

    public void setSendTimeTextColor(int color) {
        mMessageView.setSendTimeTextColor(color);
    }

    public void setDateSeparatorColor(int color) {
        mMessageView.setDateSeparatorTextColor(color);
    }

    public void setRightMessageTextColor(int color) {
        mMessageView.setRightMessageTextColor(color);
    }

    public void setMessageStatusTextColor(int color) {
        mMessageView.setMessageStatusColor(color);
    }

    public void setOnBubbleClickListener(Message.OnBubbleClickListener listener) {
        mMessageView.setOnBubbleClickListener(listener);
    }

    public void setOnBubbleLongClickListener(Message.OnBubbleLongClickListener listener) {
        mMessageView.setOnBubbleLongClickListener(listener);
    }

    public void setOnIconClickListener(Message.OnIconClickListener listener) {
        mMessageView.setOnIconClickListener(listener);
    }

    public void setOnIconLongClickListener(Message.OnIconLongClickListener listener) {
        mMessageView.setOnIconLongClickListener(listener);
    }

    public void setLeftMessageTextColor(int color) {
        mMessageView.setLeftMessageTextColor(color);
    }

    /**
     * Auto Scroll when message received.
     * @param enable Whether auto scroll is enable or not
     */
    public void setAutoScroll(boolean enable) {
        mAutoScroll = enable;
    }

    public void setMessageMarginTop(int px) {
        mMessageView.setMessageMarginTop(px);
    }

    public void setMessageMarginBottom(int px) {
        mMessageView.setMessageMarginBottom(px);
    }

    /**
     * Add TEXT watcher
     * @param watcher behavior when text view status is changed
     */
    public void addTextWatcher(TextWatcher watcher) {
        mInputText.addTextChangedListener(watcher);
    }

    public void setEnableSendButton(boolean enable) {
        mSendButton.setEnabled(enable);
    }

    public boolean isEnabledSendButton() {
        return mSendButton.isEnabled();
    }

    /**
     * Auto hiding keyboard after post
     * @param autoHidingKeyboard if true, keyboard will be hided after post
     */
    public void setAutoHidingKeyboard(boolean autoHidingKeyboard) {
        mAutoHidingKeyboard = autoHidingKeyboard;
    }

    public void setOnRefreshListener(final SwipeRefreshLayout.OnRefreshListener listener) {
        mChatContainer.setOnRefreshListener(listener);
    }

    public void setRefreshing(boolean refreshing) {
        mChatContainer.setRefreshing(refreshing);
    }

    public void setEnableSwipeRefresh(boolean enable) {
        mChatContainer.setEnabled(enable);
    }

    public void addInputChangedListener(TextWatcher watcher) {
        mInputText.addTextChangedListener(watcher);
    }

    public void scrollToEnd() {
        mMessageView.scrollToEnd();
    }

    public MessageView getMessageView() {
        return mMessageView;
    }

    public void setMaxInputLine(int lines) {
        mInputText.setMaxLines(lines);
    }

    public void setMessageFontSize(float size) {
        mMessageView.setMessageFontSize(size);
    }

    public void setUsernameFontSize(float size) {
        mMessageView.setUsernameFontSize(size);
    }

    public void setTimeLabelFontSize(float size) {
        mMessageView.setTimeLabelFontSize(size);
    }

    public void setMessageMaxWidth(int width) {
        mMessageView.setMessageMaxWidth(width);
    }

    public void setDateSeparatorFontSize(float size) {
        mMessageView.setDateSeparatorFontSize(size);
    }
}
