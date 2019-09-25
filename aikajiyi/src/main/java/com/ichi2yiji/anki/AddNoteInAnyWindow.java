package com.ichi2yiji.anki;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ichi2yiji.anki.util.CopyRawToDataForInitDeck;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Models;
import com.ichi2yiji.libanki.Note;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/13.
 */

public class AddNoteInAnyWindow {

    private final String TAG = getClass().getSimpleName();
    Context context;
    Collection mCol;
    private static Note mEditorNote;
    public AddNoteInAnyWindow(Context context){
        this.context = context;
        mCol = CollectionHelper.getInstance().getCol(context);
    }

    public static void addNote(long cardType, String deckName, List<String> fields){
        LogUtil.e("addNote: deckName = " + deckName);
        AddNoteInAnyWindow model = new AddNoteInAnyWindow(AnkiDroidApp.getInstance());
        model.addNoteInstans(cardType,deckName,fields);
    }

    private void addNoteInstans(long modelID, String deckName, List<String> fields) {

        // 根据modelID创建对应的笔记类型
        Models models = mCol.getModels();

        // 打印所有模板
        // printModels(models);

        JSONObject model = models.get(modelID);
        Note mEditorNote = new Note(mCol, model);

        // 填充笔记内容
        mEditorNote.setmFields(fields.toArray(new String[fields.size()]));

        // 根据牌组名字获取对应牌组ID
        long mCurrentDid = mCol.getDecks().id(deckName, true);
        models.setChanged();
        try {
            mEditorNote.model().put("did", String.valueOf(mCurrentDid));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ADD_FACT, mRenderQAHandler,
                new DeckTask.TaskData(mEditorNote));
    }

    /**
     * 打印所有模板
     * @param models
     */
    private void printModels(Models models) {
        Set<Map.Entry<Long, JSONObject>> entries = models.getModels().entrySet();
        for (Map.Entry<Long, JSONObject> entry : entries) {
            Log.e(TAG, "addNoteInstans: key = " + entry.getKey());
            Log.e(TAG, "addNoteInstans: value = " + entry.getValue().toString());
        }
    }


    public void saveNote(long modleId, String deckName, ArrayList<String> fields) {

        Models models = mCol.getModels();
        JSONObject map_model = models.get(modleId);
        mEditorNote = new Note(mCol,map_model);
        try {
            long mCurrentDid = mCol.getDecks().id(deckName,true);
            mEditorNote.model().put("did",String.valueOf(mCurrentDid));
            mCol.getModels().setChanged();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0;i<fields.size();i++){
            updateField(i,fields.get(i));
        }
        Log.e("AddNoteInAnyWindow", "mEditorNote>>>>>>>model>>>>>>" + mEditorNote.model());
        CopyRawToDataForInitDeck.save(mEditorNote.model().toString(), Environment.getExternalStorageDirectory().getAbsolutePath(), "map_model");
        Log.e("AddNoteInAnyWindow", "mEditorNote>>>>>>>getFields>>>>>" + Arrays.toString(mEditorNote.getFields()));
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ADD_FACT, mRenderQAHandler,
                new DeckTask.TaskData(mEditorNote));
    }
    private DeckTask.TaskListener mRenderQAHandler = new DeckTask.TaskListener() {
        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onPreExecute() {
            //showProgressBar();
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result != null) {
                Toast.makeText(context.getApplicationContext(), "制卡成功", Toast.LENGTH_SHORT).show();
            } else {
            }
        }


        @Override
        public void onCancelled() {
            //hideProgressBar();
        }
    };

    private static void save(String data, String outputPath, String outputName){
        FileOutputStream out = null;
        BufferedWriter write = null;
        File file = new File(outputPath + "/" + outputName);

        try {
            out = new FileOutputStream(file);
            write = new BufferedWriter(new OutputStreamWriter(out));
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(write != null){
                    write.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private static boolean updateField(int position,String field) {
        String newValue = field;
        if (!mEditorNote.values()[position].equals(newValue)) {
            mEditorNote.values()[position] = newValue;
            return true;
        }
        return false;
    }
}
