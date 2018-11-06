package com.choxmi.dropsworld.dropsworld.Publisher;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;


import com.choxmi.dropsworld.dropsworld.Doodle.PaintView;
import com.choxmi.dropsworld.dropsworld.Models.Post;
import com.choxmi.dropsworld.dropsworld.R;
import com.choxmi.dropsworld.dropsworld.Transaction.AssetUploader;
import com.choxmi.dropsworld.dropsworld.Transaction.AsyncResponse;
import com.choxmi.dropsworld.dropsworld.Transaction.ProgressNotification;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.valdesekamdem.library.mdtoast.MDToast;

/**
 * Created by Choxmi on 11/22/2017.
 */

public class DoodlePublisher extends AppCompatActivity implements AsyncResponse {
    private PaintView doodleView;
    private Button normal,emboss,blur,clear,dismiss;
    ImageButton btnBackground,btnSave,btnBrush,btnEraser,brushColor,btnBack;
    RelativeLayout btnContainer;
    Dialog dialog;
    ImageView brushPreview;
    SeekBar brushSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publisher_doodle);

        doodleView = (PaintView)findViewById(R.id.doodle_view);

        dialog=new Dialog(DoodlePublisher.this,android.R.style.Theme_NoTitleBar_Fullscreen);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.doodle_dialog, null);
        brushColor = (ImageButton)view.findViewById(R.id.brushColor);
        brushColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(DoodlePublisher.this)
                        .setTitle("Choose Brush Color")
                        .initialColor(doodleView.getBrushColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Log.e("Color", ""+selectedColor);
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                doodleView.setBrushColor(selectedColor);
                                brushColor.setBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        dismiss = (Button)view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        brushPreview = (ImageView)view.findViewById(R.id.brushPreview);
        brushSize = (SeekBar)view.findViewById(R.id.brushSeek);

        brushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(progress, 250);
                brushPreview.setLayoutParams(layoutParams);
                doodleView.setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        normal = (Button)view.findViewById(R.id.normalBtn);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushPreview.setImageDrawable(getDrawable(R.drawable.normal));
                doodleView.normal();
            }
        });

        emboss = (Button)view.findViewById(R.id.embossBtn);
        emboss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushPreview.setImageDrawable(getDrawable(R.drawable.emboss));
                doodleView.emboss();
            }
        });

        blur = (Button)view.findViewById(R.id.blurBtn);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushPreview.setImageDrawable(getDrawable(R.drawable.blur));
                doodleView.blur();
            }
        });

        dialog.setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        btnContainer = (RelativeLayout)findViewById(R.id.btnContainer);
        doodleView.init(metrics,btnContainer);

//        clear = (Button)findViewById(R.id.doodle_clear);
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doodleView.clear();
//            }
//        });

        btnBackground = (ImageButton)findViewById(R.id.btnBackground);
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(DoodlePublisher.this)
                        .setTitle("Choose color")
                        .initialColor(doodleView.getBrushColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Log.e("Color", ""+selectedColor);
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                doodleView.setBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        btnEraser = (ImageButton)findViewById(R.id.btnEraser);
        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.erase();
                MDToast.makeText(DoodlePublisher.this,"Eraser selected",Toast.LENGTH_SHORT,MDToast.TYPE_INFO).show();
            }
        });

        btnBrush = (ImageButton)findViewById(R.id.btnBrush);
        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        btnSave = (ImageButton)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressNotification.startLoading(DoodlePublisher.this,"Posting...");
                DoodlePub pub = new DoodlePub();
                Thread pubThread = new Thread(pub);
                pubThread.start();
            }
        });

        btnBack = (ImageButton)findViewById(R.id.btnback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void processFinish(String response) {
        MDToast.makeText(DoodlePublisher.this,"Doodle posted", Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
        onBackPressed();
    }

    class DoodlePub implements Runnable{
        @Override
        public void run() {
            Post post = new Post();
            SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String userID = saved_values.getString("owner_id","");
            post.setUser_id(Integer.valueOf(userID));
            AssetUploader.uploadContent(DoodlePublisher.this, Post.POST_TYPE.DOODLE,doodleView.saveImage(DoodlePublisher.this),post,DoodlePublisher.this);
        }
    }
}
