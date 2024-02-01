package com.ammar.fyp.ModuleChat.mainViewChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.ModelClasses.MainChatView;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class mainChatFirebaseRecAdap extends FirebaseRecyclerAdapter<MainChatView, mainChatFirebaseRecAdap.ViewHolder> {
    private OnItemClickListener mListener;

    private LayoutInflater mInflater;
    private String TAG = "DchatRecycularAdapter";
    //custom data
    private CircularImageView imgview;
    private TextView chatName, chatEmail;
    private FirebaseCrud firebaseCrud;
    private CardView recyclerViewCard;
    private Context context;
    private View mview;
    private String isEmail, searchingContent;

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private CardView recyclerViewCard;
//        private ImageView img;
//        private TextView isDonor;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
//            recyclerViewCard = itemView.findViewById(R.id.cardviewMain);
//            img = itemView.findViewById(R.id.chatImg);
//            isDonor = itemView.findViewById(R.id.isDonor);
//            recyclerViewCard.setOnClickListener(view -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//
//                        listener.onImageClick(itemView, position, (isDonor.getText().toString()));
//                    }
//                }
//
//            });
        }

    }

    public mainChatFirebaseRecAdap(Context context, @NonNull FirebaseRecyclerOptions<MainChatView> options, String isEmail, String searchingContent) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.isEmail = isEmail;
        this.searchingContent = searchingContent;
        firebaseCrud = new FirebaseCrud();


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MainChatView model) {
//  Will Set the image view will starting

        imgview = holder.itemView.findViewById(R.id.chatImg);
        imgview.setVisibility(holder.itemView.VISIBLE);
        chatName = holder.itemView.findViewById(R.id.chatName);
        chatEmail = holder.itemView.findViewById(R.id.chatEmail);
        if (isEmail.equals("email1")) { // if the email type is 1 then display it details of 2
            populateEmail1(model, holder);
        } else {
            populateEmail2(model, holder);
        }


        recyclerViewCard = holder.itemView.findViewById(R.id.cardviewMain);
        recyclerViewCard.setOnClickListener(view -> {//open chat click listener
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
        mview = mInflater.inflate(R.layout.recycularview_chat_ngo, parent, false);
        mListener.showingEnable();
        return new ViewHolder(mview, (OnItemClickListener) mListener);
    }

    void populateEmail1(MainChatView model, ViewHolder holder) {
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

    void populateEmail2(MainChatView model, ViewHolder holder) {
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


}


