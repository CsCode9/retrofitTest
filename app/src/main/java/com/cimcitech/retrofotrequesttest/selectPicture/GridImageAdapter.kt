package com.cimcitech.retrofotrequesttest.selectPicture

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cimcitech.retrofotrequesttest.R
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils
import java.io.File

/**
 *@Date 2020/6/16
 *@author Chen
 *@Description
 */
class GridImageAdapter(val context: Context, val mOnAddPicClickListener: onAddPicClickListener) :
    RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {

    val TYPE_CAMERA: Int = 1;
    val TYPE_PICTURE: Int = 2;
    private var mInflater: LayoutInflater;

    //    private List<LocalMedia> list = new ArrayList<>();
    private var list = ArrayList<LocalMedia>()
    private var selectMax: Int = 9;

    init {
        mInflater = LayoutInflater.from(context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImg: ImageView = view.findViewById(R.id.fiv)
        val ll_del: LinearLayout = view.findViewById(R.id.ll_del)
        val tv_duration: TextView = view.findViewById(R.id.tv_duration)
    }

    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    fun setList(list: List<LocalMedia>) {
        this.list = list as ArrayList<LocalMedia>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_filter_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (list.size < selectMax) {
            return list.size + 1;
        } else {
            return list.size;
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    fun isShowAddItem(position: Int): Boolean {
        val size = list.size;
        return position == size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //少于8张继续添加
        if (getItemViewType(position) == TYPE_CAMERA) {
            holder.mImg.setImageResource(R.mipmap.add_img)
            holder.mImg.setOnClickListener {
                mOnAddPicClickListener.onAddPicClick()
            }
            holder.ll_del.visibility = View.INVISIBLE
        } else {
            holder.ll_del.visibility = View.VISIBLE
            holder.ll_del.setOnClickListener {
                val index = holder.adapterPosition
                if (index != RecyclerView.NO_POSITION) {
                    list.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, list.size)
                }
            }
            val media = list.get(position)
            val mimeType = media.mimeType
            var path = ""
            if (media.isCut && !media.isCompressed) {
                // 裁剪过
                path = media.cutPath
            } else if (media.isCompressed || (media.isCut && media.isCompressed)) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.compressPath
            } else {
                //原图
                path = media.path
            }
            if (media.isCompressed) {
                Log.i(
                    "compress image result:",
                    "${File(media.getCompressPath()).length() / 1024} k"
                );
                Log.i("压缩地址::", media.getCompressPath());
            }
            Log.i("原图地址::", media.getPath());
            val pictureType = PictureMimeType.isPictureType(media.pictureType)
            if (media.isCut) {
                Log.i("裁剪地址::", media.getCutPath());
            }
            val duration = media.duration
            if (pictureType == PictureConfig.TYPE_VIDEO) {
                holder.tv_duration.visibility = View.VISIBLE
            } else {
                holder.tv_duration.visibility = View.GONE
            }
            if (mimeType == PictureMimeType.ofAudio()) {
                holder.tv_duration.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio)
                StringUtils.modifyTextViewDrawable(holder.tv_duration, drawable, 0)
            } else {
                val drawable = ContextCompat.getDrawable(context, R.drawable.video_icon)
                StringUtils.modifyTextViewDrawable(holder.tv_duration, drawable, 0)
            }
            holder.tv_duration.text = DateUtils.timeParse(duration)
            if (mimeType == PictureMimeType.ofAudio()) {
                holder.mImg.setImageResource(R.drawable.audio_placeholder)
            } else {
                val options = RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(holder.itemView.context)
                    .load(path)
                    .apply(options)
                    .into(holder.mImg)
            }
            if (mItemClickListener != null){
                holder.itemView.setOnClickListener {
                    val adapterPosition = holder.adapterPosition
                    mItemClickListener.onItemClick(adapterPosition, it)
                }
            }
        }
    }

    /**
     * 点击添加图片跳转
     */

//    private lateinit var mOnAddPicClickListener: onAddPicClickListener

    interface onAddPicClickListener {
        fun onAddPicClick()
    }

    private lateinit var mItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }
}