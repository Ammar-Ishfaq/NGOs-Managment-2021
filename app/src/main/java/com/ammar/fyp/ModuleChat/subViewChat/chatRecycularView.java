package com.ammar.fyp.ModuleChat.subViewChat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.ModelClasses.SubChatView;
import com.ammar.fyp.ModuleDirection.FollowRoute;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.ImageUtils;
import com.ammar.fyp.Tools.MyMapUtils;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class chatRecycularView extends FirebaseRecyclerAdapter<SubChatView, chatRecycularView.ViewHolder> {
    private static final int VIEW_MESSAGE_RECEIVED = 102;
    private static final int VIEW_MESSAGE_SENT = 103;
    private OnItemClickListener mListener;
    private View mview;
    private Context context;
    private LayoutInflater mInflater;
    private String
            TAG = "DchatRecycularAdapter";
    private TextView
            msgBodytv,
            Datetv,
            timetv,
            ammount,
            contact;
    private int
            isEmail1,
            lastTotalChilds = 0,//for the asurity of the new messages
            tempPosition = -1;//for not changing the bind View holder
    private boolean isShownEnabl = true;
    private LinearLayout paymentDetail;
    private ImageView
            map;
    private CircularImageView
            circularImage;
    private SimpleDateFormat timeZoneDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private SimpleDateFormat timeZone = new SimpleDateFormat("hh:mm a", Locale.getDefault());//time Zone for am Pm
    //    SimpleDateFormat DateTimePM/AM = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private ArrayList<Integer> datePositionList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();


    private boolean isJustAccount;//if true then it only show the payment related messages

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
        if (newIndex > lastTotalChilds) {
            lastTotalChilds = newIndex;
            mListener.onChildChange();
        }
    }

    public chatRecycularView(Context context, @NonNull FirebaseRecyclerOptions<SubChatView> options, int isEmail1) {
        super(options);
        this.isEmail1 = isEmail1;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        private CardView recyclerViewCard;
//        private ImageView img;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
//            recyclerViewCard = itemView.findViewById(R.id.cardviewMain);
//            img = itemView.findViewById(R.id.chatImg);
//            recyclerViewCard.setOnClickListener(view -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onImageClick(itemView, position, img);
//                    }
//                }
//

//            });


        }


    }

    public chatRecycularView(Context context, @NonNull FirebaseRecyclerOptions<SubChatView> options) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull SubChatView model) {
//  Will Set the image view will starting
        Log.d(TAG, "onBindViewHolder: position:" + position);
        msgBodytv = holder.itemView.findViewById(R.id.message_body);
        Datetv = holder.itemView.findViewById(R.id.dateBody);
        timetv = holder.itemView.findViewById(R.id.timeBody);
        circularImage = holder.itemView.findViewById(R.id.imgBody);
        map = holder.itemView.findViewById(R.id.mapBody);
        contact = holder.itemView.findViewById(R.id.contactBody);
        ammount = holder.itemView.findViewById(R.id.amountBody);
        paymentDetail = holder.itemView.findViewById(R.id.paymentDetail);

        msgBodytv.setText(model.getText());//as we are at NGO Side
        timetv.setText(timeZone.format(Long.parseLong(model.getTime())));

        String mobileDateTime = timeZoneDate.format(Long.parseLong(model.getTime()));
        String img = model.getImage();
        String Amount = model.getAmount();
        String Contact = model.getContact();
        String location = model.getLocation();


        if (img != null) {
            paymentDetail.setVisibility(holder.itemView.VISIBLE);
            Glide
                    .with(holder.itemView)
                    .load(img)
                    .into(this.circularImage);
            new ImageUtils().enablePopUpZoom(context, this.circularImage, model.getImage());//will enable the image to be zoom

            Glide
                    .with(holder.itemView)
                    .load(MyMapUtils.getInstance().getImgfromLongLat(location))
                    .into(this.map);
//adding the map listner for the direction
            map.setOnClickListener(v -> {
                Intent intent
                        = new Intent(v.getContext(), FollowRoute.class);
                intent.putExtra("longlat", model.getLocation());
                v.getContext().startActivity(intent);
            });

            ammount.setText(Amount);
            contact.setText(Contact);
        } else {
            paymentDetail.setVisibility(holder.itemView.GONE);

        }

//For the date Manging So that it show the one date only once
        if (dateList.isEmpty() || !dateList.contains(mobileDateTime)) {
            dateList.add(mobileDateTime);
            this.datePositionList.add(position);
        }
        if (this.datePositionList.contains(position)) {
            Datetv.setVisibility(holder.itemView.VISIBLE);
            Datetv.setText(mobileDateTime);

        } else {
            Datetv.setVisibility(holder.itemView.GONE);

        }
        Log.d(TAG, "onBindViewHolder: Image: " + model.getImage());

    }


    @Override
    public int getItemViewType(int position) {

        if (getItem(position).getisEmail1().equals("" + isEmail1))
            return VIEW_MESSAGE_SENT;
        else
            return VIEW_MESSAGE_RECEIVED;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //when data is fetched and
        if (viewType == VIEW_MESSAGE_SENT)
            mview = mInflater.inflate(R.layout.my_message, parent, false);
        if (viewType == VIEW_MESSAGE_RECEIVED)
            mview = mInflater.inflate(R.layout.their_message, parent, false);
        if (isShownEnabl)
            mListener.showingEnable();
        isShownEnabl = false;
        return new ViewHolder(mview, (OnItemClickListener) mListener);
    }

    public interface OnItemClickListener {
        void onImageClick(View v, int position, ImageView imageView);

        /**
         * Hide the loader as the data is fetching succesfully from the firebase
         *
         * @param
         */
        void showingEnable();

        void onChildChange();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}


