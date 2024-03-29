package com.ichi2yiji.anki.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;

public class ModelEditorContextMenu extends DialogFragment {

    public final static int FIELD_REPOSITION = 0;
    public final static int SORT_FIELD = 1;
    public final static int FIELD_RENAME = 2;
    public final static int FIELD_DELETE = 3;


    private static MaterialDialog.ListCallback mContextMenuListener;

    public static ModelEditorContextMenu newInstance(String label, MaterialDialog.ListCallback contextMenuListener) {
        ModelEditorContextMenu n = new ModelEditorContextMenu();
        mContextMenuListener = contextMenuListener;
        Bundle b = new Bundle();
        b.putString("label", label);
        mContextMenuListener = contextMenuListener;
        n.setArguments(b);
        return n;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] entries = new String[4];
        entries[FIELD_REPOSITION] = getResources().getString(R.string.model_field_editor_reposition_menu);
        entries[SORT_FIELD] = getResources().getString(R.string.model_field_editor_sort_field);
        entries[FIELD_RENAME] = getResources().getString(R.string.model_field_editor_rename);
        entries[FIELD_DELETE] = getResources().getString(R.string.model_field_editor_delete);

        return new MaterialDialog.Builder(getActivity())
                .title(getArguments().getString("label"))
                .items(entries)
                .itemsCallback(mContextMenuListener)
                .build();
    }
}
