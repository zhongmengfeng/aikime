package com.ichi2yiji.anki.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;

public class ModelBrowserContextMenu extends DialogFragment {

    public final static int MODEL_TEMPLATE = 0;
    public final static int MODEL_RENAME = 1;
    public final static int MODEL_DELETE = 2;

    private static MaterialDialog.ListCallback mContextMenuListener;

    public static ModelBrowserContextMenu newInstance(String label, MaterialDialog.ListCallback contextMenuListener) {
        mContextMenuListener = contextMenuListener;
        ModelBrowserContextMenu n = new ModelBrowserContextMenu();
        Bundle b = new Bundle();
        b.putString("label", label);
        n.setArguments(b);
        return n;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] entries = new String[3];
        entries[MODEL_TEMPLATE] = getResources().getString(R.string.model_browser_template);
        entries[MODEL_RENAME] = getResources().getString(R.string.model_browser_rename);
        entries[MODEL_DELETE] = getResources().getString(R.string.model_browser_delete);

        return new MaterialDialog.Builder(getActivity())
                .title(getArguments().getString("label"))
                .items(entries)
                .itemsCallback(mContextMenuListener)
                .build();
    }
}
