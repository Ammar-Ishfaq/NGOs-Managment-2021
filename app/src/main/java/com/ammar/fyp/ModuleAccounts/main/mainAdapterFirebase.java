package com.ammar.fyp.ModuleAccounts.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.ModelClasses.MainChatView;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class mainAdapterFirebase extends FirebaseRecyclerAdapter<MainChatView, mainAdapterFirebase.ViewHolder> {
    private OnItemClickListener mListener;

    private LayoutInflater mInflater;
    private String TAG = "AccountMain";
    //custom data
    private ImageView
            imgview,
            accountImage,
            mapImage;
    private TextView chatName, chatEmail;
    private CardView recyclerViewCard;
    private Context context;
    private View mview;
    private String isEmail, searchingContent;


    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
        }

    }

    public mainAdapterFirebase(Context context, @NonNull FirebaseRecyclerOptions<MainChatView> options, String isEmail, String searchingContent) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.isEmail = isEmail;
        this.searchingContent = searchingContent;


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MainChatView model) {
//  Will Set the image view will starting

        imgview = holder.itemView.findViewById(R.id.Img);
        imgview.setVisibility(holder.itemView.VISIBLE);

        chatName = holder.itemView.findViewById(R.id.Name);
        chatEmail = holder.itemView.findViewById(R.id.Email);
        accountImage = holder.itemView.findViewById(R.id.imgBody);
        mapImage = holder.itemView.findViewById(R.id.mapBody);


        if (isEmail.equals("email1")) {
            populateEmail1(model, holder);
//            chatName.setText(model.getName2().toUpperCase());//as we are at NGO Side
//            chatEmail.setText(model.getEmail2());//as we are at NGO Side
//            Glide.with(holder.itemView)
//                    .load(model.getImage2())
//                    .into(imgview);
        } else {
            populateEmail2(model, holder);
//            chatName.setText(model.getName1().toUpperCase());//as we are at NGO Side
//            chatEmail.setText(model.getEmail1());//as we are at NGO Side
//            Glide.with(holder.itemView)
//                    .load(model.getImage1())
//                    .into(imgview);
        }


        FirebaseCrud.getInstance().getLastPyamentDetail(
                holder.itemView.getContext(),
                model.getCombineemail(),
                accountImage,
                mapImage,
                holder.itemView.findViewById(R.id.contactBody),
                holder.itemView.findViewById(R.id.amountBody),
                holder.itemView.findViewById(R.id.paymentDetail)

        );


        recyclerViewCard = holder.itemView.findViewById(R.id.cardviewMain);
        recyclerViewCard.setOnClickListener(view -> {
            String combineEmail = model.getCombineemail();
            if (mListener != null) {
                int isEmail1 = 0;//Reference Code -> 1-Donor,0-NGO  email1-Donor,email2-NGO
                String email = model.getEmail1();
                String image = model.getImage1();
                if (isEmail.equals("email1")) {
                    isEmail1 = 1;
                    email = model.getEmail2();
                    image = model.getImage2();
                }
                mListener.onImageClick(holder.itemView, position, isEmail1, combineEmail, email, image);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //when data is fetched and
        mview = mInflater.inflate(R.layout.recycularview_account_main, parent, false);
        mListener.showingEnable();
        return new ViewHolder(mview, (OnItemClickListener) mListener);
    }

    public interface OnItemClickListener {
        void onImageClick(View v, int position, int isEmail1, String combineEmail, String chatWithEmail, String imageUrl);

        /**
         * Hide the loader as the data is fetching succesfully from the firebase
         *
         * @param
         */
        void showingEnable();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    void populateEmail1(MainChatView model, mainAdapterFirebase.ViewHolder holder) {
        if (searchingContent != null) {
            if (model.getEmail2().contains(searchingContent) || model.getName2().contains(searchingContent)) {
                chatName.setText(model.getName2().toUpperCase());//as we are at NGO Side
                chatEmail.setText(model.getEmail2());//as we are at NGO Side
                Glide.with(holder.itemView)
                        .load(model.getImage2())
                        .into(imgview);
            } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } else {
            chatName.setText(model.getName2().toUpperCase());//as we are at NGO Side
            chatEmail.setText(model.getEmail2());//as we are at NGO Side
            Glide.with(holder.itemView)
                    .load(model.getImage2())
                    .into(imgview);

        }
    }

    void populateEmail2(MainChatView model, mainAdapterFirebase.ViewHolder holder) {
        if (searchingContent != null) {
            if (model.getEmail1().contains(searchingContent) || model.getName1().contains(searchingContent)) {
                chatName.setText(model.getName1().toUpperCase());//as we are at NGO Side
                chatEmail.setText(model.getEmail1());//as we are at NGO Side
                Glide.with(holder.itemView)
                        .load(model.getImage1())
                        .into(imgview);
            } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } else {
            chatName.setText(model.getName1().toUpperCase());//as we are at NGO Side
            chatEmail.setText(model.getEmail1());//as we are at NGO Side
            Glide.with(holder.itemView)
                    .load(model.getImage1())
                    .into(imgview);

        }
    }

}


