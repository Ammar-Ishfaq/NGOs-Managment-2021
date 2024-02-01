package com.ammar.fyp.views.MainPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammar.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class firebaseRecycularViewAdapter extends FirebaseRecyclerAdapter<product_getter_setter, firebaseRecycularViewAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private View mview;
    private Context context;
    private LayoutInflater mInflater;

    //custom data
    private ImageView imgview;
    private TextView textView;

    public void setOnItemClickListener() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView recyclerViewCard;
        private ImageView img;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            recyclerViewCard = itemView.findViewById(R.id.customLayoutcardView);
            img = itemView.findViewById(R.id.productimage);
            recyclerViewCard.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onImageClick(itemView, position, img);
                    }
                }

            });
        }

    }

    public firebaseRecycularViewAdapter(Context context, @NonNull FirebaseRecyclerOptions<product_getter_setter> options) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull product_getter_setter model) {
//  Will Set the image view will starting

        imgview = holder.itemView.findViewById(R.id.productimage);
        imgview.setVisibility(holder.itemView.VISIBLE);

        Picasso.get().load(model.getImage()).into(imgview);
        textView = holder.itemView.findViewById(R.id.text1);
        textView.setText(model.getName());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //when data is fetched and
        View view = mInflater.inflate(R.layout.customelayout, parent, false);
        mview = view;
        mListener.showingEnable(view);
        return new ViewHolder(view, (OnItemClickListener) mListener);
    }

    public interface OnItemClickListener {
        void onImageClick(View v, int position, ImageView imageView);

        /**
         * Hide the loader as the data is fetching succesfully from the firebase
         *
         * @param v
         */
        void showingEnable(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}


