package com.wan.systemmanager;

/**
 * Created by 万文杰 on 2016/6/16.
 */

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class PwdEditText extends EditText {
    private final String TAG = "PwdEditText";
    private Drawable dRight;
    private Drawable dLeft;
    private Rect rBounds;
    private int state=0;
    private Drawable mIconPerson;
    public PwdEditText(Context paramContext) {
        super(paramContext);
        dRight=getResources().getDrawable(R.drawable.icon_loginyincang);
        dRight.setBounds(-20, 0, 55, 55);
        setCompoundDrawables(dLeft, null, dRight, null);
        initEditText();
    }

    public PwdEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initEditText();
    }

    public PwdEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {

        addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
            @Override
            public void afterTextChanged(Editable paramEditable) {
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                if (getText().toString().length() == 0) {
                    if (state == 1) {
                        dRight=getResources().getDrawable(R.drawable.icon_loginkejian);
                        dRight.setBounds(-20, 0, 55, 55);
                        setCompoundDrawables(dLeft, null, dRight, null);
                        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    } else{
                        dRight=getResources().getDrawable(R.drawable.icon_loginyincang);
                        dRight.setBounds(-20, 0, 55, 55);
                        setCompoundDrawables(dLeft, null, dRight, null);
                        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
                else{
                    if (state == 1) {
                        dRight=getResources().getDrawable(R.drawable.icon_loginkejian);
                        dRight.setBounds(-20, 0, 55, 55);
                        setCompoundDrawables(dLeft, null, dRight, null);
                        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    } else{
                        dRight=getResources().getDrawable(R.drawable.icon_loginyincang);
                        dRight.setBounds(-20, 0, 55, 55);
                        setCompoundDrawables(dLeft, null, dRight, null);
                        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }

            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                if (state == 1) {
                    dRight=getResources().getDrawable(R.drawable.icon_loginkejian);
                    dRight.setBounds(-20, 0, 55, 55);
                    setCompoundDrawables(dLeft, null, dRight, null);
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else{
                    dRight=getResources().getDrawable(R.drawable.icon_loginyincang);
                    dRight.setBounds(-20, 0, 55, 55);
                    setCompoundDrawables(dLeft, null, dRight, null);
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    /**
     * 添加触摸事件 点击之后 出现 切换editText的效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ( (paramMotionEvent.getAction() == 1)) {
            this.rBounds = this.dRight.getBounds();
            int i = (int) paramMotionEvent.getRawX();// 距离屏幕的距离
            //int i = (int) paramMotionEvent.getX();//距离边框的距离

            if (i > getRight() - 3 * this.rBounds.width()) {
                // 控制图片的显示
                if (state == 1) {
                    dRight=getResources().getDrawable(R.drawable.icon_loginkejian);
                    dRight.setBounds(-20, 0, 55, 55);
                    setCompoundDrawables(dLeft, null, dRight, null);

                    // setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    state=0;
                }else  if (state == 0){
                    dRight=getResources().getDrawable(R.drawable.icon_loginyincang);
                    dRight.setBounds(-20, 0, 55, 55);
                    setCompoundDrawables(dLeft, null, dRight, null);

                  //  setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    state=1;
                }
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    /**
     * 显示右侧X图片的
     *
     * 左上右下
     */

    @Override
    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        if (paramDrawable1 != null) {
            dLeft = paramDrawable1;
        }
        if (paramDrawable3 != null) {
            dRight = paramDrawable3;
        }

    }
}