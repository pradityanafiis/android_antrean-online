package com.praditya.antreanonline.view.ui;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;

    public DividerItemDecoration(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 2; i++) {
            View view = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            int dividerTop = view.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + drawable.getIntrinsicHeight();

            drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            drawable.draw(c);
        }
    }
}
