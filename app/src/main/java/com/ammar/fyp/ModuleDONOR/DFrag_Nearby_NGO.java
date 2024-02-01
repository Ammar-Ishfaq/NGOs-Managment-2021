package com.ammar.fyp.ModuleDONOR;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ammar.fyp.Interfaces.getNGOList;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.ModelClasses.FeaturesJsonHandling.FeaturesData;
import com.ammar.fyp.ModelClasses.ModelUser;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Config;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.Tools.ImageUtils;
import com.ammar.fyp.Tools.mProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * Display {@link SymbolLayer} icons on the map and determine the appropriate icon
 * based on a property in each {@link Feature}.
 */
public class DFrag_Nearby_NGO extends Fragment implements
        OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String RED_ICON_ID = "RED_ICON_ID";
    private static final String YELLOW_ICON_ID = "YELLOW_ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String ICON_PROPERTY = "ICON_PROPERTY";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private AnimatorSet animatorSet;
    private static final long CAMERA_ANIMATION_TIME = 1950;
    private View view;
    private FirebaseCrud firebaseCrud;
    private Model model;
    private List<ModelUser> NGOList;
    private int lastSelectedNgoId;
    private Firebase firebase;
    private mProgressDialog progressDialog;
    //


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(getContext(), Config.API);

        // This contains the MapView in XML and needs to be called after the access token is configured.
        view = inflater.inflate(R.layout.d_f_nearby_ngo, container, false);
        progressDialog = new mProgressDialog(view.getContext());
        mapView = view.findViewById(R.id.mapView);

        model = new Model();
        firebaseCrud = new FirebaseCrud();
        firebase = new Firebase();

        //will get the all ngos is the list
        firebaseCrud.getNGOs(new getNGOList() {
            @Override
            public void NGOList(List<ModelUser> NGOs) {
                NGOList = NGOs;
                int i = 0;
                for (ModelUser user : NGOList) {
                    String text = user.getEmail();
                    text = text.substring(0, text.indexOf("@"));
                    model.Add(
                            user.getLonglat(),
                            text//text to shown there
                    );

                    Log.d("alllist", "List: " + user.getLonglat());
                    // this will contain "Fruit";
                    mapView.onCreate(savedInstanceState);
                    mapView.getMapAsync(DFrag_Nearby_NGO.this);


                }
            }

            @Override
            public void onFail(String msg) {
                Snackbar.make(view,
                        msg,
                        500).show();
            }
        });
        return view;
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        mapboxMap.setStyle(
//                new Style.Builder().fromUri("mapbox://styles/mapbox/cj44mfrt20f082snokim4ungi")//for the Different Style of mapbox
                new Style.Builder().fromUri("mapbox://styles/mapbox/streets-v11")

                        // Add the SymbolLayer icon image to the map style
                        .withImage(RED_ICON_ID, new ImageUtils().getBitmapFromVectorDrawable(view.getContext(), R.drawable.ic_building))

                        .withImage(YELLOW_ICON_ID, new ImageUtils().getBitmapFromVectorDrawable(view.getContext(), R.drawable.yellow_marker))

                        // Adding a GeoJson source for the SymbolLayer icons.
                        .withSource(new GeoJsonSource(SOURCE_ID,
                                FeatureCollection.fromFeatures(model.getFeature())))

                        // Adding the actual SymbolLayer to the map style. The match expression will check the
                        // ICON_PROPERTY property key and then use the partner value for the actual icon id.
                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(iconImage(match(
                                        get(ICON_PROPERTY), literal(RED_ICON_ID),
                                        stop(YELLOW_ICON_ID, YELLOW_ICON_ID),
                                        stop(RED_ICON_ID, RED_ICON_ID))),
                                        iconAllowOverlap(true),
                                        iconAnchor(Property.ICON_ANCHOR_BOTTOM))
                        ), style -> {

                    // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.

                    DFrag_Nearby_NGO.this.mapboxMap = mapboxMap;

                    mapboxMap.addOnMapClickListener(DFrag_Nearby_NGO.this);

//            Toast.makeText(view.getContext(), R.string.tap_on_marker_instruction,
//                    Toast.LENGTH_SHORT).show();

                    ShowingInstructionAboutIcon();

                    enableLocationPlugin(style);

                });
    }

    void ShowingInstructionAboutIcon() {

        Snackbar snack = Snackbar.make(view,
                R.string.tap_on_marker_instruction,
                BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(view.getResources().getColor(R.color.white))
                .setTextColor(view.getResources().getColor(R.color.ForLightThemeBlue));
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    /**
     * Will Simply Show you the Current Location of the USer
     *
     * @param loadedMapStyle
     */
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity().getApplicationContext())) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                    getActivity().getApplicationContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
            Location lo = locationComponent.getLastKnownLocation();
            try {
                lo.getLatitude();
                lo.getLongitude();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
        if (!features.isEmpty()) {
            // Show the Feature in the TextView to show that the icon is based on the ICON_PROPERTY key/value
            CardView cardView = view.findViewById(R.id.DFragment1_cardviewid);
            TextView featureInfoTextView = view.findViewById(R.id.feature_info);
            ImageButton imageButton = view.findViewById(R.id.callNGO);
            ImageButton chatPing = view.findViewById(R.id.pingChatNGO);
            ImageView imgView = view.findViewById(R.id.imgNGO);

            OpenBottomSheet(
                    NGOList.get(lastSelectedNgoId).getFirstName() + " " + NGOList.get(lastSelectedNgoId).getLastName(),
                    NGOList.get(lastSelectedNgoId).getNumberPersonal(),
                    NGOList.get(lastSelectedNgoId).getEmail(),
                    NGOList.get(lastSelectedNgoId).getImage()
            );
//            cardView.setVisibility(view.VISIBLE);


            String jsonObject = features.get(0).toJson();
            Gson gson = new Gson();
            FeaturesData featuresData = gson.fromJson(jsonObject, FeaturesData.class);

//            Log.d("TAGFeatureJson", jsonObject);
            double id = featuresData.getProperties().getId();
            lastSelectedNgoId = (int) id;
            featureInfoTextView.setText(
                    NGOList.get(lastSelectedNgoId).getFirstName() + " " +
                            NGOList.get(lastSelectedNgoId).getLastName() + "\n" +
                            NGOList.get(lastSelectedNgoId).getNumberPersonal() + "\n" +
                            NGOList.get(lastSelectedNgoId).getEmail() + "\n"
            );
            Glide.with(view.getContext())
                    .load(NGOList.get(lastSelectedNgoId).getImage())
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(imgView);
            new ImageUtils().enablePopUpZoom(view.getContext(), imgView, NGOList.get(lastSelectedNgoId).getImage());//will enable the image to be zoom

            imageButton.setOnClickListener(v -> {
                String phone = NGOList.get(lastSelectedNgoId).getNumberPersonal();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            });
            chatPing.setOnClickListener(v -> {
                String DONOREmail = firebase.getmAuth().getCurrentUser().getEmail();
                progressDialog.show();

                firebaseCrud.startChatWithNGO(DONOREmail, NGOList.get(lastSelectedNgoId).getEmail(),
                        new response() {
                            @Override
                            public void onSuccess(String msg) {
                                Snackbar.make(view, msg, 1000).show();
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onFail(String msg) {
                                Snackbar.make(view, msg, 1000).show();
                                progressDialog.dismiss();

                            }
                        });
            });

            double d = 13.5;
            animateCameraToSelection(features.get(0), d);
            return true;
        } else {
            return false;
        }

    }

    private void OpenBottomSheet(String name, String phone, String email, String img) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.TransparentDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        TextView nametv = bottomSheetDialog.findViewById(R.id.name);
        TextView phonetv = bottomSheetDialog.findViewById(R.id.phone);
        TextView emailtv = bottomSheetDialog.findViewById(R.id.email);
        ImageView imageViewtv = bottomSheetDialog.findViewById(R.id.imageView);
        ImageButton chatPing = bottomSheetDialog.findViewById(R.id.chatpping);
        nametv.setText(name);
        phonetv.setText(phone);
        emailtv.setText(email);
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imageViewtv);
        new ImageUtils().enablePopUpZoom(getContext(), imageViewtv, img);
        chatPing.setOnClickListener(v -> {
            String DONOREmail = firebase.getmAuth().getCurrentUser().getEmail();
            progressDialog.show();

            firebaseCrud.startChatWithNGO(DONOREmail, NGOList.get(lastSelectedNgoId).getEmail(),
                    new response() {
                        @Override
                        public void onSuccess(String msg) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
//                            Snackbar.make(bottomSheetDialog.findViewById(android.R.id.content), msg, 1000).show();

                        }

                        @Override
                        public void onFail(String msg) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
//                            Snackbar.make(bottomSheetDialog.findViewById(android.R.id.content), msg, 1000).show();

                        }
                    });
        });
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private List<Feature> initCoordinateData() {


        Feature singleFeatureOne = Feature.fromGeometry(
                Point.fromLngLat(72.88055419921875,
                        19.05822387777432));
        singleFeatureOne.addStringProperty(ICON_PROPERTY, RED_ICON_ID);
        Feature singleFeatureTwo = Feature.fromGeometry(
                Point.fromLngLat(77.22015380859375,
                        28.549544699103865));

        singleFeatureTwo.addStringProperty(ICON_PROPERTY, YELLOW_ICON_ID);

        Feature singleFeatureThree = Feature.fromGeometry(
                Point.fromLngLat(88.36647033691406,
                        22.52016858599439));

        singleFeatureThree.addStringProperty(ICON_PROPERTY, RED_ICON_ID);

        // Not adding a ICON_PROPERTY property to fourth and fifth features in order to show off the default
        // nature of the match expression used in the ammar up above
        Feature singleFeatureFour = Feature.fromGeometry(
                Point.fromLngLat(78.42315673828125,
                        17.43320034474222));

        Feature singleFeatureFive = Feature.fromGeometry(
                Point.fromLngLat(80.16448974609375,
                        12.988500396985364));

        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(singleFeatureOne);
        symbolLayerIconFeatureList.add(singleFeatureTwo);
        symbolLayerIconFeatureList.add(singleFeatureThree);
        symbolLayerIconFeatureList.add(singleFeatureFour);
        symbolLayerIconFeatureList.add(singleFeatureFive);

        return symbolLayerIconFeatureList;

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * Animate camera to a feature.
     *
     * @param feature the feature to animate to
     */
    private void animateCameraToSelection(Feature feature, double newZoom) {
        CameraPosition cameraPosition = mapboxMap.getCameraPosition();

        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                createLatLngAnimator(cameraPosition.target, convertToLatLng(feature)),
                createZoomAnimator(cameraPosition.zoom, newZoom),
                createBearingAnimator(cameraPosition.bearing, 35),
                createTiltAnimator(cameraPosition.tilt, 28)
        );
        animatorSet.start();
    }

    private Animator createLatLngAnimator(LatLng currentPosition, LatLng targetPosition) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), currentPosition, targetPosition);
        latLngAnimator.setDuration(CAMERA_ANIMATION_TIME);
        latLngAnimator.setInterpolator(new FastOutSlowInInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLng((LatLng) animation.getAnimatedValue()));
            }
        });
        return latLngAnimator;
    }

    /**
     * Helper class to evaluate LatLng objects with a ValueAnimator
     */
    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    private Animator createZoomAnimator(double currentZoom, double targetZoom) {
        ValueAnimator zoomAnimator = ValueAnimator.ofFloat((float) currentZoom, (float) targetZoom);
        zoomAnimator.setDuration(CAMERA_ANIMATION_TIME);
        zoomAnimator.setInterpolator(new FastOutSlowInInterpolator());
        zoomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo((Float) animation.getAnimatedValue()));
            }
        });
        return zoomAnimator;
    }

    private Animator createBearingAnimator(double currentBearing, double targetBearing) {
        ValueAnimator bearingAnimator = ValueAnimator.ofFloat((float) currentBearing, (float) targetBearing);
        bearingAnimator.setDuration(CAMERA_ANIMATION_TIME);
        bearingAnimator.setInterpolator(new FastOutSlowInInterpolator());
        bearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo((Float) animation.getAnimatedValue()));
            }
        });
        return bearingAnimator;
    }

    private Animator createTiltAnimator(double currentTilt, double targetTilt) {
        ValueAnimator tiltAnimator = ValueAnimator.ofFloat((float) currentTilt, (float) targetTilt);
        tiltAnimator.setDuration(CAMERA_ANIMATION_TIME);
        tiltAnimator.setInterpolator(new FastOutSlowInInterpolator());
        tiltAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.tiltTo((Float) animation.getAnimatedValue()));
            }
        });
        return tiltAnimator;
    }

    private LatLng convertToLatLng(Feature feature) {
        Point symbolPoint = (Point) feature.geometry();
        return new LatLng(symbolPoint.latitude(), symbolPoint.longitude());
    }

    private class Model {
        ArrayList<Feature> feature = new ArrayList<>();
        int i = 0;

        public ArrayList<Feature> getFeature() {
            return feature;
        }

        public void Add(String LongLat, String email) {

            String[] items = LongLat.split(",");
            double longitude = Double.parseDouble(items[0]);//longitude
            double latitude = Double.parseDouble(items[1]);

            Feature temp = Feature.fromGeometry(
                    Point.fromLngLat(
                            longitude,
                            latitude));
            temp.addStringProperty(ICON_PROPERTY, RED_ICON_ID);
            temp.addNumberProperty("Id", i);
            feature.add(temp);
            i++;
        }


    }
}