package com.workbzw.lib_ui;

import com.workbzw.baseannotation.Action;
import com.workbzw.componentframe.IAction;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/28 11:19 PM
 * @desc
 */
@Action(actor = CustomerView.class, name = "Widget", note = "自定义控件")
public interface WidgetAction extends IAction {
}
