package com.example.richard.ectablet.Clases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.example.richard.ectablet.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mapbox.geojson.Feature;
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.Arrays;

import static com.android.volley.VolleyLog.TAG;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapBoxManager {

    private MapboxMap mapboxMap;

    private MapView mapView;
    private Style styleMap;

    GeoJsonSource geoJsonSource;
    SymbolLayer symbolLayer;

    int PERMISSION_ALL = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION};


    public void SetMapBoxMap(MapboxMap mbMap){
        mapboxMap = mbMap;
    }

    public void getMapBoxAccessToken(Context context){
        // Mapbox Access token
        Mapbox.getInstance(context, "pk.eyJ1IjoiemVyb2RzIiwiYSI6ImNrM2t3cG0xNzB5bzgzam12dHdwY2luMXgifQ.3qy4aurdz4Vjp4QNr1-feg");
    }

    public MapboxMap GetMapBoxMap()
    {
        if(mapboxMap == null)
        {
            Log.d("ERROR MAPBOX", "MAPBOXMAP ES NULL");
        }

        return  mapboxMap;
    }

    public MapView GetMapView(){
        return mapView;
    }

    public void InicializarMapBox(OnMapReadyCallback contexto, View view, Bundle bundle){

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(bundle);
        mapView.getMapAsync(contexto);

    }

    public void DefinirStyle(AutocompleteSupportFragment autoCompleteSupportFragment, Resources recursos, Activity actividad)
    {
        String a = "";
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/zerods/cjkvmr0q606ks2ro0rt0o8wxc"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                styleMap = style;
                // Initialize Places.
                Places.initialize(getApplicationContext(), "AIzaSyAXRgGC2NP-RPcP0YCcpuw2QMUPEO4Hqsc");
                // Create a new Places client instance.
                //PlacesClient placesClient = Places.createClient(getApplicationContext());

                /**
                 * Initialize Places. For simplicity, the API key is hard-coded. In a production
                 * environment we recommend using a secure mechanism to manage API keys.
                 */
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyAXRgGC2NP-RPcP0YCcpuw2QMUPEO4Hqsc");
                }

// Initialize the AutocompleteSupportFragment.
                AutocompleteSupportFragment autocompleteFragment = autoCompleteSupportFragment;

                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {

                        if (symbolLayer != null) {
                            style.removeLayer("layer-id");
                            style.removeSource("source-id");
                        }

                        HideStatusBarNavigation hideStatusBarNavigation = new HideStatusBarNavigation();
                        ActionBarActivity actionBarActivity = new ActionBarActivity();

                        hideStatusBarNavigation.hideUI(actionBarActivity.view, actionBarActivity.actionBar);

                        // Add the marker image to map
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        recursos, R.drawable.mapbox_marker_icon_default));

                        geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(place.getLatLng().longitude, place.getLatLng().latitude)));
                        style.addSource(geoJsonSource);

                        symbolLayer = new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)) // Sets the new camera position
                                .zoom(18) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 8000);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                    }
                });

                verifyStoragePermissions(actividad);
            }
        });
    }

    private void verifyStoragePermissions(Activity activity) {
        // Check if permissions are enabled and if not request
        int permissionLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            if (Build.VERSION.SDK_INT >= 23) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ALL);
            }
            else{
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            }

        }
        else
        {
            //Inicializa Mapas, posición & crea directorios si los permisos se han concedido
            enableLocationComponent(activity.getBaseContext());
        }


    }

    @SuppressLint("MissingPermission")
    public void enableLocationComponent(Context context) {

        if(styleMap == null)
        {
            Log.d("ERROR MAPBOX", "STYLEMAP ES NULL");
            return;
        }

        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with a built LocationComponentActivationOptions object
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(context, styleMap).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING_GPS);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.GPS);

        locationComponent.setLocationComponentEnabled(true);
    }

}
