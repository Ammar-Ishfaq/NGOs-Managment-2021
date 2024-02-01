package com.ammar.fyp.views.MainPanel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ammar.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPanel extends AppCompatActivity {
    RecyclerView recyclerView;
    Query query1;
    private DatabaseReference mdatabasereference;
    private ProgressDialog progressDialog;
    //    FirebaseRecyclerAdapter<product_getter_setter, BlogViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLayoutManager;
    private firebaseRecycularViewAdapter adapter;
    private ImageView imgView = null;//for the hiding particular view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);
        progressDialog = new ProgressDialog(MainPanel.this);
        progressDialog.setMessage("Loading Products Please Wait...");
        progressDialog.show();
        mdatabasereference = FirebaseDatabase.getInstance().getReference("products").child("accessories");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGirdView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        query1 = FirebaseDatabase.getInstance().getReference().child("products").child("accessories");
        FirebaseRecyclerOptions<product_getter_setter> options =
                new FirebaseRecyclerOptions.Builder<product_getter_setter>()
                        .setQuery(query1, product_getter_setter.class)
                        .build();
        Log.d("Options", " data : " + options);

        adapter = new firebaseRecycularViewAdapter(MainPanel.this, options);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter.startListening();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new firebaseRecycularViewAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(View v, int position, ImageView imageView) {
                if (imgView == null) {
                    imgView = imageView;
                    imageView.setVisibility(v.GONE);
                } else {
                    imgView.setVisibility(v.VISIBLE);
                    imgView = imageView;
                    imageView.setVisibility(v.GONE);
                }
                Toast.makeText(MainPanel.this, "Image cardView click", Toast.LENGTH_SHORT).show();
            }

            /**
             * Hide the loader as the data is fetching succesfully from the firebase
             * @param v
             */
            @Override
            public void showingEnable(View v) {
                progressDialog.hide();
            }
        });
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<product_getter_setter, BlogViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull BlogViewHolder blogViewHolder, final int i, @NonNull product_getter_setter product_get_set_v) {
//                blogViewHolder.setname(product_get_set_v.getName());
//                String image_url = blogViewHolder.setimage(product_get_set_v.getImage());
//                String link = product_get_set_v.getLink();
//                Log.d("LINKDATA", " data : " + link);
//                blogViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String productid = getRef(i).getKey();
//                        Log.d("productid", " data : " + productid);
//                        mdatabasereference.child(productid).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                String finallink = dataSnapshot.child("link").getValue(String.class);
//                                Log.d("productLink", " data : " + finallink);
//                                if (finallink != null) {
//                                    Uri uriUrl = Uri.parse(finallink);
//                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//                                    startActivity(launchBrowser);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                            }
//                        });
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.customelayout, parent, false);
//                mProgressDialog.dismiss();
//                return new BlogViewHolder(view);
//            }
//        };
//        firebaseRecyclerAdapter.startListening();
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

//    public static class BlogViewHolder extends RecyclerView.ViewHolder {
//        View mView;
//
//        public BlogViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//        }
//
//        public void setname(String name) {
//            TextView ename = (TextView) mView.findViewById(R.id.text1);
//            ename.setText(name);
//        }
//
//        public String setimage(String url) {
//            ImageView image = (ImageView) mView.findViewById(R.id.productimage);
//            Picasso.get().load(url).into(image);
//            return url;
//        }
//    }
}