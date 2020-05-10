package com.example.richard.ectablet.Clases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.richard.ectablet.R;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapBoxManager {

    private MapboxMap mapboxMap;

    private MapView mapView;
    public Style styleMap;

    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    GeoJsonSource geoJsonSource;
    SymbolLayer symbolLayer;

    int PERMISSION_ALL = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION};


    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    private MapboxDirections client;
    private Point origin;
    private Point destination;

    public void SetMapBoxMap(MapboxMap mbMap){
        mapboxMap = mbMap;
    }

    public Style getStyleMap(){
        return styleMap;
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




// Set the origin location to the Alhambra landmark in Granada, Spain.
                        origin = Point.fromLngLat(-73.170903, -40.567141);

// Set the destination location to the Plaza del Triunfo in Granada, Spain.
                        destination = Point.fromLngLat(place.getLatLng().longitude, place.getLatLng().latitude);

                        initSource(style);

                        initLayers(style, recursos);

// Get the directions route from the Mapbox Directions API
                        getRoute(mapboxMap, origin, destination);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                    }
                });

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {


                        LatLng pointOr = new LatLng(-40.567703, -73.170281);
                        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                        Point originPoint = Point.fromLngLat(pointOr.getLongitude(), pointOr.getLatitude());
                        //Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),locationComponent.getLastKnownLocation().getLatitude());

                        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
                        if (source != null) {
                            source.setGeoJson(Feature.fromGeometry(destinationPoint));
                        }

                        getRoute(originPoint, destinationPoint);

                        return false;
                    }
                });

                verifyStoragePermissions(actividad);
            }
        });
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(getApplicationContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
                        List<LegStep> directions = currentRoute.legs().get(0).steps();
                        LegStep dir = directions.get(0);

                        String nameDirection = dir.name();
                        Double distanceInMts = currentRoute.distance();
                        Double durationInMinutes = currentRoute.duration();


                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }
    /**
     * Add the route and marker sources to the map
     */
    public void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    /**
     * Add the route and marker icon layers to the map
     */
    public void initLayers(@NonNull Style loadedMapStyle, Resources recursos) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(ColorTemplate.getHoloBlue())
        );
        loadedMapStyle.addLayer(routeLayer);
    }

    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken("pk.eyJ1IjoiemVyb2RzIiwiYSI6ImNrM2t3cG0xNzB5bzgzam12dHdwY2luMXgifQ.3qy4aurdz4Vjp4QNr1-feg")
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                //Timber.d("Response code: " + response.code());
                if (response.body() == null) {
                    //Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    //Timber.e("No routes found");
                    return;
                }

// Get the directions route
                currentRoute = response.body().routes().get(0);

                String duration = String.valueOf(TimeUnit.SECONDS.toMinutes(currentRoute.duration().longValue()));
                Double distance = currentRoute.distance();

                //MapFragment.duration.setText(duration + " min.");
                //MapFragment.distance.setText(distance + " mts.");

// Make a toast which displays the route's distance
                //Toast.makeText(DirectionsActivity.this, String.format( getString(R.string.directions_activity_toast_message),currentRoute.distance()), Toast.LENGTH_SHORT).show();

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                //Timber.e("Error: " + throwable.getMessage());
                //Toast.makeText(getActivity(), "Error: " + throwable.getMessage(),Toast.LENGTH_SHORT).show();
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
