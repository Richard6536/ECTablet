package com.example.richard.ectablet.Clases;

import androidx.appcompat.app.ActionBar;
import android.view.View;

public class ActionBarActivity {

    public static ActionBar actionBar;
    public static View view;

    public ActionBar getActionBar() {
        return actionBar;
    }

    public View getView() {
        return view;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public void setView(View view) {
        this.view = view;
    }
}
