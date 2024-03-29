
package com.ichi2yiji.anki.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DeckPicker;


public class DeckPickerConfirmDeleteDeckDialog extends DialogFragment {
    public static DeckPickerConfirmDeleteDeckDialog newInstance(String dialogMessage) {
        DeckPickerConfirmDeleteDeckDialog f = new DeckPickerConfirmDeleteDeckDialog();
        Bundle args = new Bundle();
        args.putString("dialogMessage", dialogMessage);
        f.setArguments(args);
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        return new MaterialDialog.Builder(getActivity())
                .title(res.getString(R.string.delete_deck_title))
                .content(getArguments().getString("dialogMessage"))
                .iconAttr(R.attr.dialogErrorIcon)
                .positiveText(res.getString(R.string.dialog_positive_delete))
                .negativeText(res.getString(R.string.dialog_cancel))
                .cancelable(true)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        ((DeckPicker) getActivity()).deleteContextMenuDeck();
                        ((DeckPicker) getActivity()).dismissAllDialogFragments();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        ((DeckPicker) getActivity()).dismissAllDialogFragments();
                    }
                })
                .build();

    }
}
