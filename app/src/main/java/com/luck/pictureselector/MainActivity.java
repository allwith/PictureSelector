package com.luck.pictureselector;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.luck.pictureselector.adapter.GridImageAdapter;
import com.luck.pictureselector.util.FullyGridLayoutManager;
import com.yalantis.ucrop.ui.AlbumDirectoryActivity;
import com.yalantis.ucrop.ui.ImageGridActivity;
import com.yalantis.ucrop.util.Constants;
import com.yalantis.ucrop.util.LocalMediaLoader;
import com.yalantis.ucrop.util.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.ui
 * email：邮箱->893855882@qq.com
 * data：16/12/31
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private List<String> images = new ArrayList<>();
    private RadioGroup rgbs0, rgbs1, rgbs2, rgbs3, rgbs4, rgbs5, rgbs6, rgbs7, rgbs8, rgbs9;
    private int selectMode = Constants.MODE_MULTIPLE;
    private int maxSelectNum = 9;// 图片最大可选数量
    private ImageButton minus, plus;
    private EditText select_num;
    private EditText et_w, et_h;
    private boolean isShow = true;
    private int selectType = LocalMediaLoader.TYPE_IMAGE;
    private int copyMode = Constants.COPY_MODEL_DEFAULT;
    private boolean enablePreview = true;
    private boolean isPreviewVideo = true;
    private boolean enableCrop = true;
    private boolean theme = false;
    private boolean selectImageType = false;
    private int cropW = 0;
    private int cropH = 0;
    private boolean isCompress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        rgbs0 = (RadioGroup) findViewById(R.id.rgbs0);
        rgbs1 = (RadioGroup) findViewById(R.id.rgbs1);
        rgbs2 = (RadioGroup) findViewById(R.id.rgbs2);
        rgbs3 = (RadioGroup) findViewById(R.id.rgbs3);
        rgbs4 = (RadioGroup) findViewById(R.id.rgbs4);
        rgbs5 = (RadioGroup) findViewById(R.id.rgbs5);
        rgbs6 = (RadioGroup) findViewById(R.id.rgbs6);
        rgbs7 = (RadioGroup) findViewById(R.id.rgbs7);
        rgbs8 = (RadioGroup) findViewById(R.id.rgbs8);
        rgbs9 = (RadioGroup) findViewById(R.id.rgbs9);
        et_w = (EditText) findViewById(R.id.et_w);
        et_h = (EditText) findViewById(R.id.et_h);
        minus = (ImageButton) findViewById(R.id.minus);
        plus = (ImageButton) findViewById(R.id.plus);
        select_num = (EditText) findViewById(R.id.select_num);
        select_num.setText(maxSelectNum + "");
        FullyGridLayoutManager manager = new FullyGridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(MainActivity.this, onAddPicClickListener);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        rgbs0.setOnCheckedChangeListener(this);
        rgbs1.setOnCheckedChangeListener(this);
        rgbs2.setOnCheckedChangeListener(this);
        rgbs3.setOnCheckedChangeListener(this);
        rgbs4.setOnCheckedChangeListener(this);
        rgbs5.setOnCheckedChangeListener(this);
        rgbs6.setOnCheckedChangeListener(this);
        rgbs7.setOnCheckedChangeListener(this);
        rgbs8.setOnCheckedChangeListener(this);
        rgbs9.setOnCheckedChangeListener(this);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxSelectNum > 1) {
                    maxSelectNum--;
                }
                select_num.setText(maxSelectNum + "");
                adapter.setSelectMax(maxSelectNum);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxSelectNum++;
                select_num.setText(maxSelectNum + "");
                adapter.setSelectMax(maxSelectNum);
            }
        });

    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    // 进入相册
                    /**
                     * type --> 1图片 or 2视频
                     * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
                     * maxSelectNum --> 可选择图片的数量
                     * selectMode         --> 单选 or 多选
                     * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
                     * isPreview    --> 是否打开预览选项
                     * isCrop       --> 是否打开剪切选项
                     * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
                     * ThemeStyle -->主题颜色
                     * CheckedBoxDrawable -->图片勾选样式
                     * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                     * cropH-->裁剪高度 值不能小于100
                     * isCompress -->是否压缩图片
                     * setImageSpanCount -->每行显示个数
                     * 注意-->type为2时 设置isPreview or isCrop 无效
                     * 注意：Options可以为空，默认标准模式
                     */
                    String ws = et_w.getText().toString().trim();
                    String hs = et_h.getText().toString().trim();
                    if (!isNull(ws) && !isNull(hs)) {
                        cropW = Integer.parseInt(ws);
                        cropH = Integer.parseInt(hs);
                    }
                    int selector = R.drawable.select_cb;
                    Options options = new Options();
                    options.setType(selectType);
                    options.setCopyMode(copyMode);
                    options.setCompress(isCompress);
                    options.setMaxSelectNum(maxSelectNum - images.size());
                    options.setSelectMode(selectMode);
                    options.setShowCamera(isShow);
                    options.setEnablePreview(enablePreview);
                    options.setEnableCrop(enableCrop);
                    options.setPreviewVideo(isPreviewVideo);
                    options.setCropW(cropW);
                    options.setCropH(cropH);
                    options.setImageSpanCount(4);

                    if (theme) {
                        options.setThemeStyle(ContextCompat.getColor(MainActivity.this, R.color.blue));
                    }
                    if (selectImageType) {
                        options.setCheckedBoxDrawable(selector);
                    }
                    AlbumDirectoryActivity.startPhoto(MainActivity.this, options);
                    break;
                case 1:
                    // 删除图片
                    Log.i("删除的下标---->", position + "");
                    images.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_IMAGE:
                    ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra(Constants.REQUEST_OUTPUT);
                    if (result != null) {
                        images.addAll(result);
                        adapter.setList(images);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_single:
                selectMode = Constants.MODE_SINGLE;
                break;
            case R.id.rb_multiple:
                selectMode = Constants.MODE_MULTIPLE;
                break;
            case R.id.rb_image:
                selectType = LocalMediaLoader.TYPE_IMAGE;
                break;
            case R.id.rb_video:
                selectType = LocalMediaLoader.TYPE_VIDEO;
                break;
            case R.id.rb_photo_display:
                isShow = true;
                break;
            case R.id.rb_photo_hide:
                isShow = false;
                break;
            case R.id.rb_default:
                copyMode = Constants.COPY_MODEL_DEFAULT;
                break;
            case R.id.rb_to1_1:
                copyMode = Constants.COPY_MODEL_1_1;
                break;
            case R.id.rb_to3_2:
                copyMode = Constants.COPY_MODEL_3_2;
                break;
            case R.id.rb_to3_4:
                copyMode = Constants.COPY_MODEL_3_4;
                break;
            case R.id.rb_to16_9:
                copyMode = Constants.COPY_MODEL_16_9;
                break;
            case R.id.rb_preview:
                enablePreview = true;
                break;
            case R.id.rb_preview_false:
                enablePreview = false;
                break;
            case R.id.rb_preview_video:
                isPreviewVideo = true;
                break;
            case R.id.rb_preview_video_false:
                isPreviewVideo = false;
                break;
            case R.id.rb_yes_copy:
                enableCrop = true;
                break;
            case R.id.rb_no_copy:
                enableCrop = false;
                break;
            case R.id.rb_theme1:
                theme = false;
                break;
            case R.id.rb_theme2:
                theme = true;
                break;
            case R.id.rb_select1:
                selectImageType = false;
                break;
            case R.id.rb_select2:
                selectImageType = true;
                break;
            case R.id.rb_compress_false:
                isCompress = false;
                break;
            case R.id.rb_compress_true:
                isCompress = true;
                break;
        }
    }


    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }

        return false;
    }
}
