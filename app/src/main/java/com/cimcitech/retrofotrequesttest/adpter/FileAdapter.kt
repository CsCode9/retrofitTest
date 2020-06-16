package com.cimcitech.retrofotrequesttest.adpter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.cimcitech.retrofotrequesttest.R
import java.io.File
import java.util.zip.Inflater

/**
 *@Date 2020/6/16
 *@author Chen
 *@Description
 */
class FileAdapter(val context: Context, val resourceId: Int, val data: List<File>): RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.iv_file)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = data.get(position)
//        holder.image.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))


    }
}