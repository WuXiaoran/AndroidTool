package com.example.ran.androidmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ran.androidmodule.utils.GlideUtil;
import com.tool.picture.components.photoviewer.PhotoViewer;
import com.tool.picture.components.progressimg.ProgressImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<String> data;
    private Context context;
    private RecyclerView recyclerView;

    public MainAdapter(ArrayList<String> data, Context context,RecyclerView recyclerView) {
        this.data = data;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        GlideUtil.loadImage(context, data.get(position), holder.piv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewer photoViewer = new PhotoViewer();
                photoViewer.setData(data)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(ProgressImageView iv, String url) {
                                GlideUtil.loadImage(context, url, iv);
                            }
                        })
                        .setCurrentPage(position)
                        .setImgContainer(recyclerView)
                        .start((AppCompatActivity) context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ProgressImageView piv;
        private ProgressBar progressView1;

        public ViewHolder(View itemView) {
            super(itemView);
            piv = itemView.findViewById(R.id.img);
            progressView1 = itemView.findViewById(R.id.progressView1);
        }
    }

}
