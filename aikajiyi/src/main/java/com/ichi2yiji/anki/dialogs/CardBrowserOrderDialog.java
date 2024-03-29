
package com.ichi2yiji.anki.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.CardBrowser;

public class CardBrowserOrderDialog extends DialogFragment {

    private static MaterialDialog.ListCallbackSingleChoice mOrderDialogListener;


    public static CardBrowserOrderDialog newInstance(int order, boolean isOrderAsc,
            MaterialDialog.ListCallbackSingleChoice orderDialogListener) {
        CardBrowserOrderDialog f = new CardBrowserOrderDialog();
        Bundle args = new Bundle();
        args.putInt("order", order);
        args.putBoolean("isOrderAsc", isOrderAsc);
        mOrderDialogListener = orderDialogListener;
        f.setArguments(args);
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        String[] items = res.getStringArray(R.array.card_browser_order_labels);
        // Set sort order arrow
        for (int i = 0; i < items.length; ++i) {
            if (i != CardBrowser.CARD_ORDER_NONE && i == getArguments().getInt("order")) {
                if (getArguments().getBoolean("isOrderAsc")) {
                    items[i] = items[i] + " (\u25b2)";
                } else {
                    items[i] = items[i] + " (\u25bc)";

                }
            }
        }
        return new MaterialDialog.Builder(getActivity())
                .title(res.getString(R.string.card_browser_change_display_order_title))
                .content(res.getString(R.string.card_browser_change_display_order_reverse))
                .items(items)
                .itemsCallbackSingleChoice(getArguments().getInt("order"), mOrderDialogListener)
                .build();
    }

}
