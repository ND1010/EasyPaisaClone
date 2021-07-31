package com.app.bhaskar.easypaisa.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bhaskar.easypaisa.R;
import com.app.bhaskar.easypaisa.ui.listener.PhotoSelectionListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PhotoSelectionAsk {
    private String msg;
    private PhotoSelectionListeners photoSelectionListeners;

    private Context mContext;

    public PhotoSelectionAsk(Context context, String msg, PhotoSelectionListeners photoSelectionListeners) {
        this.mContext = context;
        this.msg = msg;
        this.photoSelectionListeners = photoSelectionListeners;
    }

    public void show() {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.dialog_add_document, null);

        TextView tv_Title_PhotoSelection = (TextView) sheetView.findViewById(R.id.tv_Title_PhotoSelection);
        tv_Title_PhotoSelection.setText(msg);
        ImageView iv_Gallery_PhotoSelection = (ImageView) sheetView.findViewById(R.id.iv_Gallery_PhotoSelection);
        ImageView iv_Camera_PhotoSelection = (ImageView) sheetView.findViewById(R.id.iv_Camera_PhotoSelection);

        iv_Gallery_PhotoSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectionListeners.onGalleryClick();
                mBottomSheetDialog.dismiss();
            }
        });
        iv_Camera_PhotoSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectionListeners.onCameraClick();
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();


    }
}
