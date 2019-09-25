
package com.ichi2yiji.anki.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.NoteEditor;


public class NoteEditorRescheduleCard extends DialogFragment {
    public static NoteEditorRescheduleCard newInstance() {
        return new NoteEditorRescheduleCard();
    }


    @Override
    public MaterialDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.reschedule_card_dialog_title)
                .positiveText(getResources().getString(R.string.dialog_ok))
                .negativeText(R.string.cancel)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRange(1, 9999)
                .input(R.string.reschedule_card_dialog_message, R.string.empty_string, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence text) {
                        int days = Integer.parseInt(text.toString());
                        ((NoteEditor) getActivity()).onRescheduleCard(days);
                    }
                })
                .show();
    }
    }
